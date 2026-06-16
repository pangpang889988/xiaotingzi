package com.fayin.pronunciation.service.tts

import kotlinx.cinterop.ExperimentalForeignApi
import platform.AVFoundation.AVSpeechSynthesizer
import platform.AVFoundation.AVSpeechUtterance
import platform.AVFoundation.AVSpeechSynthesisVoice
import platform.Foundation.NSUUID

actual class PlatformTtsEngine {
    private val synthesizer = AVSpeechSynthesizer()
    private var selectedVoiceId: String = ""
    private val availableVoices = mutableListOf<TtsVoice>()

    init {
        refreshVoices()
    }

    @OptIn(ExperimentalForeignApi::class)
    private fun refreshVoices() {
        availableVoices.clear()
        val locales = listOf("zh-CN", "zh-TW", "zh-HK", "en-US", "en-GB", "en-AU")
        for (locId in locales) {
            val voice = AVSpeechSynthesisVoice.voiceWithLanguage(locId)
            if (voice != null) {
                availableVoices.add(TtsVoice(
                    id = locId,
                    name = voiceNameForLocale(locId),
                    locale = locId
                ))
            }
        }
    }

    private fun voiceNameForLocale(locale: String): String = when (locale) {
        "zh-CN" -> "中文（普通话）"
        "zh-TW" -> "中文（台湾）"
        "zh-HK" -> "中文（香港）"
        "en-US" -> "English (US)"
        "en-GB" -> "English (UK)"
        "en-AU" -> "English (Australia)"
        else -> locale
    }

    actual fun speak(text: String, language: String) {
        val utterance = AVSpeechUtterance(string = text)
        val locId = if (selectedVoiceId.isNotEmpty()) selectedVoiceId
                    else if (language == "zh") "zh-CN" else "en-US"
        utterance.voice = AVSpeechSynthesisVoice.voiceWithLanguage(locId)
        utterance.rate = 0.5
        utterance.volume = 1.0
        synthesizer.speakUtterance(utterance)
    }

    actual fun stop() { synthesizer.stopSpeakingAtBoundary(0u) }
    actual fun isSpeaking(): Boolean = synthesizer.isSpeaking
    actual fun release() { stop() }

    actual fun getAvailableVoices(): List<TtsVoice> = availableVoices.toList()

    actual fun setSelectedVoice(voiceId: String) { selectedVoiceId = voiceId }

    actual fun getSelectedVoiceId(): String = selectedVoiceId
}
