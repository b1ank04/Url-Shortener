package com.blank.router

import com.blank.service.UrlShorteningService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject


fun Application.urlRouting() {
    val urlShorteningService by inject<UrlShorteningService>()
    
    routing {
        post("/") {
            val link = call.parameters["original"] ?: throw IllegalArgumentException("Empty link provided.")
            call.respondText(constructFullUrl(call) + urlShorteningService.generate(link))
        }

        get("/{link}") {
            val link = call.parameters["link"] ?: throw IllegalArgumentException("Empty link provided.")
            urlShorteningService.retrieve(link)?.let { call.respondRedirect(it) }
        }
    }
}

private fun constructFullUrl(call: ApplicationCall): String {
    val scheme = call.request.origin.scheme // "http" or "https"

    val host = call.request.host()
    val port = call.request.port().takeIf { it != 80 && it != 443 } // non-standard ports

    return if (port != null) {
        "$scheme://$host:$port/"
    } else {
        "$scheme://$host/"
    }
}
