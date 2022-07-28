package com.example.mynewnoteapp.Model;

public class Status {
    private boolean status;
    private String msg;

    public Status(boolean status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
