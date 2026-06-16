package com.fayin.pronunciation.service.phonetics

actual class ChinesePhoneticGenerator {
    actual fun generate(text: String): String {
        if (text.isBlank()) return ""
        return text
    }
}
