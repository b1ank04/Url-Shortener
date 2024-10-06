package com.blank.exceptions

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import org.slf4j.LoggerFactory

fun Application.exceptionHandler() {

    install(StatusPages) {
        val logger = LoggerFactory.getLogger("ExceptionLogger")

        exception<IllegalArgumentException> { call, cause ->
            logger.error("Illegal argument error: ${cause.message}", cause)
            call.respond(HttpStatusCode.BadRequest, "Invalid input: ${cause.message}")
        }
        
        exception<IllegalStateException> { call, cause ->
            logger.error("Illegal state error: ${cause.message}", cause)
            call.respond(HttpStatusCode.Conflict, "Invalid application state: ${cause.message}")
        }

        exception<NoSuchElementException> { call, cause ->
            logger.error("Resource not found: ${cause.message}", cause)
            call.respond(HttpStatusCode.NotFound, "Resource not found: ${cause.message}")
        }

        exception<Exception> { call, cause ->
            logger.error("An unexpected error occurred: ${cause.message}", cause)
            call.respond(HttpStatusCode.InternalServerError, "An unexpected error occurred: ${cause.message}")
        }
    }

}