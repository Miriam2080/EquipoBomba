
package com.equipobomba

import com.nimbusds.jose.JWSAlgorithm
import com.nimbusds.jose.jwk.source.RemoteJWKSet
import com.nimbusds.jose.proc.JWSVerificationKeySelector
import com.nimbusds.jose.proc.SecurityContext
import com.nimbusds.jwt.proc.DefaultJWTProcessor
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.forms.submitForm
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.parameters
import kotlinx.serialization.json.*
import java.net.URI
import java.time.Instant
import java.util.Date

// Datos del usuario autenticado con LinkedIn.
data class UsuarioLinkedIn(
    val id: String,
    val nombre: String,
    val correo: String?,
    val foto: String?
)

object LinkedInAuthService {

    // Cliente HTTP para comunicarse con LinkedIn.
    private val cliente = HttpClient(CIO) {
        expectSuccess = false
    }

    // Verificador de la firma digital del ID Token.
    private val procesadorJwt =
        DefaultJWTProcessor<SecurityContext>().apply {

            // Claves publicas oficiales de LinkedIn.
            val clavesLinkedIn =
                RemoteJWKSet<SecurityContext>(
                    URI(
                        "https://www.linkedin.com/oauth/openid/jwks"
                    ).toURL()
                )

            // Solo aceptamos tokens firmados con RS256.
            jwsKeySelector = JWSVerificationKeySelector(
                JWSAlgorithm.RS256,
                clavesLinkedIn
            )
        }

    // Obtener un campo de texto desde una respuesta JSON.
    private fun JsonObject.cadena(campo: String): String? {
        return this[campo]?.jsonPrimitive?.contentOrNull
    }

    // Autenticar al usuario utilizando el codigo de LinkedIn.
    suspend fun autenticar(codigo: String): UsuarioLinkedIn {

        // ==========================================
        // 1. OBTENER LAS CREDENCIALES DEL SERVIDOR
        // ==========================================

        val clientId =
            System.getenv("LINKEDIN_CLIENT_ID")
                ?.takeIf { it.isNotBlank() }
                ?: error("Falta configurar LINKEDIN_CLIENT_ID")

        val clientSecret =
            System.getenv("LINKEDIN_CLIENT_SECRET")
                ?.takeIf { it.isNotBlank() }
                ?: error("Falta configurar LINKEDIN_CLIENT_SECRET")

        val redirectUri =
            System.getenv("LINKEDIN_REDIRECT_URI")
                ?.takeIf { it.isNotBlank() }
                ?: error("Falta configurar LINKEDIN_REDIRECT_URI")

        // ==========================================
        // 2. INTERCAMBIAR EL CODIGO POR TOKENS
        // ==========================================

        val respuesta = cliente.submitForm(
            url = "https://www.linkedin.com/oauth/v2/accessToken",
            formParameters = parameters {
                append("grant_type", "authorization_code")
                append("code", codigo)
                append("client_id", clientId)
                append("client_secret", clientSecret)
                append("redirect_uri", redirectUri)
            }
        )

        if (respuesta.status != HttpStatusCode.OK) {
            error("LinkedIn rechazo el intercambio de tokens")
        }

        val tokens = Json.parseToJsonElement(
            respuesta.bodyAsText()
        ).jsonObject

        val accessToken = tokens.cadena("access_token")
            ?: error("LinkedIn no devolvio access_token")

        val idToken = tokens.cadena("id_token")
            ?: error("LinkedIn no devolvio id_token")

        // ==========================================
        // 3. VERIFICAR LA FIRMA DEL ID TOKEN
        // ==========================================

        val claims = procesadorJwt.process(idToken, null)

        // ==========================================
        // 4. VALIDAR EL EMISOR DEL TOKEN
        // ==========================================


        check(claims.issuer == "https://www.linkedin.com/oauth") {
            "Emisor de token invalido"
        }


        // ==========================================
        // 5. VALIDAR LA APLICACION DESTINATARIA
        // ==========================================

        check(clientId in claims.audience) {
            "El token no pertenece a EquipoBomba"
        }

        // ==========================================
        // 6. VALIDAR LA FECHA DEL TOKEN
        // ==========================================

        val ahora = Date()

        val fechaEmision = claims.issueTime
        val fechaExpiracion = claims.expirationTime

        check(
            fechaExpiracion != null &&
                    fechaExpiracion.after(ahora) &&
                    fechaEmision != null &&
                    !fechaEmision.after(
                        Date.from(Instant.now().plusSeconds(60))
                    )
        ) {
            "Token expirado o con fecha invalida"
        }

        // ==========================================
        // 7. OBTENER EL IDENTIFICADOR DEL USUARIO
        // ==========================================

        val identificador = claims.subject

        check(!identificador.isNullOrBlank()) {
            "El token no contiene un identificador de usuario"
        }

        // ==========================================
        // 8. CONSULTAR EL PERFIL EN LINKEDIN
        // ==========================================

        val respuestaUsuario = cliente.get(
            "https://api.linkedin.com/v2/userinfo"
        ) {
            header(
                HttpHeaders.Authorization,
                "Bearer $accessToken"
            )
        }

        if (respuestaUsuario.status != HttpStatusCode.OK) {
            error("No fue posible consultar el perfil de LinkedIn")
        }

        val perfil = Json.parseToJsonElement(
            respuestaUsuario.bodyAsText()
        ).jsonObject

        // ==========================================
        // 9. COMPROBAR QUE SEA EL MISMO USUARIO
        // ==========================================

        check(perfil.cadena("sub") == identificador) {
            "El perfil no coincide con el token validado"
        }

        // ==========================================
        // 10. DEVOLVER LOS DATOS A EQUIPOBOMBA
        // ==========================================

        return UsuarioLinkedIn(
            id = identificador,
            nombre = perfil.cadena("name")
                ?: "Usuario de LinkedIn",
            correo = perfil.cadena("email"),
            foto = perfil.cadena("picture")
        )
    }
}
