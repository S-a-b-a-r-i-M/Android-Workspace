package com.example.firstapplication;

import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.firstapplication.databinding.ActivityPracticeJavaBinding;

public class PracticeJavaActivity extends AppCompatActivity {

    private ActivityPracticeJavaBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityPracticeJavaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        setupUI();
    }

    private void setupUI() {
        binding.getRoot().addView(getContainerWithTextView());

        LinearLayout container = new LinearLayout(this);
        container.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams containerParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        container.setLayoutParams(containerParams);
        TextView textView = new TextView(this);
        LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1f
        );
        textView.setLayoutParams(textParams);
        textView.setText("asdafsdfsdfadfasdfasdf asdafsdfsdfadfasdfasdf asdafsdfsdfadfasdfasdf asdafsdfsdfadfasdfasdf asdafsdfsdfadfasdfasdf asdafsdfsdfadfasdfasdf asdafsdfsdfadfasdfasdf asdafsdfsdfadfasdfasdf asdafsdfsdfadfasdfasdf asdafsdfsdfadfasdfasdf");
        textView.setTextColor(Color.BLACK);
        textView.setEllipsize(TextUtils.TruncateAt.END);
        textView.setMaxLines(2);
        container.addView(textView);
        binding.getRoot().addView(container);
    }

    private LinearLayout getContainerWithTextView() {
        // Parent Container
        LinearLayout container = new LinearLayout(this);
        container.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams containerParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        container.setLayoutParams(containerParams);

        // Text View
        TextView textView = new TextView(this);
        LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1f
        );
        textParams.setMargins(24, 24, 24, 24);
        textView.setLayoutParams(textParams);
        textView.setText("As a developer, you can define shortcuts to perform specific actions in your app. You can display these shortcuts in a supported launcher or assistant—like Google Assistant—and help your users quickly start common or recommended tasks within your app.");
        // Truncate
        textView.setMaxLines(2);
        textView.setEllipsize(TextUtils.TruncateAt.END);

        // Add TextView Into Container
        container.addView(textView);
        return container;
    }
}