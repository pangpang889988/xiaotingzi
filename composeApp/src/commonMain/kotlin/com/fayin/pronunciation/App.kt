package com.fayin.pronunciation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.fayin.pronunciation.data.repository.EntryRepository
import com.fayin.pronunciation.data.repository.GroupRepository
import com.fayin.pronunciation.service.phonetics.ChinesePhoneticGenerator
import com.fayin.pronunciation.service.phonetics.EnglishPhoneticGenerator
import com.fayin.pronunciation.service.tts.TtsService
import com.fayin.pronunciation.ui.edit.EntryEditScreen
import com.fayin.pronunciation.ui.edit.EntryEditViewModel
import com.fayin.pronunciation.ui.entrylist.EntryListScreen
import com.fayin.pronunciation.ui.entrylist.EntryListViewModel
import com.fayin.pronunciation.ui.group.GroupListScreen
import com.fayin.pronunciation.ui.group.GroupViewModel
import com.fayin.pronunciation.ui.settings.VoiceSettingsScreen
import com.fayin.pronunciation.ui.settings.VoiceSettingsViewModel
import com.fayin.pronunciation.ui.theme.FayinTheme

sealed class Screen {
    data object Groups : Screen()
    data class Entries(val groupId: Long, val title: String) : Screen()
    data class Edit(val groupId: Long, val entryId: Long?) : Screen()
    data object Settings : Screen()
}

@Composable
fun App() {
    FayinTheme {
        var currentScreen by remember { mutableStateOf<Screen>(Screen.Groups) }

        when (val screen = currentScreen) {
            is Screen.Groups -> GroupListScreen(
                onGroupClick = { id, title -> currentScreen = Screen.Entries(id, title) },
                onSettingsClick = { currentScreen = Screen.Settings }
            )
            is Screen.Entries -> EntryListScreen(
                groupId = screen.groupId,
                groupTitle = screen.title,
                onAddEntry = { currentScreen = Screen.Edit(screen.groupId, null) },
                onEditEntry = { eid -> currentScreen = Screen.Edit(screen.groupId, eid) },
                onBack = { currentScreen = Screen.Groups }
            )
            is Screen.Edit -> EntryEditScreen(
                groupId = screen.groupId,
                entryId = screen.entryId,
                onSaved = { currentScreen = Screen.Entries(screen.groupId, "") },
                onBack = { currentScreen = Screen.Entries(screen.groupId, "") }
            )
            is Screen.Settings -> VoiceSettingsScreen(
                onBack = { currentScreen = Screen.Groups }
            )
        }
    }
}
