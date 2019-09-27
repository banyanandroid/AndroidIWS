package com.banyan.androidiws.database;

public class Model_DPR_ID {
    int _id;
    String _dpr_id;

    public Model_DPR_ID(){   }
    public Model_DPR_ID(int id, String name){
        this._id = id;
        this._dpr_id = name;

    }
    public Model_DPR_ID(Integer int_id){
        this._id = int_id;
    }
    public Model_DPR_ID(String str_dpr_id){
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

