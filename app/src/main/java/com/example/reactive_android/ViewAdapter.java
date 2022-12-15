package com.example.reactive_android;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ViewAdapter extends RecyclerView.Adapter<VH> {
    ArrayList<Bitmap> images = new ArrayList<>();
    ViewInterface vI;

    public ViewAdapter(ArrayList<Bitmap> images, ViewInterface vI)
    {
        this.images = images;
        this.vI = vI;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.scview, parent, false);
        VH vH = new VH(view, vI);
        return  vH;
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        holder.imageView.setImageBitmap(images.get(position));
    }

    @Override
    public int getItemCount() {
        return images.size();
    }
}
