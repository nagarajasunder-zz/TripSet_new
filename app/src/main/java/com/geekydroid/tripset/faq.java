package com.geekydroid.tripset;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class faq extends AppCompatActivity {

    private RecyclerView recyclerView;
    private adapter adapter;
    private ArrayList<Faq> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);
        setUI();
        inflate_data();
    }

    private void inflate_data() {
        list.add(new Faq("What is the use of Tripset?","Tripset, mainly focuses on your travel expenses whether you are a solo/group traveller the need for managing your expenses is necessary. Tripset helps you in managing your travel expenses which indeed helps a lot in your travel adventures."));
        list.add(new Faq("What is default currency?","Tripset is officialy available all over the world and different countries use different currencies so default currency is something that you use for your travel. It all depends on your country or the place you travel."));
        list.add(new Faq("How to change the default currency?","It is so simple! Just go to Settings>default currency and choose yours."));
        list.add(new Faq("How to add more members for my trip while creating it?","Whenever you create a new trip you were asked to add atleast one member for your trip, you can add members for your trip by clicking the button add members. You can add more members by clicking add member button again"));
        list.add(new Faq("How do I add a new member after the creation/in between the trip ?","You can add new members in between the trip by opening your trip and you can find a menu in the top write corner of your trip tab and click add member and you can add a new member for your trip."));
        list.add(new Faq("How does the share by works while we add a new expense?","Share by is used to split and tally your expenses while you travel the person you select for share by while you add new expense will share the expenses with the person who spent the expense sometimes without the person who spent the expense"));
        list.add(new Faq("How to add new Category?","You can add a new category at the time of creating a new expense by clicking the add icon or you can do it by Settings>Add a category."));


        adapter = new adapter(list);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void setUI() {
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
    }

    public class Faq
    {
        private String question,answer;
        private boolean isExpanded;
        public Faq(String question, String answer) {
            this.question = question;
            this.answer = answer;
            isExpanded = false;
        }

        public String getQuestion() {
            return question;
        }

        public void setQuestion(String question) {
            this.question = question;
        }

        public String getAnswer() {
            return answer;
        }

        public void setAnswer(String answer) {
            this.answer = answer;
        }

        public boolean isExpanded() {
            return isExpanded;
        }

        public void setExpanded(boolean expanded) {
            isExpanded = expanded;
        }
    }

    public class adapter extends RecyclerView.Adapter<adapter.ViewHolder>
    {
        private ArrayList<Faq> list;

        public adapter(ArrayList<Faq> list) {
            this.list = list;
        }

        @NonNull
        @Override
        public adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.faq_layout,null);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final adapter.ViewHolder holder, final int position) {
            holder.question.setText(list.get(position).getQuestion());
            holder.answer.setText(list.get(position).getAnswer());
            holder.main_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean isExpanded = list.get(position).isExpanded();
                    holder.layout.setVisibility(isExpanded? View.VISIBLE:View.GONE);
                    list.get(position).setExpanded(!list.get(position).isExpanded());
                }
            });
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder
        {
            public TextView question,answer;
            public LinearLayout layout,main_layout;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                question = itemView.findViewById(R.id.question);
                answer = itemView.findViewById(R.id.answer);
                layout = itemView.findViewById(R.id.expandable_layout);
                main_layout = itemView.findViewById(R.id.main_layout);
            }
        }
    }


}