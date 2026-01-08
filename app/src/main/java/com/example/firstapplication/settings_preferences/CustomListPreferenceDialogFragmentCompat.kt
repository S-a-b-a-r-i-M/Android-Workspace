package com.example.firstapplication.settings_preferences

import android.app.Dialog
import android.content.Context
import androidx.appcompat.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.AbsListView.OnScrollListener.SCROLL_STATE_IDLE
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.AppCompatCheckedTextView
import androidx.preference.ListPreferenceDialogFragmentCompat

open class CustomListPreferenceDialogFragmentCompat protected constructor() : ListPreferenceDialogFragmentCompat() {
    var onChildViewAddedCallBack: OnChildTextViewAddedCallback? = null
        private set

    fun interface OnChildTextViewAddedCallback {
        fun onAdded(textView: TextView)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as AlertDialog
        dialog.listView.setOnHierarchyChangeListener(object :  ViewGroup.OnHierarchyChangeListener {
            override fun onChildViewAdded(parent: View?, child: View?) {
                if (child is TextView) {
                    onChildViewAddedCallBack?.onAdded(child)
                    println("onChildViewAdded: ${child.text}")
                }
            }

            override fun onChildViewRemoved(parent: View?, child: View?) {}
        })

        dialog.listView.setOnScrollListener(object : AbsListView.OnScrollListener{
            override fun onScroll(
                view: AbsListView?, firstVisibleItem: Int, visibleItemCount: Int, totalItemCount: Int
            ) {}

            override fun onScrollStateChanged(
                view: AbsListView?,
                scrollState: Int
            ) {
                if (scrollState == SCROLL_STATE_IDLE && view is ListView) {
                    // Final update when scroll stops
                    val firstVisible = view.firstVisiblePosition
                    val visibleCount = view.childCount
                    updateVisibleViews(view, firstVisible, visibleCount)
                }
            }
        })

        return dialog
    }

    private fun updateVisibleViews(listView: ListView, firstVisibleItem: Int, visibleItemCount: Int) {
        for (i in 0 until visibleItemCount) {
            val view = listView.getChildAt(i)
            if (view is TextView) {
                val position = firstVisibleItem + i
                onChildViewAddedCallBack?.onAdded(view)
                println("Visible view at position $position: ${view.text}")
            }
        }
    }

    override fun onPrepareDialogBuilder(builder: AlertDialog.Builder) {
        super.onPrepareDialogBuilder(builder)
//        builder.setAdapter(
//            MyCustomListAdapter<Any>(activity as Context, android.R.layout.select_dialog_singlechoice, android.R.id.text1, listOf("Reply", "Reply to all")),
//        ) { dialog , which ->
//
//        }
    }

    companion object {
        fun newInstance(key: String, callBack: (OnChildTextViewAddedCallback)? = null): CustomListPreferenceDialogFragmentCompat {
            val bundle = Bundle(1)
            bundle.putString(ARG_KEY, key)
            return CustomListPreferenceDialogFragmentCompat().apply {
                arguments = bundle
                if (callBack != null) {
                    onChildViewAddedCallBack = callBack
                }
            }
        }
    }
}

class MyCustomListAdapter<T>(
    context: Context,
    @LayoutRes resourceId: Int,
    @IdRes textViewResourceId: Int,
    items: List<T>
) : ArrayAdapter<T>(context, resourceId, textViewResourceId, items) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getView(position, convertView, parent)
        if(view is TextView) {
            view.contentDescription = view.text
            println("contentDescription: ${view.text}")
        }

        return view
    }
}