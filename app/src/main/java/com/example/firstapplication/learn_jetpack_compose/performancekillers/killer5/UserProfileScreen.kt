package com.example.firstapplication.learn_jetpack_compose.performancekillers.killer5

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.firstapplication.learn_jetpack_compose.performancekillers.RotatingIcon
import com.example.firstapplication.learn_jetpack_compose.ui.theme.FirstApplicationTheme

/*
 * ============================================================
 * PERFORMANCE KILLER #5 — Blocking Work on the Main Dispatcher
 * ============================================================
 *
 * PROBLEM:
 * - `loadUserDataBad()` launches a coroutine with `viewModelScope.launch { }`,
 *   which by default runs on `Dispatchers.Main`.
 * - Inside, `simulateBlockingFileReadBad()` and `processUserDataBad()` are
 *   plain (non-suspend) functions that do heavy CPU/IO work:
 *     • Building a 1,000,000-line StringBuilder  → CPU-intensive
 *     • Looping 10,000 times over a string        → CPU-intensive
 * - Running these on the Main thread blocks the UI thread → app freezes/janks.
 *
 * HOW TO RESOLVE:
 * - Move the heavy work off the Main thread using `withContext(Dispatchers.IO)`.
 * - `loadUserDataGood()` wraps blocking calls inside `withContext(Dispatchers.IO) { }`,
 *   so the coroutine suspends on Main, executes work on an IO thread,
 *   then resumes on Main — keeping the UI responsive throughout.
 *
 *   ❌ Bad:
 *   suspend fun loadUserDataBad(...): String {
 *       val content = simulateBlockingFileReadBad()   // runs on Main → blocks UI
 *       return processUserDataBad(content, userId)    // runs on Main → blocks UI
 *   }
 *
 *   ✅ Good:
 *   suspend fun loadUserDataGood(...): String {
 *       val content = withContext(Dispatchers.IO) { simulateBlockingFileReadBad() }
 *       return withContext(Dispatchers.IO) { processUserDataBad(content, userId) }
 *   }
 *
 * CONCEPTS TO KNOW:
 * 1. **Coroutine Dispatchers:**
 *    - `Dispatchers.Main`    → UI thread; use only for UI updates, fast operations.
 *    - `Dispatchers.IO`      → Optimised for blocking IO/CPU work (file, network, DB).
 *    - `Dispatchers.Default` → Optimised for CPU-intensive computations.
 *
 * 2. **viewModelScope.launch { }:**
 *    - Defaults to `Dispatchers.Main`.
 *    - Safe for updating StateFlow/LiveData, but NOT for heavy work.
 *
 * 3. **withContext(Dispatcher) { }:**
 *    - Suspends the current coroutine and resumes it on a different dispatcher.
 *    - The calling coroutine (on Main) is NOT blocked — it suspends and waits.
 *    - After the block completes, execution returns to the original dispatcher.
 *
 * 4. **suspend vs blocking:**
 *    - A suspend function does NOT automatically run off the Main thread.
 *    - You must explicitly use `withContext` to switch threads for blocking code.
 * ============================================================
 */

@Composable
fun UserProfileScreen(
    modifier: Modifier = Modifier,
    viewModel: UserViewModel = viewModel()
) {
    val userData by viewModel.userData.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        if (isLoading) {
            // Show rotating loader while data is being fetched
            RotatingIcon()
        } else {
            if (userData.isNotEmpty()) {
                Text(userData)
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Button(onClick = { viewModel.loadUserDataBad("user_001") }) {
                Text("Load UserData Bad")
            }
            Button(onClick = { viewModel.loadUserDataGood("user_001") }) {
                Text("Load UserData Good")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UserProfileScreenPreview() {
    FirstApplicationTheme {
        UserProfileScreen(Modifier.padding(24.dp))
    }
}