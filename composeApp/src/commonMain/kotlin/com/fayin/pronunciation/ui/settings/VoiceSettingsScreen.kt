package com.fayin.pronunciation.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.koin.core.context.GlobalContext

fun voiceSettingsViewModel(): VoiceSettingsViewModel = GlobalContext.get().get()

@Composable
fun VoiceSettingsScreen(onBack: () -> Unit, viewModel: VoiceSettingsViewModel = voiceSettingsViewModel()) {
    val voices = viewModel.voices; val selectedVoiceId = viewModel.selectedVoiceId
    Scaffold(containerColor = MaterialTheme.colorScheme.background) { padding ->
        Column(Modifier.fillMaxSize().padding(padding)) {
            Box(Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.background).padding(horizontal = 4.dp, vertical = 8.dp)) {
                Row(Modifier.align(Alignment.CenterStart).clickable(onClick = onBack).padding(horizontal = 12.dp, vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
                    Spacer(Modifier.width(2.dp)); Text("返回", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.primary, fontSize = 17.sp)
                }
                Text("语音设置", style = MaterialTheme.typography.displayLarge, color = MaterialTheme.colorScheme.onBackground, modifier = Modifier.align(Alignment.CenterStart).padding(start = 16.dp, top = 44.dp))
            }
            Box(Modifier.fillMaxWidth().height(0.5.dp).background(MaterialTheme.colorScheme.outline))
            Text("发音角色", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(start = 20.dp, top = 24.dp, bottom = 8.dp))
            if (voices.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text("正在加载语音...", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant) }
            } else {
                LazyColumn(Modifier.fillMaxSize().padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(0.dp)) {
                    items(voices) { voice ->
                        val isLast = voice.id == (voices.lastOrNull()?.id ?: "")
                        Column {
                            Row(Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.surface).clickable { viewModel.selectVoice(voice.id) }.padding(horizontal = 16.dp, vertical = 14.dp), verticalAlignment = Alignment.CenterVertically) {
                                Icon(if (voice.id == selectedVoiceId) Icons.Default.CheckCircle else Icons.Default.Circle, null, tint = if (voice.id == selectedVoiceId) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline, modifier = Modifier.size(22.dp))
                                Spacer(Modifier.width(14.dp))
                                Column {
                                    Text(voice.name, style = MaterialTheme.typography.bodyLarge, fontWeight = if (voice.id == selectedVoiceId) FontWeight.SemiBold else FontWeight.Normal, fontFamily = FontFamily.Monospace)
                                    Text(voice.locale, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                            }
                            if (!isLast) Box(Modifier.fillMaxWidth().padding(start = 52.dp).height(0.5.dp).background(MaterialTheme.colorScheme.outline))
                        }
                    }
                }
            }
        }
    }
}
