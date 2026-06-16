package com.fayin.pronunciation.ui.entrylist

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fayin.pronunciation.Dependencies


@Composable
fun EntryListScreen(
    groupId: Long, groupTitle: String, onAddEntry: () -> Unit, onEditEntry: (Long) -> Unit, onBack: () -> Unit,
    viewModel: EntryListViewModel = remember { EntryListViewModel(Dependencies.entryRepository, Dependencies.ttsService) }
) {
    LaunchedEffect(groupId) { viewModel.loadGroup(groupId) }
    val entries by viewModel.entries.collectAsState()
    val isSpeaking = viewModel.speakingEntryId
    Scaffold(containerColor = MaterialTheme.colorScheme.background) { padding ->
        Column(Modifier.fillMaxSize().padding(padding)) {
            Box(Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.background).padding(horizontal = 4.dp, vertical = 8.dp)) {
                Row(Modifier.align(Alignment.CenterStart).clickable(onClick = onBack).padding(horizontal = 12.dp, vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
                    Spacer(Modifier.width(2.dp)); Text("返回", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.primary, fontSize = 17.sp)
                }
                Text(groupTitle, style = MaterialTheme.typography.displayLarge, color = MaterialTheme.colorScheme.onBackground, modifier = Modifier.align(Alignment.CenterStart).padding(start = 16.dp, top = 44.dp))
                IconButton(onClick = onAddEntry, modifier = Modifier.align(Alignment.TopEnd).padding(top = 4.dp)) { Icon(Icons.Default.Add, "添加条目", tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(24.dp)) }
            }
            Box(Modifier.fillMaxWidth().height(0.5.dp).background(MaterialTheme.colorScheme.outline))
            if (entries.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.VolumeUp, null, Modifier.size(56.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f))
                        Spacer(Modifier.height(12.dp)); Text("还没有条目", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f))
                        Spacer(Modifier.height(4.dp)); Text("点击右上角 + 添加学习内容", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f))
                    }
                }
            } else {
                LazyColumn(Modifier.fillMaxSize(), contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 16.dp), verticalArrangement = Arrangement.spacedBy(0.dp)) {
                    items(entries, key = { it.id }) { entry ->
                        val isLast = entry.id == (entries.lastOrNull()?.id ?: -1L)
                        val bgColor by animateColorAsState(if (isSpeaking == entry.id) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface, label = "bg")
                        Column {
                            Row(Modifier.fillMaxWidth().background(bgColor).padding(horizontal = 16.dp, vertical = 12.dp), verticalAlignment = Alignment.CenterVertically) {
                                Column(Modifier.weight(1f)) {
                                    Text(entry.title, style = MaterialTheme.typography.titleLarge, maxLines = 1, overflow = TextOverflow.Ellipsis, fontWeight = FontWeight.SemiBold)
                                    Spacer(Modifier.height(2.dp))
                                    Text(entry.content, style = MaterialTheme.typography.bodyLarge, maxLines = 1, overflow = TextOverflow.Ellipsis, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    if (entry.pinyin.isNotBlank()) { Spacer(Modifier.height(2.dp)); Text(entry.pinyin, style = MaterialTheme.typography.bodySmall, maxLines = 1, overflow = TextOverflow.Ellipsis, color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)) }
                                }
                                Spacer(Modifier.width(8.dp))
                                Box(Modifier.clip(RoundedCornerShape(4.dp)).background(if (entry.language == "zh") MaterialTheme.colorScheme.primary.copy(alpha = 0.1f) else MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f)).padding(horizontal = 6.dp, vertical = 2.dp)) {
                                    Text(if (entry.language == "zh") "中" else "EN", style = MaterialTheme.typography.labelSmall, color = if (entry.language == "zh") MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary, fontWeight = FontWeight.Bold)
                                }
                                Spacer(Modifier.width(4.dp))
                                IconButton(onClick = { onEditEntry(entry.id) }, modifier = Modifier.size(32.dp)) { Icon(Icons.Default.Edit, "编辑", tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(18.dp)) }
                                IconButton(onClick = { viewModel.speak(entry) }, modifier = Modifier.size(36.dp)) { Icon(Icons.Default.VolumeUp, "朗读", tint = if (isSpeaking == entry.id) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(20.dp)) }
                            }
                            if (!isLast) Box(Modifier.fillMaxWidth().padding(start = 16.dp).height(0.5.dp).background(MaterialTheme.colorScheme.outline))
                        }
                    }
                }
            }
        }
    }
    if (viewModel.showDeleteConfirm && viewModel.deleteTarget != null) {
        AlertDialog(onDismissRequest = { viewModel.hideDelete() },
            confirmButton = { TextButton({ viewModel.confirmDelete(viewModel.deleteTarget!!.id) }) { Text("删除", color = MaterialTheme.colorScheme.error) } },
            dismissButton = { TextButton({ viewModel.hideDelete() }) { Text("取消") } },
            title = { Text("删除条目") }, text = { Text("确定要删除「${viewModel.deleteTarget!!.title}」吗？") }) }
}

