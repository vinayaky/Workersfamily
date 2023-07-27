package com.example.workersfamily;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class profileActivity extends AppCompatActivity {
    RecyclerView profile;
    FloatingActionButton fab;
    List<DataClass> profilesArrayList;
    ValueEventListener eventListener;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    String currentUser;
    Boolean activity = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
         profile = findViewById(R.id.Profile);
        fab = findViewById(R.id.fab);
        currentUser = mAuth.getCurrentUser().getUid();

        GridLayoutManager gridLayoutManager = new GridLayoutManager(profileActivity.this,1);
        profile.setLayoutManager(gridLayoutManager);

        AlertDialog.Builder profileBuilder = new AlertDialog.Builder(this);
        profileBuilder.setCancelable(false);
        profileBuilder.setView(R.layout.progress_layout);
        AlertDialog profileDialog = profileBuilder.create();
        profileDialog.show();

         profilesArrayList = new ArrayList<>();

        profileAdapter pAdapter = new profileAdapter(this,profilesArrayList);
        profile.setAdapter(pAdapter);

        DatabaseReference databaseReferenceProfile = FirebaseDatabase.getInstance().getReference("workers").child(currentUser);
        profileDialog.show();

         eventListener = databaseReferenceProfile.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                profilesArrayList.clear();
                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    DataClass dataClass= dataSnapshot.getValue(DataClass.class);
                     dataClass.setKey(dataSnapshot.getKey());
                    profilesArrayList.add(dataClass);
                }
                pAdapter.notifyDataSetChanged();
                profileDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                profileDialog.dismiss();
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                        Intent home = new Intent(getApplicationContext(), uploadActivity.class);
                        startActivity(home);
                        finish();

            }

        });
    }
    }
