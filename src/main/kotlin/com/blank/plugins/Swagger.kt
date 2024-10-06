package com.blank.plugins

import io.ktor.server.application.*
import io.ktor.server.plugins.swagger.*
import io.ktor.server.routing.*

fun Application.swagger() {
    routing {
        swaggerUI(path = "swagger", swaggerFile = "src/main/resources/documentation.yaml")
    }
}