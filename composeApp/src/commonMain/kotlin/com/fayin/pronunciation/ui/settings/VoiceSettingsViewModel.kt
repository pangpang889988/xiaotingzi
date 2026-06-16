package com.fayin.pronunciation.ui.settings

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.fayin.pronunciation.service.tts.TtsService
import com.fayin.pronunciation.service.tts.TtsVoice

class VoiceSettingsViewModel(private val ttsService: TtsService) {
    var voices by mutableStateOf<List<TtsVoice>>(emptyList()); private set
    var selectedVoiceId by mutableStateOf(""); private set

    fun load() {
        voices = ttsService.getAvailableVoices()
        selectedVoiceId = ttsService.getSelectedVoiceId()
    }

    fun selectVoice(voiceId: String) {
        selectedVoiceId = voiceId; ttsService.setSelectedVoice(voiceId)
    }
}
