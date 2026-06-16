package com.fayin.pronunciation.ui.edit

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.fayin.pronunciation.data.repository.EntryRepository
import com.fayin.pronunciation.service.language.LanguageDetector
import com.fayin.pronunciation.service.phonetics.ChinesePhoneticGenerator
import com.fayin.pronunciation.service.phonetics.EnglishPhoneticGenerator
import com.fayin.pronunciation.service.tts.TtsService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class EntryEditViewModel(
    private val repo: EntryRepository, private val tts: TtsService,
    private val chinese: ChinesePhoneticGenerator, private val english: EnglishPhoneticGenerator
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    var title by mutableStateOf(""); private set
    var content by mutableStateOf(""); private set
    var pinyinPreview by mutableStateOf(""); private set
    var detectedLanguage by mutableStateOf("zh"); private set
    var isEditMode by mutableStateOf(false); private set
    var isSaving by mutableStateOf(false); private set
    private var editingEntryId: Long? = null; private var currentGroupId: Long = 0L
    private var loadJob: Job? = null

    fun setGroup(gid: Long) { if (gid == currentGroupId && !isEditMode) return; resetState(); currentGroupId = gid }
    private fun resetState() { title = ""; content = ""; pinyinPreview = ""; detectedLanguage = "zh"; isEditMode = false; isSaving = false; editingEntryId = null; loadJob?.cancel() }
    fun loadEntry(eid: Long) { loadJob?.cancel()
        loadJob = scope.launch {
            val e = repo.getById(eid) ?: return@launch; editingEntryId = e.id; title = e.title; content = e.content
            detectedLanguage = e.language; pinyinPreview = e.pinyin; isEditMode = true } }
    fun onTitleChanged(v: String) { title = v }
    fun onContentChanged(v: String) { content = v; detectedLanguage = LanguageDetector.detect(v); pinyinPreview = genPinyin(v, detectedLanguage) }
    fun previewPronunciation() { if (content.isNotBlank()) tts.speak(content, detectedLanguage) }
    fun save(onSaved: () -> Unit) { if (content.isBlank()) return; isSaving = true
        scope.launch {
            try { if (isEditMode && editingEntryId != null) repo.update(editingEntryId!!, title, content) else repo.create(currentGroupId, title, content) }
            catch (_: Exception) {} finally { isSaving = false; onSaved() } } }
    private fun genPinyin(text: String, lang: String): String = when (lang) { "zh" -> chinese.generate(text); "en" -> english.generate(text); else -> "" }
    fun cleanup() { loadJob?.cancel() }
}
