package com.fayin.pronunciation.service.phonetics

interface PhoneticGenerator {
    fun supports(language: String): Boolean
    fun generate(text: String): String
}
