package com.blank.redis

import reactor.core.publisher.Mono

interface RedisRepository {
    suspend fun set(hashed: String, url: String)
    suspend fun get(hashed: String): String?
    suspend fun exists(hashed: String): Boolean
}
