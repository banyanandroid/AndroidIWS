package com.banyan.androidiws.database;

public class Model_15_Minutes_Photo_Upload {
    int _id;
    String _dpr_id;

    public Model_15_Minutes_Photo_Upload(){   }
    public Model_15_Minutes_Photo_Upload(int id, String name){
        this._id = id;
        this._dpr_id = name;

    }

    public Model_15_Minutes_Photo_Upload(String str_dpr_id){
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

