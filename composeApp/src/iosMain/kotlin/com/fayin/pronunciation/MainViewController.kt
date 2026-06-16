package com.fayin.pronunciation

import androidx.compose.ui.window.ComposeUIViewController
import com.fayin.pronunciation.data.repository.EntryRepository
import com.fayin.pronunciation.data.repository.GroupRepository
import com.fayin.pronunciation.service.phonetics.ChinesePhoneticGenerator
import com.fayin.pronunciation.service.phonetics.EnglishPhoneticGenerator
import com.fayin.pronunciation.service.tts.PlatformTtsEngine
import com.fayin.pronunciation.service.tts.TtsService

private var initialized = false

fun MainViewController() = ComposeUIViewController {
    if (!initialized) {
        initialized = true
        val driver = DatabaseDriverFactory().createDriver()
        val db = com.fayin.pronunciation.db.PronunciationDatabase(driver)
        val chinese = ChinesePhoneticGenerator()
        val english = EnglishPhoneticGenerator()
        val tts = TtsService(PlatformTtsEngine())
        Dependencies.groupRepository = GroupRepository(db)
        Dependencies.entryRepository = EntryRepository(db, chinese, english)
        Dependencies.ttsService = tts
        Dependencies.chinesePhoneticGenerator = chinese
        Dependencies.englishPhoneticGenerator = english
    }
    App()
}
