package com.moodjournal.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.moodjournal.app.platform.HapticFeedback

val moods = listOf("😊" to "Great", "🙂" to "Good", "😐" to "Okay", "😢" to "Sad", "😡" to "Angry")
data class JournalEntry(val date: String, val mood: String, val emoji: String, val note: String)

@Composable
fun MainScreen() {
    var entries by remember { mutableStateOf(listOf<JournalEntry>()) }
    var currentMood by remember { mutableStateOf(moods[2]) }
    var currentNote by remember { mutableStateOf("") }
    var showSave by remember { mutableStateOf(false) }
    val haptic = remember { HapticFeedback() }
    val today = java.text.SimpleDateFormat("MMM dd, yyyy", java.util.Locale.US).format(java.util.Date())

    Scaffold(
        topBar = { CenterAlignedTopAppBar(title = { Text("My Journal", fontWeight = FontWeight.Bold) }) },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(Modifier.fillMaxSize().padding(padding).padding(20.dp)) {
            // Today section
            Card(shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
                Column(Modifier.padding(20.dp)) {
                    Text(today, fontSize = 14.sp, color = MaterialTheme.colorScheme.onMaterialTheme.colorScheme.surfaceVariant)
                    Spacer(Modifier.height(12.dp))
                    Text("How are you feeling?", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                    Spacer(Modifier.height(12.dp))
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                        moods.forEach { (emoji, label) ->
                            Column(horizontalAlignment = Alignment.CenterHorizontally,
                                   modifier = Modifier.clickable { currentMood = emoji to label; haptic.light() }) {
                                Text(emoji, fontSize = 32.sp)
                                Text(label, fontSize = 11.sp, color = if (currentMood.first == emoji) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onMaterialTheme.colorScheme.surfaceVariant)
                            }
                        }
                    }
                    Spacer(Modifier.height(16.dp))
                    OutlinedTextField(value = currentNote, onValueChange = { currentNote = it },
                        modifier = Modifier.fillMaxWidth(), placeholder = { Text("Write one sentence...") },
                        shape = RoundedCornerShape(12.dp), maxLines = 3)
                    Spacer(Modifier.height(12.dp))
                    Button(onClick = {
                        if (currentNote.isNotBlank()) {
                            entries = listOf(JournalEntry(today, currentMood.second, currentMood.first, currentNote)) + entries
                            currentNote = ""; haptic.medium()
                        }
                    }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)) {
                        Icon(Icons.Default.Save, null, Modifier.size(18.dp)); Spacer(Modifier.width(8.dp))
                        Text("Save Entry")
                    }
                }
            }
            Spacer(Modifier.height(16.dp))
            Text("History", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(8.dp))
            if (entries.isEmpty()) {
                Spacer(Modifier.weight(1f))
                Text("No entries yet. Start journaling!", fontSize = 14.sp, color = MaterialTheme.colorScheme.onMaterialTheme.colorScheme.surfaceVariant, modifier = Modifier.align(Alignment.CenterHorizontally))
                Spacer(Modifier.weight(1f))
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(entries) { entry ->
                        Card(shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
                            Row(Modifier.padding(16.dp), verticalAlignment = Alignment.Top) {
                                Text(entry.emoji, fontSize = 28.sp)
                                Spacer(Modifier.width(12.dp))
                                Column(Modifier.weight(1f)) {
                                    Text(entry.note, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                                    Spacer(Modifier.height(4.dp))
                                    Text("{entry.date} · {entry.mood}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onMaterialTheme.colorScheme.surfaceVariant)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
