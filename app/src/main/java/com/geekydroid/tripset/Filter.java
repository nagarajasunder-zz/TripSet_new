package com.geekydroid.tripset;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;

public class Filter extends AppCompatActivity {

    private static final String TAG = "Filter";
    //views
    private ViewPager2 viewPager2;
    private TabLayout tabLayout;


    //vars
    private String t_id;
    private MydatabaseHelper helper;
    private ArrayList<String> names, sublist;
    private Filter_adapter adapter;
    private int id;
    private String column_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        setUI();
        sublist = helper.get_all_trip_items(t_id, column_name);
        names = helper.get_all_trip_items(t_id, "M_NAME");
        Log.d(TAG, "onCreate: " + names.toString());
        if (id == R.id.filter_by_date) {
            ArrayList<String> check = new ArrayList<>();
            ArrayList<String> date_list = new ArrayList<>();
            for (int i = 0; i < sublist.size(); i++) {
                String date = sublist.get(i).substring(0, 10);
                if (i == 0 || date_list.indexOf(date) == -1) {
                    date_list.add(date);
                    check.add(sublist.get(i));
                }
            }

            sublist.clear();
            sublist.addAll(check);
        }
        adapter = new Filter_adapter(sublist, t_id, id, helper, column_name);
        viewPager2.setAdapter(adapter);
        TabLayoutMediator mediator = new TabLayoutMediator(tabLayout, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                TextView tv = (TextView) LayoutInflater.from(Filter.this).inflate(R.layout.tablayout_view, null);

                if (id == R.id.filter_by_person) {
                    Log.d(TAG, "onConfigureTab: postition " + names.get(position));
                    tv.setText(names.get(position));
                } else if (id == R.id.filter_by_category) {
                    tv.setText(sublist.get(position));
                } else {
                    tv.setText(sublist.get(position).substring(0, 10));
                }
                tab.setCustomView(tv);
            }
        });
        mediator.attach();
    }

    private void setUI() {

        t_id = getIntent().getStringExtra("t_id");
        id = getIntent().getIntExtra("id", id);
        column_name = getIntent().getStringExtra("column_name");
        helper = new MydatabaseHelper(this);

        viewPager2 = (ViewPager2) findViewById(R.id.viewpager);
        viewPager2.setOffscreenPageLimit(1);
        tabLayout = (TabLayout) findViewById(R.id.tablayout);
        names = new ArrayList<>();

    }
}