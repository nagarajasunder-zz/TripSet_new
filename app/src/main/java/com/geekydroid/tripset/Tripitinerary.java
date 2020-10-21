package com.geekydroid.tripset;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Tripitinerary extends AppCompatActivity {

    private ImageButton add_desc, add_places;
    private TextView desc;
    private MydatabaseHelper helper;
    private String t_id;
    boolean popup = false;
    private RecyclerView recyclerView;
    private ArrayList<String> places, p_id;
    private Places_adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tripitinerary);
        setUI();
        fetch_data();

        if (desc.getText().toString().trim().length() > 0) {
            add_desc.setImageResource(R.drawable.ic_more);
            popup = true;
        }
        add_desc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (popup) {
                    PopupMenu popupMenu = new PopupMenu(Tripitinerary.this, v);
                    popupMenu.inflate(R.menu.desc_popup_menu);
                    popupMenu.show();
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.Add:
                                    show_dialog();
                                    return true;
                                case R.id.Edit:
                                    show_edit_dialog();
                                    return true;
                                case R.id.Delete:
                                    show_del_dialog();
                                    return true;
                                default:
                                    return false;
                            }
                        }
                    });
                } else {
                    show_dialog();
                }
            }
        });

        add_places.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show_places_add_dialog();
            }
        });
    }

    private void show_places_add_dialog() {
        View view = LayoutInflater.from(this).inflate(R.layout.desc_dialog, null);
        final EditText place;
        place = (EditText) view.findViewById(R.id.desc);
        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(view)
                .setTitle("Add a place")
                .setPositiveButton("Add", null)
                .setNegativeButton("Cancel", null)
                .show();

        Button button = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!place.getText().toString().isEmpty()) {

                    String s = place.getText().toString();
                    s = String.valueOf(s.charAt(0)).toUpperCase() + s.substring(1);
                    if (places.indexOf(s.trim()) == -1) {
                        helper.add_places_to_visit(t_id, s);
                        places.clear();
                        p_id.clear();
                        places.addAll(helper.get_places_to_visit(t_id));
                        p_id.addAll(helper.get_p_id());
                    }
                    dialog.dismiss();
                } else {
                    place.setError("Add a place");
                }
            }
        });

        dialog.create();
    }

    private void show_del_dialog() {
        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Delete")
                .setMessage("Do you want to delete the trip description ? ")
                .setPositiveButton("Yes", null)
                .setNegativeButton("No", null)
                .show();

        Button button = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helper.delete_trip_desc(t_id);
                popup = false;
                add_desc.setImageResource(R.drawable.ic_add);
                fetch_data();
                dialog.dismiss();
            }
        });

    }

    private void show_edit_dialog() {
        View view = LayoutInflater.from(this).inflate(R.layout.desc_dialog, null);
        final EditText desc;

        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(view)
                .setTitle("Edit the description")
                .setPositiveButton("Ok", null)
                .setNegativeButton("Cancel", null)
                .show();
        desc = view.findViewById(R.id.desc);
        desc.setText(new MydatabaseHelper(Tripitinerary.this).get_trip_desc(t_id));

        Button button = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (desc.getText().toString().trim().isEmpty()) {
                    desc.setError("Please add a description");
                } else {
                    MydatabaseHelper helper = new MydatabaseHelper(Tripitinerary.this);
                    helper.update_trip_desc(t_id, desc.getText().toString());
                    fetch_data();
                    dialog.dismiss();
                }
            }
        });

    }

    private void fetch_data() {
        desc.setText(helper.get_trip_desc(t_id));
    }

    private void show_dialog() {
        View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.desc_dialog, null);
        final EditText desc = (EditText) view.findViewById(R.id.desc);
        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Add a Description/Checklist")
                .setView(view)
                .setPositiveButton("Add", null)
                .setNegativeButton("Cancel", null)
                .show();
        Button add = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (desc.getText().toString().trim().isEmpty()) {
                    desc.setError("Add a description/note");
                } else {
                    String old_desc = helper.get_trip_desc(t_id);
                    if (old_desc.trim().length() == 0) {
                        helper.add_trip_desc(t_id, desc.getText().toString());
                        fetch_data();
                        add_desc.setImageResource(R.drawable.ic_more);
                        popup = true;
                    } else {
                        helper.update_trip_desc(t_id, old_desc + "\n" + desc.getText().toString());
                        fetch_data();
                    }
                    dialog.dismiss();
                }
            }
        });
    }

    private void setUI() {
        add_places = (ImageButton) findViewById(R.id.add_places);
        add_desc = (ImageButton) findViewById(R.id.add_desc);
        desc = (TextView) findViewById(R.id.desc);
        helper = new MydatabaseHelper(this);

        t_id = getIntent().getStringExtra("t_id");

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        p_id = new ArrayList<>();
        places = new ArrayList<>();

        places.addAll(helper.get_places_to_visit(t_id));
        p_id.addAll(helper.get_p_id());
        adapter = new Places_adapter(this,places,p_id);

        recyclerView.setAdapter(adapter);

    }
}