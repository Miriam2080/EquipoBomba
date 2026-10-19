
package com.equipobomba.app

import androidx.compose.runtime.Composable

@Composable
fun PerfilSherlynScreen(
    onVolver: () -> Unit
) {
    PerfilVacioScreen(
        nombre = "Sherlyn Guillén Mérida",
        onVolver = onVolver
    )
}
