package com.fayin.pronunciation.di

import com.fayin.pronunciation.db.PronunciationDatabase
import com.fayin.pronunciation.data.repository.EntryRepository
import com.fayin.pronunciation.data.repository.GroupRepository
import com.fayin.pronunciation.service.phonetics.ChinesePhoneticGenerator
import com.fayin.pronunciation.service.phonetics.EnglishPhoneticGenerator
import com.fayin.pronunciation.service.tts.TtsService
import com.fayin.pronunciation.ui.edit.EntryEditViewModel
import com.fayin.pronunciation.ui.entrylist.EntryListViewModel
import com.fayin.pronunciation.ui.group.GroupViewModel
import com.fayin.pronunciation.ui.settings.VoiceSettingsViewModel
import org.koin.dsl.module

val appModule = module {
    single { PronunciationDatabase(get<com.fayin.pronunciation.DatabaseDriverFactory>().createDriver()) }
    single { ChinesePhoneticGenerator() }
    single { EnglishPhoneticGenerator() }
    single { GroupRepository(get()) }
    single { EntryRepository(get(), get(), get()) }
    single { TtsService(get()) }
    factory { GroupViewModel(get()) }
    factory { EntryListViewModel(get(), get()) }
    factory { EntryEditViewModel(get(), get(), get(), get()) }
    factory { VoiceSettingsViewModel(get()) }
}
