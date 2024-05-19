package com.example.travel

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.travel.navigation.MainNavigation
import com.example.travel.ui.theme.TravelTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            TravelTheme {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    Column {
                        MainNavigation()
                    }
                }
            }
        }
    }
}