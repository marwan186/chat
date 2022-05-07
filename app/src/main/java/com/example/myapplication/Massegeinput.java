package com.example.myapplication;

public class Massegeinput {
    String sender;
    String reciver;
    String content;

    public Massegeinput(){

    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReciver() {
        return reciver;
    }

    public void setReciver(String reciver) {
        this.reciver = reciver;
    }

    public String getContet() {
        return content;
    }

    public void setContet(String contet) {
        this.content = contet;
    }

    public Massegeinput(String sender, String reciver, String contet) {
        this.sender = sender;
        this.reciver = reciver;
        this.content = contet;
    }
}
