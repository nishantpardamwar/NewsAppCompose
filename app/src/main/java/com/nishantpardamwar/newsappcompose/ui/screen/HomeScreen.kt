package com.nishantpardamwar.newsappcompose.ui.screen

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.nishantpardamwar.newsappcompose.R
import com.nishantpardamwar.newsappcompose.models.Articles
import com.nishantpardamwar.newsappcompose.states.HomeState
import com.nishantpardamwar.newsappcompose.ui.ExpandableText
import com.nishantpardamwar.newsappcompose.viewmodels.HomeVM

@Composable
fun HomeScreen(vm: HomeVM) {
    val context = LocalContext.current
    val state by vm.state.collectAsState()

    LaunchedEffect(Unit) {
        vm.loadHeadLines()
    }

    HomeScreen(state = state, onSearchQuery = vm::loadNews, onNewsOrgClick = {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse("https://newsapi.org")
        }
        context.startActivity(intent)
    })
}

@Composable
private fun HomeScreen(state: HomeState, onSearchQuery: (String) -> Unit, onNewsOrgClick: () -> Unit) {
    Scaffold(topBar = {
        TopBar(
            onSearchQuery = onSearchQuery
        )
    }, bottomBar = {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(5.dp), Arrangement.End
        ) {
            val annotatedText = buildAnnotatedString {
                append("Powered By ")

                pushStringAnnotation(tag = "link", annotation = "link")
                withStyle(style = SpanStyle(color = MaterialTheme.colors.primary)) {
                    append("NewsAPI.org")
                }
            }
            ClickableText(text = annotatedText, onClick = { offset ->
                annotatedText.getStringAnnotations(tag = "link", start = offset, end = offset).firstOrNull()?.let {
                    onNewsOrgClick()
                }
            })
        }
    }) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding), Alignment.Center
        ) {
            when {
                state.isLoading.not() && state.error != null -> {
                    Failure(error = state.error ?: "Something went wrong")
                }
                state.isLoading -> {
                    CircularProgressIndicator()
                }
                else -> {
                    NewsListing(news = state.news)
                }
            }
        }
    }
}

@Composable
private fun TopBar(onSearchQuery: (String) -> Unit) {
    var text by remember { mutableStateOf("") }

    LaunchedEffect(text) {
        onSearchQuery(text)
    }

    TextField(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 10.dp, vertical = 5.dp)
        .background(Color.Cyan, RoundedCornerShape(8.dp))
        .height(56.dp), value = text, onValueChange = {
        text = it
    }, leadingIcon = {
        Icon(imageVector = Icons.Default.Search, contentDescription = "Search Icon", tint = Color.Gray)
    }, trailingIcon = {
        if (text.isNotEmpty()) {
            Icon(modifier = Modifier.clickable {
                text = ""
            }, imageVector = Icons.Default.Clear, contentDescription = "Clear Icon", tint = Color.Gray)
        }
    }, colors = TextFieldDefaults.textFieldColors(
        unfocusedIndicatorColor = Color.Transparent, focusedIndicatorColor = Color.Transparent
    ), label = {
        Text(text = "Search News")
    })
}

@Composable
private fun Failure(error: String) {
    Text(text = error)
}

@Composable
private fun NewsListing(news: List<Articles>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        itemsIndexed(news) { index, item ->
            NewsItem(news = item)
        }
    }
}

@Composable
private fun NewsItem(news: Articles) {
    Card(
        modifier = Modifier,
        shape = RoundedCornerShape(8.dp),
        elevation = 10.dp,
        border = BorderStroke(1.dp, Color.Gray.copy(alpha = .5f))
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            AsyncImage(
                modifier = Modifier
                    .padding(2.dp)
                    .size(112.dp, 70.dp)
                    .clip(RoundedCornerShape(12.dp)),
                model = news.urlToImage,
                placeholder = painterResource(id = R.drawable.placeholder),
                contentDescription = ""
            )
            Spacer(modifier = Modifier.padding(horizontal = 5.dp))
            Column {
                if (news.title != null) {
                    Text(text = news.title, fontWeight = FontWeight.Bold)
                }
                if (news.description != null) {
                    Spacer(modifier = Modifier.padding(top = 5.dp))
                    ExpandableText(text = news.description, minLines = 3)
                }
            }
        }
    }
}


@Preview
@Composable
fun HomeScreenLoadingPreview() {
    val state = HomeState.initialState

    HomeScreen(state = state, onSearchQuery = {}, onNewsOrgClick = {})
}

@Preview
@Composable
fun HomeScreenNewsPreview() {
    val state = HomeState.initialState.copy(
        isLoading = false, news = listOf(
            Articles(title = "Test News 1", description = "TestDescription 1"),
            Articles(title = "Test News 2", description = "TestDescription 2")
        )
    )

    HomeScreen(state = state, onSearchQuery = {}, onNewsOrgClick = {})
}

@Preview
@Composable
fun HomeScreenErrorPreview() {
    val state = HomeState.initialState.copy(isLoading = false, error = "No Result Found")

    HomeScreen(state = state, onSearchQuery = {}, onNewsOrgClick = {})
}