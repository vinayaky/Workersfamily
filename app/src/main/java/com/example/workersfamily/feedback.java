package com.example.workersfamily;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class feedback extends AppCompatActivity {
EditText feedbackText;
DatabaseReference firebaseDatabase;
String uid;
Button sendFeedback;
ValueEventListener feedbackValueEventListener;
List<FeedbackClass> feedbackClassList;
feedbackAdapter feedbackAdapter;
RecyclerView feedbackRecycler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        feedbackText = findViewById(R.id.feedbackText);

        uid= FirebaseAuth.getInstance().getCurrentUser().getUid();
        sendFeedback=findViewById(R.id.feedbackButton);
        feedbackRecycler= findViewById(R.id.feedbackRecycler);
        firebaseDatabase  =  FirebaseDatabase.getInstance().getReference("feedback").child(uid);
      sendFeedback.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              String feedbackValue=  feedbackText.getText().toString();
              FeedbackClass setValue = new FeedbackClass(feedbackValue);
              firebaseDatabase.setValue(setValue).addOnCompleteListener(new OnCompleteListener<Void>() {
                  @Override
                  public void onComplete(@NonNull Task<Void> task) {
                      Toast.makeText(feedback.this, "feedback Send", Toast.LENGTH_SHORT).show();
                      Intent intent = new Intent(feedback.this,Home_activity.class);
                      startActivity(intent);

                  }
              });
          }
      });


      if(uid.equals("2IrdEePlaOUByJAsobG2GBhlaZl1")){
          feedbackRecycler.setLayoutManager(new LinearLayoutManager(this));

          AlertDialog.Builder builder = new AlertDialog.Builder(this);
          builder.setCancelable(false);
          builder.setView(R.layout.progress_layout);
          AlertDialog dialog = builder.create();
          dialog.show();
          feedbackClassList= new ArrayList<>();
          feedbackAdapter = new feedbackAdapter(this,feedbackClassList);
          feedbackRecycler.setAdapter(feedbackAdapter);
          DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("feedback");
          dialog.show();
          feedbackValueEventListener= databaseReference.addValueEventListener(new ValueEventListener() {
              @Override
              public void onDataChange(@NonNull DataSnapshot snapshot) {
                  feedbackClassList.clear();
                  for (DataSnapshot itemS : snapshot.getChildren()) {
                      FeedbackClass feedbackData = itemS.getValue(FeedbackClass.class);
                      feedbackClassList.add(feedbackData);
                  }
                  feedbackAdapter.notifyDataSetChanged();
                  dialog.dismiss();
              }

              @Override
              public void onCancelled(@NonNull DatabaseError error) {

              }
          });
      }

    }
}