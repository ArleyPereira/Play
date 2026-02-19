package br.com.hellodev.play.application

import android.app.Application
import br.com.hellodev.di.initializeKoin
import org.koin.android.ext.koin.androidContext

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        initializeKoin {
            androidContext(this@MainApplication)
        }
    }
}
