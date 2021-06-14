package com.example.cenidet.models;

public class Post {

    private String id;
    private String title;
    private String description;
    private String image1;
    private String image2;
    private String idUser;
    private String category;
    private String tipocuenta;
    private Boolean isforAdministrative;
    private Boolean isforTeacher;
    private Boolean isforStudent;
    private Boolean isforExternal;
    private long timestamp;

    public Post(){

    }

    public Post(String id, String title, String description, String image1, String image2, String idUser, String category, String tipocuenta,
                Boolean isforAdministrative, Boolean isforTeacher, Boolean isforStudent, Boolean isforExternal,
                long timestamp) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.image1 = image1;
        this.image2 = image2;
        this.idUser = idUser;
        this.category = category;
        this.tipocuenta = tipocuenta;
        this.isforAdministrative = isforAdministrative;
        this.isforTeacher = isforTeacher;
        this.isforStudent = isforStudent;
        this.isforExternal = isforExternal;
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage1() {
        return image1;
    }

    public void setImage1(String image1) {
        this.image1 = image1;
    }

    public String getImage2() {
        return image2;
    }

    public void setImage2(String image2) {
        this.image2 = image2;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public long getTimestamp() {
        return timestamp;
    }
    //*** metodos nuevos inicio
    public Boolean getIsforAdministrative() {
        return isforAdministrative;
    }

    public void setIsforAdministrative(Boolean isforAdministrative) {
        this.isforAdministrative = isforAdministrative;
    }

    public Boolean getIsforTeacher() {
        return isforTeacher;
    }

    public void setIsforTeacher(Boolean isforTeacher) {
        this.isforTeacher = isforTeacher;
    }

    public Boolean getIsforStudent() {
        return isforStudent;
    }

    public void setIsforStudent(Boolean isforStudent) {
        this.isforStudent = isforStudent;
    }

    public Boolean getIsforExternal() {
        return isforExternal;
    }

    public void setIsforExternal(Boolean isforExternal) {
        this.isforExternal = isforExternal;
    }
    //metodos nuevos fin
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getTipocuenta() {
        return tipocuenta;
    }

    public void setTipocuenta(String tipocuenta) {
        this.tipocuenta = tipocuenta;
    }
}
