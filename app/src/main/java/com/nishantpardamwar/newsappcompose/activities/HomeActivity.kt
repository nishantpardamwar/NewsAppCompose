package com.nishantpardamwar.newsappcompose.activities

import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import com.nishantpardamwar.newsappcompose.activities.BaseActivity
import com.nishantpardamwar.newsappcompose.states.HomeState
import com.nishantpardamwar.newsappcompose.ui.screen.HomeScreen
import com.nishantpardamwar.newsappcompose.viewmodels.HomeVM
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : BaseActivity<HomeVM, HomeState>() {
    override val viewModel: HomeVM by viewModels()

    @Composable
    override fun Content() {
        HomeScreen(viewModel)
    }
}