package com.blank

import com.blank.model.ShortenRequest
import com.blank.repository.RedisRepository
import com.blank.service.UrlShorteningService
import com.blank.utils.parseLocalDateTime
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.assertThrows

class UrlShorteningServiceTest {

    private lateinit var redisRepository: RedisRepository
    private lateinit var urlShorteningService: UrlShorteningService

    @BeforeEach
    fun setUp() {
        redisRepository = mockk()
        urlShorteningService = UrlShorteningService(redisRepository)
    }

    @Test
    fun `generate should return a hashed URL and store it in Redis`() = runBlocking {
        val originalUrl = "https://example.com"
        val request = ShortenRequest(originalUrl = originalUrl)
        coEvery { redisRepository.exists(any()) } returns false
        coEvery { redisRepository.set(any(), any()) } returns Unit
        
        val result = urlShorteningService.generate(request)
        
        assertNotNull(result)
        assertEquals(8, result.length)
        coVerify { redisRepository.set(result, originalUrl) }
    }

    @Test
    fun `generate should store URL with expiration when expireDate is provided`() = runBlocking {
        val originalUrl = "https://example.com"
        val expireDate = "2023-12-31T23:59:59"
        val request = ShortenRequest(originalUrl = originalUrl, expireDate = expireDate)
        coEvery { redisRepository.exists(any()) } returns false
        coEvery { redisRepository.setExpired(any(), any(), any()) } returns Unit
        
        val result = urlShorteningService.generate(request)
        
        assertNotNull(result)
        coVerify { redisRepository.setExpired(result, originalUrl, parseLocalDateTime(expireDate)) }
    }

    @Test
    fun `generate should throw IllegalArgumentException when URL is empty`() {
        val request = ShortenRequest(originalUrl = null)
        
        assertThrows<IllegalArgumentException> {
            runBlocking { urlShorteningService.generate(request) }
        }
    }

    @Test
    fun `generate should retry up to 5 times on hash collision`() = runBlocking {
        val originalUrl = "https://example.com"
        val request = ShortenRequest(originalUrl = originalUrl)
        coEvery { redisRepository.exists(any()) } returnsMany listOf(true, true, true, true, false)
        coEvery { redisRepository.set(any(), any()) } returns Unit
        
        val result = urlShorteningService.generate(request)
        
        assertNotNull(result)
        coVerify(exactly = 5) { redisRepository.exists(any()) }
    }

    @Test
    fun `generate should throw IllegalStateException after 5 failed attempts`() {
        val originalUrl = "https://example.com"
        val request = ShortenRequest(originalUrl = originalUrl)
        coEvery { redisRepository.exists(any()) } returns true
        
        assertThrows<IllegalStateException> {
            runBlocking { urlShorteningService.generate(request) }
        }
    }

    @Test
    fun `retrieve should return the original URL for a valid hash`() = runBlocking {
        val hash = "abcdefgh"
        val originalUrl = "https://example.com"
        coEvery { redisRepository.get(hash) } returns originalUrl
        
        val result = urlShorteningService.retrieve(hash)
        
        assertEquals(originalUrl, result)
    }

    @Test
    fun `retrieve should throw NoSuchElementException for an invalid hash`() {
        val hash = "invalidhash"
        coEvery { redisRepository.get(hash) } returns null
        
        assertThrows<NoSuchElementException> {
            runBlocking { urlShorteningService.retrieve(hash) }
        }
    }
}