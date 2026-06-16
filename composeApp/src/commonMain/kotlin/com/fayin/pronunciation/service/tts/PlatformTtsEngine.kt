package com.fayin.pronunciation.service.tts

data class TtsVoice(
    val id: String,
    val name: String,
    val locale: String
)

expect class PlatformTtsEngine {
    fun speak(text: String, language: String)
    fun stop()
    fun isSpeaking(): Boolean
    fun release()
    fun getAvailableVoices(): List<TtsVoice>
    fun setSelectedVoice(voiceId: String)
    fun getSelectedVoiceId(): String
}
