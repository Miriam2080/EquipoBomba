
plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(ktorLibs.plugins.ktor)
    alias(libs.plugins.kotlin.serialization)
}

group = "com.equipobomba"
version = "1.0.0-SNAPSHOT"

application {
    mainClass = "com.equipobomba.MainKt"
}

kotlin {
    jvmToolchain(21)
}

dependencies {
    // Dependencias existentes del servidor Ktor
    implementation(ktorLibs.serialization.kotlinx.json)
    implementation(ktorLibs.server.callLogging)
    implementation(ktorLibs.server.contentNegotiation)
    implementation(ktorLibs.server.core)
    implementation(ktorLibs.server.netty)
    implementation(libs.logback.classic)


    // Cliente HTTP para comunicarse con LinkedIn
    implementation("io.ktor:ktor-client-core:3.6.0")
    implementation("io.ktor:ktor-client-cio:3.6.0")
    implementation("io.ktor:ktor-client-content-negotiation:3.6.0")
    implementation("com.nimbusds:nimbus-jose-jwt:10.10")


    // Pruebas
    testImplementation(kotlin("test"))
    testImplementation(ktorLibs.server.testHost)
}
