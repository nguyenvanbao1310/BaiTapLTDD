package vn.iotstar.viewflipper;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;


import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import me.relex.circleindicator.CircleIndicator;
import vn.iotstar.viewflipper.adapter.ImageAdapter;
import vn.iotstar.viewflipper.model.ImageModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private CircleIndicator circleIndicator;
    private List<ImageModel> imagesList;
    private Handler handler = new Handler(Looper.getMainLooper());
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = findViewById(R.id.viewPager);
        circleIndicator = findViewById(R.id.indicator);

        imagesList = getListImages();
        if (imagesList.isEmpty()) {
            Log.e("DEBUG", "Image list is empty!");
        }

        ImageAdapter adapter = new ImageAdapter(this, imagesList);
        viewPager.setAdapter(adapter);

        // Liên kết ViewPager với CircleIndicator
        circleIndicator.setViewPager(viewPager);
    }


    private List<ImageModel> getListImages() {
        List<ImageModel> list = new ArrayList<>();
        list.add(new ImageModel(R.drawable.quangcao));
        list.add(new ImageModel(R.drawable.coffee));
        list.add(new ImageModel(R.drawable.companypizza));
        list.add(new ImageModel(R.drawable.themoingon));

        Log.d("DEBUG", "Total images: " + list.size()); // Kiểm tra số ảnh

        return list;
    }


    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            int currentItem = viewPager.getCurrentItem();
            int totalItems = imagesList.size();

            if (currentItem < totalItems - 1) {
                viewPager.setCurrentItem(currentItem + 1);
            } else {
                viewPager.setCurrentItem(0);
            }
            handler.postDelayed(this, 3000);
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        handler.postDelayed(runnable, 3000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable);
    }
}