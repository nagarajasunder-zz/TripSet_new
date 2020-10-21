package com.geekydroid.tripset;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputLayout;
import com.mynameismidori.currencypicker.ExtendedCurrency;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class NewTrip extends AppCompatActivity {
    private TextInputLayout trip_name, desc;
    private Button add, save_trip;
    private String Trip_name, Desc = "", Name, Date = "", Def_currency = "";
    private int noc = 0;
    private RecyclerView recyclerView;
    private new_trip_adapter adapter;
    private ArrayList<String> names, currency_list;
    private TextView size, date;
    private MydatabaseHelper helper;
    private boolean ans = true;
    private ProgressDialog dialog;
    private LinearLayout group_layout;
    private Spinner currency_spinner;
    private ArrayAdapter<String> currency_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_trip);
        setUI();
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open_dialog();
            }
        });

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(NewTrip.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.YEAR, year);
                        date.setText(new SimpleDateFormat("dd/MM/yyyy").format(new Date(calendar.getTimeInMillis())));
                    }
                }, Calendar.getInstance().get(Calendar.DAY_OF_MONTH), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.YEAR));

                dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                dialog.show();
            }

        });

        save_trip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
                Trip_name = trip_name.getEditText().getText().toString().replace("\n", "");
                Desc = desc.getEditText().getText().toString().replace("\n", "");
                Def_currency = currency_spinner.getSelectedItem().toString();
                if (validate()) {
                    create_new_trip();
                } else {
                    dialog.dismiss();
                }

            }
        });


    }

    private void create_new_trip() {
        long result = helper.create_new_trip(Trip_name, Desc, noc, 0, Date, Def_currency);
        if (result != -1) {
            Cursor cursor = helper.get_trip_id();
            if (cursor.getCount() > 0 && cursor.getColumnCount() == 1) {
                String t_id = "";
                while (cursor.moveToNext()) {
                    t_id = cursor.getString(0);
                }

                for (int i = 0; i < noc; i++) {
                    long result2 = helper.add_mem_in_trip(names.get(i), 0, 0, Integer.parseInt(t_id));
                    if (result2 == -1) {
                        dialog.dismiss();
                        ans = false;
                        Toast.makeText(this, "Trip creation failure", Toast.LENGTH_SHORT).show();
                        break;
                    }
                }
                if (ans) {
                    dialog.dismiss();
                    Toast.makeText(this, "Trip created successfully!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), Home.class));
                    finish();
                }
            }


        } else {
            dialog.dismiss();
        }
    }


    private void open_dialog() {
        final View view = LayoutInflater.from(this).inflate(R.layout.add_trippies_dialog, null);
        final EditText name = (EditText) view.findViewById(R.id.name);
        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(view)
                .setPositiveButton("Add Member", null)
                .setNegativeButton("Cancel", null)
                .setNegativeButton("Cancel", null)
                .show();

        Button add = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Name = null;
                Name = name.getText().toString();
                if (Name.trim().isEmpty()) {
                    name.setError("Please Enter a name");
                } else {
                    Name = String.valueOf(Name.charAt(0)).toUpperCase() + Name.substring(1).toLowerCase().trim().replace("\n", "");
                    if (names.indexOf(Name) == -1) {
                        noc++;
                        names.add(Name);
                        adapter.notifyDataSetChanged();
                        if (noc > 0) {
                            group_layout.setVisibility(View.VISIBLE);
                            size.setVisibility(View.VISIBLE);
                            size.setText("Group size: " + String.valueOf(noc));
                        } else {
                            group_layout.setVisibility(View.GONE);
                            size.setVisibility(View.INVISIBLE);
                        }
                        dialog.dismiss();
                    } else {
                        name.setError("The person is already added in the group");
                    }
                }
            }
        });
    }

    private boolean validate() {

        if (Trip_name.trim().isEmpty()) {
            Toast.makeText(this, "please enter a trip name/title", Toast.LENGTH_SHORT).show();
            return false;
        } else if (noc <= 0) {
            Toast.makeText(this, "Please add at least one member for the group", Toast.LENGTH_SHORT).show();
            return false;
        } else if (Date.trim().isEmpty()) {
            Toast.makeText(this, "Please select a date for the trip", Toast.LENGTH_SHORT).show();
            return false;
        } else if (Def_currency.trim().isEmpty()) {
            Toast.makeText(this, "Please select currency for the trip", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void setUI() {

        helper = new MydatabaseHelper(this);

        names = new ArrayList<>();

        date = (TextView) findViewById(R.id.date);
        Date = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
        date.setText(Date);
        size = (TextView) findViewById(R.id.size);
        trip_name = (TextInputLayout) findViewById(R.id.trip_name);
        desc = (TextInputLayout) findViewById(R.id.desc);
        add = (Button) findViewById(R.id.add);
        save_trip = (Button) findViewById(R.id.save);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new new_trip_adapter(size, names, getApplicationContext());
        recyclerView.setAdapter(adapter);

        dialog = new ProgressDialog(this);
        dialog.setMessage("Creating new trip..");
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        group_layout = (LinearLayout) findViewById(R.id.group_layout);

        currency_spinner = (Spinner) findViewById(R.id.currency_spinner);
        List<ExtendedCurrency> currencies = ExtendedCurrency.getAllCurrencies();
        currency_list = new ArrayList<>();
        for (ExtendedCurrency i : currencies) {
            currency_list.add(i.getCode());
        }
        Collections.sort(currency_list);
        currency_adapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, currency_list);
        currency_spinner.setAdapter(currency_adapter);
        int pos = currency_adapter.getPosition(helper.get_currency());
        Def_currency = helper.get_currency();
        currency_spinner.setSelection(pos);


    }


    public class new_trip_adapter extends RecyclerView.Adapter<new_trip_adapter.ViewHolder> {

        private TextView size;
        private ArrayList<String> names;
        private Context context;

        public new_trip_adapter(TextView size, ArrayList<String> names, Context context) {
            this.size = size;
            this.names = names;
            this.context = context;
        }

        @NonNull
        @Override
        public new_trip_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.new_trippies_layout, null);
            return new new_trip_adapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull new_trip_adapter.ViewHolder holder, final int position) {
            holder.name.setText(names.get(position));
            holder.count.setText(names.get(position).substring(0, 1).toUpperCase());
            holder.name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

        }

        @Override
        public int getItemCount() {
            return names.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            public TextView name, count;
            public ImageView more;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                name = (TextView) itemView.findViewById(R.id.name);
                count = (TextView) itemView.findViewById(R.id.count);
                more = (ImageView) itemView.findViewById(R.id.more);
            }
        }

    }


}