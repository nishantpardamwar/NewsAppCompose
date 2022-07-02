package com.nishantpardamwar.newsappcompose.viewmodels

import androidx.lifecycle.viewModelScope
import com.nishantpardamwar.newsappcompose.repo.Repo
import com.nishantpardamwar.newsappcompose.states.HomeState
import com.nishantpardamwar.newsappcompose.states.NewsCategory
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
        viewModelScope.launch(Dispatchers.Default) {
            val categories = repo.getNewsCategories()
            val selectedCategory = categories.firstOrNull()
            val selectedCategoryIndex = 0
            setState {
                copy(
                    newsCategories = categories,
                    selectedCategory = selectedCategory,
                    selectedCategoryIndex = selectedCategoryIndex
                )
            }
            if (selectedCategory != null) {
                loadHeadLines(selectedCategory, selectedCategoryIndex)
            }
        }
    }

    fun loadHeadLines(newsCategory: NewsCategory, index: Int) {
        viewModelScope.launch(Dispatchers.Default) {
            setState {
                copy(
                    isLoading = true, selectedCategoryIndex = index, showNewCategoryTabs = true
                )
            }
            val countryCode = try {
                repo.getIpInfo().countryCode.lowercase()
            } catch (e: Exception) {
                "in" //india country code
            }

            try {
                val news = repo.getTopHeadlines(countryCode, newsCategory.category)
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
                val selectedCategory = currentState.selectedCategory
                val selectedCategoryIndex = currentState.selectedCategoryIndex
                if (selectedCategory != null) {
                    loadHeadLines(selectedCategory, selectedCategoryIndex)
                }
            } else {
                queryFlow.emit(query)
            }
        }
    }

    private suspend fun queryNews(query: String) = withContext(Dispatchers.Default) {
        setState {
            copy(
                isLoading = true, showNewCategoryTabs = false
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