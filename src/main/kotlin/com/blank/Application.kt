package com.blank

import com.blank.exceptions.exceptionHandler
import com.blank.plugins.*
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    configureSerialization()
    configureFrameworks()
    configureRouting()
    exceptionHandler()
}
