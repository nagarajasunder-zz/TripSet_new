package com.geekydroid.tripset;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Trip_adapter extends RecyclerView.Adapter<Trip_adapter.ViewHolder> {

    private Trip_adapter adapter;
    private Context context;
    private ArrayList<Trip> list;

    public Trip_adapter(Trip_adapter adapter, Context context, ArrayList<Trip> list) {
        this.adapter = adapter;
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public Trip_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trip_layout, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final Trip_adapter.ViewHolder holder, final int position) {
        holder.name.setText(list.get(position).getT_name());
        try {
            holder.amt.setText(new MydatabaseHelper(context).get_trip_currency(list.get(position).getT_id())+" "+ list.get(position).getT_amt());
        } catch (Exception e) {
            holder.amt.setText("INR "+ list.get(position).getT_amt());
        }
        holder.date.setText(list.get(position).getT_date());
        holder.mainlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Trip_expand.class);
                ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) context, holder.imageView, ViewCompat.getTransitionName(holder.imageView));
                intent.putExtra("t_id", list.get(position).getT_id());
                intent.putExtra("t_name", list.get(position).getT_name());
                intent.putExtra("date", list.get(position).getT_date());
                intent.putExtra("tot_amt", list.get(position).getT_amt());
                intent.putExtra("group_size", list.get(position).getGrp_count());
                intent.putExtra("t_desc", list.get(position).getT_desc());
                context.startActivity(intent,optionsCompat.toBundle());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void update_list(ArrayList<Trip> result_list) {
        list = new ArrayList<>();
        list.addAll(result_list);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name, amt, date;
        public RelativeLayout mainlayout;
        public ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.trip_name);
            amt = (TextView) itemView.findViewById(R.id.amt);
            date = (TextView) itemView.findViewById(R.id.date);
            mainlayout = (RelativeLayout) itemView.findViewById(R.id.main_layout);
            imageView = (ImageView) itemView.findViewById(R.id.decor);
        }
    }
}
