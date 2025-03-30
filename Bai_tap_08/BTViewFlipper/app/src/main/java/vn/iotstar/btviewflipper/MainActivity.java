package vn.iotstar.btviewflipper;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;
import me.relex.circleindicator.CircleIndicator3;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import vn.iotstar.btviewflipper.adapter.ImageAdapter;
import vn.iotstar.btviewflipper.model.ImageModel;
import vn.iotstar.btviewflipper.model.MessageModel;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ViewPager2 viewPager;
    private CircleIndicator3 indicator;
    private ImageAdapter adapter;
    private Handler handler;
    private Runnable autoSlideRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = findViewById(R.id.viewPager);
        indicator = findViewById(R.id.indicator);

        loadImagesFromApi();

        // Auto-run slider
        handler = new Handler(Looper.getMainLooper());
        autoSlideRunnable = () -> {
            int nextItem = (viewPager.getCurrentItem() + 1) % adapter.getItemCount();
            viewPager.setCurrentItem(nextItem, true);
            handler.postDelayed(autoSlideRunnable, 3000);
        };
    }

    private void loadImagesFromApi() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://app.iotstar.vn:8081/appfoods/flipper/coffee.jpg")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiService apiService = retrofit.create(ApiService.class);
        apiService.LoadImageSlider(0).enqueue(new Callback<MessageModel>() {
            @Override
            public void onResponse(Call<MessageModel> call, Response<MessageModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<ImageModel> images = response.body().getData();
                    adapter = new ImageAdapter(images, MainActivity.this);
                    viewPager.setAdapter(adapter);
                    indicator.setViewPager(viewPager);
                    handler.postDelayed(autoSlideRunnable, 3000);
                }
            }

            @Override
            public void onFailure(Call<MessageModel> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(autoSlideRunnable);
    }

    @Override
    protected void onResume() {
        super.onResume();
        handler.postDelayed(autoSlideRunnable, 3000);
    }
}
