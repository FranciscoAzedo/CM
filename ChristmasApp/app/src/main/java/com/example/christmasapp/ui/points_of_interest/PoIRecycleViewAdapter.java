package com.example.christmasapp.ui.points_of_interest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.christmasapp.R;
import com.example.christmasapp.data.model.PointOfInterest;

import java.util.List;

public class PoIRecycleViewAdapter extends RecyclerView.Adapter<PoIRecycleViewAdapter.MyViewHolder> {

    private Context context;
    private List<PointOfInterest> pointOfInterestList;

    public PoIRecycleViewAdapter(Context context, List<PointOfInterest> pointOfInterestList) {
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
        holder.textView.setText(pointOfInterestList.get(position).getName());
        holder.imageView.setImageBitmap(pointOfInterestList.get(position).getBitmap());
    }

    @Override
    public int getItemCount() {
        return pointOfInterestList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textView;
        ImageView imageView;

        public MyViewHolder(View view) {
            super(view);
            textView = (TextView) view.findViewById(R.id.poi_name);
            imageView = (ImageView) view.findViewById(R.id.poi_image);
        }
    }
}
