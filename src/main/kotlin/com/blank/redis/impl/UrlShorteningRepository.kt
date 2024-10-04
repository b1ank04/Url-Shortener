package com.blank.redis.impl

import com.blank.redis.RedisRepository
import io.lettuce.core.ExperimentalLettuceCoroutinesApi
import io.lettuce.core.api.coroutines.RedisCoroutinesCommands

@OptIn(ExperimentalLettuceCoroutinesApi::class)
class UrlShorteningRepository(private val redisCommands: RedisCoroutinesCommands<String, String>) : RedisRepository {

    override suspend fun set(hashed: String, url: String) {
        try {
            redisCommands.set(hashed, url)
        } catch (e: Exception) {
            throw IllegalStateException("Error saving URL to Redis: ${e.message}")
        }
    }

    override suspend fun get(hashed: String): String? {
        return try {
            redisCommands.get(hashed)
        } catch (e: Exception) {
            throw IllegalStateException("Error retrieving URL from Redis: ${e.message}")
        }
    }

    override suspend fun exists(hashed: String): Boolean {
        return try {
            redisCommands.exists(hashed)!! > 0
        } catch (e: Exception) {
            throw IllegalStateException("Error checking URL existence in Redis: ${e.message}")
        }
    }
}
