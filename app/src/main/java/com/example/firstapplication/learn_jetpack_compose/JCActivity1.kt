package com.example.firstapplication.learn_jetpack_compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.firstapplication.learn_jetpack_compose.ui.theme.FirstApplicationTheme

class JCActivity1 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FirstApplicationTheme {
                GreetingScreen()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingScreen() {
    val modifier1 = Modifier.width(100.dp)
    val modifier2 = modifier1.height(100.dp)

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Surface(
            modifier = Modifier.padding(innerPadding).padding(8.dp),
            shadowElevation = 16.dp,
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(160.dp)
                            .padding(16.dp)
                            .background(Color.Red)
                            .padding(16.dp)
                            .background(Color.Blue)
                            .padding(16.dp)
                            .background(Color.Green)
                    ) {
                        Greeting(
                            name = "Universe",
                            modifier = Modifier
                                .padding(innerPadding)
                                .background(MaterialTheme.colorScheme.primaryContainer)
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .background(Color.Red)
                            .padding(20.dp)
                            .background(Color.Blue)
                    )
                }
                Box (modifier = Modifier
                    .width(200.dp)
                    .height(100.dp)
                    .padding(16.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.Blue)
                    .clickable {}
                ) { }
                Text(text = if(modifier1 == modifier2) "Modifiers are mutable" else "Modifiers are immutable")
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}
