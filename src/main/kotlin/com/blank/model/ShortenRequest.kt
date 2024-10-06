package com.blank.model

import kotlinx.serialization.Serializable

@Serializable
data class ShortenRequest(val originalUrl: String?, val expireDate: String? = null)