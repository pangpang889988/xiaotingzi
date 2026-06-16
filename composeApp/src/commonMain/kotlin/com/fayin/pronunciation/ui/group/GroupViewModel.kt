package com.fayin.pronunciation.ui.group

import androidx.compose.runtime.getValue; import androidx.compose.runtime.mutableStateOf; import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel; import androidx.lifecycle.viewModelScope
import com.fayin.pronunciation.data.repository.GroupRepository
import kotlinx.coroutines.Dispatchers; import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow; import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow; import kotlinx.coroutines.flow.update; import kotlinx.coroutines.launch

data class GroupItem(val id: Long, val title: String, val createdAt: Long, val sortOrder: Long)

class GroupViewModel(private val repo: GroupRepository) : ViewModel() {
    private val _g = MutableStateFlow<List<GroupItem>>(emptyList())
    val groups: StateFlow<List<GroupItem>> = _g.asStateFlow()
    var showCreateDialog by mutableStateOf(false); private set
    var showRenameDialog by mutableStateOf(false); private set
    var renameTarget by mutableStateOf<GroupItem?>(null); private set
    var showDeleteConfirm by mutableStateOf(false); private set
    var deleteTarget by mutableStateOf<GroupItem?>(null); private set
    init { load() }
    fun load() { viewModelScope.launch(Dispatchers.IO) { try { _g.update { repo.getAll().map { GroupItem(it.id, it.title, it.createdAt, it.sortOrder) } } } catch (_: Exception) { _g.update { emptyList() } } } }
    fun showCreate() { showCreateDialog = true }
    fun hideCreate() { showCreateDialog = false }
    fun createGroup(title: String) { if (title.isBlank()) return; viewModelScope.launch(Dispatchers.IO) { repo.create(title); showCreateDialog = false; load() } }
    fun requestRename(g: GroupItem) { renameTarget = g; showRenameDialog = true }
    fun hideRename() { showRenameDialog = false; renameTarget = null }
    fun renameGroup(id: Long, newTitle: String) { if (newTitle.isBlank()) return; viewModelScope.launch(Dispatchers.IO) { repo.rename(id, newTitle); showRenameDialog = false; renameTarget = null; load() } }
    fun requestDelete(g: GroupItem) { deleteTarget = g; showDeleteConfirm = true }
    fun hideDelete() { showDeleteConfirm = false; deleteTarget = null }
    fun confirmDelete(id: Long) { viewModelScope.launch(Dispatchers.IO) { repo.delete(id); showDeleteConfirm = false; deleteTarget = null; load() } }
    fun shuffleGroups() { viewModelScope.launch(Dispatchers.IO) { repo.shuffle(); load() } }
}
