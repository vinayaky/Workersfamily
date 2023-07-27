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
import android.renderscript.ScriptGroup;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class updateActivity extends AppCompatActivity {
    EditText updateName,updateWork,updateNumber,updateAdd;
    ImageView updateImage;
    Spinner updateClint;
    String imageUrl;
    String value;
    Uri uri;
    FirebaseAuth mAuth;
    Task<Void> databaseReference;
    Button updateButton;
    String key;
    StorageReference storageReference;
    String oldImageURl;
    String uid;
    DataClass dataClass1;
    String[] arrClint = {"none","want work","Hiring"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        updateAdd = findViewById(R.id.updateAdd);
        updateClint = findViewById(R.id.updateSpinner);
        updateImage = findViewById(R.id.updateImage);
        updateName = findViewById(R.id.updateName);
        updateNumber =findViewById(R.id.updateNumber);
        updateWork = findViewById(R.id.updateWork);
        updateButton = findViewById(R.id.updateButton);
        uid= FirebaseAuth.getInstance().getCurrentUser().getUid();
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            updateName.setText(bundle.getString("name"));
            updateWork.setText(bundle.getString("work"));
            updateAdd.setText(bundle.getString("add"));
            key = bundle.getString("key");
            oldImageURl= bundle.getString("oldImage");
            updateNumber.setText(bundle.getString("number"));
            Glide.with(this).load(bundle.getString("image")).into(updateImage);
        }

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(updateActivity.this,R.layout.spinner_layout,arrClint);
        spinnerAdapter.setDropDownViewResource(R.layout.spinner_layout);
        updateClint.setAdapter(spinnerAdapter);
        updateClint.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                value =parent.getItemAtPosition(position).toString();
                Toast.makeText(updateActivity.this, value, Toast.LENGTH_SHORT).show();
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
                            updateImage.setImageURI(uri);
                        }else{
                            Toast.makeText(updateActivity.this, "no image selected", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );


        updateImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPicker = new Intent(Intent.ACTION_PICK);
                photoPicker.setType("image/*");
                activityResultLauncher.launch(photoPicker);

            }
        });
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = updateName.getText().toString();
                String work = updateWork.getText().toString();
                String number= updateNumber.getText().toString();
                String address= updateAdd.getText().toString();
                String clint=value;
                HashMap<String,Object> objData =new HashMap<>();
                objData.put("dataName",name);
                objData.put("dataWork",work);
                objData.put("dataNumber",number);
                objData.put("dataAdd",address);
                objData.put("dataClint",clint);
                FirebaseDatabase.getInstance().getReference("workers").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(key)
                        .updateChildren(objData).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(updateActivity.this, "saved", Toast.LENGTH_SHORT).show();
                            }
                        });

               updateData();
                Intent intent =new Intent(updateActivity.this,profileActivity.class);
                startActivity(intent);
                finish();
            }
        });
        
    }
    public void updateData(){
      try {
          storageReference = FirebaseStorage.getInstance().getReference().child("workers")
                  .child(uri.getLastPathSegment());
      }catch(NullPointerException e){
             return;
         }
        AlertDialog.Builder builder = new AlertDialog.Builder(updateActivity.this);
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
                HashMap<String,Object> objImage =new HashMap<>();
                objImage.put("dataImage",imageUrl);
                FirebaseDatabase.getInstance().getReference("workers").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(key)
                        .updateChildren(objImage).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                StorageReference reference =FirebaseStorage.getInstance().getReferenceFromUrl(oldImageURl);
                                reference.delete();
                                Toast.makeText(updateActivity.this, "saved", Toast.LENGTH_SHORT).show();

                            }
                        });

                dialog.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialog.dismiss();
            }
        });

    }
}