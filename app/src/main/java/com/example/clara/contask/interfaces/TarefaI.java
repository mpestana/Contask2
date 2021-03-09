package com.example.clara.contask.interfaces;

import com.example.clara.contask.model.ContribuicaoForm;
import com.example.clara.contask.model.Tarefa;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface TarefaI {

    @GET("tarefas/{deviceID}")
    Call<List<Tarefa>> getTarefasByDeviceID(@Path("deviceID") String deviceID);

    @GET("tarefas")
    Call<List<Tarefa>> getTarefas();

    @POST("contribuicao")
    Call<Void> sendContribuicao(@Body ContribuicaoForm requestBody);
}
