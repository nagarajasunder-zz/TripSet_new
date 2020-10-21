package com.geekydroid.tripset;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Random;

public class Trip_Expense_History_Adapter extends RecyclerView.Adapter<Trip_Expense_History_Adapter.ViewHolder> {

    private double tot_amt;
    private Context context;
    private MydatabaseHelper helper;
    private ArrayList<String> desc, amt_spent, date, category, spent_by, share_by, m_id, s_id;
    private String t_id;
    private TextView total_amt;
    private Activity activity;

    public Trip_Expense_History_Adapter(Context context, String t_id, ArrayList<String> desc, ArrayList<String> amt_spent, ArrayList<String> date, ArrayList<String> category, ArrayList<String> spent_by, ArrayList<String> share_by, ArrayList<String> m_id, ArrayList<String> s_id,TextView total_amt,Activity activity) {
        this.context = context;
        this.desc = desc;
        this.amt_spent = amt_spent;
        this.date = date;
        this.category = category;
        this.spent_by = spent_by;
        this.share_by = share_by;
        this.s_id = s_id;
        this.t_id = t_id;
        this.m_id = m_id;
        this.total_amt = total_amt;
        this.activity = activity;

        helper = new MydatabaseHelper(context);
    }



    @NonNull
    @Override
    public Trip_Expense_History_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.expense_history_layout, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Trip_Expense_History_Adapter.ViewHolder holder, final int position) {
        holder.spent_by.setText(spent_by.get(position));
        holder.category.setText(category.get(position));
        holder.desc.setText(desc.get(position));
        holder.amt.setText(amt_spent.get(position));
        holder.date.setText(date.get(position));
        holder.share_by.setText(share_by.get(position));

        holder.main_layout.setBackgroundColor(Color.argb(100,new Random().nextInt(100),new Random().nextInt(200),new Random().nextInt(100)));

        holder.more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(context, v);
                popupMenu.inflate(R.menu.expense_history_menu);
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.Edit:
                                Intent intent = new Intent(context,Edit_Expenses.class);
                                intent.putExtra("t_id",t_id);
                                intent.putExtra("desc",desc.get(position));
                                intent.putExtra("date",date.get(position));
                                intent.putExtra("s_id",s_id.get(position));
                                intent.putExtra("category",category.get(position));
                                intent.putExtra("spent_by",spent_by.get(position));
                                intent.putExtra("share_by",share_by.get(position));
                                intent.putExtra("amt",amt_spent.get(position));
                                context.startActivity(intent);
                                ((Activity)context).finish();
                                return true;
                            case R.id.Delete:
                                spent_by.remove(position);
                                category.remove(position);
                                desc.remove(position);
                                date.remove(position);
                                share_by.remove(position);
                                notifyItemRemoved(position);
                                helper.delete_spent_history(s_id.get(position));
                                tot_amt = helper.get_trip_total_amt(t_id);
                                helper.update_total_amt(t_id, tot_amt);
                                update_due_amt();
                                return true;
                            default:
                                return false;
                        }

                    }
                });
            }
        });
    }

    private void update_due_amt() {
        for (int i = 0; i < m_id.size(); i++) {
            double due_amt = helper.get_due_amt(t_id, m_id.get(i));
            double spent_amt = helper.get_spent_amt(t_id, m_id.get(i));
            helper.add_new_expense(spent_amt, due_amt, m_id.get(i), t_id);
            Toast.makeText(context, "Expense deleted Successfully..", Toast.LENGTH_SHORT).show();

            double tot_amt = helper.get_trip_total_amt(t_id);
            total_amt.setText("Total amount "+tot_amt);
            if (helper.get_trip_total_amt(t_id) == 0)
            {
                activity.finish();
            }
        }
    }

    @Override
    public int getItemCount() {
        return spent_by.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView desc, amt, date, category, spent_by, share_by;
        public ImageView more;
        public RelativeLayout main_layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            desc = (TextView) itemView.findViewById(R.id.desc);
            amt = (TextView) itemView.findViewById(R.id.amt_spent);
            date = (TextView) itemView.findViewById(R.id.date);
            category = (TextView) itemView.findViewById(R.id.category);
            spent_by = (TextView) itemView.findViewById(R.id.expense_by);
            share_by = (TextView) itemView.findViewById(R.id.share_by);
            more = (ImageView) itemView.findViewById(R.id.more);
            main_layout = (RelativeLayout) itemView.findViewById(R.id.main_layout);

        }
    }
}
