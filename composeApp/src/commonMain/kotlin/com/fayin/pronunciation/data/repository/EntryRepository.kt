package com.fayin.pronunciation.data.repository

import com.fayin.pronunciation.db.PronunciationDatabase
import com.fayin.pronunciation.platformCurrentTimeMillis
import com.fayin.pronunciation.service.language.LanguageDetector
import com.fayin.pronunciation.service.phonetics.ChinesePhoneticGenerator
import com.fayin.pronunciation.service.phonetics.EnglishPhoneticGenerator

class EntryRepository(
    private val db: PronunciationDatabase,
    private val chinesePinyin: ChinesePhoneticGenerator,
    private val englishPinyin: EnglishPhoneticGenerator
) {
    fun getByGroup(groupId: Long) = db.entryQueries.getByGroup(groupId).executeAsList()

    fun getById(id: Long) = db.entryQueries.getById(id).executeAsOneOrNull()

    fun create(groupId: Long, title: String, content: String): Long {
        val lang = LanguageDetector.detect(content)
        val pinyin = genPinyin(content, lang)
        val t = title.trim().ifBlank { content.take(20).trim() }
        db.entryQueries.insert(groupId, t, content.trim(), pinyin, lang, platformCurrentTimeMillis())
        return db.entryQueries.getByGroup(groupId).executeAsList().lastOrNull()?.id ?: 0L
    }

    fun update(id: Long, title: String, content: String) {
        val e = db.entryQueries.getById(id).executeAsOneOrNull() ?: return
        val lang = LanguageDetector.detect(content)
        val pinyin = genPinyin(content, lang)
        val t = title.trim().ifBlank { content.take(20).trim() }
        db.entryQueries.update(t, content.trim(), pinyin, lang, id)
    }

    fun delete(id: Long) { db.entryQueries.deleteById(id) }

    private fun genPinyin(text: String, language: String): String = when (language) {
        "zh" -> chinesePinyin.generate(text); "en" -> englishPinyin.generate(text); else -> ""
    }
}
