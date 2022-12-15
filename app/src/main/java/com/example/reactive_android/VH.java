package com.example.reactive_android;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class VH extends RecyclerView.ViewHolder{
    public ImageView imageView;
    public VH(@NonNull View itemView, ViewInterface vI) {
        super(itemView);

        imageView = itemView.findViewById(R.id.specialImage);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(vI != null)
                {
                    int position = getAdapterPosition();

                    if(position != RecyclerView.NO_POSITION)
                    {
                        vI.onItemClick(position);
                    }
                }
            }
        });
    }
}
