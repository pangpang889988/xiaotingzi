package com.fayin.pronunciation.ui.edit

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fayin.pronunciation.Dependencies

@Composable
fun EntryEditScreen(
    groupId: Long, entryId: Long?, onSaved: () -> Unit, onBack: () -> Unit,
    viewModel: EntryEditViewModel = remember {
        EntryEditViewModel(Dependencies.entryRepository, Dependencies.ttsService,
            Dependencies.chinesePhoneticGenerator, Dependencies.englishPhoneticGenerator)
    }
) {
    LaunchedEffect(groupId) { viewModel.setGroup(groupId) }
    LaunchedEffect(entryId) { if (entryId != null) viewModel.loadEntry(entryId) }
    Scaffold(containerColor = MaterialTheme.colorScheme.background) { padding ->
        Column(Modifier.fillMaxSize().padding(padding)) {
            Box(Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.background).padding(horizontal = 4.dp, vertical = 8.dp)) {
                Row(Modifier.align(Alignment.CenterStart).clickable(onClick = onBack).padding(horizontal = 12.dp, vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
                    Spacer(Modifier.width(2.dp)); Text("返回", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.primary, fontSize = 17.sp)
                }
                Text(if (viewModel.isEditMode) "编辑条目" else "新增条目", style = MaterialTheme.typography.displayLarge, color = MaterialTheme.colorScheme.onBackground, modifier = Modifier.align(Alignment.CenterStart).padding(start = 16.dp, top = 44.dp))
                TextButton(onClick = { viewModel.save(onSaved) }, enabled = viewModel.content.isNotBlank() && !viewModel.isSaving, modifier = Modifier.align(Alignment.TopEnd).padding(top = 6.dp)) {
                    Text("保存", fontWeight = FontWeight.SemiBold, fontSize = 17.sp, color = if (viewModel.content.isNotBlank() && !viewModel.isSaving) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f))
                }
            }
            Box(Modifier.fillMaxWidth().height(0.5.dp).background(MaterialTheme.colorScheme.outline))
            Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(horizontal = 16.dp).padding(top = 24.dp)) {
                OutlinedTextField(viewModel.title, { viewModel.onTitleChanged(it) }, label = { Text("自定义标题") }, placeholder = { Text("如：重点单词") }, singleLine = true, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = MaterialTheme.colorScheme.primary))
                Spacer(Modifier.height(16.dp))
                OutlinedTextField(viewModel.content, { viewModel.onContentChanged(it) }, label = { Text("文本内容") }, placeholder = { Text("输入中文或英文文本") }, minLines = 5, maxLines = 10, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = MaterialTheme.colorScheme.primary))
                Spacer(Modifier.height(12.dp))
                Box(Modifier.background(if (viewModel.detectedLanguage == "zh") MaterialTheme.colorScheme.primary.copy(alpha = 0.1f) else MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f), shape = RoundedCornerShape(8.dp)).padding(horizontal = 12.dp, vertical = 6.dp)) {
                    Text(if (viewModel.detectedLanguage == "zh") "中文 - Chinese" else "English - EN", style = MaterialTheme.typography.titleSmall, color = if (viewModel.detectedLanguage == "zh") MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary)
                }
                Spacer(Modifier.height(24.dp))
                Box(Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f), shape = RoundedCornerShape(12.dp)).padding(16.dp)) {
                    Column {
                        Text("注音预览", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Spacer(Modifier.height(6.dp))
                        if (viewModel.pinyinPreview.isNotBlank()) Text(viewModel.pinyinPreview, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurface)
                        else Text("输入内容后将自动生成注音", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f))
                    }
                }
                Spacer(Modifier.height(24.dp))
                Box(Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f), shape = RoundedCornerShape(12.dp)).clickable(enabled = viewModel.content.isNotBlank()) { viewModel.previewPronunciation() }.padding(vertical = 14.dp), contentAlignment = Alignment.Center) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.VolumeUp, null, tint = if (viewModel.content.isNotBlank()) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f), modifier = Modifier.size(20.dp))
                        Spacer(Modifier.width(8.dp)); Text("试听发音", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold, color = if (viewModel.content.isNotBlank()) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f))
                    }
                }
                Spacer(Modifier.height(40.dp))
            }
        }
    }
}
