package com.example.baitap04_recyclerview.Activity;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.baitap04_recyclerview.Adapter.SongAdapter;
import com.example.baitap04_recyclerview.Model.SongModel;
import com.example.baitap04_recyclerview.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView rvSongs;
    private SongAdapter mSongAdapter;
    private List<SongModel> mSongs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        rvSongs = (RecyclerView) findViewById(R.id.rv_songs);

        // Create song data
        mSongs = new ArrayList<>();
        mSongs.add(new SongModel("66869", "NẾU EM CÒN TỒN TẠI", "Khi anh bắt đầu 1 tình yêu Là lúc anh tự thay", "Trịnh Đình Quang"));
        mSongs.add(new SongModel("60871", "NGỐC", "Có rất nhiều những câu chuyện Em dấu riêng mình em biết.", "Khắc Việt"));
        mSongs.add(new SongModel("60650", "HÃY TIN ANH LẦN NỮA", "Dẫu cho ta đã sai khi ở bên nhau Có yêu thương", "Thiên Dũng"));
        mSongs.add(new SongModel("60680", "CHUỐI NGÀY VẮNG EM", "Từ khi em bước ra đi cõi lòng anh ngập tràn bao", "Duy Cường"));
        mSongs.add(new SongModel("60656", "KHI NGƯỜI MÌNH YÊU KHÓC", "Nước mắt em đang rơi trên những ngón tay Nước mắt em", "Phạm Mạnh Quỳnh"));
        mSongs.add(new SongModel("60685", "MƠ", "Anh mở mắt gặp em anh mở được ấm anh mở được gần", "Trịnh Thăng Bình"));
        mSongs.add(new SongModel("60752", "TÌNH YÊU CHẤP VÁ", "Muốn ai đó yêu ai thương mình thì cũng có Để không nghe", "Mr. Siro"));
        mSongs.add(new SongModel("60608", "CHỜ NGÀY MƯA TAN", "1 ngày mưa và em khuất xa nơi anh bóng dáng cứ", "Trung Đức"));
        mSongs.add(new SongModel("60663", "CÂU HỎI EM CHƯA TRẢ LỜI", "Cần nói em 1 lời giải thích thật lòng Đừng lặng im", "Yuki Huy Nam"));
        mSongs.add(new SongModel("60720", "QUA ĐI LÃNG LẼ", "Đôi khi đến với nhau yêu thương chẳng được lâu nhưng kỉ", "Phạm Mạnh Quỳnh"));
        mSongs.add(new SongModel("60856", "QUÊN ANH LÀ ĐIỀU EM KHÔNG THỂ - REMIX", "Cần thêm bao lâu để em quên đi niềm đau Cần thêm", "Thiện Ngôn"));

        mSongAdapter = new SongAdapter(this, mSongs);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvSongs.setLayoutManager(linearLayoutManager);
        rvSongs.setAdapter(mSongAdapter);


    }
}