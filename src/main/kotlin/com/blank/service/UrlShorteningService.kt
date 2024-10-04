package com.blank.service

import io.lettuce.core.ExperimentalLettuceCoroutinesApi
import io.lettuce.core.api.coroutines.RedisCoroutinesCommands
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.security.MessageDigest
import java.time.Instant
import java.util.*


@OptIn(ExperimentalLettuceCoroutinesApi::class)
class UrlShorteningService(private val redisRepository: RedisCoroutinesCommands<String, String>) {


    suspend fun generate(url: String): String {
        val hashed: String = generateUniqueShortUrl(url)
        runBlocking {
            redisRepository.set(hashed, url)
        }
        return hashed
    }
    fun retrieve(hashed: String): String? {
        return runBlocking {
            redisRepository.get(hashed)
        }
    }
    
    private suspend fun generateUniqueShortUrl(url: String): String {
        var hashed = hash(url)
        var attempts = 0
        while (redisContains(url) && attempts <= 5) {
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
    
    private suspend fun redisContains(hashed: String): Boolean {
        return withContext(Dispatchers.IO) {
            redisRepository.exists(hashed)
        }!! > 0
    }
}