package com.nishantpardamwar.newsappcompose.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.nishantpardamwar.newsappcompose.states.BaseState
import com.nishantpardamwar.newsappcompose.ui.theme.NewsAppComposeTheme
import com.nishantpardamwar.newsappcompose.viewmodels.BaseVM

abstract class BaseActivity<VM : BaseVM<State>, State : BaseState> : ComponentActivity() {
    abstract val viewModel: VM

    @Composable
    abstract fun Content()

    final override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NewsAppComposeTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
                    Content()
                }
            }
        }
    }
}