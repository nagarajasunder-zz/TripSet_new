package com.geekydroid.tripset;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Filter_adapter extends RecyclerView.Adapter<Filter_adapter.ViewHolder> {


    private static final String TAG = "Filter_adapter";
    private Expense_by_person_adapter adapter;
    private ArrayList<String> column_value;
    private String t_id, column_name;
    private int id;
    private MydatabaseHelper helper;
    private ArrayList<Expense_by_person> sub_list;

    public Filter_adapter(ArrayList<String> column_value, String t_id, int id, MydatabaseHelper helper, String column_name) {
        this.column_value = column_value;
        this.t_id = t_id;
        this.id = id;
        this.helper = helper;
        this.column_name = column_name;
        sub_list = new ArrayList<>();
        adapter = null;
    }


    @NonNull
    @Override
    public Filter_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.filter_item_view, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull Filter_adapter.ViewHolder holder, int position) {
        double tot_amt = helper.get_column_spent_amt(t_id, column_name, column_value.get(position));
        holder.name.setText("Total Amount Spent: " + helper.get_trip_currency(t_id) + " " + tot_amt);
        sub_list = helper.get_column_filter_data(t_id, column_name, column_value.get(position));
        adapter = new Expense_by_person_adapter(sub_list);
        holder.recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        if (column_name.equals("DATE")) {
            tot_amt = helper.get_date_expense_sum(t_id, column_name, column_value.get(position).substring(0,10));
            holder.name.setText("Total Amount Spent: " + helper.get_trip_currency(t_id) + " " + tot_amt);
        }
    }

    @Override
    public int getItemCount() {
        if (id == R.id.filter_by_person) {
            return column_value.size();
        } else {
            return column_value.size();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public RecyclerView recyclerView;
        public TextView name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            recyclerView = itemView.findViewById(R.id.recycler_view);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
            name = itemView.findViewById(R.id.name);
        }
    }
}
