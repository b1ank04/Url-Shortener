package com.blank.di_modules

import com.blank.repository.impl.UrlShorteningRepository
import com.blank.service.UrlShorteningService
import io.lettuce.core.ExperimentalLettuceCoroutinesApi
import org.koin.dsl.module

@OptIn(ExperimentalLettuceCoroutinesApi::class)
val serviceModule = module {

    single {
        UrlShorteningRepository(get())
    }

    single {
        UrlShorteningService(get<UrlShorteningRepository>())
    }
}
