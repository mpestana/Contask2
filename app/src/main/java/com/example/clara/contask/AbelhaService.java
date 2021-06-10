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

import com.example.clara.contask.interfaces.AbelhaI;
import com.example.clara.contask.interfaces.TarefaI;
import com.example.clara.contask.model.Abelha;
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


public class AbelhaService extends Service {

    private LocalBroadcastManager localBroadcastManager;
    protected static final String SERVICE_RESULT = "com.service.result";
    protected static final String SERVICE_MESSAGE = "com.service.message";
    private List<Abelha> allAbelhas;
    private static final String ULR = "https://floating-taiga-06247.herokuapp.com/api/";


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        getAbelha();
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

//    public static void enviarContribuicao(Context context, String tarefaID, String tipoResposta, String contribuicaoTexto,
//                                          String contribuicaoBoolean, String identificacaoUsuario){
//        Retrofit retrofit = new Retrofit.Builder().baseUrl(ULR).
//                addConverterFactory(GsonConverterFactory.create()).
//                build();
//        ContribuicaoForm requestBody = new ContribuicaoForm(tarefaID,tipoResposta,contribuicaoTexto,
//                contribuicaoBoolean,identificacaoUsuario, "1");
//        TarefaI api = retrofit.create(TarefaI.class);
//        Call<Void> call = api.sendContribuicao(requestBody);
//
//        call.enqueue(new Callback<Void>() {
//            @Override
//            public void onResponse(Call<Void> call, Response<Void> response) {
//                Toast.makeText(context, "Contribuição enviado com sucesso!", Toast.LENGTH_SHORT).show();
//
//            }
//
//            @Override
//            public void onFailure(Call<Void> call, Throwable t) {
//                Toast.makeText(context, "Ops! ocorreu um erro.", Toast.LENGTH_SHORT).show();
//
//            }
//        });
//    }

    private List<Abelha> getAbelha() {

        Retrofit retrofit = new Retrofit.Builder().baseUrl(ULR).
                addConverterFactory(GsonConverterFactory.create()).
                build();

        AbelhaI api = retrofit.create(AbelhaI.class);
        Call<List<Abelha>> call = api.getAbelha();

        call.enqueue(new Callback<List<Abelha>>() {
            @Override
            public void onResponse(Call<List<Abelha>> call, Response<List<Abelha>> response) {
                allAbelhas = response.body();
                Intent intent = new Intent(SERVICE_RESULT);
                localBroadcastManager.sendBroadcast(intent);
                Gson gson = new Gson();
                String jsonList = gson.toJson(allAbelhas);
                intent.putExtra("allAbelhas", jsonList);

            }

            @Override
            public void onFailure(Call<List<Abelha>> call, Throwable t) {
                allAbelhas = new ArrayList<Abelha>();
            }
        });
        return allAbelhas;
    }


}