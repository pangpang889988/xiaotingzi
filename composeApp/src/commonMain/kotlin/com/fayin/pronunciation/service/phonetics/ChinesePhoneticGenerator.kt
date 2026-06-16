package com.fayin.pronunciation.service.phonetics

expect class ChinesePhoneticGenerator() {
    fun generate(text: String): String
}
