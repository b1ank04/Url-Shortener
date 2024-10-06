package com.blank.plugins

import io.ktor.server.application.*
import io.ktor.server.plugins.swagger.*
import io.ktor.server.routing.*

fun Application.swagger() {
    routing {
        val documentationPath = System.getenv("DOCUMENTATION_PATH") ?: "src/main/resources/documentation.yaml"
        swaggerUI(path = "swagger", swaggerFile = documentationPath)
    }
}