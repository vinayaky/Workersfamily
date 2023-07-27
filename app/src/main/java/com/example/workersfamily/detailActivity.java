package com.example.workersfamily;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Random;

public class detailActivity extends AppCompatActivity {
     TextView name , work, add;
     ImageView image;
     private static final int Request_call=1;
     FloatingActionButton callFab;
     String  number,key,uid,userValue;
     String callerId;
     DatabaseReference callReference;
     AdView adView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        image = findViewById(R.id.detailImage);
        name = findViewById(R.id.detailName);
        work = findViewById(R.id.detailWork);
        add =  findViewById(R.id.detailAdd);
        callFab= findViewById(R.id.callFab);
        adView= findViewById(R.id.adView);

        userValue= "1";
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            name.setText(bundle.getString("name"));
            work.setText(bundle.getString("work"));
            add.setText(bundle.getString("add"));
            number= bundle.getString("number");
             key = bundle.getString("key");
             uid = bundle.getString("userUid");
            Glide.with(this).load(bundle.getString("image")).into(image);

        }
        callFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + number));
//call History reference
              callerId=FirebaseAuth.getInstance().getCurrentUser().getUid();


             try {
              callReference=FirebaseDatabase.getInstance().getReference("callHistory");
              callReference.addValueEventListener(new ValueEventListener() {
                  @Override
                  public void onDataChange(@NonNull DataSnapshot snapshot) {
                      SimpleDateFormat dateFormat= new SimpleDateFormat("E.LLL.yyyy , KK.mm.ss.aaa");
                      String cDate = dateFormat.format(Calendar.getInstance().getTime());
                      callReference.child(uid).child(key).child(callerId).setValue(cDate);

                  }

                  @Override
                  public void onCancelled(@NonNull DatabaseError error) {

                  }
              });

             }catch (NullPointerException e){
                 Toast.makeText(detailActivity.this, "not save", Toast.LENGTH_SHORT).show();
                 startActivity(intent);
             }

                startActivity(intent);
            }
        });

        //ADS SET ON DETAIL ACTIVITY
        MobileAds.initialize(this);
        AdRequest adRequest= new AdRequest.Builder().build();
        adView.loadAd(adRequest);

    }
}