
package com.equipobomba

import io.ktor.http.Cookie
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.URLBuilder
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.security.MessageDigest
import java.security.SecureRandom
import java.util.Base64

// Generar valores aleatorios para proteger OAuth.
private fun generarValorSeguro(): String {
    val bytes = ByteArray(32)
    SecureRandom().nextBytes(bytes)

    return Base64.getUrlEncoder()
        .withoutPadding()
        .encodeToString(bytes)
}

// Obtener el token de sesion enviado por Android.
private fun ApplicationCall.obtenerTokenBearer(): String? {
    val autorizacion =
        request.headers[HttpHeaders.Authorization] ?: return null

    if (!autorizacion.startsWith("Bearer ", ignoreCase = true)) {
        return null
    }

    return autorizacion.substring(7)
        .trim()
        .takeIf { it.isNotBlank() }
}

fun Application.configureRouting() {

    routing {

        // ==========================================
        // 1. RUTA PRINCIPAL
        // ==========================================
        get("/") {
            call.respondText("Hello, World!")
        }

        // ==========================================
        // 2. PRUEBA DE JSON
        // ==========================================
        get("/json/kotlinx-serialization") {
            call.respond(mapOf("hello" to "world"))
        }

        // ==========================================
        // 3. VERIFICAR EL BACKEND
        // ==========================================
        get("/health") {
            call.respond(
                mapOf(
                    "status" to "ok",
                    "service" to "EquipoBomba Backend"
                )
            )
        }

        // ==========================================
        // 4. INICIAR LOGIN CON LINKEDIN
        // ==========================================
        get("/auth/linkedin") {

            val clientId =
                System.getenv("LINKEDIN_CLIENT_ID")?.trim()

            val redirectUri =
                System.getenv("LINKEDIN_REDIRECT_URI")?.trim()

            if (clientId.isNullOrBlank() ||
                redirectUri.isNullOrBlank()
            ) {
                call.respondText(
                    "Falta configurar LinkedIn en el backend.",
                    status = HttpStatusCode.ServiceUnavailable
                )
                return@get
            }

            // Android generara este desafio antes de abrir
            // el navegador. Servira para proteger el ticket.
            val desafio =
                call.request.queryParameters["code_challenge"]

            if (
                desafio == null ||
                !Regex("^[A-Za-z0-9_-]{43}$").matches(desafio)
            ) {
                call.respondText(
                    "El inicio de sesion debe comenzar " +
                            "desde la aplicacion EquipoBomba.",
                    status = HttpStatusCode.BadRequest
                )
                return@get
            }

            val state = generarValorSeguro()

            // Guardar el state temporal.
            call.response.cookies.append(
                Cookie(
                    name = "equipo_oauth_state",
                    value = state,
                    path = "/auth/linkedin",
                    maxAge = 600,
                    secure = true,
                    httpOnly = true,
                    extensions = mapOf("SameSite" to "Lax")
                )
            )

            // Asociar el desafio de Android a este login.
            call.response.cookies.append(
                Cookie(
                    name = "equipo_oauth_challenge",
                    value = desafio,
                    path = "/auth/linkedin",
                    maxAge = 600,
                    secure = true,
                    httpOnly = true,
                    extensions = mapOf("SameSite" to "Lax")
                )
            )

            val linkedinUrl = URLBuilder(
                "https://www.linkedin.com/oauth/v2/authorization"
            ).apply {
                parameters.append("response_type", "code")
                parameters.append("client_id", clientId)
                parameters.append("redirect_uri", redirectUri)
                parameters.append("scope", "openid profile email")
                parameters.append("state", state)
            }.buildString()

            call.respondRedirect(linkedinUrl)
        }

        // ==========================================
        // 5. CALLBACK DE LINKEDIN
        // ==========================================
        get("/auth/linkedin/callback") {

            val codigo =
                call.request.queryParameters["code"]

            val estadoRecibido =
                call.request.queryParameters["state"]

            val errorLinkedin =
                call.request.queryParameters["error"]

            val estadoGuardado =
                call.request.cookies["equipo_oauth_state"]

            val desafioGuardado =
                call.request.cookies["equipo_oauth_challenge"]

            // Eliminar las cookies temporales.
            listOf(
                "equipo_oauth_state",
                "equipo_oauth_challenge"
            ).forEach { nombre ->

                call.response.cookies.append(
                    Cookie(
                        name = nombre,
                        value = "",
                        path = "/auth/linkedin",
                        maxAge = 0,
                        secure = true,
                        httpOnly = true,
                        extensions = mapOf("SameSite" to "Lax")
                    )
                )
            }

            // Comprobar el state y el desafio.
            if (
                estadoGuardado.isNullOrBlank() ||
                estadoRecibido.isNullOrBlank() ||
                desafioGuardado.isNullOrBlank() ||
                !MessageDigest.isEqual(
                    estadoGuardado.toByteArray(Charsets.UTF_8),
                    estadoRecibido.toByteArray(Charsets.UTF_8)
                )
            ) {
                call.respondText(
                    "Solicitud de autenticacion invalida o expirada.",
                    status = HttpStatusCode.BadRequest
                )
                return@get
            }

            if (!errorLinkedin.isNullOrBlank()) {
                call.respondText(
                    "La autorizacion fue cancelada o rechazada.",
                    status = HttpStatusCode.BadRequest
                )
                return@get
            }

            if (codigo.isNullOrBlank()) {
                call.respondText(
                    "LinkedIn no envio el codigo de autorizacion.",
                    status = HttpStatusCode.BadRequest
                )
                return@get
            }

            // Validar la cuenta con LinkedIn.
            try {
                val usuario =
                    LinkedInAuthService.autenticar(codigo)

                // Crear ticket temporal de un solo uso.
                val ticket = AuthSessionStore.crearTicket(
                    usuario = usuario,
                    desafio = desafioGuardado
                )

                // No enviar tokens de LinkedIn a Android.
                // Solamente enviamos el ticket temporal.
                call.response.header(
                    HttpHeaders.CacheControl,
                    "no-store"
                )

                // Regresar automaticamente a EquipoBomba.
                call.respondRedirect(
                    "equipobomba://auth/callback?ticket=$ticket"
                )

            } catch (error: Exception) {

                call.application.log.error(
                    "Fallo la validacion de LinkedIn",
                    error
                )

                call.respondText(
                    "No fue posible verificar la cuenta de LinkedIn. " +
                            "Inicia nuevamente el proceso.",
                    status = HttpStatusCode.Unauthorized
                )
            }
        }

        // ==========================================
        // 6. CAMBIAR TICKET POR SESION
        // ==========================================
        post("/auth/session") {

            call.response.header(
                HttpHeaders.CacheControl,
                "no-store"
            )

            val solicitud = call.receive<SolicitudSesion>()

            val sesion = AuthSessionStore.intercambiar(
                ticket = solicitud.ticket,
                verificador = solicitud.verificador
            )

            if (sesion == null) {
                call.respond(
                    HttpStatusCode.Unauthorized,
                    mapOf("error" to "Ticket invalido o expirado")
                )
                return@post
            }

            call.respond(sesion)
        }

        // ==========================================
        // 7. CONSULTAR USUARIO AUTENTICADO
        // ==========================================
        get("/api/user") {

            val token = call.obtenerTokenBearer()

            val usuario = token?.let {
                AuthSessionStore.obtenerUsuario(it)
            }

            if (usuario == null) {
                call.respond(
                    HttpStatusCode.Unauthorized,
                    mapOf("error" to "Usuario no autenticado")
                )
                return@get
            }

            call.respond(usuario.aDatosUsuario())
        }

        // ==========================================
        // 8. CERRAR SESION
        // ==========================================
        post("/auth/logout") {

            val token = call.obtenerTokenBearer()

            if (token == null) {
                call.respond(
                    HttpStatusCode.Unauthorized,
                    mapOf("error" to "Sesion no valida")
                )
                return@post
            }

            AuthSessionStore.cerrarSesion(token)

            call.respond(
                mapOf("mensaje" to "Sesion cerrada")
            )
        }

    }
}
