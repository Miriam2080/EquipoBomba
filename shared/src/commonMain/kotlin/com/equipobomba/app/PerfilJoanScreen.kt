package com.equipobomba.app

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
import androidx.compose.ui.graphics.Color
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
    primary = JoanCarbon, onPrimary = Color.White,
    primaryContainer = JoanArena, onPrimaryContainer = JoanCarbon,
    secondary = JoanGris, secondaryContainer = JoanArena,
    onSecondaryContainer = JoanCarbon,
    background = JoanFondo, onBackground = JoanCarbon,
    surface = Color(0xFFFFFDFA), onSurface = JoanCarbon,
    surfaceVariant = JoanArena, onSurfaceVariant = JoanGris,
    outline = Color(0xFF79746B)
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PerfilJoanScreen(onVolver: () -> Unit) {
    // Estado restaurable durante la vida de la pantalla; no es almacenamiento permanente.
    var presentacion by rememberSaveable {
        mutableStateOf("Soy estudiante de Ingeniería en Desarrollo y Gestión de Software. " +
            "Me interesa crear aplicaciones útiles, cuidar los detalles de la interfaz " +
            "y aprender a conectar soluciones móviles con servicios en la nube.")
    }
    var editando by rememberSaveable { mutableStateOf(false) }
    var borrador by rememberSaveable { mutableStateOf("") }
    var intereses by rememberSaveable { mutableStateOf(listOf("Apps móviles", "Diseño de interfaces")) }
    var completas by rememberSaveable { mutableStateOf(emptyList<String>()) }
    val metas = listOf(
        "interfaz" to "Crear una interfaz clara y accesible",
        "nube" to "Conectar una app con un servicio en la nube",
        "equipo" to "Documentar y compartir lo aprendido con el equipo"
    )

    MaterialTheme(colorScheme = JoanColores) {
        Surface(modifier = Modifier.fillMaxSize(), color = JoanFondo) {
            Box(Modifier.safeContentPadding(), contentAlignment = Alignment.TopCenter) {
                Column(
                    Modifier.widthIn(max = 640.dp).fillMaxWidth()
                        .verticalScroll(rememberScrollState()).padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically) {
                        TextButton(onClick = onVolver) { Text("‹ Volver") }
                        Text("EQUIPO BOMBA / JOAN", fontSize = 11.sp, color = JoanGris)
                    }
                    Surface(color = JoanCarbon, shape = RoundedCornerShape(28.dp)) {
                        Column(Modifier.fillMaxWidth().padding(24.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)) {
                            Box(Modifier.size(76.dp).background(JoanArena, CircleShape),
                                contentAlignment = Alignment.Center) {
                                Text("JG", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = JoanCarbon)
                            }
                            Text("APRENDER. CREAR. MEJORAR.", fontSize = 11.sp,
                                letterSpacing = 2.sp, color = JoanArena)
                            Text("Joan Sebastian\nGómez Hernández", fontSize = 30.sp,
                                lineHeight = 36.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            Text("Estudiante de ingeniería · EquipoBomba", color = JoanArena)
                        }
                    }
                    JoanSeccion("01", "Sobre mí") {
                        Text(presentacion, lineHeight = 24.sp)
                        OutlinedButton(onClick = { borrador = presentacion; editando = true }) {
                            Text("Editar presentación")
                        }
                    }
                    JoanSeccion("02", "Mi formación") {
                        JoanDato("CARRERA", "Ingeniería en Desarrollo y Gestión de Software")
                        HorizontalDivider(color = JoanArena)
                        JoanDato("MATERIA", "Integración de aplicaciones móviles con servicios en la nube")
                        HorizontalDivider(color = JoanArena)
                        JoanDato("PROYECTO", "EquipoBomba · Kotlin Multiplatform")
                    }
                    JoanSeccion("03", "Lo que me interesa") {
                        Text("Selecciona los temas que quieres explorar.", color = JoanGris)
                        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            listOf("Apps móviles", "Diseño de interfaces", "Servicios en la nube",
                                "Desarrollo web", "Trabajo en equipo").forEach { tema ->
                                FilterChip(selected = tema in intereses,
                                    onClick = {
                                        intereses = if (tema in intereses) intereses - tema else intereses + tema
                                    }, label = { Text(tema) })
                            }
                        }
                    }
                    JoanSeccion("04", "Mi ruta de aprendizaje") {
                        Text("Metas personales", fontWeight = FontWeight.Medium)
                        Text("${completas.size} de ${metas.size} completadas", color = JoanGris)
                        LinearProgressIndicator(progress = { completas.size.toFloat() / metas.size },
                            modifier = Modifier.fillMaxWidth(), color = JoanCarbon, trackColor = JoanArena)
                        metas.forEach { (id, texto) ->
                            val marcada = id in completas
                            Row(Modifier.fillMaxWidth()
                                .toggleable(value = marcada, role = Role.Checkbox,
                                    onValueChange = { valor ->
                                        completas = if (valor) completas + id else completas - id
                                    }).padding(vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                Checkbox(checked = marcada, onCheckedChange = null)
                                Text(texto, modifier = Modifier.weight(1f), lineHeight = 22.sp)
                            }
                        }
                        if (completas.size == metas.size) {
                            Text("¡Ruta completada! Cada paso cuenta.", fontWeight = FontWeight.SemiBold)
                        }
                    }
                    Text("Personaliza tu presentación, intereses y metas. Los cambios son temporales " +
                        "y se reinician al salir del perfil.",
                        style = MaterialTheme.typography.bodySmall, color = JoanGris)
                    Button(onClick = onVolver, modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(16.dp), shape = RoundedCornerShape(16.dp)) {
                        Text("Volver a Acerca de nosotros")
                    }
                }
            }
        }
        if (editando) {
            AlertDialog(onDismissRequest = { editando = false },
                title = { Text("Mi presentación") },
                text = {
                    OutlinedTextField(value = borrador,
                        onValueChange = { if (it.length <= 400) borrador = it },
                        label = { Text("Sobre mí") },
                        supportingText = { Text("${borrador.length}/400 caracteres") },
                        minLines = 4, maxLines = 7, modifier = Modifier.fillMaxWidth())
                },
                confirmButton = {
                    TextButton(enabled = borrador.isNotBlank(), onClick = {
                        presentacion = borrador.trim()
                        editando = false
                    }) { Text("Guardar") }
                },
                dismissButton = {
                    TextButton(onClick = { editando = false }) { Text("Cancelar") }
                })
        }
    }
}

@Composable
private fun JoanSeccion(numero: String, titulo: String, contenido: @Composable ColumnScope.() -> Unit) {
    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
        Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(numero, fontSize = 12.sp, letterSpacing = 2.sp, color = JoanGris)
            Text(titulo, fontSize = 21.sp, fontWeight = FontWeight.SemiBold)
            contenido()
        }
    }
}

@Composable
private fun JoanDato(etiqueta: String, valor: String) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(etiqueta, fontSize = 11.sp, letterSpacing = 1.sp, color = JoanGris)
        Text(valor, lineHeight = 23.sp)
    }
}
