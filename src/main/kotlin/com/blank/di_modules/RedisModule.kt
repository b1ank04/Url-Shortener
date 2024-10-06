package com.blank.di_modules

import io.lettuce.core.ExperimentalLettuceCoroutinesApi
import io.lettuce.core.RedisClient
import io.lettuce.core.api.coroutines
import org.koin.dsl.module


@OptIn(ExperimentalLettuceCoroutinesApi::class)
fun redisModule(redisUrl: String) = module {
    single {
        RedisClient.create(redisUrl)
    }

    single {
        val redisClient = get<RedisClient>()
        redisClient.connect().coroutines()
    }
}
