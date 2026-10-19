
package com.equipobomba.app

import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.tooling.preview.Preview

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {

            // Fotografía seleccionada
            var foto by remember {
                mutableStateOf<ImageBitmap?>(null)
            }

            // Abrir la galería del celular
            val selectorFoto = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.GetContent()
            ) { uri ->

                if (uri != null) {

                    val imagen = runCatching {
                        contentResolver.openInputStream(uri)?.use {
                            BitmapFactory.decodeStream(it)
                                ?.asImageBitmap()
                        }
                    }.getOrNull()

                    if (imagen != null) {
                        foto = imagen
                    }
                }
            }

            // Mostrar aplicación principal
            App(
                fotoPerfil = foto,
                onSeleccionarFoto = {
                    selectorFoto.launch("image/*")
                }
            )
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}
