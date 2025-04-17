package com.example.workthread.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workthread.R;
import com.example.workthread.model.Dish;

import java.util.List;

public class DishAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_ITEM = 0;
    private static final int VIEW_TYPE_LOADING = 1;
    private List<Dish> dishList;
    private Context context;
    private LayoutInflater inflater;

    public DishAdapter(List<Dish> dishList, Context context) {
        this.dishList = dishList;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        if (viewType == VIEW_TYPE_ITEM){
            View view = inflater.inflate(R.layout.row_dish, parent, false);
            return new DishViewHolder(view);
        }
        else {
            View view = inflater.inflate(R.layout.item_progressbar, parent, false);
            return new ProcessBarViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position){
        if (holder instanceof DishViewHolder){
            Dish dish = dishList.get(position);
            if (dish.getImage() != null){
            ((DishViewHolder) holder).dishImage.setImageBitmap(dish.getImage());}
            ((DishViewHolder) holder).dishName.setText(dish.getName());
            ((DishViewHolder) holder).dishDescription.setText(dish.getDescription());
            ((DishViewHolder) holder).dishPrice.setText(dish.getPrice());
        }
        else {
            ((ProcessBarViewHolder) holder).loading.setIndeterminate(true);
        }
    }


    @Override
    public int getItemCount(){
        return dishList.size();
    }

    @Override
    public  int getItemViewType(int position){
        if (dishList.get(position)!=null){
            return VIEW_TYPE_ITEM;
        }
        else {
            return VIEW_TYPE_LOADING;
        }
    }

    public class DishViewHolder extends RecyclerView.ViewHolder {
        ImageView dishImage;
        TextView dishName;
        TextView dishDescription;
        TextView dishPrice;

        public DishViewHolder(View itemView) {
            super(itemView);
            dishImage = itemView.findViewById(R.id.im_dish);
            dishName = itemView.findViewById(R.id.name_dish);
            dishDescription = itemView.findViewById(R.id.description_dish);
            dishPrice = itemView.findViewById(R.id.price_dish);
        }
    }

    public class ProcessBarViewHolder extends RecyclerView.ViewHolder{
        ProgressBar loading;
        public ProcessBarViewHolder(@NonNull View itemView) {
            super(itemView);
            loading = itemView.findViewById(R.id.loading);
        }
    }
}
