package com.blank.plugins

import com.blank.redis.impl.UrlShorteningRepository
import com.blank.service.UrlShorteningService
import io.ktor.server.application.*
import io.lettuce.core.ExperimentalLettuceCoroutinesApi
import io.lettuce.core.RedisClient
import io.lettuce.core.api.coroutines
import io.lettuce.core.api.coroutines.RedisCoroutinesCommands
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

@OptIn(ExperimentalLettuceCoroutinesApi::class)
fun Application.configureFrameworks() {
    install(Koin) {
        slf4jLogger()
        modules(
            module {

                single { RedisClient.create("redis://localhost:6379") }
                
                single { get<RedisClient>().connect().coroutines() }
                
                single { UrlShorteningRepository(get<RedisCoroutinesCommands<String, String>>())}
                
                single { UrlShorteningService(get<UrlShorteningRepository>()) }
                
            }
        )
    }
}
