package com.example.myapplication;

import java.io.Serializable;

public class Time implements Serializable {
    private String descricao;
    private int idTime;

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getIdTime() {
        return idTime;
    }

    public void setIdTime(int idTime) {
        this.idTime = idTime;
    }

    @Override
    public String toString() {
        return "Time{" +
                "idTime='" + idTime + '\'' +
                ", descricao=" + descricao +
                '}';
    }
}
