package com.example.clara.contask.interfaces;

import com.example.clara.contask.model.Abelha;
import com.example.clara.contask.model.ContribuicaoForm;
import com.example.clara.contask.model.Tarefa;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface AbelhaI {

    @GET("abelha")
    Call<List<Abelha>> getAbelha();

}
