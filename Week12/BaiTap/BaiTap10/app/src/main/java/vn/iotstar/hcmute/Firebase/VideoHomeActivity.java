package vn.iotstar.hcmute.Firebase;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import de.hdodenhof.circleimageview.CircleImageView;
import vn.iotstar.hcmute.R;
import vn.iotstar.hcmute.Firebase.Ref.Refs;

public class VideoHomeActivity extends AppCompatActivity {
    private ViewPager2 viewPager;
    private VideoShortAdapter videoAdapter;
    private CircleImageView userAccountImage;
    public static boolean isInitialized = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_video_home);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainLayout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        viewPager = findViewById(R.id.videoViewPager);
        viewPager.setOrientation(ViewPager2.ORIENTATION_VERTICAL);

        DatabaseReference videoRef = FirebaseDatabase
                .getInstance(Refs.VIDEO_SHORTS_FIREBASE_URL)
                .getReference(Refs.VIDEO_SHORTS_URL);
        FirebaseRecyclerOptions<VideoShortModel> options = new FirebaseRecyclerOptions.Builder<VideoShortModel>()
                .setQuery(videoRef, VideoShortModel.class)
                .build();
        videoAdapter = new VideoShortAdapter(options);
        viewPager.setAdapter(videoAdapter);

        userAccountImage = findViewById(R.id.userAccountImage);
        loadUserProfileImage();

        userAccountImage.setOnClickListener(v -> {
            Intent intent;
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            if (currentUser == null) {
                intent = new Intent(VideoHomeActivity.this, LoginActivity.class);
            } else {
                intent = new Intent(VideoHomeActivity.this, ProfileActivity.class);
            }
            startActivity(intent);
        });
    }

    private void loadUserProfileImage() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            DatabaseReference userRef = FirebaseDatabase
                    .getInstance(Refs.VIDEO_SHORTS_FIREBASE_URL)
                    .getReference(Refs.USERS_URL).child(currentUser.getUid());
            userRef.child("pfpUrl").get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    String pfpUrl = task.getResult().getValue(String.class);
                    if (pfpUrl != null && !pfpUrl.isEmpty()) {
                        Glide.with(this)
                                .load(pfpUrl)
                                .into(userAccountImage);
                    }
                }
            });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadUserProfileImage();
        videoAdapter.startListening();
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onResume() {
        super.onResume();
        videoAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onStop() {
        super.onStop();
        videoAdapter.stopListening();
    }
}
