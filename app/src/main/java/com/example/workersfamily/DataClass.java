package com.example.workersfamily;

public class DataClass {
    private String dataName;
    private String dataImage;
    private String dataWork;
    private String dataAdd;

    private String dataNumber;
    private String key;
    private String dataClint;
    private String userUid;

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    private String date;

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }
    public String getUserUid() {
        return userUid;
    }



    public String getDataClint() {
        return dataClint;}
    public String getKey(){
        return key;
    }
    public void setKey(String key){
        this.key = key;
    }

    public String getDataNumber() {
        return dataNumber;
    }

    public String getDataName() {
        return dataName;
    }

    public String getDataImage() {
        return dataImage;
    }

    public String getDataWork() {
        return dataWork;
    }

    public String getDataAdd() {
        return dataAdd;
    }




    public DataClass(String dataImage,String dataName,String dataWork,String dataNumber, String dataAdd,String dataClint) {
        this.dataName = dataName;
        this.dataImage = dataImage;
        this.dataWork = dataWork;
        this.dataAdd = dataAdd;
        this.dataNumber = dataNumber;
        this.dataClint = dataClint;
    }
    public DataClass(){

    }


}
