package com.example.clara.contask;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /****** Facebook Data *********/
        FacebookSdk.sdkInitialize(getApplicationContext());
        Bundle inBundle = getIntent().getExtras();
        String name = inBundle.get("name").toString();
        String surname = inBundle.get("surname").toString();
        String imageUrl = inBundle.get("imageUrl").toString();

        Log.i("MainActivity2", "Worker Complete Name: " + name + " " + surname);

        TextView nameView = (TextView) findViewById(R.id.nameAndSurname);
        nameView.setText(" " + name + " " + surname);
        new DownloadImage((ImageView) findViewById(R.id.profileImage)).execute(imageUrl);

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
                if(abelhasList!=null){
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
                            managerCompat.notify(1, builder.build());
                            //showNotification(MainActivity.this , "ALERTA", "ENXAME DE ABELHAS DETECTADO");

                            //criarNotificacaoSimples("Notificacao", "Alerta! Abelhas!");
                        }
                    }
                }
            }
        };
    }

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


    public void loadScrollActivity(View view) {

        Intent intent = new Intent(this, SampleCarouselViewActivity.class);
        startService(new Intent(this, ServiceTask.class));
        startActivity(intent);
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