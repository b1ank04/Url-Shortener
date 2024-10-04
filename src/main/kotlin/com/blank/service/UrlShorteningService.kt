package com.blank.service

import com.blank.redis.RedisRepository
import java.security.MessageDigest
import java.time.Instant
import java.util.*

class UrlShorteningService(private val redisRepository: RedisRepository) {


    suspend fun generate(url: String): String {
        val hashed = generateUniqueShortUrl(url)
        redisRepository.set(hashed, url)
        return hashed
    }
    
    suspend fun retrieve(hashed: String): String? {
        return redisRepository.get(hashed)
    }
    
    private suspend fun generateUniqueShortUrl(url: String): String {
        var hashed = hash(url)
        var attempts = 0
        while (redisRepository.exists(url) && attempts <= 5) {
            hashed = hash(url + UUID.randomUUID().toString())
            attempts++
        }

        if (attempts >= 5) {
            throw IllegalStateException("Failed to generate a unique short URL after 5 attempts")
        }
        
        return hashed
    }
    
    private fun hash(input: String): String {
        val timestamp = Instant.now().toEpochMilli()
        val uniqueUrl = "$input|$timestamp"
        val digest =  MessageDigest.getInstance("SHA-256").digest(uniqueUrl.toByteArray())
        return Base64.getUrlEncoder().withoutPadding().encodeToString(digest).substring(0, 8)
    }
    
}