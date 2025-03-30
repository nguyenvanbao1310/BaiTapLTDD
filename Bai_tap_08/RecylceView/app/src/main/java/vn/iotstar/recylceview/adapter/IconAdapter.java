package vn.iotstar.recylceview.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import vn.iotstar.recylceview.R;
import vn.iotstar.recylceview.model.IconModel;

public class IconAdapter extends RecyclerView.Adapter<IconAdapter.IconHolder> {
    private Context context;
    private List<IconModel> iconList;

    public IconAdapter(Context context, List<IconModel> iconList) {
        this.context = context;
        this.iconList = iconList;
    }

    @NonNull
    @Override
    public IconHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_icon_promotion, parent, false);
        return new IconHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IconHolder holder, int position) {
        holder.iconImageView.setImageResource(iconList.get(position).getImageResId());
        holder.tvIcon.setText(iconList.get(position).getIconName());
    }

    @Override
    public int getItemCount() {
        return iconList.size();
    }

    public static class IconHolder extends RecyclerView.ViewHolder {
        ImageView iconImageView;
        TextView tvIcon;

        public IconHolder(@NonNull View itemView) {
            super(itemView);
            iconImageView = itemView.findViewById(R.id.iconImageView);
            tvIcon = itemView.findViewById(R.id.tvIcon);
        }
    }
    public void setListenerList(List<IconModel> filteredList) {
        this.iconList = filteredList;
        notifyDataSetChanged();
    }
}