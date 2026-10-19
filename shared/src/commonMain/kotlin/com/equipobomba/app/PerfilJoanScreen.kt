
package com.equipobomba.app

import androidx.compose.runtime.Composable

@Composable
fun PerfilJoanScreen(
    onVolver: () -> Unit
) {
    PerfilVacioScreen(
        nombre = "Joan Sebastian Gómez Hernández",
        onVolver = onVolver
    )
}
