package com.blank.exceptions

import com.blank.router.urlRouting
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*

fun Application.module() {

//    install(StatusPages) {
//        exception<IllegalArgumentException> { cause ->
//            call.respond(HttpStatusCode.BadRequest, "Invalid input: ${cause.message}")
//        }
//
//        exception<NoSuchElementException> { cause ->
//            call.respond(HttpStatusCode.NotFound, "Resource not found: ${cause.message}")
//        }
//
//        exception<Exception> { cause ->
//            call.respond(HttpStatusCode.InternalServerError, "An unexpected error occurred: ${cause.message}")
//        }
//    }

    
}