package com.example.firstapplication.kt_java;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

public class ToastDemoJ {
    private Context context = null;

    public ToastDemoJ(Context context) {
        this.context = context;
    }

    private Toast getToast(String message, int durationTime) {
        Toast toast = new Toast(context);
        toast.setText(message);
        toast.setDuration(durationTime);
        return toast;
    }

    public void setShortToast(View view) {
        getToast("Welcome to Great Kirigaln's short show", Toast.LENGTH_SHORT).show();
    }

    public void setLongToast(View view) {
        view.setOnClickListener(v ->
            getToast("Welcome to Great Kirigaln's long show", Toast.LENGTH_LONG).show()
        );
    }
}
