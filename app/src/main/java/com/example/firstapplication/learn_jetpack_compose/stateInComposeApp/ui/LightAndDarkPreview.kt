package com.example.firstapplication.learn_jetpack_compose.stateInComposeApp.ui

import androidx.compose.ui.tooling.preview.Preview

@Preview(
    name = "Light Mode",
    showBackground = true,
    backgroundColor = 0xFFFFFFFF
)
@Preview(
    name = "Dark Mode",
    showBackground = true,
    backgroundColor = 0xFF000000
)
annotation class LightAndDarkPreview // Like this we can create multiple preview annotations with different configurations and use them in our composables.
