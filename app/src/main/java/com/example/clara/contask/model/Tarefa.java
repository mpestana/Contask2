package com.example.clara.contask.model;

public class Tarefa {

    private String id;
    private String titulo;
    private String tags;
    private String localizacao;
    private String inicio;
    private String fim;
    private String relacoes;
    private String carro;
    private String departamento;
    private String temperatura;
    private String clima;
    private String humidade;
    private String tipo;

    public Tarefa(String id, String titulo, String tags,
                  String localizacao, String inicio, String fim, String relacoes, String carro,
                  String departamento, String temperatura, String clima, String humidade, String tipo) {
        this.id = id;
        this.titulo = titulo;
        this.tags = tags;
        this.localizacao = localizacao;
        this.inicio = inicio;
        this.fim = fim;
        this.relacoes = relacoes;
        this.carro = carro;
        this.departamento = departamento;
        this.temperatura = temperatura;
        this.clima = clima;
        this.humidade = humidade;
        this.tipo = tipo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getLocalizacao() {
        return localizacao;
    }

    public void setLocalizacao(String localizacao) {
        this.localizacao = localizacao;
    }

    public String getInicio() {
        return inicio;
    }

    public void setInicio(String inicio) {
        this.inicio = inicio;
    }

    public String getFim() {
        return fim;
    }

    public void setFim(String fim) {
        this.fim = fim;
    }

    public String getRelacoes() {
        return relacoes;
    }

    public void setRelacoes(String relacoes) {
        this.relacoes = relacoes;
    }

    public String getCarro() {
        return carro;
    }

    public void setCarro(String carro) {
        this.carro = carro;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public String getTemperatura() {
        return temperatura;
    }

    public void setTemperatura(String temperatura) {
        this.temperatura = temperatura;
    }

    public String getClima() {
        return clima;
    }

    public void setClima(String clima) {
        this.clima = clima;
    }

    public String getHumidade() {
        return humidade;
    }

    public void setHumidade(String humidade) {
        this.humidade = humidade;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }


}
