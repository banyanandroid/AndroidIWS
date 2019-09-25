package com.banyan.androidiws.database;

public class Model_Profile {
    int _id;
    String str_first_name, str_last_name, str_designation, str_photo, str_level;

    public Model_Profile(){   }

    public Model_Profile(int id, String str_first_name, String str_last_name, String str_designation, String str_photo,
                         String str_level){
        this._id = id;
        this.str_first_name = str_first_name;
        this.str_last_name = str_last_name;
        this.str_designation = str_designation;
        this.str_photo = str_photo;
        this.str_level = str_level;

    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getStr_first_name() {
        return str_first_name;
    }

    public void setStr_first_name(String str_first_name) {
        this.str_first_name = str_first_name;
    }

    public String getStr_last_name() {
        return str_last_name;
    }

    public void setStr_last_name(String str_last_name) {
        this.str_last_name = str_last_name;
    }

    public String getStr_designation() {
        return str_designation;
    }

    public void setStr_designation(String str_designation) {
        this.str_designation = str_designation;
    }

    public String getStr_photo() {
        return str_photo;
    }

    public void setStr_photo(String str_photo) {
        this.str_photo = str_photo;
    }

    public String getStr_level() {
        return str_level;
    }

    public void setStr_level(String str_level) {
        this.str_level = str_level;
    }

}

