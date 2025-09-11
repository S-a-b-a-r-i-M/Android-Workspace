package com.example.firstapplication

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class PopUpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_pop_up)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}
/*
You can achieve this with a **PopupMenu** or **Custom Popup Window**. Here are both approaches:

## Option 1: PopupMenu (Recommended - Simple)

```kotlin
class PropertyListFragment : Fragment() {

    private fun setupPropertyActions(button: View, property: Property) {
        button.setOnClickListener { view ->
            showPropertyActionsMenu(view, property)
        }
    }

    private fun showPropertyActionsMenu(anchorView: View, property: Property) {
        val popupMenu = PopupMenu(requireContext(), anchorView)

        // Inflate menu
        popupMenu.menuInflater.inflate(R.menu.property_actions_menu, popupMenu.menu)

        // Handle menu item clicks
        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_edit -> {
                    editProperty(property)
                    true
                }
                R.id.action_make_unavailable -> {
                    makePropertyUnavailable(property)
                    true
                }
                R.id.action_delete -> {
                    deleteProperty(property)
                    true
                }
                else -> false
            }
        }

        // Show popup
        popupMenu.show()
    }

    private fun editProperty(property: Property) {
        // Navigate to edit screen
        val editFragment = EditPropertyFragment().apply {
            arguments = Bundle().apply {
                putParcelable("property", property)
            }
        }

        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, editFragment)
            .addToBackStack("edit_property")
            .commit()
    }

    private fun makePropertyUnavailable(property: Property) {
        // Show confirmation dialog first
        AlertDialog.Builder(requireContext())
            .setTitle("Make Property Unavailable")
            .setMessage("This property will be hidden from listings. Continue?")
            .setPositiveButton("Yes") { _, _ ->
                viewModel.updatePropertyAvailability(property.id, false)
            }
            .setNegativeButton("No", null)
            .show()
    }

    private fun deleteProperty(property: Property) {
        // Show confirmation dialog
        AlertDialog.Builder(requireContext())
            .setTitle("Delete Property")
            .setMessage("This action cannot be undone. Delete this property?")
            .setPositiveButton("Delete") { _, _ ->
                viewModel.deleteProperty(property.id)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}
```

### Menu Resource File (res/menu/property_actions_menu.xml):
```xml
<?xml version="1.0" encoding="utf-8"?>
<menu xmlns:android="http://schemas.android.com/apk/res/android">

    <item
        android:id="@+id/action_edit"
        android:title="Edit"
        android:icon="@drawable/ic_edit_24" />

    <item
        android:id="@+id/action_make_unavailable"
        android:title="Make Unavailable"
        android:icon="@drawable/ic_visibility_off_24" />

    <item
        android:id="@+id/action_delete"
        android:title="Delete"
        android:icon="@drawable/ic_delete_24" />

</menu>
```

## Option 2: Custom PopupWindow (More Control)

```kotlin
class PropertyActionsPopup(
    private val context: Context,
    private val onActionSelected: (ActionType) -> Unit
) {

    enum class ActionType { EDIT, MAKE_UNAVAILABLE, DELETE }

    private var popupWindow: PopupWindow? = null

    fun show(anchorView: View) {
        val inflater = LayoutInflater.from(context)
        val popupView = inflater.inflate(R.layout.popup_property_actions, null)

        setupActions(popupView)

        popupWindow = PopupWindow(
            popupView,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        ).apply {
            elevation = 8f
            setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.popup_background))

            // Show popup near the anchor view
            showAsDropDown(anchorView, 0, 0)
        }
    }

    private fun setupActions(popupView: View) {
        popupView.findViewById<View>(R.id.action_edit).setOnClickListener {
            onActionSelected(ActionType.EDIT)
            dismiss()
        }

        popupView.findViewById<View>(R.id.action_make_unavailable).setOnClickListener {
            onActionSelected(ActionType.MAKE_UNAVAILABLE)
            dismiss()
        }

        popupView.findViewById<View>(R.id.action_delete).setOnClickListener {
            onActionSelected(ActionType.DELETE)
            dismiss()
        }
    }

    private fun dismiss() {
        popupWindow?.dismiss()
        popupWindow = null
    }
}
```

### Custom Popup Layout (res/layout/popup_property_actions.xml):
```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:orientation="vertical"
    android:background="@drawable/popup_background"
    android:padding="8dp">

    <TextView
        android:id="@+id/action_edit"
        android:layout_width="match_parent"
        android:layout_height="48dp"
        android:text="Edit"
        android:drawableStart="@drawable/ic_edit_24"
        android:drawablePadding="12dp"
        android:gravity="center_vertical"
        android:paddingStart="16dp"
        android:paddingEnd="16dp"
        android:background="?attr/selectableItemBackground"
        android:textSize="16sp" />

    <TextView
        android:id="@+id/action_make_unavailable"
        android:layout_width="match_parent"
        android:layout_height="48dp"
        android:text="Make Unavailable"
        android:drawableStart="@drawable/ic_visibility_off_24"
        android:drawablePadding="12dp"
        android:gravity="center_vertical"
        android:paddingStart="16dp"
        android:paddingEnd="16dp"
        android:background="?attr/selectableItemBackground"
        android:textSize="16sp" />

    <TextView
        android:id="@+id/action_delete"
        android:layout_width="match_parent"
        android:layout_height="48dp"
        android:text="Delete"
        android:drawableStart="@drawable/ic_delete_24"
        android:drawablePadding="12dp"
        android:gravity="center_vertical"
        android:paddingStart="16dp"
        android:paddingEnd="16dp"
        android:background="?attr/selectableItemBackground"
        android:textSize="16sp"
        android:textColor="@color/error_red" />

</LinearLayout>
```

### Background Drawable (res/drawable/popup_background.xml):
```xml
<?xml version="1.0" encoding="utf-8"?>
<shape xmlns:android="http://schemas.android.com/apk/res/android"
    android:shape="rectangle">

    <solid android:color="@color/surface" />
    <corners android:radius="8dp" />
    <stroke
        android:width="1dp"
        android:color="@color/outline" />

</shape>
```

## Usage in RecyclerView Adapter:

```kotlin
class PropertyAdapter : RecyclerView.Adapter<PropertyAdapter.PropertyViewHolder>() {

    inner class PropertyViewHolder(private val binding: ItemPropertyBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(property: Property) {
            binding.propertyTitle.text = property.title
            binding.propertyPrice.text = property.price

            // Setup three-dot menu button
            binding.menuButton.setOnClickListener { view ->
                showActionsMenu(view, property)
            }
        }

        private fun showActionsMenu(anchorView: View, property: Property) {
            // Option 1: PopupMenu
            val popupMenu = PopupMenu(anchorView.context, anchorView)
            popupMenu.menuInflater.inflate(R.menu.property_actions_menu, popupMenu.menu)

            popupMenu.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.action_edit -> {
                        onPropertyAction(property, PropertyAction.EDIT)
                        true
                    }
                    R.id.action_make_unavailable -> {
                        onPropertyAction(property, PropertyAction.MAKE_UNAVAILABLE)
                        true
                    }
                    R.id.action_delete -> {
                        onPropertyAction(property, PropertyAction.DELETE)
                        true
                    }
                    else -> false
                }
            }

            popupMenu.show()

            // Option 2: Custom PopupWindow
            /*
            val popup = PropertyActionsPopup(anchorView.context) { actionType ->
                when (actionType) {
                    PropertyActionsPopup.ActionType.EDIT ->
                        onPropertyAction(property, PropertyAction.EDIT)
                    PropertyActionsPopup.ActionType.MAKE_UNAVAILABLE ->
                        onPropertyAction(property, PropertyAction.MAKE_UNAVAILABLE)
                    PropertyActionsPopup.ActionType.DELETE ->
                        onPropertyAction(property, PropertyAction.DELETE)
                }
            }
            popup.show(anchorView)
            */
        }
    }

    private var onPropertyAction: (Property, PropertyAction) -> Unit = { _, _ -> }

    fun setOnPropertyActionListener(listener: (Property, PropertyAction) -> Unit) {
        onPropertyAction = listener
    }

    enum class PropertyAction { EDIT, MAKE_UNAVAILABLE, DELETE }
}
```

## Recommendations:

- **Use PopupMenu** for simple menus (easier implementation)
- **Use Custom PopupWindow** when you need more control over styling/behavior
- **Always show confirmation dialogs** for destructive actions (delete, make unavailable)
- **Position popup** using `showAsDropDown()` for consistent placement

**PopupMenu** is usually the best choice for your use case - it's simple, follows Material Design guidelines, and handles positioning automatically!
 */