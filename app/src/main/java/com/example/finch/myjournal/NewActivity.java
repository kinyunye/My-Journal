package com.example.finch.myjournal;

import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.finch.myjournal.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class NewActivity extends AppCompatActivity {

    private EditText jTitle, jDesc;
    private Button jBtn;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mJournalDatabase;
    private String saveCurrentTime, saveCurrentDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new);
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm");
        saveCurrentTime = currentTime.format(calendar.getTime());

        Calendar cal_date = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
        saveCurrentDate = currentDate.format(cal_date.getTime());




        jTitle = (EditText) findViewById(R.id.title);
        jDesc = (EditText) findViewById(R.id.desc);
        jBtn = (Button) findViewById(R.id.journal_btn);


        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mJournalDatabase = FirebaseDatabase.getInstance().getReference().child("myjournal");





        jBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = jTitle.getText().toString().trim();
                String desc = jDesc.getText().toString().trim();

                if (!TextUtils.isEmpty(title) && !TextUtils.isEmpty(desc)) {
                    createJournal(title, desc);

                } else {
                    Snackbar.make(view, "Fill in empty fields", Snackbar.LENGTH_SHORT).show();
                }


            }
        });

    }

    private void createJournal(String title, String desc) {


        if (null != mFirebaseDatabase.getApp()) {
            final DatabaseReference newJournalRef = mJournalDatabase.push();

            final Map journalMap = new HashMap();
            journalMap.put("title", title);
            journalMap.put("content", desc);
            journalMap.put("date", saveCurrentDate);
            journalMap.put("time", saveCurrentTime);


           ;


            Thread mainThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    newJournalRef.setValue(journalMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(NewActivity.this, "Successfully Added", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(NewActivity.this, "ERROR!!" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }

                        }
                    });

                }
            });
            mainThread.start();

        }
    }
}