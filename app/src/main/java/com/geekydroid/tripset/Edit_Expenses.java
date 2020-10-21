package com.geekydroid.tripset;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

public class Edit_Expenses extends AppCompatActivity implements Share_by_Dialog.Value_passer, Category_dialog_class.Selected_item {

    private TextView share_by_d, shared_by, category;
    private TextInputLayout desc, amt;
    private Spinner spent_by;
    private Button save;
    private ArrayList<String> m_id, names, spent_list, due_list, share_by;
    private String t_id, Share_by = "";
    private ArrayAdapter<String> names_adapter;
    private String Spent_by = "", Amt = "", Desc = "", Category = "", Date = "";
    private double tot_amt;
    private MydatabaseHelper helper;
    private int session_id;
    private ImageView add_category;
    private Category_dialog_class dialog_class;
    private Share_by_Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit__expenses);
        getIntents();
        setUI();
        assign_values();
        fetch_data();
        category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_class = new Category_dialog_class();
                dialog_class.show(getSupportFragmentManager(), "open");
            }
        });

        share_by_d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new Share_by_Dialog(t_id);
                dialog.show(getSupportFragmentManager(), "open");
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                get_values();
                if (validate()) {
                    helper.delete_spent_instances(String.valueOf(session_id));
                    update_spent_and_due_amount();
                    fetch_data();
                    int index = names.indexOf(Spent_by);
                    double share_amt = Double.parseDouble(Amt) / share_by.size();
                    long result = helper.update_spent_history(String.valueOf(session_id), t_id, m_id.get(index), names.get(index), Amt, Category, Desc, Share_by);
                    for (int i = 0; i < share_by.size(); i++) {
                        int s_index = names.indexOf(share_by.get(i));
                        String spent_on = String.valueOf(m_id.get(s_index));
                        helper.add_share(String.valueOf(session_id), t_id, spent_on, m_id.get(index), share_amt);
                        if (!share_by.get(i).equals(names.get(index))) {
                            double due = helper.get_due_amt(t_id, m_id.get(names.indexOf(share_by.get(i))));
                            helper.add_new_expense(Double.parseDouble(spent_list.get(s_index)), due, m_id.get(s_index), t_id);
                        }
                    }
                    double due = helper.get_due_amt(t_id, m_id.get(index));
                    double spent = Double.parseDouble(spent_list.get(index)) + Double.parseDouble(Amt);
                    helper.add_new_expense(spent, due, m_id.get(index), t_id);
                    helper.update_total_amt(t_id, helper.get_trip_total_amt(t_id));
                    finish();
                }
            }
        });
    }

    private void update_spent_and_due_amount() {
        for (int i = 0; i < m_id.size(); i++) {
            helper.add_new_expense(helper.get_spent_amt(t_id, m_id.get(i)), helper.get_due_amt(t_id, m_id.get(i)), m_id.get(i), t_id);
        }
    }

    private void fetch_data() {
        spent_list.clear();
        due_list.clear();
        Cursor cursor;
        cursor = helper.fetch_all_members(Integer.parseInt(t_id));
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                spent_list.add(cursor.getString(3));
                due_list.add(cursor.getString(4));
                m_id.add(cursor.getString(0));
                names.add(cursor.getString(2));
            }
        }
    }

    private void assign_values() {
        desc.getEditText().setText(Desc);
        category.setText("Category Selected: " + Category);
        amt.getEditText().setText(Amt);
    }

    private void getIntents() {
        t_id = getIntent().getStringExtra("t_id");
        Desc = getIntent().getStringExtra("desc");
        Date = getIntent().getStringExtra("date");
        session_id = Integer.parseInt(getIntent().getStringExtra("s_id"));
        Category = getIntent().getStringExtra("category");
        Spent_by = getIntent().getStringExtra("spent_by");
        Share_by = getIntent().getStringExtra("share_by");
        Amt = getIntent().getStringExtra("amt");
    }

    private void setUI() {
        helper = new MydatabaseHelper(this);
        amt = (TextInputLayout) findViewById(R.id.amt);
        desc = (TextInputLayout) findViewById(R.id.desc);
        spent_by = (Spinner) findViewById(R.id.spent_by);
        save = (Button) findViewById(R.id.save);

        names = new ArrayList<>();
        names.addAll(helper.get_trip_members_names(t_id));
        ArrayList<String> s_names = new ArrayList<>();
        s_names.addAll(names);
        names_adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, s_names);
        spent_by.setAdapter(names_adapter);


        tot_amt = helper.get_trip_total_amt(t_id);

        share_by = new ArrayList<>();

        share_by_d = (TextView) findViewById(R.id.share_by_d);
        shared_by = (TextView) findViewById(R.id.share_by);
        category = (TextView) findViewById(R.id.category);

        add_category = (ImageView) findViewById(R.id.add_category);

        share_by = new ArrayList<>();

        spent_list = new ArrayList<>();
        due_list = new ArrayList<>();
        m_id = new ArrayList<>();

    }

    @Override
    public void get_names(ArrayList<String> list, String Share_By) {
        share_by.clear();
        Share_by = "";
        share_by.addAll(list);
        Share_by = Share_By;
        if (list.size() > 0) {
            shared_by.setVisibility(View.VISIBLE);
            shared_by.setText("Shared by " + Share_by);
        } else {
            shared_by.setVisibility(View.GONE);
        }
    }

    @Override
    public void selected_item(String Category_) {
        Category = Category_;
        category.setText("Category Selected: " + Category);
    }

    private void get_values() {
        Spent_by = spent_by.getSelectedItem().toString();
        Amt = amt.getEditText().getText().toString();
        Desc = desc.getEditText().getText().toString().replace("\n", "");
    }

    private boolean validate() {
        if (Desc.trim().isEmpty()) {
            Toast.makeText(this, "Please enter a description(Reason) for your expense", Toast.LENGTH_SHORT).show();
            return false;
        } else if (Category.trim().isEmpty()) {
            Toast.makeText(this, "Please select a category", Toast.LENGTH_SHORT).show();
            return false;
        } else if (Spent_by.trim().isEmpty()) {
            Toast.makeText(this, "Please select a person who spent the expense", Toast.LENGTH_SHORT).show();
            return false;
        } else if (Share_by.trim().isEmpty()) {
            Toast.makeText(this, "Please Select the shares", Toast.LENGTH_SHORT).show();
            return false;
        } else if (Amt.trim().isEmpty()) {
            Toast.makeText(this, "Please enter the expense amount", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

}