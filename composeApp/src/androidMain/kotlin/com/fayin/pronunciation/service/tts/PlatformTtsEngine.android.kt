package com.fayin.pronunciation.service.tts

import android.content.Context
import android.speech.tts.TextToSpeech
import java.util.Locale

actual class PlatformTtsEngine(private val context: Context) {
    private var tts: TextToSpeech? = null
    private var initialized = false
    private val pendingQueue = mutableListOf<Pair<String, String>>()
    private var cachedVoices: List<com.fayin.pronunciation.service.tts.TtsVoice> = emptyList()
    private var selectedVoiceId: String = ""

    init {
        tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                val prefs = context.getSharedPreferences("tts_prefs", Context.MODE_PRIVATE)
                selectedVoiceId = prefs.getString("selected_voice_id", "") ?: ""
                if (selectedVoiceId.isNotEmpty()) trySetVoice(selectedVoiceId)
                else tts?.language = Locale.CHINESE
                refreshCachedVoices()
                initialized = true
                synchronized(pendingQueue) {
                    val q = pendingQueue.toList()
                    pendingQueue.clear()
                    for ((t, l) in q) doSpeak(t, l)
                }
            }
        }
    }

    actual fun speak(text: String, language: String) {
        if (!initialized) { synchronized(pendingQueue) { pendingQueue.add(text to language) }; return }
        doSpeak(text, language)
    }

    private fun doSpeak(text: String, language: String) {
        val engine = tts ?: return
        if (selectedVoiceId.isEmpty()) {
            val locale = if (language == "zh") Locale.CHINESE else Locale.ENGLISH
            engine.language = locale
            if (language == "zh") {
                val zhVoices = engine.voices.filter { v ->
                    val lang = v.locale?.language ?: ""
                    lang == "zh" || lang == "cmn"
                }
                val preferred = zhVoices.firstOrNull { v ->
                    v.name.contains("female", true) || v.name.contains("high", true)
                }
                if (preferred != null) engine.voice = preferred
            }
        }
        engine.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
    }

    private fun trySetVoice(voiceId: String): Boolean {
        val engine = tts ?: return false
        for (v in engine.voices) { if (v.name == voiceId) { engine.voice = v; return true } }
        return false
    }

    private fun refreshCachedVoices() {
        val engine = tts ?: return
        cachedVoices = engine.voices
            .filter { v -> v.locale?.language in listOf("zh", "cmn", "en") }
            .map { v -> com.fayin.pronunciation.service.tts.TtsVoice(v.name,
                v.name.substringAfterLast("/").replace("_", " "),
                "${v.locale?.displayLanguage ?: ""}") }
            .sortedBy { it.name }
    }

    actual fun getAvailableVoices(): List<com.fayin.pronunciation.service.tts.TtsVoice> = cachedVoices
    actual fun setSelectedVoice(voiceId: String) {
        selectedVoiceId = voiceId
        context.getSharedPreferences("tts_prefs", Context.MODE_PRIVATE)
            .edit().putString("selected_voice_id", voiceId).apply()
        if (initialized) trySetVoice(voiceId)
    }
    actual fun getSelectedVoiceId(): String = selectedVoiceId
    actual fun stop() { tts?.stop() }
    actual fun isSpeaking(): Boolean = tts?.isSpeaking == true
    actual fun release() { tts?.stop(); tts?.shutdown(); tts = null }
}
