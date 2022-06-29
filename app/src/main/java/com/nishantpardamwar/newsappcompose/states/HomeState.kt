package com.nishantpardamwar.newsappcompose.states

import com.nishantpardamwar.newsappcompose.models.Articles

data class HomeState private constructor(
    val isLoading: Boolean = true, val news: List<Articles> = emptyList(), val error: String? = null
) : BaseState {
    companion object {
        val initialState: HomeState
            get() = HomeState()
    }
}