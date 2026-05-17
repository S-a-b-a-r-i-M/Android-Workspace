package com.example.firstapplication.learn_jetpack_compose.performancekillers

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.firstapplication.learn_jetpack_compose.ui.theme.FirstApplicationTheme

/*
Problem: Recomposition occurs while reordering items while not using LazyColumn/LazyRow.
Goal: Recomposition shouldn't happen while reordering items regardless of the layout or parent.
*/
/*============================================================
* PERFORMANCE KILLER #3 — Not Using `key()` When Rendering Lists
* ============================================================
*
* PROBLEM:
* - When rendering a list of composables using `forEach` (or any loop)
*   WITHOUT `key()`, Compose identifies each item purely by its **position** in the composition tree.
* - If items are reordered, added, or removed, Compose cannot tell which
*   composable maps to which item — so it recomposes ALL items from scratch.
* - Example (Bad):
*       fields.forEach { field ->
    *           FormFieldItem(field)   // ❌ identified by index, not identity
    *       }
* - When "Swap Items" is clicked, both items recompose unnecessarily,
*   and any internal state (e.g., text input, scroll position) is LOST or RESET.
*
* HOW TO RESOLVE:
* - Wrap each item with `key(uniqueId) { }` so Compose tracks items by
*   their **stable identity**, not their position.
* - Example (Good):
*       fields.forEach { field ->
    *           key(field.id) {
        *               FormFieldItem(field)   // ✅ identified by field.id
        *           }
    *       }
* - Now when items are reordered, Compose moves existing compositions
*   instead of destroying and recreating them — preserving internal state.
*
* CONCEPTS TO KNOW:
* 1. **Composition Tree & Position-Based Identity:**
*    - By default, Compose identifies composables by their call-site position
*      in the tree. Changing order = different position = full recomposition.
*
* 2. **`key(value) { }` Composable:**
*    - Assigns a stable, unique identity to a composable subtree.
*    - When the list is reordered, Compose matches old and new compositions
*      by key and simply moves them — avoiding unnecessary recomposition.
*    - The key must be unique and stable (e.g., an ID, not an index).
*
* 3. **LazyColumn / LazyRow — `key` parameter:**
*    - In lazy lists, `key` is provided directly in the `items()` DSL:
*          items(fields, key = { it.id }) { field -> FormFieldItem(field) }
*    - Same concept, built into the lazy list API.
* ============================================================
*/

data class FormField(val id : String, val label: String, val color: Color)

@Composable
fun DynamicForm(modifier: Modifier = Modifier) {
    var fields by remember {
        mutableStateOf(listOf(
            FormField("name", "Name", Color.Red),
            FormField("email", "Email", Color.Gray),
        ))
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Button(onClick = { fields = fields.reversed() }) {
            Text("Swap Items")
        }
        /* // ❌ Bad
        fields.forEach { field ->
            FormFieldItem(field)
        }
        */
        // ✅ Good
        fields.forEach { field ->
            key(field.id) {
                FormFieldItem(field)
            }
        }
    }
}


@Composable
fun FormFieldItem(field: FormField, modifier: Modifier = Modifier) {
    Text(
        text = field.label,
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .border(
                width = 1.dp,
                color = field.color,
                shape = RoundedCornerShape(4.dp)
            )
            .padding(16.dp),
        color = field.color
    )
}


@Preview(showBackground = true)
@Composable
fun DynamicFormPreview() {
    FirstApplicationTheme {
        DynamicForm(Modifier.padding(24.dp))
    }
}

