package vn.iotstar.viewflipper;

import android.os.Bundle;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ViewFlipper;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class ViewFlipperActivity extends AppCompatActivity {
    private ViewFlipper viewFlipper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewflipper);

        viewFlipper = findViewById(R.id.viewFlipperMain);
        ActionViewFlipperMain();
    }

    private void ActionViewFlipperMain() {
        // Danh sách chứa các URL hình ảnh
        List<String> imageUrls = new ArrayList<>();
        imageUrls.add("http://app.iotstar.vn:8081/appfoods/flipper/quangcao.png");
        imageUrls.add("http://app.iotstar.vn:8081/appfoods/flipper/coffee.jpg");
        imageUrls.add("http://app.iotstar.vn:8081/appfoods/flipper/companypizza.jpeg");
        imageUrls.add("http://app.iotstar.vn:8081/appfoods/flipper/themoingon.jpeg");

        for (String url : imageUrls) {
            ImageView imageView = new ImageView(this);
            Glide.with(this).load(url).into(imageView);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            viewFlipper.addView(imageView);
        }

        // Gán hiệu ứng chuyển đổi giữa các ảnh
        viewFlipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_in_right));
        viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_out_left));

        // Thiết lập thời gian chuyển đổi giữa các ảnh
        viewFlipper.setFlipInterval(3000);
        viewFlipper.setAutoStart(true);
    }
}
