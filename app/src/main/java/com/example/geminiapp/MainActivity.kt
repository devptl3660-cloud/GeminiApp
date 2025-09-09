package com.example.geminiapp

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.geminiapp.ui.ChatScreen
import com.example.geminiapp.ui.theme.GeminiAppTheme

class MainActivity : ComponentActivity() {

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)

        setContent {
            val vm: ChatViewModel = viewModel()
            GeminiAppTheme(darkTheme = vm.isDarkTheme) {
                Surface(color = MaterialTheme.colors.background) {
                    ChatScreen(viewModel = vm)
                }
            }
        }
    }
}
  
