package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.example.ui.DiceViewModel
import com.example.ui.MainScreen

class MainActivity : ComponentActivity() {
  private val viewModel: DiceViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      MainScreen(viewModel = viewModel)
    }
  }
}
