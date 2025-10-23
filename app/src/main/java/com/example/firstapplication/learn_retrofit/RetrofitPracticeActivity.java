package com.example.firstapplication.learn_retrofit;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.firstapplication.R;
import com.example.firstapplication.databinding.ActivityRetrofitPracticeBinding;

public class RetrofitPracticeActivity extends AppCompatActivity {

    private ActivityRetrofitPracticeBinding binding = null;
    private AlbumViewModel albumViewModel = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityRetrofitPracticeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSystemBarPadding();

        albumViewModel = new ViewModelProvider(this).get(AlbumViewModel.class);

        albumViewModel.getAlbumsLiveData().observe(this, albumResponse -> {
            if (albumResponse.body() == null) return;

            for (AlbumItem albumItem : albumResponse.body()) {
                String albumTitle = albumItem.getAlbumId() + ".  Album Title: " + albumItem.getTitle() + "\n\n";
                binding.tvTitle.append(albumTitle);
            }
        });
    }

    private void setSystemBarPadding() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}