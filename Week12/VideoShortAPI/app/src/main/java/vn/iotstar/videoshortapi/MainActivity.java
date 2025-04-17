package vn.iotstar.videoshortapi;

import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.iotstar.videoshortapi.Adapter.VideosAdapter;
import vn.iotstar.videoshortapi.Model.MessageVideoModel;
import vn.iotstar.videoshortapi.Model.VideoModel;

public class MainActivity extends AppCompatActivity {
    private ViewPager2 viewPager2;
    private VideosAdapter videosAdapter;
    private List<VideoModel> videoList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Fullscreen mode
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        viewPager2 = findViewById(R.id.vpager);
        videosAdapter = new VideosAdapter(this, videoList);

        // Cấu hình ViewPager2
        viewPager2.setOrientation(ViewPager2.ORIENTATION_VERTICAL);
        viewPager2.setAdapter(videosAdapter);

        // Lắng nghe khi chuyển trang
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                playVideoAt(position);
                stopOtherVideos(position);
            }
        });

        // Lấy dữ liệu video từ API
        getVideosFromApi();
    }

    private void getVideosFromApi() {
        APIService apiService = RetrofitClient.getClient().create(APIService.class);
        Call<MessageVideoModel> call = apiService.getVideos();

        call.enqueue(new Callback<MessageVideoModel>() {
            @Override
            public void onResponse(Call<MessageVideoModel> call, Response<MessageVideoModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    videoList.clear();
                    videoList.addAll(response.body().getResult());
                    videosAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<MessageVideoModel> call, Throwable t) {
                Log.e("API Error", t.getMessage());
                Toast.makeText(MainActivity.this, "Lỗi khi tải video", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void playVideoAt(int position) {
        RecyclerView recyclerView = (RecyclerView) viewPager2.getChildAt(0);
        RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForAdapterPosition(position);
        if (viewHolder instanceof VideosAdapter.MyViewHolder) {
            VideosAdapter.MyViewHolder holder = (VideosAdapter.MyViewHolder) viewHolder;
            if (holder.videoView != null) {
                holder.videoView.start();
            }
        }
    }

    private void stopOtherVideos(int currentPosition) {
        RecyclerView recyclerView = (RecyclerView) viewPager2.getChildAt(0);
        for (int i = 0; i < videoList.size(); i++) {
            if (i != currentPosition) {
                RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForAdapterPosition(i);
                if (viewHolder instanceof VideosAdapter.MyViewHolder) {
                    VideosAdapter.MyViewHolder holder = (VideosAdapter.MyViewHolder) viewHolder;
                    if (holder.videoView != null && holder.videoView.isPlaying()) {
                        holder.videoView.pause();
                    }
                }
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        pauseCurrentVideo();
    }

    @Override
    protected void onResume() {
        super.onResume();
        resumeCurrentVideo();
    }

    private void pauseCurrentVideo() {
        int currentItem = viewPager2.getCurrentItem();
        RecyclerView recyclerView = (RecyclerView) viewPager2.getChildAt(0);
        RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForAdapterPosition(currentItem);
        if (viewHolder instanceof VideosAdapter.MyViewHolder) {
            VideosAdapter.MyViewHolder holder = (VideosAdapter.MyViewHolder) viewHolder;
            if (holder.videoView != null && holder.videoView.isPlaying()) {
                holder.videoView.pause();
            }
        }
    }

    private void resumeCurrentVideo() {
        int currentItem = viewPager2.getCurrentItem();
        RecyclerView recyclerView = (RecyclerView) viewPager2.getChildAt(0);
        RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForAdapterPosition(currentItem);
        if (viewHolder instanceof VideosAdapter.MyViewHolder) {
            VideosAdapter.MyViewHolder holder = (VideosAdapter.MyViewHolder) viewHolder;
            if (holder.videoView != null) {
                holder.videoView.start();
            }
        }
    }
}
