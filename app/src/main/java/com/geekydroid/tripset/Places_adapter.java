package com.geekydroid.tripset;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Places_adapter extends RecyclerView.Adapter<Places_adapter.ViewHolder> {

    private Context context;
    private ArrayList<String> places, p_id;
    private MydatabaseHelper helper;
    private String s;

    public Places_adapter(Context context, ArrayList<String> places, ArrayList<String> p_id) {
        this.context = context;
        this.places = places;
        this.p_id = p_id;
        helper = new MydatabaseHelper(context);
    }

    @NonNull
    @Override
    public Places_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.places_adapter_layout, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Places_adapter.ViewHolder holder, final int position) {
        holder.place.setText(places.get(position));
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
                                return true;
                            case R.id.Delete:
                                show_del_dialog(position);
                                return true;
                            default:
                                return false;
                        }
                    }
                });
            }

            private void show_del_dialog(final int position) {
                final AlertDialog dialog = new AlertDialog.Builder(context)
                        .setTitle("Delete")
                        .setMessage("Do you want to delete this place?")
                        .setPositiveButton("Yes", null)
                        .setNegativeButton("No", null)
                        .show();

                Button button = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        long result;
                        result = helper.delete_a_place(p_id.get(position));
                        places.remove(position);
                        p_id.remove(position);
                        notifyDataSetChanged();
                        if (result != -1) {
                            Toast.makeText(context, "Deleted successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Deletion failed", Toast.LENGTH_SHORT).show();
                        }
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return places.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView place;
        public ImageView more;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            place = (TextView) itemView.findViewById(R.id.place);
            more = (ImageView) itemView.findViewById(R.id.more);
        }
    }
}
