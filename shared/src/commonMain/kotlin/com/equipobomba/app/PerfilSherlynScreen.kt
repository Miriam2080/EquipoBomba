
package com.equipobomba.app

import androidx.compose.foundation.background
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Azul = Color(0xFF1565C0)
private val AzulOscuro = Color(0xFF0D47A1)
private val Fondo = Color(0xFFF2F6FC)

@Composable
fun PerfilSherlynScreen(
    onVolver: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Fondo)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // Encabezado
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.verticalGradient(
                        listOf(Azul, AzulOscuro)
                    ),
                    shape = RoundedCornerShape(
                        bottomStart = 32.dp,
                        bottomEnd = 32.dp
                    )
                )
                .padding(
                    top = 45.dp,
                    bottom = 35.dp,
                    start = 20.dp,
                    end = 20.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Avatar
            Box(
                modifier = Modifier
                    .size(115.dp)
                    .clip(CircleShape)
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "SG",
                    fontSize = 42.sp,
                    fontWeight = FontWeight.Bold,
                    color = AzulOscuro
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = "Sherlyn Guillén Mérida",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Estudiante de Ingeniería",
                fontSize = 15.sp,
                color = Color(0xFFE3F2FD)
            )
        }

        Spacer(modifier = Modifier.height(25.dp))

        // Información académica
        TarjetaPerfil(
            titulo = "Información académica",
            contenido = listOf(
                "Carrera" to
                        "Ingeniería en Desarrollo y Gestión de Software",
                "Universidad" to
                        "Universidad Tecnológica de la Selva",
                "Proyecto" to "EquipoBomba",
                "Tecnología" to "Kotlin Multiplatform"
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Información personal
        TarjetaPerfil(
            titulo = "Sobre mí",
            contenido = listOf(
                "Especialidad" to
                        "Desarrollo de aplicaciones",
                "Intereses" to
                        "Programación y tecnología",
                "Objetivo" to
                        "Seguir desarrollando mis habilidades profesionales"
            )
        )

        Spacer(modifier = Modifier.height(25.dp))

        Button(
            onClick = onVolver,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 25.dp)
                .height(52.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = AzulOscuro
            )
        ) {
            Text(
                text = "Volver al menú",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(30.dp))
    }
}

@Composable
private fun TarjetaPerfil(
    titulo: String,
    contenido: List<Pair<String, String>>
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 5.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = titulo,
                fontSize = 19.sp,
                fontWeight = FontWeight.Bold,
                color = AzulOscuro
            )

            Spacer(modifier = Modifier.height(14.dp))

            contenido.forEach { (etiqueta, valor) ->
                Text(
                    text = etiqueta,
                    fontSize = 13.sp,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(3.dp))

                Text(
                    text = valor,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF263238)
                )

                Spacer(modifier = Modifier.height(15.dp))
            }
        }
    }
}
