package vn.iotstar.videoshort;

import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import vn.iotstar.videoshort.Adapter.VideosFireBaseAdapter;
import vn.iotstar.videoshort.Model.VideoModel;

public class MainActivity extends AppCompatActivity {
    private ViewPager2 viewPager2;
    private VideosFireBaseAdapter videosAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        // Kiểm tra kết nối Firebase
//        DatabaseReference connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected");
//        connectedRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                boolean connected = snapshot.getValue(Boolean.class);
//                Log.d("Firebase", "Kết nối Firebase: " + (connected ? "THÀNH CÔNG" : "THẤT BẠI"));
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Log.e("Firebase", "Lỗi listener kết nối: ", error.toException());
//            }
//        });

        //saveYouTubeVideoToFirebase();
        saveVideoUrlToFirebase();
        // Fullscreen mode
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //setContentView(R.layout.activity_main);

        viewPager2 = findViewById(R.id.vpager);
        getVideos();
    }

    private void getVideos() {
        // Set database reference
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("videos");

        FirebaseRecyclerOptions<VideoModel> options = new FirebaseRecyclerOptions.Builder<VideoModel>()
                .setQuery(mDatabase, VideoModel.class)
                .build();

        // Set adapter
        videosAdapter = new VideosFireBaseAdapter(options, this);
        viewPager2.setOrientation(ViewPager2.ORIENTATION_VERTICAL);
        viewPager2.setAdapter(videosAdapter);
    }
    private void saveVideoUrlToFirebase() {
        String videoUrl = "https://www.learningcontainer.com/wp-content/uploads/2020/05/sample-mp4-file.mp4";

        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("videos");
        String newVideoKey = databaseRef.push().getKey();

        Map<String, Object> videoData = new HashMap<>();
        videoData.put("title", "Video Test OK");
        videoData.put("desc", "Video từ URL hợp lệ");
        videoData.put("url", videoUrl);

        databaseRef.child(newVideoKey).setValue(videoData)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(MainActivity.this, "Đã lưu video thành công!", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(MainActivity.this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
    private void saveYouTubeVideoToFirebase() {
        String videoId = "sBrWXeXDgJ1abxOq";
        String videoUrl = "https://www.youtube.com/watch?v=" + videoId;

        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("videos");
        String newVideoKey = databaseRef.push().getKey();

        Map<String, Object> videoData = new HashMap<>();
        videoData.put("title", "Upload video");
        videoData.put("description", "Mô tả video");
        videoData.put("url", videoUrl);
        //videoData.put("thumbnail", "https://img.youtube.com/vi/" + videoId + "/0.jpg");
        //videoData.put("timestamp", ServerValue.TIMESTAMP);

        databaseRef.child(newVideoKey).setValue(videoData)
                .addOnSuccessListener(aVoid -> {
                    Log.d("Firebase", "Lưu thành công");
                    // Hiển thị thông báo
                    runOnUiThread(() -> Toast.makeText(MainActivity.this,
                            "Đã upload video lên Firebase", Toast.LENGTH_SHORT).show());

                    // In ra Logcat key của video
                    Log.d("Firebase", "Video key: " + newVideoKey);
                })
                .addOnFailureListener(e -> {
                    Log.e("Firebase", "Lỗi: " + e.getMessage());
                    runOnUiThread(() -> Toast.makeText(MainActivity.this,
                            "Upload thất bại: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                });
    }
    @Override
    protected void onStart() {
        super.onStart();
        videosAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        videosAdapter.stopListening();
    }

    @Override
    protected void onResume() {
        super.onResume();
        videosAdapter.notifyDataSetChanged();
    }
}