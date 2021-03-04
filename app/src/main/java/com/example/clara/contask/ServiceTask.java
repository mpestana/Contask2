package com.example.clara.contask;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.example.clara.contask.interfaces.TarefaI;
import com.example.clara.contask.model.Tarefa;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ServiceTask extends Service {


    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 50000;
    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = UPDATE_INTERVAL_IN_MILLISECONDS / 5;

    private LocalBroadcastManager localBroadcastManager;
    protected static final String SERVICE_RESULT = "com.service.result";
    protected static final String SERVICE_MESSAGE = "com.service.message";

    private  List<Tarefa> allTasks;
    private static final String ULR = "https://floating-taiga-06247.herokuapp.com/api/";


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        callAllTasks();
        super.onCreate();
    }


    private void sendResult(String id, String question, String answer, String context, String distance, String time) {
        Intent intent = new Intent(SERVICE_RESULT);
        if(id != null){
            intent.putExtra(SERVICE_MESSAGE, id);
        }
        if (question != null && answer != null && context!=null) {
            intent.putExtra("question", question);
            intent.putExtra("answer", answer);
            intent.putExtra("context", context);
            intent.putExtra("distance", distance);
            intent.putExtra("time", time);
        }
        localBroadcastManager.sendBroadcast(intent);
    }

    private List<Tarefa> callAllTasks() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(ULR).
                addConverterFactory(GsonConverterFactory.create()).
                build();

        TarefaI api = retrofit.create(TarefaI.class);
        Call<List<Tarefa>> call = api.getTarefas();

        call.enqueue(new Callback<List<Tarefa>>() {
            @Override
            public void onResponse(Call<List<Tarefa>> call, Response<List<Tarefa>> response) {
                allTasks = response.body();
                for(Tarefa t: allTasks){
                    sendResult(t.getId(), t.getTitulo(), t.getTipo(), "", "", "" );
                }

            }

            @Override
            public void onFailure(Call<List<Tarefa>> call, Throwable t) {
                allTasks = new ArrayList<Tarefa>();
            }
        });
        return allTasks;
    }


}