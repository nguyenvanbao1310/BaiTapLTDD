package com.example.baitap3;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import java.util.Random;

import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;


public class MainActivity extends AppCompatActivity {

    private int[] bgImages = {
            R.drawable.bg1,
            R.drawable.bg2,
            R.drawable.bg3,
            R.drawable.bg4,
            R.drawable.bg5
    };

    private ImageView imgView;
    private Switch switchBg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        imgView = findViewById(R.id.backgroundImage);
        switchBg = findViewById(R.id.switchbg);

        imgView.setImageResource(getRandomImage());

        switchBg.setOnCheckedChangeListener((new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    imgView.setImageResource(getRandomImage());
                }
                else
                {
                    imgView.setImageResource(bgImages[0]);
                }
            }
        }));

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


    }
    private int getRandomImage() {
        Random random = new Random();
        return bgImages[random.nextInt(bgImages.length)];
    }
}