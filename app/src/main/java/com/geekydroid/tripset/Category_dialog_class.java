package com.geekydroid.tripset;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Category_dialog_class extends AppCompatDialogFragment {

    private TextView title;
    private View view;
    private RecyclerView recyclerView;
    private Category_re_adapter adapter;
    private ArrayList<String> list;
    private MydatabaseHelper helper;
    private String category;
    private Selected_item selected_item;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        view = LayoutInflater.from(getActivity()).inflate(R.layout.category_dialog_layout, null);
        final AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setView(view)
                .setPositiveButton("Ok", null)
                .show();


        title = view.findViewById(R.id.title);
        title.setVisibility(View.VISIBLE);
        title.setText("Select a category");

        list = new ArrayList<>();
        helper = new MydatabaseHelper(getActivity());


        list.addAll(helper.get_all_category());
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setVisibility(View.VISIBLE);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapter = new Category_re_adapter(list, getActivity());
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        Button button = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                category = adapter.get_selected_item();
                if (category.trim().length() == 0) {
                    Toast.makeText(getActivity(), "Please select a category", Toast.LENGTH_SHORT).show();
                } else {
                    selected_item.selected_item(category);
                    dialog.dismiss();
                }
            }
        });
        return dialog;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        selected_item = (Selected_item) context;
    }

    public interface Selected_item {
        void selected_item(String category);
    }
}
