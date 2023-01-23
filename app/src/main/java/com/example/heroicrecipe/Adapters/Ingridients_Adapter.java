package com.example.heroicrecipe.Adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.heroicrecipe.Models.Ingridients_model;
import com.example.heroicrecipe.R;

import java.util.List;

public class Ingridients_Adapter extends RecyclerView.Adapter<IngridientsAdapter> {
    public final Context context;
    public final List<Ingridients_model> imodel;


    public Ingridients_Adapter(Context context, List<Ingridients_model> Imodels) {
        this.context = context;
        this.imodel = Imodels;
    }


    @NonNull
    @Override
    public IngridientsAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.ingridients_items,parent,false);
        return new IngridientsAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngridientsAdapter holder, int position) {
        Ingridients_model im=imodel.get(position);
        holder.ing.setText(im.getIngridient_name());


    }

    @Override
    public int getItemCount() {
        return imodel.size();
    }
}

class IngridientsAdapter extends RecyclerView.ViewHolder{
    TextView ing;

    public IngridientsAdapter(@NonNull View itemView) {
        super(itemView);

        ing=itemView.findViewById(R.id.ingrid);
    }
}
