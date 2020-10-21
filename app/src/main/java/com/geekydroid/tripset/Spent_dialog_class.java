package com.geekydroid.tripset;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Spent_dialog_class extends AppCompatDialogFragment {
    ArrayList<String> list;
    Spent_re_adapter adapter;
    Spent_passer spent_passer;
    String spent_by;

    public Spent_dialog_class(ArrayList<String> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.category_dialog_layout,null);
        RecyclerView recyclerView;
        TextView title;
        final AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setView(view)
                .setPositiveButton("Ok",null)
                .show();
        title = (TextView) view.findViewById(R.id.title);
        title.setText("Select a member");
        title.setVisibility(View.VISIBLE);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setVisibility(View.VISIBLE);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new Spent_re_adapter(list,getActivity());
        recyclerView.setAdapter(adapter);

        Button button = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spent_by = adapter.get_selected_item();
                if (spent_by.trim().isEmpty())
                {
                    Toast.makeText(getActivity(), "Please select the person who spent the expense", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    spent_passer.Passer(spent_by);
                    dialog.dismiss();
                }
            }
        });
        return dialog;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            spent_passer = (Spent_passer)context;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface Spent_passer
    {
         void Passer(String Spent_By);
    }
}
