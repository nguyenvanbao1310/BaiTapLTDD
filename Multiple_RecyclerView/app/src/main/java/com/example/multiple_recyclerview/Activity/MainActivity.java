package com.example.multiple_recyclerview.Activity;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.multiple_recyclerview.Adapter.CustomAdapter;
import com.example.multiple_recyclerview.Model.UserModel;
import com.example.multiple_recyclerview.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView rvMultipleViewType;
    private List<Object> mData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        rvMultipleViewType = (RecyclerView) findViewById(R.id.rv_multiple_view_type);
        mData = new ArrayList<>();
        mData.add(new UserModel("Nguyen Van Nghia", "Quan 11"));
        mData.add(R.drawable.avatar);
        mData.add("Text 0");
        mData.add("Text 1");
        mData.add(new UserModel("Nguyen Hoang Minh", "Quan 3"));
        mData.add("Text 2");
        mData.add(R.drawable.avatar);
        mData.add(R.drawable.avatar);
        mData.add(new UserModel("Pham Nguyen Tam Phu", "Quan 10"));
        mData.add("Text 3");
        mData.add(R.drawable.avatar);
        mData.add(new UserModel("Tran Van Phuc", "Quan 1"));
        mData.add(R.drawable.avatar);

        CustomAdapter adapter = new CustomAdapter(this, mData);
        rvMultipleViewType.setAdapter(adapter);
        rvMultipleViewType.setLayoutManager(new LinearLayoutManager(this));
    }
}