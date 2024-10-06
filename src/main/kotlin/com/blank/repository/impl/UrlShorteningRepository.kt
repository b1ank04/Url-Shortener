package com.blank.repository.impl

import com.blank.repository.RedisRepository
import io.lettuce.core.ExperimentalLettuceCoroutinesApi
import io.lettuce.core.api.coroutines.RedisCoroutinesCommands
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

@OptIn(ExperimentalLettuceCoroutinesApi::class)
class UrlShorteningRepository(private val redisCommands: RedisCoroutinesCommands<String, String>) : RedisRepository {

    override suspend fun set(hashed: String, originalUrl: String) {
        executeWithHandling("set") { redisCommands.set(hashed, originalUrl) }
    }

    override suspend fun setExpired(hashed: String, originalUrl: String, expirationDateTime: LocalDateTime) {
        val lifespan = ChronoUnit.SECONDS.between(LocalDateTime.now(), expirationDateTime)
        executeWithHandling("setex") { redisCommands.setex(hashed, lifespan, originalUrl) }
    }

    override suspend fun get(hashed: String): String? {
        return executeWithHandling("get") { redisCommands.get(hashed) }
    }

    override suspend fun exists(hashed: String): Boolean {
        return executeWithHandling("exists") { redisCommands.exists(hashed)!! > 0 }
    }

    private suspend fun <T> executeWithHandling(operationName: String, operation: suspend () -> T): T {
        return try {
            operation()
        } catch (e: Exception) {
            throw IllegalStateException("Error executing Redis operation: $operationName, cause: ${e.message}", e)
        }
    }
}
