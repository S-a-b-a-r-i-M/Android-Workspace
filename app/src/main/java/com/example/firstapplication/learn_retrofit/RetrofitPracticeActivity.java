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
import com.example.firstapplication.learn_retrofit.models.AlbumItem;
import com.example.firstapplication.learn_retrofit.models.NewPost;
import com.example.firstapplication.learn_retrofit.models.UserItem;

import java.util.ArrayList;
import java.util.List;


/* TODO:
    1. Crete an RecyclerView
    2. Load Particular Item by clicking(ex: fetch particular user by id or name)
    3.
 */
public class RetrofitPracticeActivity extends AppCompatActivity {

    private ActivityRetrofitPracticeBinding binding = null;
    private RetrofitPracticeViewModel viewModel = null;
    private final List<UserItem> userItemList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityRetrofitPracticeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSystemBarPadding();

        viewModel = new ViewModelProvider(this).get(RetrofitPracticeViewModel.class);

        setupListeners();
    }

    private void setupListeners() {
        binding.btnLoadAlbum.setOnClickListener(btn -> loadAlbums());

        binding.btnLoadUser.setOnClickListener(btn -> loadUsers());

        binding.btnPostById.setOnClickListener(btn -> loadPostById());

        binding.btnCreatePost.setOnClickListener(btn -> createPost());
    }

    private void loadAlbums() {
        viewModel.getAlbumsLiveData().observe(this, albumResponse -> {
            if (albumResponse.body() == null) return;

            for (AlbumItem albumItem : albumResponse.body()) {
                String albumTitle = albumItem.getAlbumId() + ".  Album Title: " + albumItem.getTitle() + "\n\n";
                binding.tvText.append(albumTitle);
            }
        });
    }

    private void loadUsers() {
        viewModel.getUsersLiveData().observe(this, users -> {
            binding.tvText.setText("");
            for(UserItem user : users) {
                String str =  user + "\n\n";
                binding.tvText.append(str);
                userItemList.add(user);
            }
        });

        viewModel.loadUserByName();
    }

    private void loadPostById() {
        viewModel.getPostById().observe(this, post -> {
            binding.tvText.setText(post.toString());
        });
    }

    private void createPost() {
        NewPost newPost = new NewPost(1, "My Title", "My Body");
        viewModel.createPost(newPost).observe(this, createdPost -> {
            binding.tvText.setText(createdPost.toString());
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