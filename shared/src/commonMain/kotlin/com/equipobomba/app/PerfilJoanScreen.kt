
package com.equipobomba.app

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val JoanCarbon = Color(0xFF292824)
private val JoanArena = Color(0xFFE8DFD0)
private val JoanFondo = Color(0xFFF6F3EE)
private val JoanGris = Color(0xFF625F58)

// Tema local: no cambia los colores de los perfiles de los compañeros.
private val JoanColores = lightColorScheme(
    primary = JoanCarbon,
    onPrimary = Color.White,
    primaryContainer = JoanArena,
    onPrimaryContainer = JoanCarbon,
    secondary = JoanGris,
    secondaryContainer = JoanArena,
    onSecondaryContainer = JoanCarbon,
    background = JoanFondo,
    onBackground = JoanCarbon,
    surface = Color(0xFFFFFDFA),
    onSurface = JoanCarbon,
    surfaceVariant = JoanArena,
    onSurfaceVariant = JoanGris,
    outline = Color(0xFF79746B)
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PerfilJoanScreen(
    onVolver: () -> Unit,
    fotoPerfil: ImageBitmap? = null,
    onSeleccionarFoto: () -> Unit = {}
) {

    // Presentacion editable original de Joan
    var presentacion by rememberSaveable {
        mutableStateOf(
            "Soy estudiante de Ingeniería en Desarrollo y Gestión de Software. " +
                    "Me interesa crear aplicaciones útiles, cuidar los detalles de la interfaz " +
                    "y aprender a conectar soluciones móviles con servicios en la nube."
        )
    }

    var editando by rememberSaveable {
        mutableStateOf(false)
    }

    var borrador by rememberSaveable {
        mutableStateOf("")
    }

    var intereses by rememberSaveable {
        mutableStateOf(
            listOf("Apps móviles", "Diseño de interfaces")
        )
    }

    var completas by rememberSaveable {
        mutableStateOf(emptyList<String>())
    }

    val metas = listOf(
        "interfaz" to "Crear una interfaz clara y accesible",
        "nube" to "Conectar una app con un servicio en la nube",
        "equipo" to "Documentar y compartir lo aprendido con el equipo"
    )

    MaterialTheme(colorScheme = JoanColores) {

        Surface(
            modifier = Modifier.fillMaxSize(),
            color = JoanFondo
        ) {

            Box(
                modifier = Modifier.safeContentPadding(),
                contentAlignment = Alignment.TopCenter
            ) {

                Column(
                    modifier = Modifier
                        .widthIn(max = 640.dp)
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {

                    // Navegacion
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextButton(onClick = onVolver) {
                            Text("‹ Volver")
                        }

                        Text(
                            text = "EQUIPO BOMBA / JOAN",
                            fontSize = 11.sp,
                            color = JoanGris
                        )
                    }

                    // Encabezado del perfil
                    Surface(
                        color = JoanCarbon,
                        shape = RoundedCornerShape(28.dp)
                    ) {

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {

                            // NUEVO: Fotografia de perfil de Joan
                            if (fotoPerfil != null) {

                                Image(
                                    bitmap = fotoPerfil,
                                    contentDescription = "Foto de Joan",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .size(76.dp)
                                        .clip(CircleShape)
                                )

                            } else {

                                Box(
                                    modifier = Modifier
                                        .size(76.dp)
                                        .background(
                                            JoanArena,
                                            CircleShape
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {

                                    Text(
                                        text = "JG",
                                        fontSize = 28.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = JoanCarbon
                                    )
                                }
                            }

                            // NUEVO: Boton para seleccionar fotografia
                            OutlinedButton(
                                onClick = onSeleccionarFoto,
                                colors = ButtonDefaults.outlinedButtonColors(
                                    containerColor = JoanArena,
                                    contentColor = JoanCarbon
                                )
                            ) {
                                Text(
                                    text = if (fotoPerfil == null) {
                                        "Seleccionar mi foto"
                                    } else {
                                        "Cambiar mi foto"
                                    },
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            // Informacion original
                            Text(
                                text = "APRENDER. CREAR. MEJORAR.",
                                fontSize = 11.sp,
                                letterSpacing = 2.sp,
                                color = JoanArena
                            )

                            Text(
                                text = "Joan Sebastian\nGómez Hernández",
                                fontSize = 30.sp,
                                lineHeight = 36.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )

                            Text(
                                text = "Estudiante de ingeniería · EquipoBomba",
                                color = JoanArena
                            )
                        }
                    }

                    // 01 - Sobre mi
                    JoanSeccion("01", "Sobre mí") {

                        Text(
                            text = presentacion,
                            lineHeight = 24.sp
                        )

                        OutlinedButton(
                            onClick = {
                                borrador = presentacion
                                editando = true
                            }
                        ) {
                            Text("Editar presentación")
                        }
                    }

                    // 02 - Mi formacion
                    JoanSeccion("02", "Mi formación") {

                        JoanDato(
                            "CARRERA",
                            "Ingeniería en Desarrollo y Gestión de Software"
                        )

                        HorizontalDivider(color = JoanArena)

                        JoanDato(
                            "MATERIA",
                            "Integración de aplicaciones móviles con servicios en la nube"
                        )

                        HorizontalDivider(color = JoanArena)

                        JoanDato(
                            "PROYECTO",
                            "EquipoBomba · Kotlin Multiplatform"
                        )
                    }

                    // 03 - Intereses
                    JoanSeccion("03", "Lo que me interesa") {

                        Text(
                            text = "Selecciona los temas que quieres explorar.",
                            color = JoanGris
                        )

                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {

                            listOf(
                                "Apps móviles",
                                "Diseño de interfaces",
                                "Servicios en la nube",
                                "Desarrollo web",
                                "Trabajo en equipo"
                            ).forEach { tema ->

                                FilterChip(
                                    selected = tema in intereses,
                                    onClick = {
                                        intereses = if (tema in intereses) {
                                            intereses - tema
                                        } else {
                                            intereses + tema
                                        }
                                    },
                                    label = {
                                        Text(tema)
                                    }
                                )
                            }
                        }
                    }

                    // 04 - Ruta de aprendizaje
                    JoanSeccion("04", "Mi ruta de aprendizaje") {

                        Text(
                            text = "Metas personales",
                            fontWeight = FontWeight.Medium
                        )

                        Text(
                            text = "${completas.size} de ${metas.size} completadas",
                            color = JoanGris
                        )

                        LinearProgressIndicator(
                            progress = {
                                completas.size.toFloat() / metas.size
                            },
                            modifier = Modifier.fillMaxWidth(),
                            color = JoanCarbon,
                            trackColor = JoanArena
                        )

                        metas.forEach { (id, texto) ->

                            val marcada = id in completas

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .toggleable(
                                        value = marcada,
                                        role = Role.Checkbox,
                                        onValueChange = { valor ->
                                            completas = if (valor) {
                                                completas + id
                                            } else {
                                                completas - id
                                            }
                                        }
                                    )
                                    .padding(vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {

                                Checkbox(
                                    checked = marcada,
                                    onCheckedChange = null
                                )

                                Text(
                                    text = texto,
                                    modifier = Modifier.weight(1f),
                                    lineHeight = 22.sp
                                )
                            }
                        }

                        if (completas.size == metas.size) {
                            Text(
                                text = "¡Ruta completada! Cada paso cuenta.",
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    Text(
                        text = "Personaliza tu presentación, intereses y metas. Los cambios son temporales " +
                                "y se reinician al salir del perfil.",
                        style = MaterialTheme.typography.bodySmall,
                        color = JoanGris
                    )

                    Button(
                        onClick = onVolver,
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(16.dp),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text("Volver a Acerca de nosotros")
                    }
                }
            }
        }

        // Ventana original para editar presentacion
        if (editando) {

            AlertDialog(
                onDismissRequest = {
                    editando = false
                },
                title = {
                    Text("Mi presentación")
                },
                text = {
                    OutlinedTextField(
                        value = borrador,
                        onValueChange = {
                            if (it.length <= 400) {
                                borrador = it
                            }
                        },
                        label = {
                            Text("Sobre mí")
                        },
                        supportingText = {
                            Text("${borrador.length}/400 caracteres")
                        },
                        minLines = 4,
                        maxLines = 7,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                confirmButton = {
                    TextButton(
                        enabled = borrador.isNotBlank(),
                        onClick = {
                            presentacion = borrador.trim()
                            editando = false
                        }
                    ) {
                        Text("Guardar")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            editando = false
                        }
                    ) {
                        Text("Cancelar")
                    }
                }
            )
        }
    }
}

@Composable
private fun JoanSeccion(
    numero: String,
    titulo: String,
    contenido: @Composable ColumnScope.() -> Unit
) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {

        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            Text(
                text = numero,
                fontSize = 12.sp,
                letterSpacing = 2.sp,
                color = JoanGris
            )

            Text(
                text = titulo,
                fontSize = 21.sp,
                fontWeight = FontWeight.SemiBold
            )

            contenido()
        }
    }
}

@Composable
private fun JoanDato(
    etiqueta: String,
    valor: String
) {

    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {

        Text(
            text = etiqueta,
            fontSize = 11.sp,
            letterSpacing = 1.sp,
            color = JoanGris
        )

        Text(
            text = valor,
            lineHeight = 23.sp
        )
    }
}
