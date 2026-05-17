package com.example.firstapplication.learn_jetpack_compose.performancekillers

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.firstapplication.learn_jetpack_compose.ui.theme.FirstApplicationTheme

data class UserScreenState(
    val userName: String,
    val email: String,
    val profileImageUrl: String,
    val followerCount: Int,
    val notificationCount: Int,
    val isVerified: Boolean,
)

/**
 * BAD: Passing entire state object to composables that only need specific fields.
 * This causes unnecessary recompositions when ANY field changes.
 */
@Composable
fun UserScreen(
    state: UserScreenState,
    modifier: Modifier = Modifier,
    onUpdate: (String) -> Unit = {}
) {
    Column(
        modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        UserHeaderBad(state)

        NotificationBadgeBad(state)

        HorizontalDivider()

        UserHeaderGood(state.userName, state.followerCount)

        NotificationBadgeGood(state.email, state.notificationCount)

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Absolute.SpaceEvenly,
        ) {
            Button(onClick = { onUpdate("notification") }) {
                Text("Add Notification")
            }

            Button(onClick = { onUpdate("follower") }) {
                Text("Add Follower")
            }
        }
    }
}

@Composable
fun UserHeaderBad(state: UserScreenState) {
    Column(
        Modifier.border(
            width = 1.dp,
            color = Color.Black,
            shape = RoundedCornerShape(4.dp)
        ).padding(12.dp)
    ) {
        Text(state.userName, modifier = Modifier.padding(vertical = 4.dp))
        Text("${state.followerCount} Followers")
    }
}

@Composable
fun NotificationBadgeBad(state: UserScreenState) {
    Column(
        Modifier.border(
            width = 1.dp,
            color = Color.Black,
            shape = RoundedCornerShape(4.dp)
        ).padding(12.dp)
    ) {
        Text(state.email, modifier = Modifier.padding(vertical = 4.dp))
        Text("${state.notificationCount} Notifications",)
    }
}

@Composable
fun UserHeaderGood(userName: String, followerCount: Int) {
    Column(
        Modifier.border(
            width = 1.dp,
            color = Color.Black,
            shape = RoundedCornerShape(4.dp)
        ).padding(12.dp)
    ) {
        Text(userName, modifier = Modifier.padding(vertical = 4.dp))
        Text("$followerCount Followers")
    }
}

@Composable
fun NotificationBadgeGood(email: String, notificationCount: Int) {
    Column(
        Modifier.border(
            width = 1.dp,
            color = Color.Black,
            shape = RoundedCornerShape(4.dp)
        ).padding(12.dp)
    ) {
        Text(email, modifier = Modifier.padding(vertical = 4.dp))
        Text("$notificationCount Notifications",)
    }
}

@Preview(showBackground = true)
@Composable
fun UserScreenPreview() {
    var state by remember {
        mutableStateOf(
            UserScreenState(
                userName = "UserName",
                email = "email@gmail.com",
                profileImageUrl = "TODO()",
                followerCount = 1231,
                notificationCount = 12,
                isVerified = true
            )
        )
    }

    FirstApplicationTheme {
        UserScreen(state, Modifier.padding(24.dp)) { updateReqKey ->
            when(updateReqKey.lowercase()) {
                "notification" -> {
                    state = state.copy(notificationCount = state.notificationCount + 1)
                }
                "follower" -> {
                    state = state.copy(followerCount = state.followerCount + 1)
                }
            }
        }
    }
}