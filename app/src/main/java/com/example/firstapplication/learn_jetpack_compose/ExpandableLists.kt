package com.example.firstapplication.learn_jetpack_compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.DrawableRes
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.coerceAtLeast
import androidx.compose.ui.unit.dp
import com.example.firstapplication.R
import com.example.firstapplication.learn_jetpack_compose.ui.theme.FirstApplicationTheme

class ExpandableLists : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FirstApplicationTheme {
                ExpandableListsScreen()
            }
        }
    }
}

// @Preview(showBackground = true, widthDp = 420, heightDp = 800, uiMode = UI_MODE_NIGHT_YES, name = "PreviewDark")
@Preview(showBackground = true, widthDp = 320, heightDp = 700)
@Composable
fun ExpandableListsScreen(modifier: Modifier = Modifier) {
    val cardDataList = listOf(
        CardData(
            id = 1,
            heading = "Favorites",
            highlights = listOf("Quick access", "Pinned items"),
            description = "Keep all your favorite items in one place for faster access."
        ),
        CardData(
            id = 2,
            heading = "Health Tracker",
            highlights = listOf("Steps", "Calories", "Sleep"),
            description = "Track your daily health stats and stay fit effortlessly."
        ),
        CardData(
            id = 3,
            heading = "Finance Overview",
            highlights = listOf("Expenses", "Savings", "Reports"),
            description = "Get a clear overview of your financial activities and goals."
        ),
        CardData(
            id = 4,
            heading = "Travel Planner",
            highlights = listOf("Trips", "Bookings", "Maps"),
            description = "Plan trips, manage bookings, and explore destinations easily."
        ),
        CardData(
            id = 5,
            heading = "Learning Hub",
            highlights = listOf("Courses", "Progress", "Certificates"),
            description = "Centralized place to learn new skills and track your growth."
        ),
        CardData(
            id = 6,
            heading = "Task Manager",
            highlights = listOf("To-do", "Reminders", "Deadlines"),
            description = "Organize tasks efficiently and never miss a deadline."
        ),
        CardData(
            id = 7,
            heading = "Music Library",
            highlights = listOf("Playlists", "Downloads", "Favorites"),
            description = "Manage and enjoy your music collection anytime, anywhere."
        ),
        CardData(
            id = 8,
            heading = "Smart Home",
            highlights = listOf("Lights", "Security", "Automation"),
            description = "Control and automate your smart home devices seamlessly."
        ),
        CardData(
            id = 9,
            heading = "News Feed",
            highlights = listOf("Trending", "Bookmarks", "Categories"),
            description = "Stay updated with the latest news from trusted sources."
        ),
        CardData(
            id = 10,
            heading = "Shopping Assistant",
            highlights = listOf("Deals", "Wishlist", "Orders"),
            description = "Find the best deals and track your shopping history."
        ),
        CardData(
            id = 11,
            heading = "Workout Plans",
            highlights = listOf("Strength", "Cardio", "Yoga"),
            description = "Personalized workout plans to match your fitness goals."
        ),
        CardData(
            id = 12,
            heading = "Notes",
            highlights = listOf("Quick notes", "Sync", "Search"),
            description = "Capture ideas instantly and access them across devices."
        ),
        CardData(
            id = 13,
            heading = "Calendar",
            highlights = listOf("Events", "Meetings", "Reminders"),
            description = "Manage your schedule and stay on top of important events."
        ),
        CardData(
            id = 14,
            heading = "Cloud Storage",
            highlights = listOf("Backup", "Sync", "Share", "Download", "Upload"),
            description = "Securely store, sync, and share your files in the cloud."
        ),
        CardData(
            id = 15,
            heading = "Security Center",
            highlights = listOf("Privacy", "Permissions", "Alerts"),
            description = "Monitor and control your app security and privacy settings."
        )
    )

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Surface(modifier = modifier
            .padding(innerPadding)
            .fillMaxSize()
        ) {
            Image(
                painter = painterResource(R.drawable.blak_panther),
                contentDescription = "",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            LazyColumn(
                modifier = Modifier.padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(cardDataList, key = { it.id }) { data ->
                    DataCard(data)
                }
            }
        }
    }
}

data class CardData(
    val id: Int,
    @DrawableRes val iconRes: Int = R.drawable.baseline_favorite_filled,
    val heading: String,
    val highlights: List<String>,
    val description: String
)

@Composable
private fun DataCard(data : CardData) {
    var isExpanded by rememberSaveable { mutableStateOf(false) }
    val extraPadding by animateDpAsState(
        targetValue = if (isExpanded) 4.dp else 0.dp,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        )
    )

    Column(
        modifier = Modifier
            .border(
                width = 2.dp,
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(12.dp)
            ).background(Color.Transparent)
            .padding(vertical = 8.dp, horizontal = 12.dp)
            .padding(vertical = extraPadding.coerceAtLeast(0.dp))
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // TODO: Icon Image (ex: country flag)
            Text(text = data.heading, style = MaterialTheme.typography.titleMedium)
            IconButton(
                onClick = { isExpanded = !isExpanded},
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    imageVector = if (isExpanded) Icons.Outlined.KeyboardArrowUp else Icons.Outlined.KeyboardArrowDown,
                    contentDescription = if (isExpanded) stringResource(R.string.show_less) else stringResource(R.string.show_more),
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
        // Highlights
        LazyRow(
            modifier = Modifier.padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(data.highlights) { highlight ->
                Card(
                    modifier = Modifier.padding(horizontal = 4.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.Magenta)
                ) {
                    Text(
                        highlight,
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp),
                        style = MaterialTheme.typography.labelSmall,
                    )
                }
            }
        }

        // Description
        if (isExpanded) Text(
            text = data.description,
            modifier = Modifier.padding(vertical = 4.dp)
        )
    }
}