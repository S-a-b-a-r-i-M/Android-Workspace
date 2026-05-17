package com.example.firstapplication.learn_jetpack_compose.performancekillers

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.firstapplication.learn_jetpack_compose.ui.theme.FirstApplicationTheme
/*
 * ============================================================
 * PERFORMANCE KILLER #1 — Unnecessary Recomposition
 * ============================================================
 * PROBLEM:
 * - Using `.rotate(rotation)` reads the `rotation` state during the **Composition phase**.
 * - Since `rotation` changes every animation frame (~60-120 times/sec),
 *   it triggers full recomposition of `RotatingIcon` — including the `Text` composables — even though only the icon's rotation changes.
 * - This is wasteful and can cause jank/performance issues.
 *
 * HOW TO RESOLVE:
 * - Replace `.rotate(rotation)` with `.graphicsLayer { rotationZ = rotation }`.
 * - This **defers the state read** to the **Draw phase** (inside the lambda),
 *   so Compose skips recomposition entirely and only updates the graphics layer.
 * - Result: `Text` composables are never recomposed during animation.
 *
 * CONCEPTS TO KNOW:
 * 1. **3 Phases of Jetpack Compose:**
 *       Composition  →  Layout  →  Draw
 *    - Composition : Composable functions run; UI tree is built.
 *    - Layout      : Size and position of each node is calculated.
 *    - Draw        : Nodes are rendered on screen.
 *
 * 2. **State Read Scope / Deferring State Reads:**
 *    - Where you READ a state determines which phase re-executes.
 *    - Reading state inside a composable body → triggers Recomposition.
 *    - Reading state inside `graphicsLayer { }` lambda → only triggers Draw phase.
 *    - Deferring reads to the latest possible phase avoids unnecessary work.
 *
 * 3. **graphicsLayer Modifier:**
 *    - Applies transformations (scale, rotation, alpha, etc.) at the Draw phase.
 *    - Its lambda is a DrawScope, so state reads inside it are scoped to Draw only.
 *    - More efficient than modifiers that read state during Composition.
 * ============================================================
 */

@Composable
fun RotatingIcon(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition("rotation")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        label = "rotation",
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
    )

    Column (
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Loading data....")

        Icon(
            imageVector = Icons.Default.Refresh,
            contentDescription = "loader",
            modifier = Modifier
                .size(48.dp)
//              .rotate(rotation)          // ❌ Bad: reads state in Composition phase → recomposition every frame
//              .graphicsLayer(rotationZ = rotation) // ❌ Bad: reads state in Composition phase → recomposition every frame
                .graphicsLayer {
                    rotationZ = rotation   // ✅ Good: reads state in Draw phase → no recomposition
                }
        )

        Text("Please wait....")
    }
}

@Preview(showBackground = true)
@Composable
fun RotatingIconPreview() {
    FirstApplicationTheme {
        RotatingIcon(Modifier.fillMaxSize())
    }
}