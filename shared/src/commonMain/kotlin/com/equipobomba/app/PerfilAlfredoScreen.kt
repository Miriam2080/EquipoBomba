
package com.equipobomba.app

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val MoradoOscuroAlf = Color(0xFF251247)
private val MoradoAlf = Color(0xFF6D28D9)
private val LilaAlf = Color(0xFFEDE9FE)
private val FondoAlf = Color(0xFFF7F5FC)

@Composable
fun PerfilAlfredoScreen(
    fotoPerfil: ImageBitmap?,
    onSeleccionarFoto: () -> Unit,
    onVolver: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FondoAlf)
            .safeContentPadding()
            .verticalScroll(rememberScrollState())
    ) {

        // ENCABEZADO MORADO
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.linearGradient(
                        listOf(MoradoOscuroAlf, MoradoAlf)
                    ),
                    shape = RoundedCornerShape(
                        bottomStart = 30.dp,
                        bottomEnd = 30.dp
                    )
                )
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "EQUIPOBOMBA",
                color = Color(0xFFDDD6FE),
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "PERFIL DE ALFREDO",
                color = Color.White,
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(24.dp))

            // FOTO EXCLUSIVA DE ALFREDO
            if (fotoPerfil != null) {
                Image(
                    bitmap = fotoPerfil,
                    contentDescription = "Fotografía de Alfredo",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(145.dp)
                        .border(4.dp, Color.White, CircleShape)
                        .clip(CircleShape)
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(145.dp)
                        .border(4.dp, Color.White, CircleShape)
                        .clip(CircleShape)
                        .background(LilaAlf),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "AT",
                        color = MoradoAlf,
                        fontSize = 48.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = "Alfredo Ton Gómez",
                color = Color.White,
                fontSize = 23.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Estudiante de Ingeniería de Software",
                color = Color(0xFFE9D5FF),
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )
        }

        // CONTENIDO DEL PERFIL
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Button(
                onClick = onSeleccionarFoto,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(15.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MoradoAlf
                )
            ) {
                Text(
                    text = "Cambiar mi fotografía",
                    modifier = Modifier.padding(5.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "INFORMACIÓN PERSONAL",
                color = MoradoOscuroAlf,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            DatoAlfredoNuevo(
                titulo = "Nombre completo",
                contenido = "Alfredo Ton Gómez"
            )

            DatoAlfredoNuevo(
                titulo = "Carrera",
                contenido = "Ingeniería en Desarrollo y Gestión de Software"
            )

            DatoAlfredoNuevo(
                titulo = "Materia",
                contenido = "Integración de aplicaciones móviles " +
                        "con servicios en la nube"
            )

            DatoAlfredoNuevo(
                titulo = "Sobre mí",
                contenido = "Soy estudiante de Ingeniería en Desarrollo " +
                        "y Gestión de Software. Me interesa el desarrollo " +
                        "de aplicaciones móviles, Machine Learning y " +
                        "desarrollo web."
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedButton(
                onClick = onVolver,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(15.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = MoradoAlf
                )
            ) {
                Text(
                    text = "Regresar a Acerca de nosotros",
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun DatoAlfredoNuevo(
    titulo: String,
    contenido: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 3.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(18.dp)
        ) {
            Text(
                text = titulo,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = MoradoAlf
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = contenido,
                fontSize = 15.sp,
                color = MoradoOscuroAlf,
                lineHeight = 22.sp
            )
        }
    }
}
