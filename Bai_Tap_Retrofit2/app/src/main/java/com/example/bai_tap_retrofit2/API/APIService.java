package com.example.bai_tap_retrofit2.API;

import com.example.bai_tap_retrofit2.Model.Category;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface APIService
{
    @GET("categories.php")
    Call<List<Category>> getCategoryAll();
}
