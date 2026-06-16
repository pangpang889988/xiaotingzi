package com.fayin.pronunciation.ui.group

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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.fayin.pronunciation.ui.theme.GroupColors


@Composable
fun GroupListScreen(
    onGroupClick: (Long, String) -> Unit,
    onSettingsClick: () -> Unit = {},
    viewModel: GroupViewModel = remember { GroupViewModel(com.fayin.pronunciation.Dependencies.groupRepository) }
) {
    LaunchedEffect(Unit) { viewModel.load() }
    val groups by viewModel.groups.collectAsState()
    Scaffold(containerColor = MaterialTheme.colorScheme.background) { padding ->
        Column(Modifier.fillMaxSize().padding(padding)) {
            Box(Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.background).padding(top = 8.dp)) {
                Text("可口可乐", style = MaterialTheme.typography.displayLarge, color = MaterialTheme.colorScheme.onBackground, modifier = Modifier.padding(start = 20.dp, top = 8.dp))
                Row(Modifier.align(Alignment.TopEnd).padding(end = 4.dp, top = 12.dp), verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { viewModel.shuffleGroups() }) { Icon(Icons.Default.Shuffle, "随机排列", tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(24.dp)) }
                    IconButton(onClick = onSettingsClick) { Icon(Icons.Default.Settings, "设置", tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(24.dp)) }
                    IconButton(onClick = { viewModel.showCreate() }) { Icon(Icons.Default.Add, "新建分组", tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(24.dp)) }
                }
            }
            Box(Modifier.fillMaxWidth().height(0.5.dp).background(MaterialTheme.colorScheme.outline))
            if (groups.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.Book, null, Modifier.size(56.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f))
                        Spacer(Modifier.height(12.dp)); Text("还没有分组", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f))
                        Spacer(Modifier.height(4.dp)); Text("点击右上角 + 新建分组", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f))
                    }
                }
            } else {
                LazyColumn(Modifier.fillMaxSize(), contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 16.dp), verticalArrangement = Arrangement.spacedBy(0.dp)) {
                    itemsIndexed(groups) { index, group ->
                        val isLast = index == groups.lastIndex; val accent = GroupColors[index % GroupColors.size]
                        Column {
                            Row(Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.surface).clickable { onGroupClick(group.id, group.title) }.padding(horizontal = 16.dp, vertical = 14.dp), verticalAlignment = Alignment.CenterVertically) {
                                Box(Modifier.size(36.dp).clip(CircleShape).background(accent.copy(alpha = 0.15f)), contentAlignment = Alignment.Center) { Icon(Icons.Default.Book, null, tint = accent, modifier = Modifier.size(18.dp)) }
                                Spacer(Modifier.width(14.dp))
                                Column(Modifier.weight(1f)) {
                                    Text(group.title, style = MaterialTheme.typography.titleLarge, maxLines = 1, overflow = TextOverflow.Ellipsis, fontWeight = FontWeight.Normal)
                                    Spacer(Modifier.height(2.dp))
                                    Text("创建于${group.createdAt}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 1)
                                }
                                Spacer(Modifier.width(8.dp))
                                IconButton(onClick = { viewModel.requestRename(group) }, modifier = Modifier.size(32.dp)) { Icon(Icons.Default.Edit, "重命名", tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(18.dp)) }
                                IconButton(onClick = { viewModel.requestDelete(group) }, modifier = Modifier.size(32.dp)) { Icon(Icons.Default.Delete, "删除", tint = MaterialTheme.colorScheme.error.copy(alpha = 0.7f), modifier = Modifier.size(18.dp)) }
                                Icon(Icons.Default.ChevronRight, null, tint = MaterialTheme.colorScheme.outline, modifier = Modifier.size(16.dp))
                            }
                            if (!isLast) Box(Modifier.fillMaxWidth().padding(start = 66.dp).height(0.5.dp).background(MaterialTheme.colorScheme.outline))
                        }
                    }
                }
            }
        }
    }
    if (viewModel.showCreateDialog) { var t by remember { mutableStateOf("") }
        AlertDialog(onDismissRequest = { viewModel.hideCreate() }, title = { Text("新建分组") },
            text = { OutlinedTextField(t, { t = it }, label = { Text("分组名称") }, singleLine = true, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(10.dp), colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = MaterialTheme.colorScheme.primary)) },
            confirmButton = { TextButton({ viewModel.createGroup(t) }, enabled = t.isNotBlank()) { Text("创建") } },
            dismissButton = { TextButton({ viewModel.hideCreate() }) { Text("取消") } }) }
    if (viewModel.showRenameDialog && viewModel.renameTarget != null) { var t by remember { mutableStateOf(viewModel.renameTarget!!.title) }
        AlertDialog(onDismissRequest = { viewModel.hideRename() }, title = { Text("重命名分组") },
            text = { OutlinedTextField(t, { t = it }, label = { Text("分组名称") }, singleLine = true, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(10.dp), colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = MaterialTheme.colorScheme.primary)) },
            confirmButton = { TextButton({ viewModel.renameGroup(viewModel.renameTarget!!.id, t) }, enabled = t.isNotBlank()) { Text("确定") } },
            dismissButton = { TextButton({ viewModel.hideRename() }) { Text("取消") } }) }
    if (viewModel.showDeleteConfirm && viewModel.deleteTarget != null) {
        AlertDialog(onDismissRequest = { viewModel.hideDelete() },
            confirmButton = { TextButton({ viewModel.confirmDelete(viewModel.deleteTarget!!.id) }) { Text("删除", color = MaterialTheme.colorScheme.error) } },
            dismissButton = { TextButton({ viewModel.hideDelete() }) { Text("取消") } },
            title = { Text("删除分组") }, text = { Text("确定要删除「${viewModel.deleteTarget!!.title}」吗？") }) }
}

