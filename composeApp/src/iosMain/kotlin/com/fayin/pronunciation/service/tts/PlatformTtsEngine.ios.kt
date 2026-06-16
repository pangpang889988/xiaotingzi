package com.fayin.pronunciation.service.tts

actual class PlatformTtsEngine {
    private var selectedVoiceId: String = ""
    private val availableVoices = listOf(
        TtsVoice("zh-CN", "中文（普通话）", "zh-CN"),
        TtsVoice("en-US", "English (US)", "en-US"),
        TtsVoice("en-GB", "English (UK)", "en-GB")
    )

    actual fun speak(text: String, language: String) {
        // TTS is stubbed for CI - will be implemented with AVFoundation on real device
        println("iOS TTS speak: $text ($language)")
    }

    actual fun stop() {}
    actual fun isSpeaking(): Boolean = false
    actual fun release() {}

    actual fun getAvailableVoices(): List<TtsVoice> = availableVoices

    actual fun setSelectedVoice(voiceId: String) { selectedVoiceId = voiceId }

    actual fun getSelectedVoiceId(): String = selectedVoiceId
}
