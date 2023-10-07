package com.example.clara.contask;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.clara.contask.campaign.CampaignsFragment;
import com.example.clara.contask.chat.ChatsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class CampaignActivity extends AppCompatActivity {



    private BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        verifyAuthentication();

        bottomNavigation = findViewById(R.id.bottomNavigationView);
        bottomNavigation.setOnNavigationItemSelectedListener(navListener);


        getSupportFragmentManager().beginTransaction().replace(R.id.mainFragment,new SampleCarouselViewFragment()).commit();




        String channelId = "channel-01";
        String channelName = "Channel Name";
        int importance = NotificationManager.IMPORTANCE_HIGH;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    channelId, channelName, importance);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);

            notificationManager.createNotificationChannel(mChannel);
        }


    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    if(menuItem.getItemId()==R.id.feed){
                        startService(new Intent(getApplicationContext(), ServiceTask.class));
                        getSupportFragmentManager().beginTransaction().replace(R.id.mainFragment,new SampleCarouselViewFragment()).commit();

                    } else if (menuItem.getItemId()==R.id.tasks) {
                        getSupportFragmentManager().beginTransaction().replace(R.id.mainFragment,new SettingsFragment()).commit();

                    }
                    else if (menuItem.getItemId()==R.id.chat) {
                        getSupportFragmentManager().beginTransaction().replace(R.id.mainFragment,new ChatsFragment()).commit();

                    }
                    else if (menuItem.getItemId()==R.id.participants) {
                        getSupportFragmentManager().beginTransaction().replace(R.id.mainFragment,new CampaignsFragment()).commit();

                    }


                    return true;
                }
            };

    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }



    public void verifyAuthentication() {
        if (FirebaseAuth.getInstance().getUid() == null) {
            Intent goLogin = new Intent(CampaignActivity.this, LoginActivity.class);
            goLogin.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(goLogin);
        }
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }






}



