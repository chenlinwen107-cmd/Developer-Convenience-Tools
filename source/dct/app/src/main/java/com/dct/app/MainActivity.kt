package com.dct.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.CompositionLocalProvider
import com.dct.app.core.ui.theme.DctTheme
import com.dct.app.di.LocalAppContainer
import com.dct.app.navigation.DctApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val container = (application as DctApplication).container
        setContent {
            CompositionLocalProvider(LocalAppContainer provides container) {
                DctTheme {
                    DctApp()
                }
            }
        }
    }
}
