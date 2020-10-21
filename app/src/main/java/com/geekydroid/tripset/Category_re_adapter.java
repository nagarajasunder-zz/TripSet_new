package com.geekydroid.tripset;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Category_re_adapter extends RecyclerView.Adapter<Category_re_adapter.ViewHolder> {

    private ArrayList<String> list;
    private Context context;
    private String category = "";
    private int old_pos = -1;
    private ArrayList<ViewHolder> viewHolder;

    public Category_re_adapter(ArrayList<String> list, Context context) {
        this.list = list;
        this.context = context;
        viewHolder = new ArrayList<>();
    }

    @NonNull
    @Override
    public Category_re_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_re_layout, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final Category_re_adapter.ViewHolder holder, final int position) {
        viewHolder.add(holder);
        holder.category.setText(list.get(position));
        holder.category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.category.setTextColor(Color.RED);
                category = list.get(position);
                old_pos = holder.getAdapterPosition();
                for (int i = 0; i < viewHolder.size(); i++) {

                    if (i != old_pos) {
                        viewHolder.get(i).category.setTextColor(Color.BLACK);
                    }
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView category;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            category = (TextView) itemView.findViewById(R.id.category);
        }
    }

    public String get_selected_item() {
        return category;
    }
}
