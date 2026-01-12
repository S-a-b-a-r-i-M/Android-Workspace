package com.example.firstapplication.learn_jetpack_compose

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.firstapplication.learn_jetpack_compose.ui.theme.FirstApplicationTheme
import com.example.firstapplication.learn_jetpack_compose.ui.theme.PurpleGrey10

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

    val hPaddingModifier = Modifier.padding(vertical = 8.dp)

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Surface(
            modifier = Modifier
                .padding(innerPadding)
                .padding(6.dp),
            shadowElevation = 16.dp,
        ) {
            Column {
                BoxExamples(innerPadding)

                HorizontalDivider(hPaddingModifier)

                Text(
                    text = if (modifier1 == modifier2) "Modifiers are mutable" else "Modifiers are immutable",
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(8.dp, 0.dp, 0.dp, 0.dp)
                )

                HorizontalDivider(hPaddingModifier)

                RowOfDifferentButtons()

                HorizontalDivider(hPaddingModifier)

                ResizableTextView()

            }
        }
    }
}

@Composable
fun ResizableTextView() {
    var isExpanded by remember { mutableStateOf(false) }

    Text(
        text = "Compose apps transform data into UI by calling composable functions. If your data changes, Compose re-executes these functions with the new data, creating an updated UI—this is called recomposition.",
        maxLines = if (isExpanded) Int.MAX_VALUE else 2,
        overflow = TextOverflow.Ellipsis,
    )
    Spacer(Modifier.height(4.dp))
    Text(
        text = if (isExpanded) "Show Less" else "Show More",
        color = MaterialTheme.colorScheme.primary,
        style = MaterialTheme.typography.labelMedium,
        modifier = Modifier.clickable(true) { isExpanded = !isExpanded }
    )
}

@Composable
fun BoxExamples(innerPadding: PaddingValues) {
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
            // We can use kotlin functionalities here
            for (i in 0..10)
                Text(
                    i.toString(),
                    color = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier.padding(1.dp)
                )
        }
        Box(
            modifier = Modifier
                .width(200.dp)
                .height(100.dp)
                .padding(16.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.Blue)
                .clickable {}
        ) { }
    }
}

@Composable
fun RowOfDifferentButtons() {
    for (i in listOf("V", "S", "A"))
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(PurpleGrey10),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Text(
                text = i,
                modifier = Modifier.padding(8.dp)
            )
            Button(onClick = { }) {
                Text("Button")
            }
            OutlinedButton({}) {
                Text("Outlined")
            }
            ElevatedButton({}) {
                Text("Elevated")
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
