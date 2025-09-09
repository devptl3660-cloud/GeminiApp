package com.example.geminiapp.ui

import android.app.Activity
import android.content.Intent
import android.speech.RecognizerIntent
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.geminiapp.ChatViewModel
import com.example.geminiapp.data.ChatMessage
import java.util.*

@Composable
fun ChatScreen(viewModel: ChatViewModel) {
    val messages = viewModel.messages
    val context = LocalContext.current

    val speechLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val matches = result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            if (!matches.isNullOrEmpty()) viewModel.onSpeechResult(matches[0])
        } else {
            Toast.makeText(context, "Speech canceled", Toast.LENGTH_SHORT).show()
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text("Gemini Chat") },
            actions = {
                IconButton(onClick = { viewModel.toggleTheme() }) {
                    Icon(Icons.Default.Brightness6, contentDescription = "Toggle theme")
                }
            }
        )

        LazyColumn(
            modifier = Modifier.weight(1f).padding(8.dp)
        ) {
            items(messages) { msg -> MessageRow(msg) }
        }

        Row(
            modifier = Modifier.padding(8.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = viewModel.inputText,
                onValueChange = { viewModel.inputText = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text("Type...") }
            )
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(onClick = {
                val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                    putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                    putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
                }
                speechLauncher.launch(intent)
            }) {
                Icon(Icons.Default.Mic, contentDescription = "Speak")
            }
            Button(onClick = { viewModel.sendMessage() }, enabled = !viewModel.isLoading) {
                Text(if (viewModel.isLoading) "..." else "Send")
            }
        }
    }
}

@Composable
fun MessageRow(message: ChatMessage) {
    val isUser = message.role == "user"
    Row(
        modifier = Modifier.fillMaxWidth().padding(4.dp),
        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
    ) {
        Card(modifier = Modifier.widthIn(max = 300.dp).padding(4.dp), elevation = 4.dp) {
            Text(text = message.text, modifier = Modifier.padding(12.dp))
        }
    }
}
