package vn.iotstar.recylceview;

import android.os.Bundle;
import android.widget.SearchView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import vn.iotstar.recylceview.adapter.IconAdapter;
import vn.iotstar.recylceview.model.IconModel;

public class MainActivity extends AppCompatActivity {
    private RecyclerView rcIcon;
    private IconAdapter adapter;
    private List<IconModel> iconList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SearchView searchView = findViewById(R.id.searchView);
        rcIcon = findViewById(R.id.rcIcon);
        rcIcon.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        iconList = new ArrayList<>();
        iconList.add(new IconModel(R.drawable.icon1, "Icon 1"));
        iconList.add(new IconModel(R.drawable.icon2, "Icon 2"));
        iconList.add(new IconModel(R.drawable.icon3, "Icon 3"));

        adapter = new IconAdapter(this, iconList);
        rcIcon.setAdapter(adapter);

        rcIcon.addItemDecoration(new LinePagerIndicatorDecoration());

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                List<IconModel> filteredList = new ArrayList<>();
                for (IconModel icon : iconList) {
                    if (icon.getIconName().toLowerCase().contains(newText.toLowerCase())) {
                        filteredList.add(icon);
                    }
                }
                adapter.setListenerList(filteredList);
                return true;
            }
        });
    }
}
