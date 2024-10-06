package com.blank.plugins

import com.blank.router.urlRouting
import io.ktor.server.application.Application

fun Application.configureRouting() {
    urlRouting()
    swagger()
}