package br.com.hellodev.di

import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.dsl.module

private val playModules = listOf(
    module { }
)

fun initializeKoin(
    config: (KoinApplication.() -> Unit)? = null,
) {
    startKoin {
        config?.invoke(this)
        modules(playModules)
    }
}
