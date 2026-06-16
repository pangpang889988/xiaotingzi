package com.fayin.pronunciation

import android.app.Application
import com.fayin.pronunciation.data.repository.EntryRepository
import com.fayin.pronunciation.data.repository.GroupRepository
import com.fayin.pronunciation.service.phonetics.ChinesePhoneticGenerator
import com.fayin.pronunciation.service.phonetics.EnglishPhoneticGenerator
import com.fayin.pronunciation.service.tts.PlatformTtsEngine
import com.fayin.pronunciation.service.tts.TtsService

class PronunciationApp : Application() {
    override fun onCreate() {
        super.onCreate()
        val driver = DatabaseDriverFactory(this).createDriver()
        val db = com.fayin.pronunciation.db.PronunciationDatabase(driver)
        val chinese = ChinesePhoneticGenerator()
        val english = EnglishPhoneticGenerator()
        val tts = TtsService(PlatformTtsEngine(this))
        Dependencies.groupRepository = GroupRepository(db)
        Dependencies.entryRepository = EntryRepository(db, chinese, english)
        Dependencies.ttsService = tts
        Dependencies.chinesePhoneticGenerator = chinese
        Dependencies.englishPhoneticGenerator = english
    }
}
