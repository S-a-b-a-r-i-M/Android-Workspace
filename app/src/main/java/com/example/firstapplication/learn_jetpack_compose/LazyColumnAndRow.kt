package com.example.firstapplication.learn_jetpack_compose

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.coerceAtLeast
import androidx.compose.ui.unit.dp

@Composable
fun MxNCardList() {
    val map = mapOf(
        "fruits" to listOf("Apple", "Banana", "Orange", "Mango", "Grapes", "Pineapple", "Papaya", "Watermelon", "Kiwi", "Strawberry"),
        "vegetables" to listOf("Carrot", "Potato", "Tomato", "Onion", "Spinach", "Cabbage", "Cauliflower", "Brinjal", "Radish", "Beetroot"),
        "animals" to listOf("Dog", "Cat", "Lion", "Tiger", "Elephant", "Horse", "Cow", "Goat", "Deer", "Monkey"),
        "sports" to listOf("Cricket", "Football", "Hockey", "Tennis", "Badminton", "Basketball", "Volleyball", "Kabaddi", "Baseball", "Golf"),
        "colors" to listOf("Red", "Blue", "Green", "Yellow", "Black", "White", "Purple", "Orange", "Pink", "Brown"),
        "appCategories" to listOf("Social", "Finance", "Health", "Education", "Travel", "Shopping", "Food", "Music", "News", "Productivity"),
        "mobileBrands" to listOf("Samsung", "Apple", "OnePlus", "Xiaomi", "Realme", "Vivo", "Oppo", "Motorola", "Nokia", "Google"),
        "programmingLanguages" to listOf("Kotlin", "Java", "Python", "C", "C++", "JavaScript", "Swift", "Go", "Rust", "Dart"),
        "cities" to listOf("Chennai", "Bengaluru", "Mumbai", "Delhi", "Hyderabad", "Pune", "Kolkata", "Coimbatore", "Madurai", "Trichy")
    )
    val mapToList = map.toList()

    LazyColumn (
        modifier = Modifier.padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(items = mapToList, key = { it.first }) { (title, values) ->
            MxNCard(title, values)
        }
    }
}

@Composable
fun MxNCard(title: String, values: List<String>) {
    val isExpanded = rememberSaveable { mutableStateOf(false) }
    val extraPadding by animateDpAsState(
        targetValue = if (isExpanded.value) 12.dp else 0.dp,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        )
    )

    Column {
        Row(Modifier.padding(top = extraPadding.coerceAtLeast(0.dp))) {
            Text(text = title, style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold
            ))
            Spacer(modifier = Modifier.padding(horizontal = 4.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                items(values) { value ->
                    Text(text = value)
                }
            }
        }
        ResizableTextView(isExpanded)
        HorizontalDivider(
            modifier = Modifier.padding(4.dp, top = extraPadding.coerceAtLeast(0.dp)),
            thickness = 1.dp
        )
    }
}