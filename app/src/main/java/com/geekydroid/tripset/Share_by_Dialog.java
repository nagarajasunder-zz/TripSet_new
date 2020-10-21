package com.geekydroid.tripset;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Share_by_Dialog extends AppCompatDialogFragment {

    private View view;
    private RecyclerView recyclerView;
    private CheckBox share_by_everyone;
    private ArrayList<String> list;
    private dialog_re_adapter adapter;
    private MydatabaseHelper helper;
    private Value_passer passer;
    private boolean flag = true;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            passer = (Value_passer) context;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }

    private String t_id;

    public Share_by_Dialog(String t_id) {
        this.t_id = t_id;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_layout, null);
        final AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setView(view)
                .setPositiveButton("Ok", null)
                .show();

        list = new ArrayList<>();
        helper = new MydatabaseHelper(getActivity());
        list.addAll(helper.get_trip_members_names(t_id));


        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        share_by_everyone = view.findViewById(R.id.share_by_everyone);

        adapter = new dialog_re_adapter(list, getActivity(),share_by_everyone);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();


        Button button = dialog.getButton(Dialog.BUTTON_POSITIVE);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (adapter.get_selected_list().size() > 0) {
                    passer.get_names(adapter.get_selected_list(), adapter.getShare_by());
                    dialog.dismiss();
                } else {
                    Toast.makeText(getActivity(), "Please select atleast a person", Toast.LENGTH_SHORT).show();
                }
            }
        });

        share_by_everyone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.random_method(flag);
                flag = !flag;
            }
        });

        return dialog;
    }

    public interface Value_passer {
        public void get_names(ArrayList<String> list, String share_by);
    }

}
