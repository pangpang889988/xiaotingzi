package com.fayin.pronunciation.service.phonetics

import com.github.promeg.pinyinhelper.Pinyin

actual class ChinesePhoneticGenerator {
    actual fun generate(text: String): String {
        if (text.isBlank()) return ""
        return try {
            Pinyin.toPinyin(text, " ")
        } catch (e: Exception) {
            text
        }
    }
}
