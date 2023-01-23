package com.example.heroicrecipe.Adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.heroicrecipe.Models.Img_model;
import com.example.heroicrecipe.R;

import java.util.List;

public class Img_Adapter extends RecyclerView.Adapter<ImgAdapter> {
    public final Context context;
    public final List<Img_model> imodel;


    public Img_Adapter(Context context, List<Img_model> Imodels) {
        this.context = context;
        this.imodel = Imodels;
    }

    @NonNull
    @Override
    public ImgAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.food_image_items,parent,false);
        return new ImgAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImgAdapter holder, int position) {
        Img_model img=imodel.get(position);
        Glide.with(context)
                .load(img.getImg())
                .into(holder.img);
    }
    @Override
    public int getItemCount() {
        return imodel.size();
    }
}
class ImgAdapter extends RecyclerView.ViewHolder{
    ImageView img;

    public ImgAdapter(@NonNull View itemView) {
        super(itemView);
        img=itemView.findViewById(R.id.food_img);
    }
}

