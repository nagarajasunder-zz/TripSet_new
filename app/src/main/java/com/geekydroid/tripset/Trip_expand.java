package com.geekydroid.tripset;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class Trip_expand extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private TripExpandAdapter adapter;
    private ArrayList<String> m_id, names, count, spent, due;
    private Button add_expense;
    private String t_id, t_name, size, tot_amt, t_desc, date;
    private MydatabaseHelper helper;
    private TextView history, total, stats, itinerary, title;
    private TextView expense_summary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_expand);

        get_Intents();
        setUI();
        fetch_data();
        set_enabled();
        add_expense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), New_Expense.class);
                intent.putExtra("t_id", t_id);
                intent.putExtra("t_name", t_name);
                intent.putExtra("date", date);
                intent.putExtra("tot_amt", tot_amt);
                intent.putExtra("group_size", size);
                intent.putExtra("t_desc", t_desc);
                intent.putStringArrayListExtra("m_id_list", m_id);
                intent.putStringArrayListExtra("m_names_list", names);
                intent.putStringArrayListExtra("spent_list", spent);
                intent.putStringArrayListExtra("due_list", due);
                startActivity(intent);
                finish();
            }
        });

        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Trip_Expense_history.class);
                intent.putExtra("t_id", t_id);
                intent.putStringArrayListExtra("m_id_list", m_id);
                startActivity(intent);
            }
        });

        stats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), stats.class);
                intent.putExtra("t_id", t_id);
                startActivity(intent);
            }
        });

        itinerary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Tripitinerary.class);
                intent.putExtra("t_id", t_id);
                startActivity(intent);
            }
        });

        expense_summary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Expense_summary.class);
                intent.putExtra("t_id", t_id);
                intent.putExtra("total", tot_amt);
                intent.putExtra("t_name", t_name + " " + date);
                intent.putStringArrayListExtra("names", names);
                intent.putStringArrayListExtra("spent", spent);
                intent.putStringArrayListExtra("due", due);
                startActivity(intent);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            finish();
            recreate();
        }
    }

    private void fetch_data() {

        m_id.clear();
        names.clear();
        count.clear();
        spent.clear();
        due.clear();
        Cursor cursor = helper.fetch_all_members(Integer.parseInt(t_id));
        if (cursor.getCount() > 0) {
            int i = 1;
            while (cursor.moveToNext()) {
                m_id.add(cursor.getString(0));
                names.add(cursor.getString(2));
                count.add(String.valueOf(i));
                spent.add(cursor.getString(3));
                due.add(cursor.getString(4));
                i++;
            }
            adapter.notifyDataSetChanged();
        }

    }


    private void get_Intents() {
        t_id = getIntent().getStringExtra("t_id");
        t_name = getIntent().getStringExtra("t_name");
        t_desc = getIntent().getStringExtra("t_desc");
        size = getIntent().getStringExtra("group_size");
        tot_amt = getIntent().getStringExtra("tot_amt");
        date = getIntent().getStringExtra("date");
    }

    private void setUI() {
        expense_summary = (TextView) findViewById(R.id.summary);
        stats = (TextView) findViewById(R.id.stats);
        total = (TextView) findViewById(R.id.total);
        history = (TextView) findViewById(R.id.history);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        names = new ArrayList<>();
        count = new ArrayList<>();
        spent = new ArrayList<>();
        due = new ArrayList<>();
        m_id = new ArrayList<>();
        adapter = new TripExpandAdapter(this, m_id, names, count, spent, due, t_id);
        recyclerView.setAdapter(adapter);
        helper = new MydatabaseHelper(this);
        add_expense = (Button) findViewById(R.id.add_expense);
        total.setText("Total amount spent " + helper.get_currency() + helper.get_trip_total_amt(t_id));
        itinerary = (TextView) findViewById(R.id.itinerary);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        title = (TextView) toolbar.findViewById(R.id.title);
        title.setText(t_name + "\n" + date);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.trip_expand_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_new_person:
                show_new_person_dialog();
                break;
            case R.id.delete_trip:
                show_del_dialog();
                break;
            case R.id.export:
                export_csv();
            default:
                return false;
        }
        return true;
    }


    private void export_csv() {
        StringBuilder builder = new StringBuilder();
        builder.append("id,name,spent,due");
        for (int i = 0; i < names.size(); i++) {
            builder.append("\n" + (i + 1) + "," + names.get(i) + "," + spent.get(i) + "," + due.get(i));
        }
        try {
            FileOutputStream fos = openFileOutput(t_name + ".xls", Context.MODE_PRIVATE);
            fos.write(builder.toString().getBytes());
            fos.close();

            File filelocation = new File(getFilesDir(), t_name + ".xls");
            Uri path = FileProvider.getUriForFile(getApplicationContext(), "com.geekydroid.tripset.fileprovider", filelocation);
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_SUBJECT, "Hii, I have attached my trip expenses in this file ");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.putExtra(Intent.EXTRA_STREAM, path);
            startActivity(intent.createChooser(intent, "send"));


        } catch (Exception e) {

        }
    }

    private void show_new_person_dialog() {
        View view = LayoutInflater.from(this).inflate(R.layout.add_trippies_dialog, null);
        final EditText name = view.findViewById(R.id.name);
        TextView title = view.findViewById(R.id.title);
        title.setText("Add new person");
        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(view)
                .setPositiveButton("Add", null)
                .setNegativeButton("Cancel", null)
                .show();

        Button button = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = name.getText().toString();
                if (name.getText().toString().trim().isEmpty()) {
                    Toast.makeText(Trip_expand.this, "Please enter the name for the person", Toast.LENGTH_SHORT).show();
                } else {
                    s = s.substring(0, 1).toUpperCase() + s.substring(1);
                    if (names.indexOf(s) == -1) {
                        helper.add_mem_in_trip(s, 0, 0, Integer.parseInt(t_id));
                        fetch_data();
                        adapter.notifyDataSetChanged();
                        dialog.dismiss();
                        show_info_dialog();
                    } else {
                        Toast.makeText(Trip_expand.this, "Person Aldready in the group", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void show_info_dialog() {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setMessage("New Person added successfully in our group if he/she involves in any of the expenses you have to edit the expenses according to it")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();

        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();

    }

    private void show_del_dialog() {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Delete")
                .setMessage("Do you want to delete this trip ?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        helper.delete_a_trip(t_id);
                        startActivity(new Intent(getApplicationContext(), Home.class));
                        finish();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        total.setText("Total amount spent " + helper.get_trip_currency(t_id) + " " + helper.get_trip_total_amt(t_id));
        fetch_data();
        adapter.notifyDataSetChanged();
        set_enabled();
    }

    private void set_enabled() {
        if (helper.get_trip_total_amt(t_id) == 0) {
            stats.setEnabled(false);
            history.setEnabled(false);
            expense_summary.setEnabled(false);
        } else {
            stats.setEnabled(true);
            history.setEnabled(true);
            expense_summary.setEnabled(true);
        }
    }

    public class TripExpandAdapter extends RecyclerView.Adapter<TripExpandAdapter.ViewHolder> {

        private Context context;
        private ArrayList<String> m_id, names, count, spent, due;
        private String t_id;
        private int pos;
        private MydatabaseHelper helper;

        public TripExpandAdapter(Context context, ArrayList<String> m_id, ArrayList<String> names, ArrayList<String> count, ArrayList<String> spent, ArrayList<String> due, String t_id) {
            this.context = context;
            this.m_id = m_id;
            this.names = names;
            this.count = count;
            this.spent = spent;
            this.due = due;
            this.t_id = t_id;
            helper = new MydatabaseHelper(context);
        }

        @NonNull
        @Override
        public TripExpandAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trip_expand_layout, null);
            return new TripExpandAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull TripExpandAdapter.ViewHolder holder, final int position) {

            holder.names.setText(names.get(position));
            holder.count.setText(names.get(position).substring(0, 1).toUpperCase());
            DecimalFormat format = new DecimalFormat("#.00");
            if (Float.parseFloat(due.get(position)) >= 0) {
                holder.spent.setTextColor(ContextCompat.getColor(context, R.color.Green));
            } else {
                holder.spent.setTextColor(Color.RED);
            }
            holder.spent.setText(spent.get(position) + "(" + format.format(Float.parseFloat(due.get(position))) + ")");
            holder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pos = position;
                    show_diaog(helper.get_trip_currency(t_id));
                }
            });
        }

        private void show_diaog(String currency) {
            View view = LayoutInflater.from(context).inflate(R.layout.member_info_layout, null);
            ImageView close;
            TextView name, spent_amt, share_by, due_amt, title;
            Button delete;
            final android.app.AlertDialog dialog = new android.app.AlertDialog.Builder(context)
                    .setView(view)
                    .show();

            title = view.findViewById(R.id.title);
            close = view.findViewById(R.id.close);
            name = view.findViewById(R.id.name);
            spent_amt = view.findViewById(R.id.spent);
            share_by = view.findViewById(R.id.share_by);
            due_amt = view.findViewById(R.id.due);
            delete = view.findViewById(R.id.delete);

            double share_amt = Double.parseDouble(spent.get(pos)) - Double.parseDouble(due.get(pos));
            share_amt = Math.abs(share_amt);

            if (Double.parseDouble(due.get(pos)) <= 0) {
                due_amt.setTextColor(Color.RED);
            } else {
                due_amt.setTextColor(ContextCompat.getColor(context, R.color.Green));
            }

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (helper.get_spent_amt(t_id, m_id.get(pos)) == 0 && helper.get_due_amt(t_id, m_id.get(pos)) == 0) {
                        dialog.dismiss();
                        AlertDialog dialog = new AlertDialog.Builder(context)
                                .setTitle("Delete")
                                .setMessage("Do you want to delete this person from the trip ?")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        delete_a_person();
                                    }
                                })
                                .setNegativeButton("No", null)
                                .show();
                        dialog.show();

                    } else {
                        android.app.AlertDialog dialog = new android.app.AlertDialog.Builder(context)
                                .setMessage("This person has involved in some of the expenses so the person cannot be deleted")
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                }).show();
                        dialog.setCancelable(false);
                        dialog.setCanceledOnTouchOutside(false);
                    }
                }
            });
            DecimalFormat format = new DecimalFormat("#.00");
            title.setText(names.get(pos).substring(0, 1).toUpperCase());
            name.setText(names.get(pos));
            spent_amt.setText("Amount Spent " + currency + " " + spent.get(pos));
            due_amt.setText("Due Amount " + currency + " " + format.format(Float.parseFloat(due.get(pos))));
            share_by.setText("Share By Amount " + currency + " " + format.format(share_amt));

            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.show();

        }

        private void delete_a_person() {
            helper.delete_a_person(t_id, m_id.get(pos), count.get(pos));
            fetch_data();
            notifyDataSetChanged();
        }

        @Override
        public int getItemCount() {
            return names.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView names, count, spent;
            public RelativeLayout layout;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                names = (TextView) itemView.findViewById(R.id.name);
                count = (TextView) itemView.findViewById(R.id.count);
                spent = (TextView) itemView.findViewById(R.id.spent);
                layout = (RelativeLayout) itemView.findViewById(R.id.main_layout);
            }
        }

    }

}