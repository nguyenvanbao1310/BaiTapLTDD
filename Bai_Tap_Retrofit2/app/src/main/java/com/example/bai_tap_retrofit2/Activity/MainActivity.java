package com.example.bai_tap_retrofit2.Activity;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bai_tap_retrofit2.API.APIService;
import com.example.bai_tap_retrofit2.Adapter.CategoryAdapter;
import com.example.bai_tap_retrofit2.Model.Category;
import com.example.bai_tap_retrofit2.R;
import com.example.bai_tap_retrofit2.Retrofit.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    RecyclerView rcCate;

    CategoryAdapter categoryAdapter;
    APIService apiService;
    List<Category> categoryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        AnhXa();
        GetCategory();
    }


    private void AnhXa(){
        rcCate = (RecyclerView) findViewById(R.id.rc_category);
        if (rcCate != null) {
            Log.d("logg", "RecyclerView đã được ánh xạ thành công.");
        } else {
            Log.e("logg", "Lỗi: RecyclerView không tìm thấy trong layout!");
        }
    }

    private void GetCategory() {
        // Gọi Interface trong APIService
        apiService = RetrofitClient.getRetrofit().create(APIService.class);
        apiService.getCategoryAll().enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if (response.isSuccessful()) {
                    categoryList = response.body(); // Nhận mảng
                    for (Category category : categoryList) {
                        Log.d("logg", "Category: " + category.getName() + ", Image: " + category.getImages());
                    }
                    if (categoryList != null && !categoryList.isEmpty()) {
                        Log.d("logg", "Số lượng category nhận được: " + categoryList.size());
                    } else {
                        Log.e("logg", "Danh sách category rỗng hoặc null!");
                    }
                    // Khởi tạo Adapter
                    categoryAdapter = new CategoryAdapter(MainActivity.this, categoryList);

                    rcCate.setHasFixedSize(true);
//                    RecyclerView.LayoutManager layoutManager =
//                            new LinearLayoutManager(getApplicationContext(),
//                                    LinearLayoutManager.HORIZONTAL,
//                                    false);
                    GridLayoutManager layoutManager = new GridLayoutManager(MainActivity.this, 3);
                    rcCate.setLayoutManager(layoutManager);
                    rcCate.setAdapter(categoryAdapter);
                    categoryAdapter.notifyDataSetChanged();
                } else {
                    int statusCode = response.code();
                }
            }
            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
//                Log.d("logg", t.getMessage());
                Log.e("logg", "Lỗi khi gọi API: " + t.getMessage());
                t.printStackTrace();
            }
        });
    }

}