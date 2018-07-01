package com.example.finch.myjournal;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SingleActivity extends AppCompatActivity {

    TextView mTitle, mContent;
    Button editBtn, delBtn;
    private DatabaseReference clickRef;
    private FirebaseAuth mAuth;
    private String journalKey, currentUserID, databaseID, desc,title;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.single_menu, menu);
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
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single);

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();

        journalKey = getIntent().getExtras().getString("journalKey").toString();
        clickRef = FirebaseDatabase.getInstance().getReference().child("myjournal").child(journalKey);

        mContent = (TextView) findViewById(R.id.mContent);
        mTitle= (TextView) findViewById(R.id.mTtile);
        editBtn = (Button) findViewById(R.id.edit_btn);
        delBtn = (Button) findViewById(R.id.delete_btn);

        clickRef.addValueEventListener(new ValueEventListener() {
            @Override

            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    title = dataSnapshot.child("title").getValue().toString();
                    desc = dataSnapshot.child("content").getValue().toString();


                    mTitle.setText(title);
                    mContent.setText(desc);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditStory(desc);
            }
        });


        delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteStory();
            }
        });
    }

    private void EditStory(final String desc) {
        AlertDialog.Builder builder = new AlertDialog.Builder(SingleActivity.this);
        builder.setTitle("Edit Story");

        //final EditText inputTitle = new EditText(SingleActivity.this);
        final EditText inputContent = new EditText(SingleActivity.this);
        //inputTitle.setText(title);
        inputContent.setText(desc);
        //builder.setView(inputTitle);
        builder.setView(inputContent);
        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
              //  clickRef.child(title).setValue(inputTitle .getText().toString());
                clickRef.child(desc).setValue(inputContent.getText().toString());
                Toast.makeText(SingleActivity.this, "Story Updated Successfully", Toast.LENGTH_SHORT).show();


            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();

            }
        });

        Dialog dialog = builder.create();
        dialog.show();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.holo_green_light);
    }

    private void DeleteStory() {
        clickRef.removeValue();

        goToHomeActivity();
        Toast.makeText(this, "Deleted successfully", Toast.LENGTH_SHORT).show();
    }

    private void goToHomeActivity() {
        Intent homeIntent = new Intent(SingleActivity.this, HomeActivity.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(homeIntent);
        finish();
    }
}
