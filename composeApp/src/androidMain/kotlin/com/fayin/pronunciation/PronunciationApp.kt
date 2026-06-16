package com.fayin.pronunciation

import android.app.Application
import com.fayin.pronunciation.di.appModule
import com.fayin.pronunciation.service.tts.PlatformTtsEngine
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.dsl.module

class PronunciationApp : Application() {
    override fun onCreate() {
        super.onCreate()
        val platformModule = module {
            single { DatabaseDriverFactory(this@PronunciationApp) }
            single { PlatformTtsEngine(this@PronunciationApp) }
        }
        startKoin {
            androidContext(this@PronunciationApp)
            modules(appModule, platformModule)
        }
    }
}
