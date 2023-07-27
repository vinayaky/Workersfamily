package com.example.workersfamily;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class profileDetailActivity extends AppCompatActivity {
    String name,work,add,number,clint,image ="";
    String key="";
    String imageUrl="";
    BottomNavigationView bottomNavigationView;
    private static final String ROOT_FRAGNEMENT_TAG = String.valueOf(new mainFragment());
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_deatail);
        bottomNavigationView=(BottomNavigationView) findViewById(R.id.bottomNavigationView);

        Bundle bundle =getIntent().getExtras();
        if (bundle != null) {
            name=bundle.getString("name");
            work=bundle.getString("work");
            add=bundle.getString("add");
            number=bundle.getString("number");
            key = bundle.getString("key");
            imageUrl = bundle.getString("image");
            clint=bundle.getString("clint");
            image=bundle.getString("image");

        }
        loadFragment(new profileDetail());
       bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
           @Override
           public boolean onNavigationItemSelected(@NonNull MenuItem item) {

               switch (item.getItemId()){
                   case (R.id.pDetail):

                       loadFragment(new profileDetail());

                       break;
                   case (R.id.callHistory):
                       loadFragment(new callHistory());

                       break;}
               return true;
           }
       });

         }
    public void loadFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction =fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.detailsContent,fragment);

        Bundle bundle1 = new Bundle();
        bundle1.putString("name",name);
        bundle1.putString("work",work);
        bundle1.putString("add",add);
        bundle1.putString("clint",clint);
        bundle1.putString("number",number);
        bundle1.putString("image",image);
        bundle1.putString("key",key);
        bundle1.putString("imageUrl",imageUrl);
        fragment.setArguments(bundle1);

        fragmentTransaction.commit();
    }

    }