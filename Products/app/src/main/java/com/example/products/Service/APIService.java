package com.example.products.Service;

import com.example.products.Model.Product;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface APIService {
    @GET("products")
    Call<List<Product>> getProductAll();
}
