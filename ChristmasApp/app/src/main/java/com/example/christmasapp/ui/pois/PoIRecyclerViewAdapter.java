package com.example.christmasapp.ui.pois;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.christmasapp.R;
import com.example.christmasapp.data.model.Event;
import com.example.christmasapp.data.model.PointOfInterest;

import java.util.List;

public class PoIRecyclerViewAdapter extends RecyclerView.Adapter<PoIRecyclerViewAdapter.MyViewHolder> {

    private Context context;
    private List<PointOfInterest> pointOfInterestList;
    private OnItemClickListener listener;
    private OnIconClickListener iconListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setOnIconClickListener(OnIconClickListener iconListener) {
        this.iconListener = iconListener;
    }

    public PoIRecyclerViewAdapter(Context context, List<PointOfInterest> pointOfInterestList) {
        this.context = context;
        this.pointOfInterestList = pointOfInterestList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        view = layoutInflater.inflate(R.layout.item_card, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final PointOfInterest currentPOI = pointOfInterestList.get(position);

        holder.textView.setText(currentPOI.getName());
        holder.imageView.setImageBitmap(currentPOI.getBitmap());

        /* SUBSTITUTIR ISTO URGENTE! */
        if (currentPOI instanceof Event) {
            if (currentPOI.isSubscribed()) {
                holder.iconView.setImageDrawable(context.getDrawable(R.drawable.ic_go_filled));
            } else {
                holder.iconView.setImageDrawable(context.getDrawable(R.drawable.ic_go_empty));
            }
        }
        else {
            if (currentPOI.isSubscribed()) {
                holder.iconView.setImageDrawable(context.getDrawable(R.drawable.ic_star_filled));
            } else {
                holder.iconView.setImageDrawable(context.getDrawable(R.drawable.ic_star_empty));
            }
        }

        holder.iconView.setOnClickListener(v -> {
            iconListener.OnIconClickListener(position);
        });

        holder.itemView.setOnClickListener(v -> {
            listener.onItemClick(position);
        });
    }

    @Override
    public int getItemCount() {
        return pointOfInterestList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public interface OnIconClickListener {
        void OnIconClickListener(int position);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView textView;
        private ImageView imageView;
        private ImageView iconView;

        public MyViewHolder(View view) {
            super(view);
            textView = (TextView) view.findViewById(R.id.poi_name);
            imageView = (ImageView) view.findViewById(R.id.poi_image);
            iconView = (ImageView) view.findViewById(R.id.icon_favorite);
        }
    }
}
