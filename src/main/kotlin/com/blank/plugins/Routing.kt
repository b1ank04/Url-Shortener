package com.blank.plugins

import com.blank.service.UrlShorteningService
import io.github.smiley4.ktorswaggerui.SwaggerUI
import io.ktor.server.application.*
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.configureRouting() {
    
    val urlShorteningService by inject<UrlShorteningService>()
    routing {
        post("/") {
            val link = call.parameters["original"]?: throw IllegalArgumentException("Empty link provided.")
            call.respondText(urlShorteningService.generate(link))
        }
        
        get("/{link}") {
            val link = call.parameters["link"]?: throw IllegalArgumentException("Empty link provided.")
            urlShorteningService.retrieve(link)?.let { call.respondRedirect(it) }
        }
    }
}
