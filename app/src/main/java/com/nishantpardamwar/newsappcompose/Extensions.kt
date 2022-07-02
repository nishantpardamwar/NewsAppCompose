package com.nishantpardamwar.newsappcompose

import com.nishantpardamwar.newsappcompose.models.Article
import java.text.SimpleDateFormat
import java.util.*

fun Article.getDisplayDate(): String {
    val dateString = publishedAt
    return if (dateString.isNullOrBlank()) ""
    else {
        runCatching {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
            val formatter = SimpleDateFormat("EEEE, MMMM dd, yyyy", Locale.getDefault())
            val date = inputFormat.parse(dateString)
            formatter.format(date)
        }.getOrDefault("")
    }
}