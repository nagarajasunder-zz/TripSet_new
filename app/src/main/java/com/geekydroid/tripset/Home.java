package com.geekydroid.tripset;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import hotchemi.android.rate.AppRate;

public class Home extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FloatingActionButton new_trip;
    private Trip_adapter adapter;
    private MydatabaseHelper helper;
    private Toolbar toolbar;
    private LottieAnimationView no_data;
    private TextView text;
    private AlertDialog dialog;
    private ArrayList<Trip> list, result_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        AppRate.with(this)
                .setInstallDays(2)
                .setLaunchTimes(3)
                .setRemindInterval(2)
                .setShowLaterButton(true)
                .monitor();

        AppRate.showRateDialogIfMeetsConditions(this);


        SharedPreferences preferences = getSharedPreferences("prefs", MODE_PRIVATE);
        boolean first_start = preferences.getBoolean("first_start", true);


        if (first_start) {
            add_category();
            show_dialog();
            SharedPreferences preferences1 = getSharedPreferences("prefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences1.edit();
            editor.putBoolean("first_start", false);
            editor.apply();
        }

        setUI();
        fetch_data();
        new_trip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), NewTrip.class));
                finish();
            }
        });


    }


    private void show_dialog() {
        dialog = new AlertDialog.Builder(this)
                .setTitle("Wait a minute traveller!!")
                .setMessage("Welcome to Tripset your favourite travel expense manager. The default currency is not yet set,to change the default currency then head on to..  Setting>Default Currency and change it")
                .setPositiveButton("GOT IT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }


    private void add_category() {
        MydatabaseHelper helper = new MydatabaseHelper(this);
        ArrayList<String> category = new ArrayList<>();
        category.add("Petrol");
        category.add("Toll");
        category.add("Food");
        category.add("Hotel");
        category.add("Entertainment");
        category.add("Parking");
        category.add("Entry ticket");
        category.add("Miscellaneous");

        for (int i = 0; i < category.size(); i++) {
            helper.add_a_category(category.get(i));
        }
    }

    private void fetch_data() {
        list.clear();
        Cursor cursor = helper.fetch_trip_details();
        if (cursor.getCount() > 0) {
            no_data.setVisibility(View.GONE);
            text.setVisibility(View.GONE);
            while (cursor.moveToNext()) {
                list.add(new Trip(cursor.getString(0), cursor.getString(1), cursor.getString(5), cursor.getString(3), cursor.getString(2), cursor.getString(4)));
            }
        } else {
            no_data.setVisibility(View.VISIBLE);
            text.setVisibility(View.VISIBLE);
        }
    }

    private void setUI() {

        helper = new MydatabaseHelper(this);
        list = new ArrayList<>();
        result_list = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        adapter = new Trip_adapter(adapter, this, list);
        recyclerView.setAdapter(adapter);

        new_trip = (FloatingActionButton) findViewById(R.id.new_trip);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        no_data = (LottieAnimationView) findViewById(R.id.no_data);
        text = (TextView) findViewById(R.id.text);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        fetch_data();
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetch_data();
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.trip_popup, menu);
        MenuItem item = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setQueryHint("Search here");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                result_list.clear();
                if (newText.trim().length() > 0) {
                    for (Trip i : list) {
                        if (i.getT_name().toLowerCase().contains(newText.toLowerCase())) {
                            result_list.add(i);
                        }
                    }
                    adapter.update_list(result_list);
                } else {
                    result_list.addAll(list);
                    adapter.update_list(result_list);
                }
                return true;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                startActivity(new Intent(getApplicationContext(), Settings.class));
                break;
            case R.id.order_by_amount:
                order_by_something("TOTAL_AMT", "asc");
                break;
            case R.id.order_by_amount_desc:
                order_by_something("TOTAL_AMT", "desc");
                break;
            case R.id.sort_by_name:
                order_by_something("T_NAME", "asc");
                break;
            case R.id.sort_by_name_desc:
                order_by_something("T_NAME", "desc");
                break;
            case R.id.sort_by_count:
                order_by_something("GROUP_SIZE", "asc");
                break;
            case R.id.sort_by_count_desc:
                order_by_something("GROUP_SIZE", "desc");
                break;
            case R.id.order_by_date:
                order_by_something("DATE", "asc");
                break;
            case R.id.order_by_date_desc:
                order_by_something("DATE", "desc");
                break;
        }

        return true;
    }

    private void order_by_something(String order_by, String type) {
        adapter.update_list(helper.order_by_amount(order_by, type));
    }
}