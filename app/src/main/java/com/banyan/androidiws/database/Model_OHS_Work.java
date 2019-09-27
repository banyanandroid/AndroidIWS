package com.banyan.androidiws.database;

import android.graphics.Bitmap;

public class Model_OHS_Work {
    int _id;
    String _dpr_id, userid, time;
    Bitmap image;

    public Model_OHS_Work(){   }

    public Model_OHS_Work(Integer int_id){
        this._id = int_id;
    }

    public Model_OHS_Work(String str_dpr_id){
        this._dpr_id = str_dpr_id;
    }

    public Model_OHS_Work(int id, String _dpr_id, String userid, Bitmap image, String time){
        this._id = id;
        this._dpr_id = _dpr_id;
        this.userid = userid;
        this.image = image;
        this.time = time;

    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getID(){
        return this._id;
    }

    public void setID(int id){
        this._id = id;
    }

    public String getDPR_ID(){
        return this._dpr_id;
    }

    public void setDPR_ID(String name){
        this._dpr_id = name;
    }

}

