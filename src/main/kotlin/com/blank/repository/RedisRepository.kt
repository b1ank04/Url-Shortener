package com.blank.repository

import java.time.LocalDateTime

interface RedisRepository {
    suspend fun set(hashed: String, originalUrl: String)
    suspend fun setExpired(hashed: String, originalUrl: String, expirationDateTime: LocalDateTime)
    suspend fun get(hashed: String): String?
    suspend fun exists(hashed: String): Boolean
}
