package com.example.databinding;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableField;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.databinding.adapter.ListUserAdapter;
import com.example.databinding.databinding.ActivityMainBinding;
import com.example.databinding.model.User;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public ObservableField<String> title = new ObservableField<>();
    private ListUserAdapter listUserAdapter;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        title.set("Ví dụ về DataBinding cho RecycleView");
        binding.setHome(this);
        setData();
        binding.rcView.setLayoutManager(new LinearLayoutManager(this));
        binding.rcView.setAdapter(listUserAdapter);
        listUserAdapter.setOnItemClickListener(this::itemClick);
    }

    private void setData() {
        List<User> userList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            User user = new User();
            user.setFirstName("Hữu" + i);
            user.setLastName("Trung" + i);
            userList.add(user);
        }
        listUserAdapter = new ListUserAdapter(userList);
    }
    public void itemClick(User user) {
        Toast.makeText(this, "bạn vừa click", Toast.LENGTH_SHORT).show();
    }

}