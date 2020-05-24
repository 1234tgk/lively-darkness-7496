package com.example.livelydarkness;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SuntimeAdapter extends RecyclerView.Adapter<SuntimeAdapter.SuntimeViewHolder> {
    public static class SuntimeModel {
        public String dateString;
        public String suntimeString;
        public SuntimeModel(String dateString, String suntimeString) {
            this.dateString = dateString;
            this.suntimeString = suntimeString;
        }
    }
    private List<SuntimeModel> dataset;
    public static class SuntimeViewHolder extends RecyclerView.ViewHolder {
        public TextView dateText;
        public TextView suntimeText;
        public SuntimeViewHolder(View v) {
            super(v);
            dateText = v.findViewById(R.id.date_text_view);
            suntimeText = v.findViewById(R.id.suntime_text_view);
        }
    }

    public SuntimeAdapter(List<SuntimeModel> dataset) {
        this.dataset = dataset;
    }

    @NonNull
    @Override
    public SuntimeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.suntime_layout, parent, false);
        SuntimeViewHolder vh = new SuntimeViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull SuntimeViewHolder holder, int position) {
        holder.dateText.setText(dataset.get(position).dateString);
        holder.suntimeText.setText(dataset.get(position).suntimeString);
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }
}
