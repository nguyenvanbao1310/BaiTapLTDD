package com.example.baitap04.activity;

import android.os.Bundle;

import android.view.View;
import android.widget.ListView;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.baitap04.R;
import com.example.baitap04.adapter.MonhocAdapter;
import com.example.baitap04.model.MonHoc;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    //khai báo
    private ListView listView;
    private ArrayList<MonHoc> arrayList;
    private MonhocAdapter adapter;

    private EditText editText1;
    private Button btnNhap;

    private Button btnCapNhat;
    private int selectedIndex = -1;




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

        AnhXa();
        addEvents();
        adapter = new MonhocAdapter(MainActivity.this, R.layout.row_monhoc, arrayList);
        listView.setAdapter(adapter);

    }

    private void AnhXa() {
        listView = (ListView) findViewById(R.id.listview1);
        editText1 = (EditText) findViewById(R.id.editTextMonHoc);
        btnNhap = (Button) findViewById(R.id.btnThem);
        btnCapNhat = (Button) findViewById(R.id.btnCapNhat);
        //Thêm dữ liệu vào List
        arrayList = new ArrayList<>();
        arrayList.add(new MonHoc("Java","Java 1",R.drawable.java1));
        arrayList.add(new MonHoc("C#","C# 1",R.drawable.c));
        arrayList.add(new MonHoc("PHP","PHP 1",R.drawable.php));
        arrayList.add(new MonHoc("Kotlin","Kotlin 1",R.drawable.kotlin));
        arrayList.add(new MonHoc("Dart","Dart 1",R.drawable.dart));
    }

    private void addEvents() {
        // Sự kiện thêm môn học
        btnNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tenMonHoc = editText1.getText().toString().trim();

                if (!tenMonHoc.isEmpty()) {
                    // Kiểm tra trùng lặp
                    boolean isDuplicate = false;
                    for (MonHoc mh : arrayList) {
                        if (mh.getName().equalsIgnoreCase(tenMonHoc)) {
                            isDuplicate = true;
                            break;
                        }
                    }

                    if (isDuplicate) {
                        Toast.makeText(MainActivity.this, "Môn học đã tồn tại!", Toast.LENGTH_SHORT).show();
                    } else {
                        arrayList.add(new MonHoc(tenMonHoc, tenMonHoc + " 1", R.drawable.default_image));
                        adapter.notifyDataSetChanged();
                        editText1.setText("");
                        Toast.makeText(MainActivity.this, "Thêm môn học thành công!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Vui lòng nhập tên môn học!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Sự kiện cập nhật môn học
        btnCapNhat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedIndex != -1) {
                    String tenMonHocMoi = editText1.getText().toString().trim();
                    if (!tenMonHocMoi.isEmpty()) {
                        arrayList.get(selectedIndex).setName(tenMonHocMoi);
                        arrayList.get(selectedIndex).setDesc(tenMonHocMoi + " 1");
                        adapter.notifyDataSetChanged();
                        editText1.setText("");
                        Toast.makeText(MainActivity.this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "Vui lòng nhập tên môn học mới!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Hãy chọn một môn học trước!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Sự kiện khi chọn một môn học trong danh sách
        listView.setOnItemClickListener((parent, view, position, id) -> {
            selectedIndex = position;
            MonHoc selectedMonHoc = arrayList.get(position);
            editText1.setText(selectedMonHoc.getName());
        });
    }
}