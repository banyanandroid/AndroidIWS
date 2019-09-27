package com.banyan.androidiws.database;

public class Model_Work_Status {

    int _id;
    String _dpr_id, final_at, field_work;

    public Model_Work_Status(){   }

    public Model_Work_Status(int id, String _dpr_id, String final_at, String field_work){
        this._id = id;
        this._dpr_id = _dpr_id;
        this.final_at = final_at;
        this.field_work = field_work;

    }

    public String getFinal_at() {
        return final_at;
    }

    public void setFinal_at(String final_at) {
        this.final_at = final_at;
    }

    public String getField_work() {
        return field_work;
    }

    public void setField_work(String field_work) {
        this.field_work = field_work;
    }

    public Model_Work_Status(String str_dpr_id){
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

