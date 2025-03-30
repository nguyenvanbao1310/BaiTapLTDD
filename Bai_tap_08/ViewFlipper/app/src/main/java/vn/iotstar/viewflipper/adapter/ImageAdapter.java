package vn.iotstar.viewflipper.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import java.util.List;

import vn.iotstar.viewflipper.R;
import vn.iotstar.viewflipper.model.ImageModel;

public class ImageAdapter extends PagerAdapter {
    private Context context;
    private List<ImageModel> imageList;

    public ImageAdapter(Context context, List<ImageModel> imageList) {
        this.context = context;
        this.imageList = imageList;
    }

    @Override
    public int getCount() {
        return imageList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_images, container, false);
        ImageView imageView = view.findViewById(R.id.imgView);

        ImageModel imageModel = imageList.get(position);

        if (imageModel.getImageUrl() != null) {
            Glide.with(context).load(imageModel.getImageUrl()).into(imageView);
        } else {
            imageView.setImageResource(imageModel.getImageResId());
        }

        container.addView(view);
        return view;
    }


    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
