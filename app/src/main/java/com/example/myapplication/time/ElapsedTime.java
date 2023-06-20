package com.example.myapplication.time;

public class ElapsedTime {
    private ElapsedTime elapsedTime;

    private ElapsedTime() {

    }

    public ElapsedTime getInstance(){
        if (elapsedTime==null) elapsedTime = new ElapsedTime();
        return elapsedTime;
    }
}
