package com.geekydroid.tripset;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mynameismidori.currencypicker.ExtendedCurrency;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Settings extends AppCompatActivity {

    private Spinner spinner;
    private MydatabaseHelper helper;
    private TextView add_category;
    private ArrayList<String> list, currency_list;
    private ArrayAdapter<String> adapter;
    private Button save;
    private List<ExtendedCurrency> currencies;

    private CardView view_category_layout, rate_layout, share_layout, faq_layout, more_layout, def_currency_layout, privacy_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setUI();
        set_spinner();
        add_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show_dialog();
            }
        });

        view_category_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show_category();
            }
        });
        spinner.setOnItemSelectedListener(new spinnerlistener());

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currency = spinner.getSelectedItem().toString();
                Toast.makeText(Settings.this, "Default currency changed", Toast.LENGTH_SHORT).show();
                save.setVisibility(View.INVISIBLE);
                if (helper.get_currency().trim().isEmpty()) {
                    helper.set_default_currency(currency);
                } else {
                    helper.update_default_currency(currency);
                }
            }
        });


        faq_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), com.geekydroid.tripset.faq.class));
            }
        });


        privacy_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://tripset.flycricket.io/privacy.html"));
                startActivity(intent);
            }
        });

        rate_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.geekydroid.tripset"));
                startActivity(intent);
            }
        });

        share_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String share_text = "Hey guys i am using this application called Tripset, it helps me a lot in managing my travel expense and makes my travel much more easier check this out https://play.google.com/store/apps/details?id=com.geekydroid.tripset";
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT, share_text);
                intent.setType("text/plain");
                Intent share_intent = Intent.createChooser(intent, null);
                startActivity(share_intent);

            }
        });

        more_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://play.google.com/store/apps/dev?id=7436071758058984589"));
                startActivity(intent);
            }
        });


    }


    private void set_spinner() {
        currency_list = new ArrayList<>();
        for (ExtendedCurrency i : currencies) {
            currency_list.add(i.getCode());
        }
        Collections.sort(currency_list);
        adapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, currency_list);
        spinner.setAdapter(adapter);

        if (!helper.get_currency().trim().isEmpty()) {
            int pos = adapter.getPosition(helper.get_currency());
            spinner.setSelection(pos);
        }
    }

    private void show_category() {
        Category_re_adapter adapter;
        View view = LayoutInflater.from(this).inflate(R.layout.category_dialog_layout, null);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        TextView title = view.findViewById(R.id.title);
        title.setText("Category");
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(view)
                .setPositiveButton("Ok", null)
                .show();

        recyclerView.setVisibility(View.VISIBLE);
        title.setVisibility(View.VISIBLE);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new Category_re_adapter(list, this);
        recyclerView.setAdapter(adapter);

        dialog.show();
    }

    private void show_dialog() {
        View view = LayoutInflater.from(this).inflate(R.layout.new_category_layout, null);
        final EditText category;
        category = view.findViewById(R.id.category);
        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(view)
                .setTitle("Add a new Category")
                .setPositiveButton("Add", null)
                .setNegativeButton("Cancel", null)
                .show();

        Button button = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = category.getText().toString();
                if (category.getText().toString().trim().isEmpty()) {
                    category.setError("Please add a new Category");
                } else {
                    s = s.substring(0, 1).toUpperCase() + s.substring(1);
                    if (list.indexOf(s) == -1) {
                        helper.add_a_category(s);
                        list.clear();
                        list.addAll(helper.get_all_category());
                        Collections.sort(list);
                        dialog.dismiss();
                        Toast.makeText(Settings.this, "New category added successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(Settings.this, "Category already exists", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                }
            }
        });
    }

    private void setUI() {

        view_category_layout = findViewById(R.id.view_category_layout);
        rate_layout = findViewById(R.id.rate_layout);
        share_layout = findViewById(R.id.share_layout);
        more_layout = findViewById(R.id.more_layout);
        faq_layout = findViewById(R.id.faq_layout);
        def_currency_layout = findViewById(R.id.def_currency_layout);
        privacy_layout = findViewById(R.id.privacy_layout);


        add_category = (TextView) findViewById(R.id.add_category);

        helper = new MydatabaseHelper(this);
        list = new ArrayList<>();
        list.addAll(helper.get_all_category());
        Collections.sort(list);
        spinner = (Spinner) findViewById(R.id.spinner);

        save = (Button) findViewById(R.id.save);

        currencies = ExtendedCurrency.getAllCurrencies();
    }


    public class Category_re_adapter extends RecyclerView.Adapter<Category_re_adapter.ViewHolder> {

        private ArrayList<String> list;
        private Context context;

        public Category_re_adapter(ArrayList<String> list, Context context) {
            this.list = list;
            this.context = context;
        }

        @NonNull
        @Override
        public Category_re_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_re_layout, null);
            return new Category_re_adapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final Category_re_adapter.ViewHolder holder, final int position) {
            holder.category.setText(list.get(position));
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView category;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                category = (TextView) itemView.findViewById(R.id.category);
            }
        }

    }

    public class spinnerlistener implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            if (!helper.get_currency().trim().isEmpty()) {
                int pos = adapter.getPosition(helper.get_currency());
                if (pos != position) {
                    save.setVisibility(View.VISIBLE);
                } else {
                    save.setVisibility(View.GONE);
                }
            } else {
                save.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

}