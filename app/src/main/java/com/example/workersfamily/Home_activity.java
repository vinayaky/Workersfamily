package com.example.workersfamily;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public  class Home_activity extends AppCompatActivity {
    private static final String ROOT_FRAGNEMENT_TAG = String.valueOf(new mainFragment());
    DrawerLayout drawerLayout;
    boolean DoublePressToExit = false;

    NavigationView navigationView;
    String currentUserUid;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        drawerLayout = findViewById(R.id.home);
       currentUserUid = mAuth.getCurrentUser().getUid();
       navigationView = findViewById(R.id.navigationBar);


        loadFragment(new mainFragment(),0);
       navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
           @Override
           public boolean onNavigationItemSelected(@NonNull MenuItem item) {
               switch (item.getItemId()){
                   case (R.id.Home):
                   loadFragment(new mainFragment(),0);
                       Toast.makeText(Home_activity.this, "Home Activity", Toast.LENGTH_SHORT).show();
                   break;
                   case (R.id.profileMenu):
                 Intent intent = new Intent(Home_activity.this,profileActivity.class);
                 startActivity(intent);
                   Toast.makeText(Home_activity.this, "Profile Activity", Toast.LENGTH_SHORT).show();
                   break;
                   case (R.id.hiring):
                   loadFragment(new HiringFragment(),1);
                   Toast.makeText(Home_activity.this, "Hiring Activity", Toast.LENGTH_SHORT).show();
                   break;
                   case (R.id.logOut):
                   logoutMenu(Home_activity.this);
                break;
                   case (R.id.feedback):
                  Intent feedback = new Intent(Home_activity.this, com.example.workersfamily.feedback.class);
                  startActivity(feedback);
                 break;
                   case(R.id.Help):
                  Intent helpIntent = new Intent(Intent.ACTION_DIAL);
                  helpIntent.setData(Uri.parse("tel:9742553844"));
                  startActivity(helpIntent);
                  break;}
                 /*  ApplicationInfo api = getApplicationContext().getApplicationInfo();
                   String apkPath = api.sourceDir;
                   Intent intent = new Intent(Intent.ACTION_SEND);
                   intent.setType("application/vnd.android.package-archive");
                   intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(apkPath)));
                   startActivity(Intent.createChooser(intent,"ShareVia"));*/

               drawerLayout.closeDrawer(GravityCompat.START);
               return true;
           }
       });


   

    } 
    //open Drawer
    public void ClickMenu(View view) {
        openDrawer(drawerLayout);
    }

    private void openDrawer(DrawerLayout drawerLayout) {
        drawerLayout.openDrawer(GravityCompat.START);
    }
//logout menu
    public void logoutMenu(Home_activity home_activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(home_activity);
        builder.setTitle("logout");
        builder.setMessage("Are you sure you want to logout ? ");
        builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mAuth.signOut();
                Intent intent = new Intent(Home_activity.this, loginactivity.class);
                startActivity(intent);
                finish();
            }
        });
        builder.setNegativeButton("no", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }
//back
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else if (DoublePressToExit) {
            finishAffinity();
        } else {
            DoublePressToExit = true;
            Toast.makeText(Home_activity.this, "Press again back to exit", Toast.LENGTH_SHORT).show();
            Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    DoublePressToExit = false;
                }
            }, 1500);
        }
    }
      public void loadFragment(Fragment fragment,int flag){
          FragmentManager fragmentManager = getSupportFragmentManager();
          FragmentTransaction fragmentTransaction =fragmentManager.beginTransaction();
          if(flag==0){
              fragmentTransaction.add(R.id.container,fragment);
              fragmentManager.popBackStack(ROOT_FRAGNEMENT_TAG,FragmentManager.POP_BACK_STACK_INCLUSIVE);
              fragmentTransaction.addToBackStack(ROOT_FRAGNEMENT_TAG);
          }else{fragmentTransaction.replace(R.id.container,fragment);
              fragmentTransaction.addToBackStack(null);
          }

          fragmentTransaction.commit();
      }


}
