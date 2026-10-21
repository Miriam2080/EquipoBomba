package com.equipobomba

import io.ktor.server.application.Application

fun Application.rootModule() {
    configureSerialization()
    configureRouting()
}
