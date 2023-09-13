package com.example.clara.contask;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Environment;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.clara.contask.model.Abelha;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.android.gms.awareness.state.Weather;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private LocationManager locationManager;
    public GoogleApiClient client;
    private LocationRequest locationRequest;
    public Weather mWeather;
    public Intent main;
    private BroadcastReceiver broadcastReceiver;

    private List<Abelha> abelhasList = new ArrayList<Abelha>();
    Boolean sem_enxame = true;

    private BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        verifyAuthentication();

        bottomNavigation = findViewById(R.id.bottomNavigationView);
        bottomNavigation.setOnNavigationItemSelectedListener(navListener);

        startService(new Intent(this, ServiceTask.class));
        getSupportFragmentManager().beginTransaction().replace(R.id.mainFragment,new SampleCarouselViewFragment()).commit();

        //new DownloadImage((ImageView) findViewById(R.id.profileImage)).execute(imageUrl);

        statusCheck();


        String channelId = "channel-01";
        String channelName = "Channel Name";
        int importance = NotificationManager.IMPORTANCE_HIGH;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    channelId, channelName, importance);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);

            notificationManager.createNotificationChannel(mChannel);
        }

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Gson gson = new Gson();
                if (abelhasList != null && abelhasList.size() != 0) {
                    abelhasList = Arrays.asList(gson.fromJson(intent.getStringExtra("allAbelhas"), Abelha[].class));
                    if (abelhasList.size() != 0) {
                        for (Abelha a : abelhasList) {
                            if (a.getBeeNobee().equals("0")) {
                                sem_enxame = false;
                            }
                        }
                        if (!sem_enxame) {
                            Toast.makeText(getApplicationContext(), "Cuidado! Enxame Detectado!", Toast.LENGTH_SHORT).show();

                            NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.this, "channel-01");
                            builder.setContentTitle("ALERTA");
                            builder.setContentText("ENXAME DE ABELHAS DETECTADO");
                            builder.setSmallIcon(R.mipmap.ic_launcher);
                            builder.setAutoCancel(true);

                            NotificationManagerCompat managerCompat = NotificationManagerCompat.from(MainActivity.this);
                            if (ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                                // TODO: Consider calling
                                //    ActivityCompat#requestPermissions
                                // here to request the missing permissions, and then overriding
                                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                //                                          int[] grantResults)
                                // to handle the case where the user grants the permission. See the documentation
                                // for ActivityCompat#requestPermissions for more details.
                                return;
                            }
                            managerCompat.notify(1, builder.build());
                            //showNotification(MainActivity.this , "ALERTA", "ENXAME DE ABELHAS DETECTADO");

                            //criarNotificacaoSimples("Notificacao", "Alerta! Abelhas!");
                        }
                    }
                }
            }
        };
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    if(menuItem.getItemId()==R.id.search){
                        startService(new Intent(getApplicationContext(), ServiceTask.class));
                        getSupportFragmentManager().beginTransaction().replace(R.id.mainFragment,new SampleCarouselViewFragment()).commit();

                    } else if (menuItem.getItemId()==R.id.settings) {
                        getSupportFragmentManager().beginTransaction().replace(R.id.mainFragment,new SettingsFragment()).commit();

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

    public void statusCheck() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        /* Pedido para ativar gps */
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
        }
    }

    public void verifyAuthentication() {
        if (FirebaseAuth.getInstance().getUid() == null) {
            Intent goLogin = new Intent(MainActivity.this, LoginActivity.class);
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
    protected void onStart() {
        LocalBroadcastManager.getInstance(this).registerReceiver((broadcastReceiver),
                new IntentFilter(AbelhaService.SERVICE_RESULT));
        super.onStart();
    }

    @Override
    protected void onStop() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.log_out) {
            logout();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void logout() {
        LoginManager.getInstance().logOut();
        Intent login = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(login);
        finish();
    }


}



/*added aqui
        String records = "", error="";
        try{

            Class.forName("com.mysql.jdbc.Driver");

            Connection connection = DriverManager.getConnection("jdbc:mysql://50.116.88.16:3306", "danjo296_vento", "VdTs@8102");

            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery("SELECT * FROM abelha");

            while(resultSet.next()) {

                records += resultSet.getString(1) + " " + resultSet.getString(2) + "\n";
            }
         }
        catch(Exception e)
        {
            error = e.toString();
        }*/