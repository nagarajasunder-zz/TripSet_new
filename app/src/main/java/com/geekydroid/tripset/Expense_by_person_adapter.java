package com.geekydroid.tripset;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Random;

public class Expense_by_person_adapter extends RecyclerView.Adapter<Expense_by_person_adapter.ViewHolder> {

    private ArrayList<Expense_by_person> list;

    public Expense_by_person_adapter(ArrayList<Expense_by_person> list) {
        this.list = list;
    }


    @NonNull
    @Override
    public Expense_by_person_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.expense_history_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Expense_by_person_adapter.ViewHolder holder, int position) {

        holder.main_layout.setBackgroundColor(Color.argb(100, new Random().nextInt(100), new Random().nextInt(200), new Random().nextInt(100)));

        holder.desc.setText(list.get(position).getDesc());
        holder.amt.setText(list.get(position).getAmt());
        holder.date.setText(list.get(position).getTime());
        holder.category.setText(list.get(position).getCategory());
        holder.spent_by.setText(list.get(position).getName());
        holder.share_by.setText(list.get(position).getShare_by());
        holder.more.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView desc, amt, date, category, spent_by, share_by;
        public ImageView more;
        public RelativeLayout main_layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            desc = (TextView) itemView.findViewById(R.id.desc);
            amt = (TextView) itemView.findViewById(R.id.amt_spent);
            date = (TextView) itemView.findViewById(R.id.date);
            category = (TextView) itemView.findViewById(R.id.category);
            spent_by = (TextView) itemView.findViewById(R.id.expense_by);
            share_by = (TextView) itemView.findViewById(R.id.share_by);
            more = (ImageView) itemView.findViewById(R.id.more);

            main_layout = (RelativeLayout) itemView.findViewById(R.id.main_layout);
        }
    }
}
