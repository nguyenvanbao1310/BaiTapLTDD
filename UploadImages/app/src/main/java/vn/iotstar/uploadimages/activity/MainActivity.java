package vn.iotstar.uploadimages.activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.IOException;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.iotstar.uploadimages.R;
import vn.iotstar.uploadimages.util.RealPathUtil;
import vn.iotstar.uploadimages.retrofit.RetrofitClient;
import vn.iotstar.uploadimages.constant.Const;
import vn.iotstar.uploadimages.upload.ImageUpload;

public class MainActivity extends AppCompatActivity {
    private Button btnChoose, btnUpload;
    private ImageView imgChoose, imgMultipart;
    private EditText editUserName;
    private TextView tvUsername;
    private Uri mUri;
    private ProgressDialog mProgressDialog;
    public static final int MY_REQUEST_CODE = 100;
    public static final String TAG = MainActivity.class.getName();

    private static String[] storage_permissions = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    private static String[] storage_permissions_33 = {
            Manifest.permission.READ_MEDIA_IMAGES,
            Manifest.permission.READ_MEDIA_AUDIO,
            Manifest.permission.READ_MEDIA_VIDEO
    };

    public static String[] permissions() {
        String[] p;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            p = storage_permissions_33;
        } else {
            p = storage_permissions;
        }
        return p;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Ánh xạ view
        AnhXa();

        // Khởi tạo ProgressDialog
        mProgressDialog = new ProgressDialog(MainActivity.this);
        mProgressDialog.setMessage("Please wait upload...");

        // Bắt sự kiện nút chọn ảnh
        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckPermission();
            }
        });

        // Bắt sự kiện upload ảnh
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mUri != null) {
                    UploadImage();
                } else {
                    Toast.makeText(MainActivity.this, "Vui lòng chọn ảnh trước", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void AnhXa() {
        btnChoose = findViewById(R.id.btnChoose);
        btnUpload = findViewById(R.id.btnUpload);
        imgChoose = findViewById(R.id.imgChoose);
        imgMultipart = findViewById(R.id.imgMultipart);
        editUserName = findViewById(R.id.editUserName);
        tvUsername = findViewById(R.id.tvUsername);
    }

    private void CheckPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            openGallery();
            return;
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            openGallery();
        } else {
            ActivityCompat.requestPermissions(MainActivity.this, permissions(), MY_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            } else {
                Toast.makeText(this, "Vui lòng cấp quyền để tiếp tục", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), MY_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            mUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), mUri);
                imgChoose.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void UploadImage() {
        mProgressDialog.show();
        String username = editUserName.getText().toString().trim();
        RequestBody requestUsername = RequestBody.create(MediaType.parse("multipart/form-data"), username);

        // Sử dụng RealPathUtil để lấy đường dẫn thực
        String IMAGE_PATH = RealPathUtil.getRealPath(this, mUri);
        Log.d("Upload", "Image path: " + IMAGE_PATH);

        if (IMAGE_PATH == null) {
            Toast.makeText(this, "Không thể lấy đường dẫn ảnh", Toast.LENGTH_SHORT).show();
            mProgressDialog.dismiss();
            return;
        }

        File file = new File(IMAGE_PATH);
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);

        MultipartBody.Part partBodyAvatar = MultipartBody.Part.createFormData(
                Const.MY_IMAGES,
                file.getName(),
                requestFile
        );

        // Gọi API upload
        RetrofitClient.getServiceAPI().upload(requestUsername, partBodyAvatar)
                .enqueue(new Callback<List<ImageUpload>>() {
                    @Override
                    public void onResponse(Call<List<ImageUpload>> call, Response<List<ImageUpload>> response) {
                        mProgressDialog.dismiss();
                        if (response.isSuccessful() && response.body() != null) {
                            List<ImageUpload> uploads = response.body();
                            ImageUpload upload = uploads.get(0);
                            // Hiển thị username
//                            tvUsername.setText("Username: " + upload.getUsername());
//
//                            // Hiển thị ảnh từ URL sử dụng Glide hoặc Picasso
//                            String imageUrl = "http://app.iotstar.vn:8081/appfoods/images/" + imageFileName;
//                            Glide.with(this).load(imageUrl).into(imgAvatar);
                            Toast.makeText(MainActivity.this, "Upload thành công", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this, "Upload thất bại", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<ImageUpload>> call, Throwable t) {
                        mProgressDialog.dismiss();
                        Toast.makeText(MainActivity.this, "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }
}