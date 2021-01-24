package com.example.clara.contask.interfaces;

import com.example.clara.contask.model.Tarefa;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface TarefaI {

    @GET("tarefas")
    Call<List<Tarefa>> getTarefas();
}
