package com.fayin.pronunciation

import androidx.compose.ui.window.ComposeUIViewController
import com.fayin.pronunciation.di.appModule
import com.fayin.pronunciation.service.tts.PlatformTtsEngine
import org.koin.core.context.startKoin
import org.koin.dsl.module

fun MainViewController() = ComposeUIViewController {
    val platformModule = module {
        single { DatabaseDriverFactory() }
        single { PlatformTtsEngine() }
    }
    startKoin { modules(appModule, platformModule) }
    App()
}
