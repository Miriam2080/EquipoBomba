
package com.equipobomba.app

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

private val Azul = Color(0xFF0A66C2)
private val Fondo = Color(0xFFF5F7FA)



@Composable
fun App(
    fotoPerfil: ImageBitmap? = null,
    onSeleccionarFoto: () -> Unit = {}
) {
    var pantalla by remember {
        mutableStateOf("splash")
    }

    LaunchedEffect(Unit) {
        delay(3000)
        pantalla = "login"
    }

    MaterialTheme {
        when (pantalla) {

            "splash" -> SplashScreen()

            "login" -> LoginScreen(
                onVerPresentacion = {
                    pantalla = "menu"
                }
            )

            "menu" -> MenuPrincipalScreen(
                onAcercaDe = {
                    pantalla = "acerca"
                },
                onSalir = {
                    pantalla = "login"
                }
            )

            "acerca" -> AcercaDeScreen(
                onMiriam = { pantalla = "miriam" },
                onSherlyn = { pantalla = "sherlyn" },
                onJoan = { pantalla = "joan" },
                onAlfredo = { pantalla = "alfredo" },
                onVolver = { pantalla = "menu" }
            )

            "miriam" -> PerfilMiriamScreen(
                fotoPerfil = fotoPerfil,
                onSeleccionarFoto = onSeleccionarFoto,
                onVolver = { pantalla = "acerca" }
            )

            "sherlyn" -> PerfilSherlynScreen(
                onVolver = { pantalla = "acerca" }
            )

            "joan" -> PerfilJoanScreen(
                onVolver = { pantalla = "acerca" }
            )

            "alfredo" -> PerfilAlfredoScreen(
                onVolver = { pantalla = "acerca" }
            )
        }
    }
}



@Composable
fun SplashScreen() {
    var iniciarAnimacion by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(Unit) {
        iniciarAnimacion = true
    }

    val opacidad by animateFloatAsState(
        targetValue = if (iniciarAnimacion) 1f else 0f,
        animationSpec = tween(1200),
        label = "AnimacionSplash"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Azul)
            .alpha(opacidad)
            .safeContentPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "EB",
            fontSize = 65.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "EQUIPO BOMBA",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Desarrollo de aplicaciones móviles",
            fontSize = 15.sp,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(35.dp))

        CircularProgressIndicator(color = Color.White)
    }
}

@Composable
fun LoginScreen(onVerPresentacion: () -> Unit) {
    var mensaje by remember {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Fondo)
            .safeContentPadding()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "EQUIPO BOMBA",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = Azul
        )

        Spacer(modifier = Modifier.height(15.dp))

        Text(
            text = "Bienvenido",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Inicia sesión para continuar",
            fontSize = 16.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(35.dp))

        Button(
            onClick = {
                mensaje = "Autenticación con LinkedIn pendiente"
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Azul
            )
        ) {
            Text("Continuar con LinkedIn")
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(
            onClick = onVerPresentacion,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Ver mi presentación")
        }

        if (mensaje.isNotEmpty()) {
            Spacer(modifier = Modifier.height(15.dp))

            Text(
                text = mensaje,
                fontSize = 14.sp,
                color = Color.DarkGray,
                textAlign = TextAlign.Center
            )
        }
    }
}


@Composable
fun PerfilScreen(
    fotoPerfil: ImageBitmap?,
    onSeleccionarFoto: () -> Unit,
    onVolver: () -> Unit
) {
    // INFORMACIÓN FIJA DE LA PRESENTACIÓN

    val integrantes = """
    1. Miriam Janneth Gómez Aguilar
    2. Sherlyn Guillén Mérida
    3. Joan Sebastian Gómez Hernández
    4. Alfredo Ton Gómez
""".trimIndent()

    val carrera =
        "Ingeniería en Desarrollo y Gestión de Software"

    val materia =
        "Integración de aplicaciones móviles con servicios en la nube"

    val descripcion =
        "Soy estudiante de Ingeniería en Desarrollo y Gestión " +
                "de Software. Me interesa aprender sobre programación, " +
                "desarrollo de aplicaciones móviles y nuevas tecnologías."

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Fondo)
            .safeContentPadding()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "MI PRESENTACIÓN",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = Azul
        )

        Spacer(modifier = Modifier.height(24.dp))

        // MOSTRAR FOTOGRAFÍA
        if (fotoPerfil != null) {
            Image(
                bitmap = fotoPerfil,
                contentDescription = "Mi fotografía",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(150.dp)
                    .clip(CircleShape)
            )
        } else {
            Box(
                modifier = Modifier
                    .size(150.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFDCEAF9)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Sin foto",
                    color = Azul
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onSeleccionarFoto,
            colors = ButtonDefaults.buttonColors(
                containerColor = Azul
            )
        ) {
            Text("Subir mi foto")
        }

        Spacer(modifier = Modifier.height(25.dp))

        // INFORMACIÓN ESTÁTICA
        DatoPresentacion(
            titulo = "Integrantes",
            contenido = integrantes
        )

        Spacer(modifier = Modifier.height(12.dp))

        DatoPresentacion(
            titulo = "Carrera",
            contenido = carrera
        )

        Spacer(modifier = Modifier.height(12.dp))

        DatoPresentacion(
            titulo = "Materia",
            contenido = materia
        )

        Spacer(modifier = Modifier.height(12.dp))

        DatoPresentacion(
            titulo = "Sobre mí",
            contenido = descripcion
        )

        Spacer(modifier = Modifier.height(25.dp))

        OutlinedButton(
            onClick = onVolver,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Volver al inicio")
        }
    }
}

// DISEÑO DE LOS DATOS FIJOS
@Composable
fun DatoPresentacion(
    titulo: String,
    contenido: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = titulo,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Azul
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = contenido,
                fontSize = 16.sp,
                color = Color.DarkGray
            )
        }
    }
}

@Composable
fun MenuPrincipalScreen(
    onAcercaDe: () -> Unit,
    onSalir: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Fondo)
            .safeContentPadding()
            .padding(24.dp),

        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "EQUIPO BOMBA",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = Azul
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Bienvenido a nuestra aplicación",
            fontSize = 19.sp,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(45.dp))

        Button(
            onClick = onAcercaDe,
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Azul
            )
        ) {
            Text(
                text = "Acerca de nosotros",
                fontSize = 17.sp
            )
        }

        Spacer(modifier = Modifier.height(18.dp))

        OutlinedButton(
            onClick = onSalir,
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp)
        ) {
            Text(
                text = "Salir",
                fontSize = 17.sp
            )
        }
    }
}
