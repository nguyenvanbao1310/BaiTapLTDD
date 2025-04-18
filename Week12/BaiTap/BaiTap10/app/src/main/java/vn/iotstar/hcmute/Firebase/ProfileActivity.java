package vn.iotstar.hcmute.Firebase;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.READ_MEDIA_IMAGES;
import static android.Manifest.permission.READ_MEDIA_VIDEO;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.compose.ui.node.Ref;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import vn.iotstar.hcmute.R;
import vn.iotstar.hcmute.Firebase.Ref.Refs;

public class ProfileActivity extends AppCompatActivity {
    private ImageView logoutImage;
    private CircleImageView profileImage;
    private Button uploadVideoButton;
    private ProgressBar progressBar;
    private TextView videoCountText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);

        // Apply window insets for edge-to-edge mode
        applyWindowInsets();

        // Initialize Cloudinary
        initializeCloudinary();

        // Initialize views
        initializeViews();

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            loadUserInfo(currentUser);
        }

        setupLogoutButton(currentUser);
        setupProfileImageClick(currentUser);
        setupUploadVideoButton(currentUser);
    }

    private void applyWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainLayout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void initializeCloudinary() {
        if (!VideoHomeActivity.isInitialized) {
            MediaManager.init(this, Refs.CLOUDINARY_CONFIGS);
            VideoHomeActivity.isInitialized = true;
        }
    }

    private void initializeViews() {
        videoCountText = findViewById(R.id.videoCountText);
        logoutImage = findViewById(R.id.logoutImage);
        profileImage = findViewById(R.id.profileImage);
        uploadVideoButton = findViewById(R.id.uploadVideoButton);
        progressBar = findViewById(R.id.progressBar);
    }

    private void setupLogoutButton(FirebaseUser currentUser) {
        logoutImage.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(ProfileActivity.this, VideoHomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }

    private void setupProfileImageClick(FirebaseUser currentUser) {
        profileImage.setOnClickListener(v -> {
            if (currentUser != null) {
                checkPermissionsAndPickImage();
            } else {
                showToast("Please sign in first!");
            }
        });
    }

    private void setupUploadVideoButton(FirebaseUser currentUser) {
        uploadVideoButton.setOnClickListener(v -> {
            if (currentUser != null) {
                checkPermissionsAndPickVideo();
            } else {
                showToast("Please sign in first!");
            }
        });
    }

    private void showToast(String message) {
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        toast.show();
        new Handler().postDelayed(toast::cancel, 1200);
    }

    private void loadUserInfo(FirebaseUser currentUser) {
        getAccount(currentUser.getUid()).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (task.getResult().exists()) {
                    AccountModel user = task.getResult().getValue(AccountModel.class);
                    if (user != null) {
                        TextView txtEmail = findViewById(R.id.txt_email);
                        txtEmail.setText(user.getEmail());
                        videoCountText.setText("Video count: " + user.getVideoCount());

                        String pfpUrl = user.getPfpUrl();
                        if (pfpUrl != null && !pfpUrl.isEmpty()) {
                            Glide.with(this).load(pfpUrl).into(profileImage);
                        }
                    }
                } else {
                    showToast("User data not found");
                }
            } else {
                showToast("Failed to load user info");
            }
        });
    }

    private DatabaseReference getAccount(String userId) {
        return FirebaseDatabase.getInstance(Refs.VIDEO_SHORTS_FIREBASE_URL)
                .getReference(Refs.USERS_URL).child(userId);
    }

    private DatabaseReference getVideoShorts(String uploadId) {
        return FirebaseDatabase.getInstance(Refs.VIDEO_SHORTS_FIREBASE_URL)
                .getReference(Refs.VIDEO_SHORTS_URL).child(uploadId);
    }

    private void checkPermissionsAndPickImage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, READ_MEDIA_IMAGES)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{READ_MEDIA_IMAGES}, 201);
            } else {
                pickImage();
            }
        } else {
            if (ContextCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{READ_EXTERNAL_STORAGE}, 201);
            } else {
                pickImage();
            }
        }
    }

    private void pickImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Profile Picture"), 102);
    }

    private void checkPermissionsAndPickVideo() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, READ_MEDIA_VIDEO)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{READ_MEDIA_VIDEO}, 200);
            } else {
                pickVideo();
            }
        } else {
            if (ContextCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{READ_EXTERNAL_STORAGE}, 200);
            } else {
                pickVideo();
            }
        }
    }

    private void pickVideo() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("video/*");
        startActivityForResult(Intent.createChooser(intent, "Select Video"), 101);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 200 && grantResults.length > 0 &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            pickVideo();
        } else {
            showToast("Permission denied!");
        }
        if (requestCode == 201 && grantResults.length > 0 &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            pickImage();
        } else if (requestCode == 201) {
            showToast("Permission denied!");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && resultCode == RESULT_OK && data != null) {
            Uri videoUri = data.getData();
            handleVideoUri(videoUri);
        }
        if (requestCode == 102 && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            handleImageUpload(imageUri);
        }
    }

    private void handleImageUpload(Uri imageUri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(imageUri);
            File tempFile = File.createTempFile("pfp_", ".jpg", getCacheDir());
            FileOutputStream outputStream = new FileOutputStream(tempFile);

            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            inputStream.close();
            outputStream.close();

            uploadImageToCloudinary(tempFile.getAbsolutePath());

        } catch (IOException e) {
            e.printStackTrace();
            showToast("Failed to process image");
        }
    }

    private void uploadImageToCloudinary(String filePath) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) return;
        MediaManager.get().upload(filePath)
                .option("resource_type", "image")
                .option("folder", "profile_pictures/")
                .callback(new UploadCallback() {
                    @Override
                    public void onStart(String requestId) {
                        Log.d("Cloudinary", "Upload started...");
                        runOnUiThread(() -> {
                            progressBar.setVisibility(View.VISIBLE);
                            progressBar.setProgress(0);
                        });
                    }

                    @Override
                    public void onProgress(String requestId, long bytes, long totalBytes) {
                        int percent = Math.round((100f * bytes) / totalBytes);
                        Log.d("Cloudinary", "Uploading: " + percent + "%");
                        runOnUiThread(() -> progressBar.setProgress(percent));
                    }

                    @Override
                    public void onSuccess(String requestId, Map resultData) {
                        String imageUrl = (String) resultData.get("secure_url");
                        getAccount(user.getUid()).child("pfpUrl").setValue(imageUrl)
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        showToast("Profile picture updated");

                                        Glide.with(ProfileActivity.this)
                                                .load(imageUrl)
                                                .into(profileImage);
                                    } else {
                                        showToast("Failed to update Firebase");
                                    }
                                    runOnUiThread(() -> progressBar.setVisibility(View.GONE));
                                });
                    }

                    @Override
                    public void onError(String requestId, ErrorInfo error) {
                        progressBar.setVisibility(View.GONE);
                        showToast("Upload failed: " + error.getDescription());
                    }

                    @Override
                    public void onReschedule(String requestId, ErrorInfo error) {}
                }).dispatch();
    }

    private void handleVideoUri(Uri videoUri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(videoUri);
            File tempFile = File.createTempFile("upload_", ".mp4", getCacheDir());
            FileOutputStream outputStream = new FileOutputStream(tempFile);

            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            inputStream.close();
            outputStream.close();

            uploadVideoToCloudinary(tempFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
            showToast("Failed to process video");
        }
    }

    private void uploadVideoToCloudinary(String filePath) {
        Map<String, String> options = new HashMap<>();
        options.put("resource_type", "video");
        options.put("upload_preset", "Video");

        MediaManager.get().upload(filePath)
                .option("resource_type", "video")
                .option("upload_preset", "Video")
                .callback(new UploadCallback() {
                    @Override
                    public void onStart(String requestId) {
                        Log.d("Cloudinary", "Upload started...");
                        runOnUiThread(() -> {
                            progressBar.setVisibility(View.VISIBLE);
                            progressBar.setProgress(0);
                        });
                    }

                    @Override
                    public void onProgress(String requestId, long bytes, long totalBytes) {
                        int percent = Math.round((100f * bytes) / totalBytes);
                        Log.d("Cloudinary", "Uploading: " + percent + "%");
                        runOnUiThread(() -> progressBar.setProgress(percent));
                    }

                    @Override
                    public void onSuccess(String requestId, Map resultData) {
                        String videoUrl = (String) resultData.get("secure_url");
                        Log.d("Cloudinary", "Video uploaded: " + videoUrl);
                        runOnUiThread(() -> progressBar.setVisibility(View.GONE));

                        // store in Firestore
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        if (user != null) {
                            LayoutInflater inflater = LayoutInflater.from(ProfileActivity.this);
                            View dialogView = inflater.inflate(R.layout.dialog_videometadata, null);

                            EditText eTxt_title = dialogView.findViewById(R.id.eTxt_title);
                            EditText eTxt_desc = dialogView.findViewById(R.id.eTxt_desc);

                            new AlertDialog.Builder(ProfileActivity.this)
                                    .setTitle("Video Details")
                                    .setView(dialogView)
                                    .setPositiveButton("Save", (dialog, which) -> {
                                        String title = eTxt_title.getText().toString().trim();
                                        String desc = eTxt_desc.getText().toString().trim();
                                        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                        DatabaseReference dbRef = FirebaseDatabase.getInstance(Refs.VIDEO_SHORTS_FIREBASE_URL).getReference(Refs.VIDEO_SHORTS_URL);
                                        String uploadId = dbRef.push().getKey();
                                        VideoShortModel videoData = new VideoShortModel(uploadId, title, desc, videoUrl, userId, 0);

                                        if (uploadId != null) {
                                            dbRef.child(uploadId).setValue(videoData)
                                                    .addOnSuccessListener(unused -> {
                                                        int videoCount = Integer.parseInt(videoCountText.getText().toString().split(" ")[2].trim()) + 1;
                                                        getAccount(userId).child("videoCount").setValue(videoCount)
                                                                .addOnCompleteListener(task -> {
                                                                    if (task.isSuccessful()) {
                                                                        videoCountText.setText("Video count: " + videoCount);
                                                                        Log.d("RealtimeDB", "Metadata saved: " + uploadId);
                                                                        showToast("Metadata saved to Realtime DB!");
                                                                    } else {
                                                                        Log.e("RealtimeDB", "Error updating video count", task.getException());
                                                                        showToast("Failed to save metadata");
                                                                    }
                                                                });
                                                    })
                                                    .addOnFailureListener(e -> {
                                                        Log.e("RealtimeDB", "Error saving", e);
                                                        showToast("Failed to save metadata");
                                                    });
                                        }
                                    })
                                    .setNegativeButton("Cancel", null)
                                    .show();
                        }
                    }

                    @Override
                    public void onError(String requestId, ErrorInfo error) {
                        Log.e("Cloudinary", "Upload failed: " + error.getDescription());
                        showToast("Upload Failed");
                    }

                    @Override
                    public void onReschedule(String requestId, ErrorInfo error) {
                        Log.e("Cloudinary", "Upload rescheduled: " + error.getDescription());
                    }
                }).dispatch();
    }
}
