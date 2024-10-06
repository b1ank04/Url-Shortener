package com.blank.router

import com.blank.model.ShortenRequest
import com.blank.service.UrlShorteningService
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject


fun Application.urlRouting() {
    val urlShorteningService by inject<UrlShorteningService>()

    routing {
        post("/v1/shorten") {
            val request = runCatching { call.receive<ShortenRequest>() }
            request.fold(
                onSuccess = { shortenRequest ->
                    call.respondText("${getApplicationDomain(call)}/${urlShorteningService.generate(shortenRequest)}")
                },
                onFailure = {
                    throw IllegalArgumentException("Wrong request body.")
                }
            )
        }

        get("/{link}") {
            val link = call.parameters["link"] ?: throw IllegalArgumentException("Empty link provided.")
            urlShorteningService.retrieve(link).let { call.respondRedirect(prependProtocol(it)) }
        }
    }
}

private fun getApplicationDomain(call: ApplicationCall): String {
    val scheme = call.request.origin.scheme // "http" or "https"

    val host = call.request.host()
    val port = call.request.port().takeIf { it != 80 && it != 443 } // non-standard ports

    return if (port != null) {
        "$scheme://$host:$port"
    } else {
        "$scheme://$host"
    }
}

private fun prependProtocol(link: String): String {
    return if (link.startsWith("http://") || link.startsWith("https://")) {
        link
    } else {
        "http://$link"
    }
}
