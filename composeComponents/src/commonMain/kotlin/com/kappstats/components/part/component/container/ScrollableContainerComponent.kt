package com.kappstats.components.part.component.container

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun <T> ScrollableContainerComponent(
    paddingValues: PaddingValues,
    listState: LazyListState = rememberLazyListState(),
    modifier: Modifier = Modifier,
    itemList: List<T>? = null,
    horizontalAlignment: Alignment.Horizontal = Alignment.CenterHorizontally,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    contentItem: @Composable (T) -> Unit = {},
    contentEmpty: @Composable () -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize().then(modifier),
        state = listState,
        contentPadding = paddingValues,
        horizontalAlignment = horizontalAlignment,
        verticalArrangement = verticalArrangement
    ) {
        if (itemList.isNullOrEmpty()) {
            item {
                contentEmpty()
            }
        } else {
            items(
                items = itemList,
                key = { key -> key.hashCode() }
            ) { item ->
                contentItem(item)
            }
        }
    }
}