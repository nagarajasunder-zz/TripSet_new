package com.geekydroid.tripset;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import java.util.ArrayList;

public class New_Category_dialog extends AppCompatDialogFragment {

    private View view;
    private EditText category;
    private String Category = "";
    private MydatabaseHelper helper;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        view  = LayoutInflater.from(getActivity()).inflate(R.layout.new_category_layout,null);
        helper = new MydatabaseHelper(getActivity());
        final AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle("Add new Category")
                .setPositiveButton("Add",null)
                .setNegativeButton("Cancel",null)
                .show();

        Button button = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Category = category.getText().toString();
                if (Category.trim().isEmpty())
                {
                    Toast.makeText(getActivity(), "Please enter a category", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    ArrayList<String> category  = new ArrayList<>();
                    category.addAll(helper.get_all_category());
                    Category = String.valueOf(Category.charAt(0)).toUpperCase()+Category.substring(1).replace("\n","");
                    if (category.indexOf(Category) == -1)
                    {
                        helper.add_a_category(Category);
                    }
                    dialog.dismiss();
                }
            }
        });

        category = (EditText) view.findViewById(R.id.category);
        return dialog;
    }
}
