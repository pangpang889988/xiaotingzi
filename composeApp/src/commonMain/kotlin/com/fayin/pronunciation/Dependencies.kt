package com.fayin.pronunciation

import com.fayin.pronunciation.data.repository.EntryRepository
import com.fayin.pronunciation.data.repository.GroupRepository
import com.fayin.pronunciation.service.phonetics.ChinesePhoneticGenerator
import com.fayin.pronunciation.service.phonetics.EnglishPhoneticGenerator
import com.fayin.pronunciation.service.tts.TtsService

object Dependencies {
    lateinit var groupRepository: GroupRepository
    lateinit var entryRepository: EntryRepository
    lateinit var ttsService: TtsService
    lateinit var chinesePhoneticGenerator: ChinesePhoneticGenerator
    lateinit var englishPhoneticGenerator: EnglishPhoneticGenerator
}
