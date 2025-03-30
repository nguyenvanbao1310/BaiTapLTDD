package com.example.products.Activity;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.products.Adapter.ProductAdapter;
import com.example.products.Model.Product;
import com.example.products.R;
import com.example.products.Retrofit.RetrofitClient;
import com.example.products.Service.APIService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProductAdapter adapter;

    private APIService apiService;
    private List<Product> productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        AnhXa();
        GetProducts();
    }
    private void AnhXa()
    {
        recyclerView = findViewById(R.id.recyclerView);
        if (recyclerView != null) {
            Log.d("logg", "RecyclerView đã được ánh xạ thành công.");
        } else {
            Log.e("logg", "Lỗi: RecyclerView không tìm thấy trong layout!");
        }

    }

    private void GetProducts(){

        apiService = RetrofitClient.getRetrofit().create(APIService.class);
        apiService.getProductAll().enqueue(new Callback<List<Product>>(){
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response){
                if (response.isSuccessful()){
                    Log.d("logg", "API gọi thành công!");
                    productList = response.body();
                    for (Product product : productList) {
                        Log.d("logg", "Product: " + product.getName() + ", Image: " + product.getImage());
                    }
                    if (productList != null && !productList.isEmpty()) {
                        Log.d("logg", "Số lượng category nhận được: " + productList.size());
                    } else {
                        Log.e("logg", "Danh sách category rỗng hoặc null!");
                    }
                    adapter = new ProductAdapter(productList);
                    recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
                else {
                    int statusCode = response.code();
                    Log.e("logg", "Lỗi khi gọi API: " );

                }
            }
            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
//                Log.d("logg", t.getMessage());
                Log.e("logg", "Lỗi khi gọi API: " + t.getMessage());
                t.printStackTrace();
            }
        });
    }
}