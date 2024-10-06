package com.blank.plugins

import com.blank.config.getRedisUrl
import com.blank.di_modules.redisModule
import com.blank.di_modules.serviceModule
import io.ktor.server.application.*
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

fun Application.configureFrameworks() {
    install(Koin) {
        slf4jLogger()
        modules(redisModule(environment.config.getRedisUrl()), serviceModule,)
    }
}


