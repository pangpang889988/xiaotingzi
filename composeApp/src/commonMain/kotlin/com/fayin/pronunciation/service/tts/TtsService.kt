package com.fayin.pronunciation.service.tts

class TtsService(private val engine: PlatformTtsEngine) {
    fun speak(text: String, language: String) = engine.speak(text, language)
    fun stop() = engine.stop()
    fun isSpeaking(): Boolean = engine.isSpeaking()
    fun release() = engine.release()
    fun getAvailableVoices(): List<TtsVoice> = engine.getAvailableVoices()
    fun setSelectedVoice(voiceId: String) = engine.setSelectedVoice(voiceId)
    fun getSelectedVoiceId(): String = engine.getSelectedVoiceId()
}
