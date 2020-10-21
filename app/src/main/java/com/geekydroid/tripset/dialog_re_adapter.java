package com.geekydroid.tripset;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class dialog_re_adapter extends RecyclerView.Adapter<dialog_re_adapter.ViewHolder> {

    private ArrayList<String> list;
    private ArrayList<String> selected;
    private Context context;
    private String share_by = "";
    private ArrayList<dialog_re_adapter.ViewHolder> viewHolder;
    private CheckBox share_by_everyone;

    public dialog_re_adapter(ArrayList<String> list, Context context,CheckBox share_by_everyone) {
        this.list = list;
        this.context = context;
        selected = new ArrayList<>();
        viewHolder = new ArrayList<>();
        this.share_by_everyone = share_by_everyone;
    }


    @NonNull
    @Override
    public dialog_re_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dialog_re_layout, null);
        return new dialog_re_adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final dialog_re_adapter.ViewHolder holder, final int position) {

        viewHolder.add(holder);
        holder.name.setText(list.get(position));
        holder.check.setClickable(false);
        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.check.isChecked()) {
                    holder.check.setChecked(false);
                    selected.remove(list.get(position));
                    if (selected.size() != list.size())
                    {
                        share_by_everyone.setChecked(false);
                    }
                } else {
                    holder.check.setChecked(true);
                    selected.add(list.get(position));

                    if (selected.size() == list.size())
                    {
                        share_by_everyone.setChecked(true);
                    }
                }
            }


        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public CheckBox check;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            check = (CheckBox) itemView.findViewById(R.id.check);
        }
    }

    public ArrayList<String> get_selected_list() {
        share_by = "";
        if (selected.size() == getItemCount() && viewHolder.size() > 1) {
            share_by = "all";
        } else {
            for (int i = 0; i < selected.size(); i++) {
                share_by += selected.get(i);
                share_by += ",";
            }
        }
        return selected;
    }

    public String getShare_by() {
        if (share_by.equals("all")) {
            return share_by;
        } else {
            return share_by.substring(0, share_by.length() - 1);
        }

    }

    public void random_method(boolean flag) {

//        for (int i = 0; i < viewHolder.size(); i++) {
//            if (flag) {
//                viewHolder.get(i).name.setEnabled(false);
//                viewHolder.get(i).check.setEnabled(false);
//            } else {
//                viewHolder.get(i).name.setEnabled(true);
//                viewHolder.get(i).check.setEnabled(true);
//            }
//        }
//
//        if (flag) {
//            selected.addAll(list);
//            share_by = "all";
//        } else {
//            selected.clear();
//            share_by = "";
//        }
        share_by = "";
        selected.clear();

        for (int i = 0; i < viewHolder.size(); i++) {
            if (flag) {
                viewHolder.get(i).name.setEnabled(false);
//                viewHolder.get(i).check.setEnabled(false);
                viewHolder.get(i).check.setChecked(true);
            } else {
                viewHolder.get(i).name.setEnabled(true);
//                viewHolder.get(i).check.setEnabled(true);
                viewHolder.get(i).check.setChecked(false);
            }

        }

        if (flag) {
            selected.addAll(list);
            if (viewHolder.size() == 1) {
                share_by = list.get(0);
            }
        } else {
            selected.clear();
            share_by = "";
        }

    }
}
