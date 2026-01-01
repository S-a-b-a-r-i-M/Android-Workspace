package com.example.firstapplication.learn_app_widgets

import android.content.Context
import android.graphics.Paint
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.currentState
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.ContentScale
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.width
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import com.example.firstapplication.R

private val WATER_COUNT_KEY = intPreferencesKey("water_count")

object WaterTrackerWidget : GlanceAppWidget() {
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            GlanceTheme() {
                WaterTrackerContent()
            }
        }
    }
}

@Composable
fun WaterTrackerContent() {
    val prefState = currentState<Preferences>()
    val currentCount = prefState[WATER_COUNT_KEY] ?: 0

    Column(modifier = GlanceModifier
        .fillMaxWidth()
        .padding(14.dp)
        .background(GlanceTheme.colors.background),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Water Counter",
            style = TextStyle(
                color = GlanceTheme.colors.onBackground,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        )

        Spacer(modifier = GlanceModifier.height(12.dp))

        Row(modifier = GlanceModifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(GlanceTheme.colors.surface),
            verticalAlignment = Alignment.CenterVertically,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "--",
                style = TextStyle(fontSize = 32.sp, color = GlanceTheme.colors.onSurface)
            )

            Spacer(GlanceModifier.width(20.dp))

            Text(
                text = "$currentCount",
                style = TextStyle(fontSize = 24.sp, color = GlanceTheme.colors.onSurface)
            )

            Image(
                provider = ImageProvider(R.drawable.ic_water_glass),
                contentDescription = "Glass Of Water",
                contentScale = ContentScale.Fit,
                modifier = GlanceModifier.width(36.dp).height(36.dp)
            )

            Spacer(GlanceModifier.width(20.dp))

            Text(
                text = "+",
                style = TextStyle(fontSize = 32.sp, color = GlanceTheme.colors.onSurface)
            )
        }
    }
}

class WaterTrackerWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = WaterTrackerWidget
}