package br.com.hellodev.di

import br.com.hellodev.main.di.mainModule
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin

private val playModules = listOf(
    mainModule,
)

fun initializeKoin(
    config: (KoinApplication.() -> Unit)? = null,
) {
    startKoin {
        config?.invoke(this)
        modules(playModules)
    }
}
