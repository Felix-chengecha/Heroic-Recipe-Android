package com.example.heroicrecipe.Adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.heroicrecipe.R;
import com.example.heroicrecipe.Models.food_model;

import java.util.List;
import java.util.Objects;

public class Foods_Adapter extends RecyclerView.Adapter<foodAdapter> {
    public final Context context;
    public final List<food_model> fmodel;
    public Foods_Adapter.Layclicklistener laylistener;


    public Foods_Adapter(Context context, List<food_model> fmodels) {
        this.context = context;
        this.fmodel = fmodels;
    }

    @NonNull
    @Override
    public foodAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.foods_items,parent,false);
        return new foodAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull foodAdapter holder, int position) {
        food_model fm=fmodel.get(position);
        String fid=fm.getFoodid();
        holder.level.setText(fm.getFoodlevel());
        holder.category.setText(fm.getFoodcategory());
        holder.name.setText(fm.getFoodname());

        String LEVEL= fm.getFoodlevel();
        if(Objects.equals(LEVEL, "beginner"))
        {
            Glide.with(context)
                    .load("https://upload.wikimedia.org/wikipedia/commons/thumb/2/29/Solid_green.svg/1024px-Solid_green.svg.png")
                    .into(holder.icon);
        }
        else if(Objects.equals(LEVEL, "+1 years"))
        {
            Glide.with(context)
                    .load("https://upload.wikimedia.org/wikipedia/commons/thumb/2/24/Solid_purple.svg/1024px-Solid_purple.svg.png")
                    .into(holder.icon);
        }

        Glide.with(context)
                .load(fm.getFoodimg())
                .into(holder.pic);

        holder.rlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                laylistener.onclicking(v,fid);
            }
        });
    }

    @Override
    public int getItemCount() {
        return fmodel.size();
    }

    //create an interface
    public  interface Layclicklistener{
        void onclicking(View v, String fid );
    }
    public void Layclick(Foods_Adapter.Layclicklistener lylistener) {
        this.laylistener=lylistener;
    }

}
class foodAdapter extends RecyclerView.ViewHolder{
    TextView name,category,level;
    RelativeLayout rlayout;
    ImageView pic,icon;
    public foodAdapter(@NonNull View itemView) {
        super(itemView);
        level=itemView.findViewById(R.id.food_level);
        icon=itemView.findViewById(R.id.signn);
        name=itemView.findViewById(R.id.food_name);
        pic=itemView.findViewById(R.id.food_img);
        category=itemView.findViewById(R.id.food_category);
        rlayout=itemView.findViewById(R.id.foodlayout);


    }
}

