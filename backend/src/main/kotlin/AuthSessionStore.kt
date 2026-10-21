
package com.equipobomba

import kotlinx.serialization.Serializable
import java.security.MessageDigest
import java.security.SecureRandom
import java.util.Base64
import java.util.concurrent.ConcurrentHashMap

// Solicitud que enviara Android para canjear el ticket.
@Serializable
data class SolicitudSesion(
    val ticket: String,
    val verificador: String
)

// Datos que devolvera el backend a Android.
@Serializable
data class DatosUsuario(
    val id: String,
    val nombre: String,
    val correo: String?,
    val foto: String?
)

@Serializable
data class RespuestaSesion(
    val token: String,
    val usuario: DatosUsuario
)

fun UsuarioLinkedIn.aDatosUsuario() = DatosUsuario(
    id = id,
    nombre = nombre,
    correo = correo,
    foto = foto
)

object AuthSessionStore {

    private data class TicketPendiente(
        val usuario: UsuarioLinkedIn,
        val desafio: String,
        val vencimiento: Long
    )

    private data class SesionActiva(
        val usuario: UsuarioLinkedIn,
        val vencimiento: Long
    )

    private val tickets =
        ConcurrentHashMap<String, TicketPendiente>()

    private val sesiones =
        ConcurrentHashMap<String, SesionActiva>()

    private val random = SecureRandom()

    private val formatoDesafio =
        Regex("^[A-Za-z0-9_-]{43}$")

    private val formatoVerificador =
        Regex("^[A-Za-z0-9._~-]{43,128}$")

    private fun generarToken(): String {
        val bytes = ByteArray(32)
        random.nextBytes(bytes)

        return Base64.getUrlEncoder()
            .withoutPadding()
            .encodeToString(bytes)
    }

    // Crear ticket de un solo uso, valido por 90 segundos.
    fun crearTicket(
        usuario: UsuarioLinkedIn,
        desafio: String
    ): String {

        require(formatoDesafio.matches(desafio)) {
            "Desafio de Android invalido"
        }

        val ticket = generarToken()

        tickets[ticket] = TicketPendiente(
            usuario = usuario,
            desafio = desafio,
            vencimiento = System.currentTimeMillis() + 90_000
        )

        return ticket
    }

    // Android entrega ticket y verificador para obtener su sesion.
    fun intercambiar(
        ticket: String,
        verificador: String
    ): RespuestaSesion? {

        if (!formatoVerificador.matches(verificador)) {
            return null
        }

        val pendiente = tickets[ticket] ?: return null

        if (System.currentTimeMillis() >= pendiente.vencimiento) {
            tickets.remove(ticket, pendiente)
            return null
        }

        val hash = MessageDigest.getInstance("SHA-256")
            .digest(verificador.toByteArray(Charsets.US_ASCII))

        val desafioCalculado = Base64.getUrlEncoder()
            .withoutPadding()
            .encodeToString(hash)

        if (!MessageDigest.isEqual(
                desafioCalculado.toByteArray(Charsets.US_ASCII),
                pendiente.desafio.toByteArray(Charsets.US_ASCII)
            )
        ) {
            return null
        }

        // Evita utilizar dos veces el mismo ticket.
        if (!tickets.remove(ticket, pendiente)) {
            return null
        }

        val tokenSesion = generarToken()

        // Sesion valida por dos horas.
        sesiones[tokenSesion] = SesionActiva(
            usuario = pendiente.usuario,
            vencimiento = System.currentTimeMillis() + 7_200_000
        )

        return RespuestaSesion(
            token = tokenSesion,
            usuario = pendiente.usuario.aDatosUsuario()
        )
    }

    // Consultar la identidad de una sesion existente.
    fun obtenerUsuario(token: String): UsuarioLinkedIn? {

        val sesion = sesiones[token] ?: return null

        if (System.currentTimeMillis() >= sesion.vencimiento) {
            sesiones.remove(token, sesion)
            return null
        }

        return sesion.usuario
    }

    // Cerrar una sesion.
    fun cerrarSesion(token: String) {
        sesiones.remove(token)
    }
}
