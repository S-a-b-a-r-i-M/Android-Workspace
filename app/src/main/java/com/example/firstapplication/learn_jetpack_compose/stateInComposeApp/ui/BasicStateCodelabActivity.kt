package com.example.firstapplication.learn_jetpack_compose.stateInComposeApp.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.firstapplication.learn_jetpack_compose.stateInComposeApp.ui.theme.FirstApplicationTheme

class BasicStateCodelabActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FirstApplicationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Surface(
                        modifier = Modifier.fillMaxSize().padding(innerPadding),
                        color = MaterialTheme.colorScheme.background,
                    ) {
                        WellnessScreen()
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(
    name: String,
    modifier: Modifier = Modifier,
) {
    var count1 by remember { mutableIntStateOf(0) }
    var colour1 by remember { mutableStateOf(Color.Red) }

    var count2 by mutableIntStateOf(0)
    var colour2 by mutableStateOf(Color.Red)

    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        // Using State With Remember
        Card(modifier = Modifier.background(colour1).padding(5.dp)) {
            Column(modifier = Modifier.padding(8.dp)) {
                Text(text = "States With Remember")
                Button(onClick = {
                    count1++
                    colour1 = if (colour1 == Color.Blue) Color.Red else Color.Blue
                }) {
                    Text(text = "Count1 : $count1")
                }
            }
        }

        // Using State Without Remember
        Card(modifier = Modifier.background(colour2).padding(5.dp)) {
            Column(modifier = Modifier.padding(8.dp)) {
                Text(text = "States Without Remember")
                Button(onClick = {
                    count2++
                    colour2 = if (colour2 == Color.Blue) Color.Red else Color.Blue
                }) {
                    Text(text = "Count2 : $count2")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FirstApplicationTheme {
        val name = listOf("Android", "Ios", "Hybrid", "Else").random()
        Greeting(name)
    }
}
