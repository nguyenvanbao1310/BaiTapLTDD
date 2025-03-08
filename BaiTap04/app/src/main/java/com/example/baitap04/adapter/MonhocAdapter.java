package com.example.baitap04.adapter;
import com.example.baitap04.model.MonHoc;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import com.example.baitap04.R;
import com.example.baitap04.model.ViewHolder;

public class MonhocAdapter extends BaseAdapter
{
    private Context context;
    private int layout;
    private List<MonHoc> monHocList;

    private ViewHolder viewHolder;

    //tạo Constructors

    public MonhocAdapter(int layout, Context context, List<MonHoc> monHocList) {
        this.layout = layout;
        this.context = context;
        this.monHocList = monHocList;
    }

    public MonhocAdapter(Context context, int
            layout, List<MonHoc> monHocList) {
        this.context = context;
        this.layout = layout;
        this.monHocList = monHocList;
    }

    @Override
    public int getCount() {
        return monHocList.size(); //lấy kích thước monhoclist
    }

    @Override
    public Object getItem(int position) {
        return monHocList.get(position); // Trả về môn học tại vị trí position
    }

    @Override
    public long getItemId(int position) {
        return position; // Trả về ID là position
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        //lấy context
        if (view==null){
            LayoutInflater inflater = (LayoutInflater)
                    context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //gọi view chứa layout
            view = inflater.inflate(layout,null);
            //ánh xạ view
            viewHolder = new ViewHolder();
            viewHolder.textName = (TextView) view.findViewById(R.id.textName);
            viewHolder.textDesc = (TextView) view.findViewById(R.id.textDesc);
            viewHolder.imagePic = (ImageView) view.findViewById(R.id.imagePic);
            view.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) view.getTag();
        }
        MonHoc monHoc = monHocList.get(i);
        viewHolder.textName.setText(monHoc.getName());
        viewHolder.textDesc.setText(monHoc.getDesc());
        viewHolder.imagePic.setImageResource(monHoc.getPic());
        //trả về view
        return view;
    }
}
