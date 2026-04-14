package com.example.firstapplication.learn_draggable.usingjc

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.firstapplication.learn_draggable.usingjc.ui.theme.FirstApplicationTheme
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyListState

class ReorderableListUsingJC : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val items = listOf(
            ReorderableJCItem(1, "one"),
            ReorderableJCItem(2, "two"),
            ReorderableJCItem(3, "three"),
            ReorderableJCItem(4, "four"),
            ReorderableJCItem(5, "five"),
        )

        setContent {
            FirstApplicationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ReorderableList(items, Modifier.padding(innerPadding))
                }
            }
        }
    }
}

data class ReorderableJCItem(val id: Int, val title: String, val description: String? = null)

@Composable
fun ReorderableList(items: List<ReorderableJCItem>, modifier: Modifier) {
    var reorderableItems by remember { mutableStateOf(items) }
    val lazyListState = rememberLazyListState()
    val reorderableLazyListState = rememberReorderableLazyListState(lazyListState = lazyListState) {
        from, to ->
        reorderableItems = reorderableItems.toMutableList().apply {
            set(to.index, set(from.index, get(to.index))) // swap
        }
    }

    LazyColumn(state = lazyListState, modifier = modifier) {
        items(reorderableItems, key = { it.id }) {
            ReorderableItem(reorderableLazyListState, it.id) { isDragging ->
                val elevation by animateDpAsState(
                    if (isDragging) 8.dp else 0.dp // visual feedback
                )

                Card(
                    modifier = Modifier.fillMaxWidth().padding(8.dp),
                    elevation = CardDefaults.cardElevation(elevation)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = it.title, modifier = Modifier.weight(1f))

                        // drag handle
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.List,
                            contentDescription = "Drag",
                            modifier = Modifier.draggableHandle() // ← library modifier
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    val items = listOf(
        ReorderableJCItem(1, "one"),
        ReorderableJCItem(2, "two"),
        ReorderableJCItem(3, "three"),
        ReorderableJCItem(4, "four"),
        ReorderableJCItem(5, "five"),
    )
    FirstApplicationTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            ReorderableList(items, Modifier.padding(innerPadding))
        }
    }
}