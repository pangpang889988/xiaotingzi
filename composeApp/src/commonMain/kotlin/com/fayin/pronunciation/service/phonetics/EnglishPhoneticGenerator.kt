package com.fayin.pronunciation.service.phonetics

class EnglishPhoneticGenerator {
    // Simple phonetic approximation for English
    fun generate(text: String): String {
        if (text.isBlank()) return ""
        // Return text for now; could integrate a real IPA library
        return text.lowercase()
    }
}
