package com.example.firstapplication.learn_jetpack_compose.performancekillers

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.firstapplication.learn_jetpack_compose.ui.theme.FirstApplicationTheme

/*
 * ============================================================
 * PERFORMANCE KILLER #2 — Side Effects Directly in Composition
 * ============================================================
 *
 * PROBLEM:
 * - Calling a side effect (e.g., a callback, logging, state mutation)
 *   directly inside a composable body causes it to execute during the Composition phase on EVERY recomposition.
 * - Example (Bad):
 *       if (counter > 5) onThresholdReached(counter)
 *   When counter > 5, the callback fires → updates `logMessages` in
 *   the parent → triggers recomposition → callback fires again → infinite loop.
 * - Result: duplicate log entries, state grows uncontrollably, potential crash.
 *
 * HOW TO RESOLVE:
 * - Wrap the side effect in `LaunchedEffect(key)`.
 * - `LaunchedEffect` runs its block only when the key changes, not on every recomposition — breaking the infinite loop.
 * - Example (Good):
 *       LaunchedEffect(counter) {
 *           if (counter > 5) onThresholdReached(counter)
 *       }
 *
 * CONCEPTS TO KNOW:
 * 1. **Side Effects in Compose:**
 *    - A side effect is any work that escapes the scope of a composable (e.g., updating external state, logging, triggering callbacks).
 *    - Side effects should NEVER run directly in the composable body because the body can recompose many times unpredictably.
 *
 * 2. **LaunchedEffect(key):**
 *    - A composable that launches a coroutine scoped to the composition.
 *    - Re-runs its block only when the provided key(s) change.
 *    - Automatically cancelled when the composable leaves the composition.
 *    - Use it to safely trigger side effects in response to state changes.
 *
 * 3. **Other Side-Effect APIs (for reference):**
 *    - `SideEffect { }` — runs after every successful recomposition (no key).
 *    - `DisposableEffect(key) { onDispose { } }` — for effects that need cleanup.
 *    - `rememberCoroutineScope()` — for imperative (event-driven) coroutine launches.
 *
 * 4. **Recomposition:**
 *    - Compose can recompose a function multiple times when its state inputs change.
 *    - Any code in the composable body must be free of side effects and idempotent.
 * ============================================================
 */

@Composable
fun ConditionalCallback(modifier: Modifier = Modifier, onThresholdReached: (Int) -> Unit) {
    var counter by remember { mutableIntStateOf(0) }
    /*
    // ❌ Bad
    if (counter > 5) {
        onThresholdReached(counter)
    } */
    // ✅ Good
    LaunchedEffect(counter) {
        if (counter > 5) {
            onThresholdReached(counter)
        }
    }

    Column(
        modifier = modifier.fillMaxWidth().padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Counter: $counter")

        Button(onClick = { counter++ }) {
            Text("Increment Counter")
        }
    }
}

@Composable
fun SideEffectDemo() {
    var logMessages  by remember { mutableStateOf(listOf<String>()) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        ConditionalCallback { count ->
            logMessages += "Threshold callback fired with count $count"
        }

        logMessages.takeLast(5).forEach { msg ->
            Text(text = msg, modifier = Modifier.padding(8.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SideEffectDemoPreview() {
    FirstApplicationTheme {
        SideEffectDemo()
    }
}