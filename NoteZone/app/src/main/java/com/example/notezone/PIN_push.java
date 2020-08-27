package com.example.notezone;

public class PIN_push {

    String PIN;

    public PIN_push() {

    }

    public String getPIN() {
        return PIN;
    }

    public void setPIN(String PIN) {
        this.PIN = PIN;
    }

    public PIN_push(String PIN) {
        this.PIN = PIN;
    }

    @Override
    public String toString() {
        return "PIN_push{" +
                "PIN='" + PIN + '\'' +
                '}';
    }
}
