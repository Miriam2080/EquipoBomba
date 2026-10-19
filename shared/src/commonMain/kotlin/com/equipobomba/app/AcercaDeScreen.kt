
package com.equipobomba.app

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AcercaDeScreen(
    onMiriam: () -> Unit,
    onSherlyn: () -> Unit,
    onJoan: () -> Unit,
    onAlfredo: () -> Unit,
    onVolver: () -> Unit
) {
    val azul = Color(0xFF0A66C2)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F7FA))
            .safeContentPadding()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "ACERCA DE NOSOTROS",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = azul,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(16.dp))

        Text(
            text = "Somos Equipo Bomba, estudiantes de " +
                    "Ingeniería en Desarrollo y Gestión de Software.",
            fontSize = 16.sp,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(30.dp))

        Text(
            text = "Integrantes del equipo",
            fontSize = 21.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(20.dp))

        IntegranteBoton(
            "1. Miriam Janneth Gómez Aguilar",
            onMiriam
        )

        IntegranteBoton(
            "2. Sherlyn Guillén Mérida",
            onSherlyn
        )

        IntegranteBoton(
            "3. Joan Sebastian Gómez Hernández",
            onJoan
        )

        IntegranteBoton(
            "4. Alfredo Ton Gómez",
            onAlfredo
        )

        Spacer(Modifier.height(20.dp))

        OutlinedButton(
            onClick = onVolver,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Regresar al menú")
        }
    }
}

@Composable
private fun IntegranteBoton(
    nombre: String,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp)
            .heightIn(min = 56.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF0A66C2)
        )
    ) {
        Text(
            text = nombre,
            textAlign = TextAlign.Center,
            fontSize = 15.sp
        )
    }
}

// Plantilla para los perfiles todavía no desarrollados.
@Composable
fun PerfilVacioScreen(
    nombre: String,
    onVolver: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F7FA))
            .safeContentPadding()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = nombre,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = Color(0xFF0A66C2)
        )

        Spacer(Modifier.height(28.dp))

        OutlinedButton(
            onClick = onVolver,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Regresar a Acerca de nosotros")
        }
    }
}
