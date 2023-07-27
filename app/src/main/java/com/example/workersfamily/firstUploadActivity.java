package com.example.workersfamily;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;

import android.net.Uri;

import android.os.Bundle;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class firstUploadActivity extends AppCompatActivity {
    ImageView uploadImage;
    Button saveButton;
    EditText uploadName, uploadNumber,uploadAdd,uploadWork;
    String imageUrl;
    String value;
    Spinner uploadClint;
    Uri uri;
    int i=0;
    FirebaseAuth mAuth;
    String uid;
    String[] arrClint = {"none","want work","Hiring"};

    protected void onStart() {
        super.onStart();
        mAuth=FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
FirebaseDatabase.getInstance().getReference("workers").addValueEventListener(new ValueEventListener() {
    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        for (DataSnapshot UidSnapshot: snapshot.getChildren()) {
            if (user.getUid().equals(UidSnapshot.getKey() )) {
                Intent home = new Intent(getApplicationContext(), Home_activity.class);
                startActivity(home);
                finish();
            }

        }
    }
    @Override
    public void onCancelled(@NonNull DatabaseError error) {
    }
});
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        uploadAdd = findViewById(R.id.uploadAdd);
        uploadWork = findViewById(R.id.uploadWork);
        uploadImage = findViewById(R.id.uploadImage);
        uploadName = findViewById(R.id.uploadName);
        uploadNumber = findViewById(R.id.uploadNumber);
        uploadClint = findViewById(R.id.spinner);
        saveButton = findViewById(R.id.saveButton);
        uid=FirebaseAuth.getInstance().getCurrentUser().getUid();
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(firstUploadActivity.this,R.layout.spinner_layout,arrClint);
        spinnerAdapter.setDropDownViewResource(R.layout.spinner_layout);
        uploadClint.setAdapter(spinnerAdapter);
        uploadClint.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                value =parent.getItemAtPosition(position).toString();
                Toast.makeText(firstUploadActivity.this, value, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if(result.getResultCode()== Activity.RESULT_OK){
                            Intent data = result.getData();
                            uri =data.getData();
                            uploadImage.setImageURI(uri);
                            i=1;
                        }else{
                            Toast.makeText(firstUploadActivity.this, "no image selected", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPicker = new Intent(Intent.ACTION_PICK);
                photoPicker.setType("image/*");
                activityResultLauncher.launch(photoPicker);
            }
        });
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(CheckAllFields()) {
                    saveData();
                }
            }
        });
    }
    public void saveData(){
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("workers")
                .child(uri.getLastPathSegment());
        AlertDialog.Builder builder = new AlertDialog.Builder(firstUploadActivity.this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        AlertDialog dialog = builder.create();
        dialog.show();

        storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask= taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isComplete());
                Uri urlImage = uriTask.getResult();
                imageUrl = urlImage.toString();
                uploadData();

                dialog.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialog.dismiss();
            }
         });
    }
    public void uploadData(){
        String name = uploadName.getText().toString();
        String work = uploadWork.getText().toString();
        String number= uploadNumber.getText().toString();
        String address= uploadAdd.getText().toString();
        String clint=value;

        DataClass dataClass = new DataClass(imageUrl,name,work,number,address,clint);


            FirebaseDatabase.getInstance().getReference("workers").child(uid).child("mainUser")

                    .setValue(dataClass).addOnCompleteListener(new OnCompleteListener  <Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(firstUploadActivity.this, "saved", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(firstUploadActivity.this,Home_activity.class);
                            startActivity(intent);
                            finish();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(firstUploadActivity.this,e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                        }
                    });
    }
    private boolean CheckAllFields(){
        if (uploadName.length()==0){
            uploadName.setError("inter your name");
            return false;
        }
        if(uploadWork.length()==0){
            uploadWork.setError("inter your work");
            return false;
        }
        if(uploadAdd.length()==0){
            uploadAdd.setError("inter your add");
            return false;
        }
        if(uploadNumber.length()<9){
            uploadNumber.setError("inter your number");
            return false;
        }
        if(i==0){
            Toast.makeText(this, "Select your profile image", Toast.LENGTH_SHORT).show();
            return false;
        }
        return  true;
    }
}