package com.nishantpardamwar.newsappcompose.viewmodels

import androidx.lifecycle.ViewModel
import com.nishantpardamwar.newsappcompose.states.BaseState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.updateAndGet

open class BaseVM<State : BaseState>(initialState: State) : ViewModel() {
    private val stateFlow = MutableStateFlow(initialState)

    val state = stateFlow.asStateFlow()

    fun setState(block: State.() -> State) {
        stateFlow.update {
            it.block()
        }
    }
}