package com.example.clara.contask.campaign;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.clara.contask.LoginActivity;
import com.example.clara.contask.R;
import com.example.clara.contask.SampleCarouselViewFragment;
import com.example.clara.contask.ServiceTask;
import com.example.clara.contask.SettingsFragment;
import com.example.clara.contask.chat.ChatsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class CampaignActivity extends AppCompatActivity {


    private String campaignId;
    private BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campaign);

        bottomNavigation = findViewById(R.id.bottomNavigationView);
        bottomNavigation.setOnNavigationItemSelectedListener(navListener);

        campaignId = getIntent().getExtras().getString("campaignId", null);

        getSupportFragmentManager().beginTransaction().replace(R.id.mainFragment,new FeedFragment(campaignId)).commit();





    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    if(menuItem.getItemId()==R.id.feed){
                        getSupportFragmentManager().beginTransaction().replace(R.id.mainFragment,new FeedFragment(campaignId)).commit();

                    } else if (menuItem.getItemId()==R.id.tasks) {
                        getSupportFragmentManager().beginTransaction().replace(R.id.mainFragment,new TasksFragment(campaignId)).commit();

                    }
                    else if (menuItem.getItemId()==R.id.chat) {
                        getSupportFragmentManager().beginTransaction().replace(R.id.mainFragment,new CampaignChatsFragment(campaignId)).commit();

                    }
                    else if (menuItem.getItemId()==R.id.participants) {
                        getSupportFragmentManager().beginTransaction().replace(R.id.mainFragment,new ParticipantsFragment(campaignId)).commit();

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



