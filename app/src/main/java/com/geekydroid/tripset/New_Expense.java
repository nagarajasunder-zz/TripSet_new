package com.geekydroid.tripset;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class New_Expense extends AppCompatActivity implements Share_by_Dialog.Value_passer, Category_dialog_class.Selected_item, Spent_dialog_class.Spent_passer {

    private TextView share_by_d, shared_by, category,spent_by_t;
    private TextInputLayout desc, amt;
    private Button save;
    private ArrayList<String> m_id, names, spent_list, due_list, share_by;
    private String t_id, Share_by = "";
    private String Spent_by = "", Amt = "", Desc = "", Category = "";
    private double tot_amt;
    private MydatabaseHelper helper;
    private int session_id;
    private ImageView add_category;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new__expense);
        getIntents();
        setUI();
        save.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {

                get_values();
                if (validate()) {
                    int index = names.indexOf(Spent_by);
                    double share_amt = Double.parseDouble(Amt) / share_by.size();

                    SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy hh:mm aa");
                    Date date = new Date();
                    long result = helper.add_spent_history(t_id, String.valueOf(m_id.get(index)),
                            names.get(index), Double.parseDouble(Amt), format.format(date), Category, Desc, Share_by);

                    session_id = helper.get_session_id();
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


                    Intent intent = new Intent(getApplicationContext(), Trip_expand.class);
                    intent.putExtra("t_id", t_id);
                    intent.putExtra("t_name", getIntent().getStringExtra("t_name"));
                    intent.putExtra("t_desc", getIntent().getStringExtra("t_desc"));
                    intent.putExtra("group_size", getIntent().getStringExtra("group_size"));
                    intent.putExtra("tot_amt", String.valueOf(helper.get_trip_total_amt(t_id)));
                    intent.putExtra("date", getIntent().getStringExtra("date"));
                    startActivity(intent);
                    finish();
                }
            }
        });

        share_by_d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Share_by_Dialog dialog = new Share_by_Dialog(t_id);
                dialog.show(getSupportFragmentManager(), "open");
            }
        });

        category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Category_dialog_class dialog_class = new Category_dialog_class();
                dialog_class.show(getSupportFragmentManager(), "open");
            }
        });

        add_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                New_Category_dialog dialog = new New_Category_dialog();
                dialog.show(getSupportFragmentManager(), "open");
            }
        });

        spent_by_t.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Spent_dialog_class dialog_class = new Spent_dialog_class(names);
                dialog_class.show(getSupportFragmentManager(),"Open");
            }
        });

    }

    private void get_values() {
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

    private void getIntents() {
        t_id = getIntent().getStringExtra("t_id");
        m_id = getIntent().getStringArrayListExtra("m_id_list");
        names = getIntent().getStringArrayListExtra("m_names_list");
        spent_list = getIntent().getStringArrayListExtra("spent_list");
        due_list = getIntent().getStringArrayListExtra("due_list");
    }

    private void setUI() {
        amt = (TextInputLayout) findViewById(R.id.amt);
        desc = (TextInputLayout) findViewById(R.id.desc);
        save = (Button) findViewById(R.id.save);



        helper = new MydatabaseHelper(this);
        tot_amt = helper.get_trip_total_amt(t_id);

        share_by = new ArrayList<>();

        share_by_d = (TextView) findViewById(R.id.share_by_d);
        shared_by = (TextView) findViewById(R.id.share_by);
        category = (TextView) findViewById(R.id.category);
        spent_by_t = (TextView) findViewById(R.id.spent_by_t);

        add_category = (ImageView) findViewById(R.id.add_category);

    }

    @Override
    public void get_names(ArrayList<String> list, String Shareby) {
        share_by.clear();
        Share_by = "";
        share_by.addAll(list);
        Share_by = Shareby;
        if (list.size() > 0) {
            shared_by.setVisibility(View.VISIBLE);
            shared_by.setText("Shared by " + Shareby);
        } else {
            shared_by.setVisibility(View.GONE);
        }
    }


    @Override
    public void selected_item(String Category_) {
        Category = Category_;
        category.setText("Category Selected: " + Category);
    }

    @Override
    public void Passer(String Spent_By) {
       Spent_by = Spent_By;
       spent_by_t.setText("Spent By: "+Spent_By);
    }
}