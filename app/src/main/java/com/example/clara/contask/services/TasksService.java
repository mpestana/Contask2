package com.example.clara.contask.services;
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

public class TasksService {

    private  List<Tarefa> allTasks;
    private static final String ULR = "https://floating-taiga-06247.herokuapp.com/api/";

    public  List<Tarefa> getAllTasks() {

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
                    Log.d("Titulo",t.getTitulo());
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
