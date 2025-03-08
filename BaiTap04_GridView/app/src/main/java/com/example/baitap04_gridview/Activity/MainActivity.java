package com.example.baitap04_gridview.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.baitap04_gridview.Adapter.MonhocAdapter;
import com.example.baitap04_gridview.Model.MonHoc;
import com.example.baitap04_gridview.R;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ArrayList<MonHoc> arrayList;
    private MonhocAdapter adapter;

    private GridView gridView;

    private EditText editText1;
    private Button btnNhap, btnCapNhat;
    private int selectedPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        AnhXa();
        addEvents();
        //Tạo Adapter
        adapter = new MonhocAdapter(MainActivity.this,R.layout.row_monhoc, arrayList);
        //truyền dữ liệu từ adapter ra listview
        gridView.setAdapter(adapter);
    }
    private void AnhXa() {
        gridView = (GridView) findViewById(R.id.gridview1);
        editText1 = (EditText) findViewById(R.id.editText1);
        btnNhap = (Button) findViewById(R.id.btnNhap);
        btnCapNhat = (Button) findViewById(R.id.btnCapNhat);
        //Thêm dữ liệu vào List
        arrayList = new ArrayList<>();
        arrayList.add(new MonHoc("Java","Java 1",R.drawable.java1));
        arrayList.add(new MonHoc("C#","C# 1",R.drawable.c));
        arrayList.add(new MonHoc("PHP","PHP 1",R.drawable.php));
        arrayList.add(new MonHoc("Kotlin","Kotlin 1",R.drawable.kotlin));
        arrayList.add(new MonHoc("Dart","Dart 1",R.drawable.dart));
    }
    private void addEvents()
    {
        // Khi người dùng nhấn vào một môn học trong GridView
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Lưu vị trí phần tử được chọn
                selectedPosition = position;
                // Hiển thị tên môn học lên EditText
                editText1.setText(arrayList.get(position).getName());
            }
        });

        btnNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tenMonHoc = editText1.getText().toString().trim();

                if (!tenMonHoc.isEmpty()) {
                    // Kiểm tra xem môn học đã tồn tại chưa
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
                        // Thêm vào danh sách nếu không trùng
                        arrayList.add(new MonHoc(tenMonHoc, tenMonHoc + " 1", R.drawable.default_image));
                        adapter.notifyDataSetChanged();
                        editText1.setText(""); // Xóa nội dung sau khi nhập
                        Toast.makeText(MainActivity.this, "Thêm môn học thành công!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Vui lòng nhập tên môn học!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnCapNhat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedPosition != -1) {
                    String tenMoi = editText1.getText().toString().trim();
                    if (!tenMoi.isEmpty()) {
                        // Cập nhật tên môn học trong danh sách
                        arrayList.get(selectedPosition).setName(tenMoi);
                        adapter.notifyDataSetChanged(); // Cập nhật giao diện
                        editText1.setText(""); // Xóa nội dung EditText
                        selectedPosition = -1; // Reset trạng thái
                        Toast.makeText(MainActivity.this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "Vui lòng nhập tên mới!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Vui lòng chọn môn học cần cập nhật!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}