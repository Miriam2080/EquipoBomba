
package com.equipobomba.app

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PerfilMiriamScreen(
    fotoPerfil: ImageBitmap?,
    onSeleccionarFoto: () -> Unit,
    onVolver: () -> Unit
) {
    val azul = Color(0xFF0A66C2)

    // Nuevo fondo azul oceano pastel
    val fondoOceano = Color(0xFFDDF3FA)

    // Datos fijos de Miriam
    val nombre = "Miriam Janneth Gómez Aguilar"
    val carrera = "Ingeniería en Desarrollo y Gestión de Software"
    val materia = "Integración de aplicaciones móviles con servicios en la nube"
    val descripcion = "Soy estudiante de Ingeniería en Desarrollo " +
            "y Gestión de Software. Me interesa el desarrollo " +
            "de aplicaciones móviles y aprender nuevas tecnologías."

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(fondoOceano)
            .safeContentPadding()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "MI PERFIL",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = azul
        )

        Spacer(Modifier.height(24.dp))

        if (fotoPerfil != null) {
            Image(
                bitmap = fotoPerfil,
                contentDescription = "Foto de Miriam",
                modifier = Modifier
                    .size(160.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        } else {
            Box(
                modifier = Modifier
                    .size(160.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFDCEAF9)),
                contentAlignment = Alignment.Center
            ) {
                Text("Sin foto", color = azul)
            }
        }

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = onSeleccionarFoto,
            colors = ButtonDefaults.buttonColors(
                containerColor = azul
            )
        ) {
            Text("Seleccionar mi foto")
        }

        Spacer(Modifier.height(24.dp))

        DatoMiriam("Nombre completo", nombre)

        DatoMiriam("Carrera", carrera)

        DatoMiriam("Materia", materia)

        DatoMiriam("Sobre mí", descripcion)

        Spacer(Modifier.height(16.dp))

        OutlinedButton(
            onClick = onVolver,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Regresar a Acerca de nosotros")
        }
    }
}

@Composable
private fun DatoMiriam(
    titulo: String,
    contenido: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = titulo,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0A66C2)
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = contenido,
                fontSize = 16.sp,
                color = Color.DarkGray
            )
        }
    }
}
