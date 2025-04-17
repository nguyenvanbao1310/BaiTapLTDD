package com.example.viewflipper;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    LinearLayout listMode;
    Button next;
    TextView title;
    ImageView image;
    ViewFlipper viewFlipper;
    int[] images = {R.drawable.caffe, R.drawable.dart, R.drawable.hamburger, R.drawable.test, R.drawable.xienque};
    String[] titles = {"Caffe", "Dart", "Hamburger", "Test", "Xien Que"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Anhxa();
        for (int i = 0; i < images.length; i++) {
            // Tạo một LinearLayout mới để tránh trùng parent
            LinearLayout layout = new LinearLayout(this);
            layout.setOrientation(LinearLayout.VERTICAL);

            // Tạo một ImageView mới
            ImageView imageView = new ImageView(this);
            imageView.setImageResource(images[i]);

            // Tạo một TextView mới
            TextView textView = new TextView(this);
            textView.setText(titles[i]);

            // Thêm ImageView và TextView vào layout
            layout.addView(imageView);
            layout.addView(textView);

            // Thêm layout vào ViewFlipper
            viewFlipper.addView(layout);
        }
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewFlipper.showNext();
            }
        });
        // Declare in and out animations and load them using AnimationUtils class
        Animation in = AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left);
        Animation out = AnimationUtils.loadAnimation(this, android.R.anim.slide_out_right);
        // set the animation type's to ViewFlipper
        viewFlipper.setInAnimation(in);
        viewFlipper.setOutAnimation(out);
        // set interval time for flipping between views
        viewFlipper.setFlipInterval(3000);
        // set auto start for flipping between views
        viewFlipper.setAutoStart(true);

    }
    public void Anhxa(){
        listMode = (LinearLayout) findViewById(R.id.listMode);
        next = (Button) findViewById(R.id.next);
        title = (TextView) findViewById(R.id.text);
        image = (ImageView) findViewById(R.id.image);
        viewFlipper = (ViewFlipper) findViewById(R.id.view_transition);
    }
}