package com.example.firstapplication.settings_preferences

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.widget.TextView
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
        val replyListPreference = findPreference<CustomListPreference>("custom_reply")
        replyListPreference?.entries = arrayOf("Replay All", "Replay Partial", "Replay None")
    }

    override fun onDisplayPreferenceDialog(preference: Preference) {
        // Setting Cont-Desc for AlertDialog ListView Items
        val fragmentManager = getParentFragmentManager()
        val tagName = CustomListPreferenceDialogFragmentCompat::class.java.getSimpleName()

        if (fragmentManager.findFragmentByTag(tagName) != null) return

        val listPreferenceFragment: CustomListPreferenceDialogFragmentCompat =
            CustomListPreferenceDialogFragmentCompat.newInstance(preference.key) { textView ->
                println("textView added: ${textView.text}")
            }
        listPreferenceFragment.setTargetFragment(this, 0)
        listPreferenceFragment.show(fragmentManager, tagName)
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