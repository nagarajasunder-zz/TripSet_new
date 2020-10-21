package com.geekydroid.tripset;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.airbnb.lottie.LottieAnimationView;

import java.util.ArrayList;

public class Startup extends AppCompatActivity {

    private ViewPager2 viewPager;
    private ArrayList<String> text;
    private ArrayList<Integer> lottie;
    private Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup);

        setUI();

    }

    private void setUI() {
        viewPager = findViewById(R.id.viewpager);
        text = new ArrayList<>();
        lottie = new ArrayList<>();

        text.add("Welcome to Tripset\nYour Travel Expense Manager");
        text.add("Tripset Manages Your Travel Expenses!!\nTravel More and Worry Less");
        text.add("Classify your expenses based on Categories and makes easy to split");

        lottie.add(R.raw.welcome);
        lottie.add(R.raw.vp1);
        lottie.add(R.raw.habit_track);

        adapter = new Adapter(text, lottie,Startup.this);
        viewPager.setAdapter(adapter);

    }

    class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

        private ArrayList<String> text;
        private ArrayList<Integer> lottie;
        private Activity activity;
        public Adapter(ArrayList<String> text, ArrayList<Integer> lottie,Activity activity) {
            this.text = text;
            this.lottie = lottie;
            this.activity = activity;
        }

        @NonNull
        @Override
        public Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.startup_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final Adapter.ViewHolder holder, int position) {
            holder.text.setText(text.get(position));
            holder.animationView.setAnimation(lottie.get(position));

            Log.d("TAG", "onBindViewHolder: "+position);
            switch (position) {
                case 0:
                    holder.dot_0.setImageResource(R.drawable.active_item);
                    break;
                case 1:
                    holder.dot_1.setImageResource(R.drawable.active_item);
                    break;
                case 2:
                    holder.dot_2.setImageResource(R.drawable.active_item);
                    holder.started.setVisibility(View.VISIBLE);
                    break;
            }

            holder.started.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences preferences = getSharedPreferences("startup",MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean("startup_anim",false);
                    editor.apply();
                    startActivity(new Intent(holder.itemView.getContext(),Home.class));
                    activity.finish();
                }
            });
        }

        @Override
        public int getItemCount() {
            return text.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView text = itemView.findViewById(R.id.text);
            LottieAnimationView animationView = itemView.findViewById(R.id.lottie);
            Button started = itemView.findViewById(R.id.started);
            ImageView dot_0 = itemView.findViewById(R.id.dot_0);
            ImageView dot_1 = itemView.findViewById(R.id.dot_1);
            ImageView dot_2 = itemView.findViewById(R.id.dot_2);

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
            }
        }
    }
}