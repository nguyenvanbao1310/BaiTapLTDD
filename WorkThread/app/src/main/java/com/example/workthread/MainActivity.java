package com.example.workthread;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workthread.adapter.DishAdapter;
import com.example.workthread.model.Dish;

import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    DishAdapter dishAdapter;
    ArrayList<Dish> dishList = new ArrayList<>();
    boolean isLoading = false;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        loadData();
        setAdapterRecyclerView();
        initScrollListener();
    }

    private void loadData(){
        for (int i = 0; i<10; i++){
            Dish dish = new Dish();
            String name = "Name " + i;
            String description = "Description " + i;
            String price = "Price " + i;
            dish.setName(name);
            dish.setDescription(description);
            dish.setPrice(price);
            dishList.add(dish);
        }
    }

    private void setAdapterRecyclerView(){
        recyclerView = findViewById(R.id.rv_dish);
        dishAdapter = new DishAdapter(dishList, getApplicationContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(),2));
        recyclerView.setAdapter(dishAdapter);
    }

    private void loadMore(){
        dishList.add(null);
        dishAdapter.notifyItemInserted(dishList.size() - 1);
        new Thread(new Runnable() {
            @Override
            public void run() {
                dishList.remove(dishList.size()-1);
                String[] imageUrls = {
                        "https://i.postimg.cc/vZfDY62N/bunmoc.jpg",
                        "https://i.postimg.cc/TPx1pYZY/bunrieu.jpg",
                        "https://i.postimg.cc/Fs27XxpS/caffe.jpg",
                        "https://i.postimg.cc/5NH6rrB1/hamburger.jpg",
                        "https://i.postimg.cc/sXD24Hjk/HauNuong.jpg",
                        "https://i.postimg.cc/xdPCHV2G/keoraucu.jpg",
                        "https://i.postimg.cc/nrXc6sQT/micay.jpg",
                        "https://i.postimg.cc/Jn6z63tG/mitron.jpg",
                        "https://i.postimg.cc/5yDN1xBJ/xienque.jpg"
                };
                int currentSize = dishList.size();
                for (int i = 0 ; i < imageUrls.length; i++){
                    Dish dish = new Dish();
                    String name = "Name " + (currentSize+i);
                    String description = "Description " + (currentSize+i);
                    String price = "Price " + (currentSize+i);
                    dish.setName(name);
                    dish.setDescription(description);
                    dish.setPrice(price);
                    dish.setImage(loadImageFromUrl(imageUrls[i]));
                    dishList.add(dish);
                }
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dishAdapter.notifyDataSetChanged();
                        isLoading = false;
                    }
                },3000);
            }
        }).start();
    }

    public static Bitmap loadImageFromUrl(String urlString) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void initScrollListener(){
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NotNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (!isLoading){
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == dishList.size() - 1){
                        loadMore();
                        isLoading = true;
                    }
                }
            }
        });
    }
}