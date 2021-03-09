package com.example.clara.contask;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.example.clara.contask.interfaces.TarefaI;
import com.example.clara.contask.model.ContribuicaoForm;
import com.example.clara.contask.model.Tarefa;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ServiceTask extends Service {

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
    public int onStartCommand(Intent intent, int flags, int startId) {
        callAllTasksByDeviceID();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
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

    public static void enviarContribuicao(Context context, String tarefaID, String tipoResposta, String contribuicaoTexto,
                                             String contribuicaoBoolean, String identificacaoUsuario){
        Retrofit retrofit = new Retrofit.Builder().baseUrl(ULR).
                addConverterFactory(GsonConverterFactory.create()).
                build();
        ContribuicaoForm requestBody = new ContribuicaoForm(tarefaID,tipoResposta,contribuicaoTexto,
                contribuicaoBoolean,identificacaoUsuario);
        TarefaI api = retrofit.create(TarefaI.class);
        Call<Void> call = api.sendContribuicao(requestBody);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Toast.makeText(context, "Contribuição enviado com sucesso!", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(context, "Ops! ocorreu um erro.", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private List<Tarefa> callAllTasksByDeviceID() {
        String deviceID = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        Retrofit retrofit = new Retrofit.Builder().baseUrl(ULR).
                addConverterFactory(GsonConverterFactory.create()).
                build();

        TarefaI api = retrofit.create(TarefaI.class);
        Call<List<Tarefa>> call = api.getTarefasByDeviceID(deviceID);

        call.enqueue(new Callback<List<Tarefa>>() {
            @Override
            public void onResponse(Call<List<Tarefa>> call, Response<List<Tarefa>> response) {
                allTasks = response.body();
                Intent intent = new Intent(SERVICE_RESULT);
                localBroadcastManager.sendBroadcast(intent);
                Gson gson = new Gson();
                String jsonList = gson.toJson(allTasks);
                intent.putExtra("allTasks", jsonList);

            }

            @Override
            public void onFailure(Call<List<Tarefa>> call, Throwable t) {
                allTasks = new ArrayList<Tarefa>();
            }
        });
        return allTasks;
    }


}