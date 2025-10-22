package com.example.firstapplication;

import android.app.ActionBar;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

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
        TextView textView = new TextView(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(24, 24, 24, 24);
        textView.setLayoutParams(params);
        textView.setText("As a developer, you can define shortcuts to perform specific actions in your app. You can display these shortcuts in a supported launcher or assistant—like Google Assistant—and help your users quickly start common or recommended tasks within your app.");
        textView.setMaxLines(2);
        textView.setEllipsize(TextUtils.TruncateAt.END);

        binding.getRoot().addView(textView);
    }
}