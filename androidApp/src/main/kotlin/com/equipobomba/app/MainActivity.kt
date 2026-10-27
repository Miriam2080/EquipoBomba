package com.equipobomba.app

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.security.MessageDigest
import java.security.SecureRandom
import java.io.File

private const val BACKEND_URL =
    "https://stuck-stunner-guzzler.ngrok-free.dev"

private const val PREFERENCIAS = "equipobomba_auth_temp"
private const val CLAVE_VERIFICADOR = "oauth_verificador"
private const val CLAVE_FECHA = "oauth_fecha"

private data class ResultadoSesion(
    val token: String,
    val nombre: String
)

class MainActivity : ComponentActivity() {

    // Enlace que recibimos al regresar desde LinkedIn.
    private var enlacePendiente by mutableStateOf<Uri?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        recibirEnlace(intent)

        setContent {


            // Buscar logo_equipobomba.png en androidApp/src/main/res/drawable.
            // Si todavia no existe, la aplicacion seguira compilando y
            // mostrara el titulo normal hasta que agregues la imagen.
            val logoEquipoBomba = remember {
                val idLogo = resources.getIdentifier(
                    "logo_equipobomba", "drawable", packageName
                )
                if (idLogo != 0) {
                    BitmapFactory.decodeResource(resources, idLogo)
                        ?.asImageBitmap()
                } else {
                    null
                }
            }


            // Fotografia seleccionada en la galeria.

            // Archivo donde guardaremos la foto de Miriam.
            val archivoFoto = File(filesDir, "foto_miriam")

            // Cargar la foto guardada cuando se abre la aplicacion.
            var foto by remember {
                mutableStateOf(
                    runCatching {
                        if (archivoFoto.exists()) {
                            BitmapFactory.decodeFile(
                                archivoFoto.absolutePath
                            )?.asImageBitmap()
                        } else {
                            null
                        }
                    }.getOrNull()
                )
            }


// ========================================
// FOTO INDEPENDIENTE DE JOAN
// ========================================

            val archivoFotoJoan = File(
                filesDir,
                "foto_joan"
            )

// Cargar fotografia guardada
            var fotoJoan by remember {
                mutableStateOf(
                    runCatching {
                        if (archivoFotoJoan.exists()) {
                            BitmapFactory.decodeFile(
                                archivoFotoJoan.absolutePath
                            )?.asImageBitmap()
                        } else {
                            null
                        }
                    }.getOrNull()
                )
            }

// Abrir galeria y seleccionar foto
            val selectorFotoJoan = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.GetContent()
            ) { uri ->

                if (uri != null) {

                    val imagen = runCatching {

                        contentResolver.openInputStream(uri)?.use { entrada ->
                            archivoFotoJoan.outputStream().use { salida ->
                                entrada.copyTo(salida)
                            }
                        } ?: error("No se pudo abrir la fotografia")

                        BitmapFactory.decodeFile(
                            archivoFotoJoan.absolutePath
                        )?.asImageBitmap()
                            ?: error("No se pudo cargar la fotografia")

                    }.getOrNull()

                    if (imagen != null) {

                        fotoJoan = imagen

                    } else {

                        Toast.makeText(
                            this@MainActivity,
                            "No fue posible guardar la foto de Joan",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }



// Fotografia independiente de Alfredo
            val archivoFotoAlfredo = File(filesDir, "foto_alfredo")

            var fotoAlfredo by remember {
                mutableStateOf(
                    runCatching {
                        if (archivoFotoAlfredo.exists()) {
                            BitmapFactory.decodeFile(
                                archivoFotoAlfredo.absolutePath
                            )?.asImageBitmap()
                        } else {
                            null
                        }
                    }.getOrNull()
                )
            }

// Selector exclusivo de Alfredo
            val selectorFotoAlfredo = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.GetContent()
            ) { uri ->

                if (uri != null) {
                    val imagen = runCatching {

                        contentResolver.openInputStream(uri)?.use { entrada ->
                            archivoFotoAlfredo.outputStream().use { salida ->
                                entrada.copyTo(salida)
                            }
                        } ?: error("No se pudo abrir la fotografía")

                        BitmapFactory.decodeFile(
                            archivoFotoAlfredo.absolutePath
                        )?.asImageBitmap()
                            ?: error("No se pudo cargar la fotografía")

                    }.getOrNull()

                    if (imagen != null) {
                        fotoAlfredo = imagen
                    } else {
                        Toast.makeText(
                            this@MainActivity,
                            "No fue posible guardar la foto de Alfredo",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }

// ==========================================
// FOTOGRAFIA INDEPENDIENTE DE SHERLYN
// ==========================================

            val archivoFotoSherlyn = File(
                filesDir,
                "foto_sherlyn"
            )

// Cargar la foto guardada al abrir EquipoBomba
            var fotoSherlyn by remember {
                mutableStateOf(
                    runCatching {
                        if (archivoFotoSherlyn.exists()) {
                            BitmapFactory.decodeFile(
                                archivoFotoSherlyn.absolutePath
                            )?.asImageBitmap()
                        } else {
                            null
                        }
                    }.getOrNull()
                )
            }

// Selector exclusivo de Sherlyn
            val selectorFotoSherlyn = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.GetContent()
            ) { uri ->

                if (uri != null) {

                    val imagen = runCatching {

                        contentResolver.openInputStream(uri)?.use { entrada ->
                            archivoFotoSherlyn.outputStream().use { salida ->
                                entrada.copyTo(salida)
                            }
                        } ?: error("No se pudo abrir la fotografía")

                        BitmapFactory.decodeFile(
                            archivoFotoSherlyn.absolutePath
                        )?.asImageBitmap()
                            ?: error("No se pudo cargar la fotografía")

                    }.getOrNull()

                    if (imagen != null) {
                        fotoSherlyn = imagen
                    } else {
                        Toast.makeText(
                            this@MainActivity,
                            "No fue posible guardar la foto de Sherlyn",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }




            // Token propio de EquipoBomba.
            // Se mantiene solo en memoria durante esta prueba.
            var tokenSesion by remember {
                mutableStateOf<String?>(null)
            }

            val scope = rememberCoroutineScope()

            // Selector de fotografias.

            val selectorFoto = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.GetContent()
            ) { uri ->

                if (uri != null) {

                    val imagen = runCatching {

                        // Copiar la imagen seleccionada al almacenamiento
                        // privado de EquipoBomba.
                        contentResolver.openInputStream(uri)?.use { entrada ->
                            archivoFoto.outputStream().use { salida ->
                                entrada.copyTo(salida)
                            }
                        } ?: error("No se pudo abrir la fotografia")

                        // Cargar la fotografia que acabamos de guardar.
                        BitmapFactory.decodeFile(
                            archivoFoto.absolutePath
                        )?.asImageBitmap()
                            ?: error("No se pudo cargar la fotografia")

                    }.getOrNull()

                    if (imagen != null) {
                        foto = imagen
                    } else {
                        Toast.makeText(
                            this,
                            "No fue posible guardar la fotografia",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }


            // Procesar el ticket recibido de Ktor.
            LaunchedEffect(enlacePendiente) {

                val enlace = enlacePendiente
                    ?: return@LaunchedEffect

                try {
                    val ticket = enlace.getQueryParameter("ticket")

                    if (ticket.isNullOrBlank()) {
                        error("No se recibio el ticket de autenticacion")
                    }

                    val preferencias = getSharedPreferences(
                        PREFERENCIAS,
                        MODE_PRIVATE
                    )

                    val verificador = preferencias.getString(
                        CLAVE_VERIFICADOR,
                        null
                    )

                    val fecha = preferencias.getLong(
                        CLAVE_FECHA,
                        0L
                    )

                    val tiempoTranscurrido =
                        System.currentTimeMillis() - fecha

                    if (
                        verificador.isNullOrBlank() ||
                        tiempoTranscurrido !in 0L..600_000L
                    ) {
                        error("La solicitud de inicio de sesion expiro")
                    }

                    // Impedir reutilizar el verificador local.
                    preferencias.edit()
                        .remove(CLAVE_VERIFICADOR)
                        .remove(CLAVE_FECHA)
                        .apply()

                    // Consultar Ktor fuera del hilo principal.
                    val sesion = withContext(Dispatchers.IO) {
                        intercambiarTicket(ticket, verificador)
                    }

                    // Solo después de la respuesta válida
                    // consideramos iniciada la sesión.
                    tokenSesion = sesion.token

                    Toast.makeText(
                        this@MainActivity,
                        "Bienvenido, ${sesion.nombre}",
                        Toast.LENGTH_LONG
                    ).show()

                } catch (error: Exception) {

                    Log.e(
                        "EquipoBombaAuth",
                        "Error al iniciar sesion",
                        error
                    )

                    Toast.makeText(
                        this@MainActivity,
                        "No se pudo completar el inicio de sesion. " +
                                "Intenta nuevamente.",
                        Toast.LENGTH_LONG
                    ).show()

                } finally {
                    enlacePendiente = null
                }
            }


            App(
                logoEquipoBomba = logoEquipoBomba,

                // Fotografia de Miriam
                fotoPerfil = foto,

                onSeleccionarFoto = {
                    selectorFoto.launch("image/*")
                },

                // Fotografia independiente de Alfredo y sherlyn
                fotoAlfredo = fotoAlfredo,

                onSeleccionarFotoAlfredo = {
                    selectorFotoAlfredo.launch("image/*")
                },

                fotoSherlyn = fotoSherlyn,

                onSeleccionarFotoSherlyn = {
                    selectorFotoSherlyn.launch("image/*")
                },

                fotoJoan = fotoJoan,

                onSeleccionarFotoJoan = {
                    selectorFotoJoan.launch("image/*")
                },


                // Inicio de sesion con LinkedIn
                onIniciarLinkedIn = {
                    iniciarLinkedIn()
                },

                // Comprobar si existe una sesion
                sesionValida = tokenSesion != null,

                // Cerrar sesion correctamente
                onCerrarSesion = {

                    val tokenAnterior = tokenSesion

                    // Cerrar inmediatamente la sesion local
                    tokenSesion = null

                    if (tokenAnterior != null) {

                        scope.launch {

                            withContext(Dispatchers.IO) {

                                try {
                                    cerrarSesionBackend(tokenAnterior)

                                } catch (error: Exception) {

                                    Log.w(
                                        "EquipoBombaAuth",
                                        "No se pudo cerrar sesion en Ktor",
                                        error
                                    )
                                }
                            }
                        }
                    }
                }
            )

        }
    }

    // Android recibe el enlace mientras la app esta abierta.
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        recibirEnlace(intent)
    }

    private fun recibirEnlace(intent: Intent?) {

        val enlace = intent?.data ?: return

        if (
            enlace.scheme == "equipobomba" &&
            enlace.host == "auth" &&
            enlace.path == "/callback"
        ) {
            enlacePendiente = enlace
        }
    }

    // ==========================================
    // ABRIR LINKEDIN
    // ==========================================
    private fun iniciarLinkedIn() {

        try {
            val bytes = ByteArray(32)
            SecureRandom().nextBytes(bytes)

            // Código secreto temporal de Android.
            val verificador = Base64.encodeToString(
                bytes,
                Base64.URL_SAFE or
                        Base64.NO_PADDING or
                        Base64.NO_WRAP
            )

            val resumen = MessageDigest.getInstance("SHA-256")
                .digest(verificador.toByteArray(Charsets.US_ASCII))

            // Desafio asociado al verificador.
            val desafio = Base64.encodeToString(
                resumen,
                Base64.URL_SAFE or
                        Base64.NO_PADDING or
                        Base64.NO_WRAP
            )

            // Guardar el verificador temporal.
            getSharedPreferences(PREFERENCIAS, MODE_PRIVATE)
                .edit()
                .putString(CLAVE_VERIFICADOR, verificador)
                .putLong(CLAVE_FECHA, System.currentTimeMillis())
                .apply()

            // Construir URL del backend.
            val enlace = Uri.parse(BACKEND_URL)
                .buildUpon()
                .appendPath("auth")
                .appendPath("linkedin")
                .appendQueryParameter("code_challenge", desafio)
                .build()

            // Abrir el navegador.
            startActivity(
                Intent(Intent.ACTION_VIEW, enlace)
            )

        } catch (error: Exception) {

            Log.e(
                "EquipoBombaAuth",
                "No se pudo abrir LinkedIn",
                error
            )

            Toast.makeText(
                this,
                "No fue posible abrir LinkedIn",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    // ==========================================
    // INTERCAMBIAR TICKET POR SESION
    // ==========================================
    private fun intercambiarTicket(
        ticket: String,
        verificador: String
    ): ResultadoSesion {

        val conexion = URL(
            "$BACKEND_URL/auth/session"
        ).openConnection() as HttpURLConnection

        return try {

            conexion.requestMethod = "POST"
            conexion.connectTimeout = 15000
            conexion.readTimeout = 15000
            conexion.doOutput = true

            conexion.setRequestProperty(
                "Content-Type",
                "application/json; charset=utf-8"
            )

            conexion.setRequestProperty(
                "ngrok-skip-browser-warning",
                "true"
            )

            val datos = JSONObject()
                .put("ticket", ticket)
                .put("verificador", verificador)
                .toString()

            conexion.outputStream.bufferedWriter(
                Charsets.UTF_8
            ).use {
                it.write(datos)
            }

            if (conexion.responseCode != 200) {
                error(
                    "El backend rechazo la sesion: " +
                            conexion.responseCode
                )
            }

            val respuesta = conexion.inputStream
                .bufferedReader()
                .use { it.readText() }

            val json = JSONObject(respuesta)

            ResultadoSesion(
                token = json.getString("token"),
                nombre = json.getJSONObject("usuario")
                    .optString("nombre", "Usuario")
            )

        } finally {
            conexion.disconnect()
        }
    }

    // ==========================================
    // CERRAR SESION
    // ==========================================
    private fun cerrarSesionBackend(token: String) {

        val conexion = URL(
            "$BACKEND_URL/auth/logout"
        ).openConnection() as HttpURLConnection

        try {
            conexion.requestMethod = "POST"
            conexion.connectTimeout = 10000
            conexion.readTimeout = 10000

            conexion.setRequestProperty(
                "Authorization",
                "Bearer $token"
            )

            conexion.setRequestProperty(
                "ngrok-skip-browser-warning",
                "true"
            )

            conexion.responseCode

        } finally {
            conexion.disconnect()
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}
