package com.example.finch.myjournal;


import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;


@SuppressWarnings("unchecked")
public class HomeActivity extends AppCompatActivity {
    private FirebaseRecyclerAdapter<JournalModel, HomeActivity.JournalViewHolder> mRecyclerViewAdapter;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.create:
                Intent i = new Intent(this,NewActivity.class);
                this.startActivity(i);
                return true;
            default:
        }

        switch (item.getItemId())
        {

            case R.id.log_out:
                FirebaseAuth.getInstance().signOut();
                finish();
                Intent intentLogOut = new Intent(this,MainActivity.class);
                this.startActivity(intentLogOut);
                break;


        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);




        DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("myjournal");
        mDatabaseReference.keepSynced(true);

        RecyclerView mRecyclerView = findViewById(R.id.myRecycleView);



        DatabaseReference personsRef = FirebaseDatabase.getInstance().getReference().child("myjournal");
        Query personsQuery = personsRef.orderByKey();

        mRecyclerView.hasFixedSize();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions personsOptions = new FirebaseRecyclerOptions.Builder<JournalModel>().
                setQuery(personsQuery, JournalModel.class).build();


        mRecyclerViewAdapter = new FirebaseRecyclerAdapter<JournalModel, JournalViewHolder>(personsOptions) {


            @Override
            protected void onBindViewHolder(@NonNull JournalViewHolder journalViewHolder, final int position, @NonNull final JournalModel model) {
                final String journalKey = getRef(position).getKey();

                journalViewHolder.setTitle(model.getTitle());
                journalViewHolder.setContent(model.getContent());
                journalViewHolder.setDate(model.getDate());
                //journalViewHolder.setTime(model.getTime());




                journalViewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(getApplicationContext(), SingleActivity.class);
                        intent.putExtra("journalKey", journalKey);
                        startActivity(intent);

                    }
                });
            }

            @NonNull
            @Override
            public JournalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.journal_row, parent, false);

                return new JournalViewHolder(view);
            }
        };

        mRecyclerView.setAdapter(mRecyclerViewAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        mRecyclerViewAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        mRecyclerViewAdapter.stopListening();


    }

    public class JournalViewHolder extends RecyclerView.ViewHolder{
        final View mView;
        JournalViewHolder(View itemView){
            super(itemView);
            mView = itemView;
        }
        void setTitle(String title){
            TextView post_title = mView.findViewById(R.id.post_title);
            post_title.setText(title);
        }
        void setContent(String content){
            TextView post_desc = mView.findViewById(R.id.post_desc);
            post_desc.setText(content);
        }


        void setDate(String timestamp) {
            TextView post_date = mView.findViewById(R.id.post_date);
            post_date.setText(timestamp);
        }

        public void setTime(String time) {
            TextView post_time = mView.findViewById(R.id.post_time);
            post_time.setText(time);
        }
    }
}
