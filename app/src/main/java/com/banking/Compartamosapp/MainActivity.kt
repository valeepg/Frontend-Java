package com.banking.Compartamosapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.banking.Compartamosapp.ui.navigation.NavGraph
import com.banking.Compartamosapp.ui.theme.CompartamosappTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CompartamosappTheme {
                NavGraph()
            }
        }
    }
}