package com.fayin.pronunciation.ui.entrylist

import androidx.compose.runtime.getValue; import androidx.compose.runtime.mutableStateOf; import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel; import androidx.lifecycle.viewModelScope
import com.fayin.pronunciation.data.repository.EntryRepository
import com.fayin.pronunciation.service.tts.TtsService
import kotlinx.coroutines.Dispatchers; import kotlinx.coroutines.IO
import kotlinx.coroutines.Job; import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow; import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow; import kotlinx.coroutines.flow.update; import kotlinx.coroutines.launch

data class EntryItem(val id: Long, val groupId: Long, val title: String, val content: String,
    val pinyin: String, val language: String, val createdAt: Long)

class EntryListViewModel(
    private val repo: EntryRepository, private val tts: TtsService
) : ViewModel() {
    private val _e = MutableStateFlow<List<EntryItem>>(emptyList())
    val entries: StateFlow<List<EntryItem>> = _e.asStateFlow()
    var showDeleteConfirm by mutableStateOf(false); private set
    var deleteTarget by mutableStateOf<EntryItem?>(null); private set
    var speakingEntryId by mutableStateOf<Long?>(null); private set
    private var resetJob: Job? = null; private var currentGroupId: Long = -1L

    fun loadGroup(gid: Long) { if (gid == currentGroupId) return; currentGroupId = gid
        viewModelScope.launch(Dispatchers.IO) { try { _e.update { repo.getByGroup(gid).map { EntryItem(it.id, it.groupId, it.title, it.content, it.pinyin, it.language, it.createdAt) } } } catch (_: Exception) { _e.update { emptyList() } } } }
    fun speak(e: EntryItem) { resetJob?.cancel(); if (tts.isSpeaking()) tts.stop()
        speakingEntryId = e.id; if (e.content.isNotBlank()) tts.speak(e.content, e.language)
        resetJob = viewModelScope.launch { delay(500); speakingEntryId = null } }
    fun requestDelete(e: EntryItem) { deleteTarget = e; showDeleteConfirm = true }
    fun hideDelete() { showDeleteConfirm = false; deleteTarget = null }
    fun confirmDelete(id: Long) { viewModelScope.launch(Dispatchers.IO) { repo.delete(id); showDeleteConfirm = false; deleteTarget = null; loadGroup(currentGroupId) } }
    override fun onCleared() { resetJob?.cancel(); if (tts.isSpeaking()) tts.stop(); super.onCleared() }
}
