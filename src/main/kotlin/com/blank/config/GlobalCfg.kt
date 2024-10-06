package com.blank.config

import io.ktor.server.config.*

fun ApplicationConfig.getRedisUrl(): String {
    return this.propertyOrNull("ktor.redis.url")?.getString() ?: throw IllegalArgumentException("Redis URL not configured")
}
