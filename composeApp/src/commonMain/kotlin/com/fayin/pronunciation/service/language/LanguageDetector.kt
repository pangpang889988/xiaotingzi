package com.fayin.pronunciation.service.language

object LanguageDetector {
    private val chineseRange1 = 0x4E00..0x9FFF
    private val chineseRange2 = 0x3400..0x4DBF

    fun detect(text: String): String {
        if (text.isBlank()) return "zh"
        return if (text.any { it.code in chineseRange1 || it.code in chineseRange2 }) "zh" else "en"
    }
}
