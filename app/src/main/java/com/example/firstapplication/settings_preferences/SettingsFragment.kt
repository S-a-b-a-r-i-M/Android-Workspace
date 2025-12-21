package com.example.firstapplication.settings_preferences

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import androidx.compose.material.ripple.createRippleModifierNode
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceCategory
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceViewHolder
import com.example.firstapplication.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)

        setupPreferences()
    }

    private fun setupPreferences() {

    }
}

////// CUSTOM PREFERENCES /////
class CustomPreference @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet,
    defStyleAttr: Int = 0
) : Preference(context, attrs, defStyleAttr) {
    override fun onBindViewHolder(holder: PreferenceViewHolder) {
        super.onBindViewHolder(holder)
        val title = holder.itemView.findViewById<TextView>(android.R.id.title)
        val summary = holder.itemView.findViewById<TextView>(android.R.id.summary)
        title.contentDescription = "title-cnt-desc"
        summary.contentDescription = "summary-cnt-desc"
    }
}

class CustomPreferenceCategory @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : PreferenceCategory(context, attrs, defStyleAttr, defStyleRes) {
    override fun onBindViewHolder(holder: PreferenceViewHolder) {
        super.onBindViewHolder(holder)
        val title = holder.itemView.findViewById<TextView>(android.R.id.title)
        val summary = holder.itemView.findViewById<TextView>(android.R.id.summary)
        title.contentDescription = "title-cnt-desc"
        summary.contentDescription = "summary-cnt-desc"
    }
}

class CustomListPreference @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : ListPreference(context, attrs, defStyleAttr, defStyleRes) {
    override fun onBindViewHolder(holder: PreferenceViewHolder) {
        super.onBindViewHolder(holder)
        val title = holder.itemView.findViewById<TextView>(android.R.id.title)
        val summary = holder.itemView.findViewById<TextView>(android.R.id.summary)
        title.contentDescription = "title-cnt-desc"
        summary.contentDescription = "summary-cnt-desc"
    }
}