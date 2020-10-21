package com.geekydroid.tripset;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class stats extends AppCompatActivity {

    private static final String TAG = "stats";
    private PieChart pieChart;
    private PieDataSet dataSet;
    private PieData data;
    private MydatabaseHelper helper;
    private ArrayList<PieEntry> spent, category_spent;
    private ArrayList<String> names, category, spinner_list;
    private Cursor cursor;
    private int i = 0;
    private String t_id;
    private double totol_amount;
    private Spinner spinner;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);
        setUI();
        fetch_person_spent();
        fetch_category_spent();
        set_piechart_data(category_spent, "Category spent", "Total Amount " + totol_amount);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (spinner.getSelectedItem().toString().equals("category")) {
                    set_piechart_data(category_spent, "Category spnet", "Total Amount " + totol_amount);
                } else {
                    set_piechart_data(spent, "Person spnet", "Total amount " + totol_amount);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void fetch_category_spent() {
        category.clear();
        category_spent.clear();
        Cursor cursor = helper.get_category_expense(t_id);
        i = 0;
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                category.add(cursor.getString(0));
                category_spent.add(new PieEntry(Float.parseFloat(cursor.getString(1)), category.get(i)));
                i++;
            }
        }

    }

    private void fetch_person_spent() {
        spent.clear();
        names.clear();
        cursor = helper.fetch_all_members(Integer.parseInt(t_id));
        if (cursor.getCount() > 0) {
            i = 0;
            while (cursor.moveToNext()) {
                names.add(cursor.getString(2));
                if (Float.parseFloat(cursor.getString(3)) > 0) {
                    spent.add(new PieEntry(Float.parseFloat(cursor.getString(3)), names.get(i)));
                }
                i++;
            }
        }

    }


    private void setUI() {

        t_id = getIntent().getStringExtra("t_id");
        helper = new MydatabaseHelper(this);
        totol_amount = helper.get_trip_total_amt(t_id);
        pieChart = (PieChart) findViewById(R.id.pie_chart);
        pieChart.setCenterTextSize(20);
        category = new ArrayList<>();
        category_spent = new ArrayList<>();
        spent = new ArrayList();
        names = new ArrayList<>();

        spinner_list = new ArrayList<>();
        spinner_list.add("category");
        spinner_list.add("spent");

        spinner = (Spinner) findViewById(R.id.spinner);
        adapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, spinner_list);
        spinner.setAdapter(adapter);

    }

    private void set_piechart_data(ArrayList<PieEntry> list, String label, String center_text) {
        dataSet = new PieDataSet(list, label);
        data = new PieData(dataSet);
        data.setValueTextSize(15);
        data.setValueTextColor(Color.WHITE);
        pieChart.setData(data);
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        pieChart.animateXY(3000, 3000);
        pieChart.setCenterText(center_text);
        pieChart.getLegend().setEnabled(false);
        pieChart.getDescription().setEnabled(false);
        pieChart.setCenterTextSize(20f);
        pieChart.setEntryLabelTextSize(15f);
    }


}