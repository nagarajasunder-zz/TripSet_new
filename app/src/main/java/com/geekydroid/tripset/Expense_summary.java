package com.geekydroid.tripset;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Expense_summary extends AppCompatActivity {

    private ArrayList<String> names, due, spent;
    private RecyclerView recyclerView;
    private TextView total, trip_name;
    private Readapter readapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_summary);
        setUI();
        getIntents();
    }

    private void getIntents() {
        trip_name.setText(getIntent().getStringExtra("t_name"));
        total.setText("Total amount: " + getIntent().getStringExtra("total"));
        names.addAll(getIntent().getStringArrayListExtra("names"));
        spent.addAll(getIntent().getStringArrayListExtra("spent"));
        due.addAll(getIntent().getStringArrayListExtra("due"));

        readapter = new Readapter(names, spent, due);
        recyclerView.setAdapter(readapter);
        readapter.notifyDataSetChanged();

    }

    private void setUI() {

        names = new ArrayList<>();
        due = new ArrayList<>();
        spent = new ArrayList<>();

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        total = (TextView) findViewById(R.id.total);
        trip_name = (TextView) findViewById(R.id.trip_name);
    }

    private class Readapter extends RecyclerView.Adapter<Readapter.ViewHolder> {

        private ArrayList<String> name, spent, due;

        public Readapter(ArrayList<String> name, ArrayList<String> spent, ArrayList<String> due) {
            this.name = name;
            this.spent = spent;
            this.due = due;
        }

        @NonNull
        @Override
        public Readapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.expense_summary_layout, null);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull Readapter.ViewHolder holder, int position) {
            holder.Name.setText(name.get(position));
            holder.Spent.setText(spent.get(position));
            if (Double.parseDouble(due.get(position)) < 0) {
                holder.due.setTextColor(Color.RED);
                holder.due.setText(due.get(position));
            } else {
                holder.due.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.Green));
                holder.due.setText(due.get(position));
            }
        }

        @Override
        public int getItemCount() {
            return name.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView Name, Spent, due;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                Name = (TextView) itemView.findViewById(R.id.name);
                Spent = (TextView) itemView.findViewById(R.id.spent);
                due = (TextView) itemView.findViewById(R.id.due);
            }
        }
    }
}