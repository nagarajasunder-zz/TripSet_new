package com.geekydroid.tripset;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Trip_Expense_history extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TextView total_amt;
    private MydatabaseHelper helper;
    private String t_id;
    private ArrayList<String> desc, amt, date, category, spent_by, share_by, s_id, m_id;
    private Trip_Expense_History_Adapter adapter;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip__expense_history);
        setUI();
        fetch_data();
    }


    private void fetch_data() {
        Cursor cursor = helper.get_trip_expense_history(t_id);
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                s_id.add(cursor.getString(0));
                amt.add(cursor.getString(3));
                date.add(cursor.getString(4));
                category.add(cursor.getString(5));
                desc.add(cursor.getString(6));
                spent_by.add(cursor.getString(7));
                share_by.add(cursor.getString(8));
            }

            adapter.notifyDataSetChanged();
        }
    }

    private void setUI() {

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        t_id = getIntent().getStringExtra("t_id");
        m_id = getIntent().getStringArrayListExtra("m_id_list");

        s_id = new ArrayList<>();

        desc = new ArrayList<>();
        date = new ArrayList<>();
        category = new ArrayList<>();
        amt = new ArrayList<>();
        spent_by = new ArrayList<>();
        share_by = new ArrayList<>();

        total_amt = (TextView) findViewById(R.id.total);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        helper = new MydatabaseHelper(this);
        total_amt.setText("Total Amount: " + helper.get_trip_total_amt(t_id));

        adapter = new Trip_Expense_History_Adapter(this, t_id, desc, amt, date, category, spent_by, share_by, m_id, s_id, total_amt, Trip_Expense_history.this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.filters_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {


        switch (item.getItemId()) {
            case R.id.filter_by_person:
                filter(t_id, R.id.filter_by_person, "M_ID");
                break;
            case R.id.filter_by_category:
                filter(t_id, R.id.filter_by_category, "CATEGORY");
                break;
            case R.id.filter_by_date:
                filter(t_id, R.id.filter_by_date, "DATE");
                break;
        }
        return true;
    }

    private void filter(String t_id, int id, String column_name) {
        Intent intent = new Intent(getApplicationContext(), Filter.class);
        intent.putExtra("t_id", t_id);
        intent.putExtra("id", id);
        intent.putExtra("column_name", column_name);
        startActivity(intent);
    }
}