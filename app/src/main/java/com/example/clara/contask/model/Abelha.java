package com.example.clara.contask.model;

public class Abelha {
    private String id;
    private String beeNobee;

    public Abelha(String id, String beeNobee) {
        this.id = id;
        this.beeNobee = beeNobee;
    }

    public Abelha() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBeeNobee() {
        return beeNobee;
    }

    public void setBeeNobee(String beeNobee) {
        this.beeNobee = beeNobee;
    }
}
