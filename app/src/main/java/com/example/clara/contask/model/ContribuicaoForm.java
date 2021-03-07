package com.example.clara.contask.model;

public class ContribuicaoForm {

    private String tarefaID;
    private String tipoResposta;
    private String contribuicaoTexto;
    private String contribuicaoBoolean;
    private String identificacaoUsuario;

    public ContribuicaoForm(String tarefaID, String tipoResposta, String contribuicaoTexto,
                            String contribuicaoBoolean, String identificacaoUsuario){
        this.tarefaID = tarefaID;
        this.tipoResposta = tipoResposta;
        this.contribuicaoTexto = contribuicaoTexto;
        this.contribuicaoBoolean = contribuicaoBoolean;
        this.identificacaoUsuario = identificacaoUsuario;
    }

    public String getTarefaID() {
        return tarefaID;
    }

    public void setTarefaID(String tarefaID) {
        this.tarefaID = tarefaID;
    }

    public String getTipoResposta() {
        return tipoResposta;
    }

    public void setTipoResposta(String tipoResposta) {
        this.tipoResposta = tipoResposta;
    }

    public String getContribuicaoTexto() {
        return contribuicaoTexto;
    }

    public void setContribuicaoTexto(String contribuicaoTexto) {
        this.contribuicaoTexto = contribuicaoTexto;
    }

    public String isContribuicaoBoolean() {
        return contribuicaoBoolean;
    }

    public void setContribuicaoBoolean(String contribuicaoBoolean) {
        this.contribuicaoBoolean = contribuicaoBoolean;
    }

    public String getIdentificacaoUsuario() {
        return identificacaoUsuario;
    }

    public void setIdentificacaoUsuario(String identificacaoUsuario) {
        this.identificacaoUsuario = identificacaoUsuario;
    }
}