package com.nishantpardamwar.newsappcompose.viewmodels

import androidx.lifecycle.viewModelScope
import com.nishantpardamwar.newsappcompose.repo.Repo
import com.nishantpardamwar.newsappcompose.states.HomeState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.Exception

@HiltViewModel
class HomeVM @Inject constructor(
    private val repo: Repo
) : BaseVM<HomeState>(HomeState.initialState) {

    private val queryFlow = MutableSharedFlow<String>()

    init {
        viewModelScope.launch {
            queryFlow.debounce(300).onEach {
                queryNews(it)
            }.launchIn(viewModelScope)
        }
    }

    fun loadHeadLines() {
        viewModelScope.launch(Dispatchers.Default) {
            setState {
                copy(
                    isLoading = true
                )
            }
            val countryCode = try {
                repo.getIpInfo().countryCode.lowercase()
            } catch (e: Exception) {
                "in" //india country code
            }

            try {
                val news = repo.getTopHeadlines(countryCode)
                setState {
                    copy(
                        isLoading = false, news = news.articles ?: emptyList()
                    )
                }
            } catch (e: Exception) {
                setState {
                    copy(
                        isLoading = false, error = e.message
                    )
                }
            }
        }
    }

    fun loadNews(query: String) {
        viewModelScope.launch {
            if (query.isBlank()) {
                loadHeadLines()
            } else {
                queryFlow.emit(query)
            }
        }
    }

    private suspend fun queryNews(query: String) = withContext(Dispatchers.Default) {
        setState {
            copy(
                isLoading = true
            )
        }

        try {
            val news = repo.getNews(query)
            setState {
                copy(
                    isLoading = false, news = news.articles ?: emptyList()
                )
            }
        } catch (e: Exception) {
            setState {
                copy(
                    isLoading = false, error = e.message
                )
            }
        }
    }
}