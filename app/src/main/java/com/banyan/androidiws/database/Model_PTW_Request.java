package com.banyan.androidiws.database;

import android.graphics.Bitmap;

public class Model_PTW_Request {
    int _id;
    String _dpr_id, ptwrequired, ptwno;
    Bitmap ptwcopy;

    public Model_PTW_Request(){   }

    public Model_PTW_Request(Integer int_id){
        this._id = int_id;
    }

    public Model_PTW_Request(int id, String _dpr_id, String ptwrequired, String ptwno, Bitmap ptwcopy){
        this._id = id;
        this._dpr_id = _dpr_id;
        this.ptwrequired = ptwrequired;
        this.ptwno = ptwno;
        this.ptwcopy = ptwcopy;

    }

    public String getPtwrequired() {
        return ptwrequired;
    }

    public void setPtwrequired(String ptwrequired) {
        this.ptwrequired = ptwrequired;
    }

    public String getPtwno() {
        return ptwno;
    }

    public void setPtwno(String ptwno) {
        this.ptwno = ptwno;
    }

    public Bitmap getPtwcopy() {
        return ptwcopy;
    }

    public void setPtwcopy(Bitmap ptwcopy) {
        this.ptwcopy = ptwcopy;
    }

    public Model_PTW_Request(String str_dpr_id){
        this._dpr_id = str_dpr_id;
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

