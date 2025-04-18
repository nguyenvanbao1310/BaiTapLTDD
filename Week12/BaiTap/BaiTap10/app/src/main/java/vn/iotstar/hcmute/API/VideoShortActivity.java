package vn.iotstar.hcmute.API;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.iotstar.hcmute.databinding.ActivityVideoShortBinding;

public class VideoShortActivity extends AppCompatActivity {
    private ActivityVideoShortBinding binding;
    private VideoAdapter videoAdapter;
    private List<VideoModel> videoList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityVideoShortBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        applyWindowInsets();
        getVideos();
    }

    private void applyWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.mainLayout, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void getVideos() {
        APIService.service.getVideos().enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<MessageVideoModel> call, Response<MessageVideoModel> response) {
                assert response.body() != null;
                videoList = response.body().getResult();

                videoAdapter = new VideoAdapter(VideoShortActivity.this, videoList);
                binding.viewPager.setOrientation(ViewPager2.ORIENTATION_VERTICAL);
                binding.viewPager.setAdapter(videoAdapter);
            }

            @Override
            public void onFailure(Call<MessageVideoModel> call, Throwable t) {
                Log.e("hcmute", "(Retrofit) Failed to get videos: " + t.getMessage());
            }
        });
    }
}
