package com.example.cenidet.models;

import java.util.ArrayList;

public class Chat {

    private String id;
    private String idUser1;
    private String idUser2;
    private int idNotification;
    private boolean isRighting;
    private long timestamp;
    private ArrayList<String> ids;

    public Chat(){

    }

    public Chat(String id, String idUser1, String idUser2, int idNotification, boolean isRighting, long timestamp, ArrayList<String> ids) {
        this.id = id;
        this.idUser1 = idUser1;
        this.idUser2 = idUser2;
        this.idNotification = idNotification;
        this.isRighting = isRighting;
        this.timestamp = timestamp;
        this.ids = ids;
    }

    public boolean isRighting() {
        return isRighting;
    }

    public void setRighting(boolean righting) {
        isRighting = righting;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getIdUser1() {
        return idUser1;
    }

    public void setIdUser1(String idUser1) {
        this.idUser1 = idUser1;
    }

    public String getIdUser2() {
        return idUser2;
    }

    public void setIdUser2(String idUser2) {
        this.idUser2 = idUser2;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<String> getIds() {
        return ids;
    }

    public void setIds(ArrayList<String> ids) {
        this.ids = ids;
    }

    public int getIdNotification() {
        return idNotification;
    }

    public void setIdNotification(int idNotification) {
        this.idNotification = idNotification;
    }
}
