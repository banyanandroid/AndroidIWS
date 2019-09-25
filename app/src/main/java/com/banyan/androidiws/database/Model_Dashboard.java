package com.banyan.androidiws.database;

public class Model_Dashboard {
    int _id;
    String str_first_name, str_last_name, str_designation, str_photo, str_level, str_no_doc_expired, str_current_project,
    str_category, str_circle, str_project_type, str_reporting_manager, str_line_manager;

    public Model_Dashboard(){   }

    public Model_Dashboard(int id, String str_first_name, String str_last_name, String str_designation, String str_photo,
                           String str_level, String str_no_doc_expired, String str_current_project,
                           String str_category, String str_circle, String str_project_type, String str_reporting_manager,
                           String str_line_manager){
        this._id = id;
        this.str_first_name = str_first_name;
        this.str_last_name = str_last_name;
        this.str_designation = str_designation;
        this.str_photo = str_photo;
        this.str_level = str_level;

        this.str_no_doc_expired = str_no_doc_expired;
        this.str_current_project = str_current_project;
        this.str_category = str_category;
        this.str_project_type = str_project_type;
        this.str_reporting_manager = str_reporting_manager;
        this.str_line_manager = str_line_manager;
        this.str_circle = str_circle;

    }

    public String getStr_circle() {
        return str_circle;
    }

    public void setStr_circle(String str_circle) {
        this.str_circle = str_circle;
    }

    public String getStr_line_manager() {
        return str_line_manager;
    }

    public void setStr_line_manager(String str_line_manager) {
        this.str_line_manager = str_line_manager;
    }

    public String getStr_reporting_manager() {
        return str_reporting_manager;
    }

    public void setStr_reporting_manager(String str_reporting_manager) {
        this.str_reporting_manager = str_reporting_manager;
    }

    public String getStr_project_type() {
        return str_project_type;
    }

    public void setStr_project_type(String str_project_type) {
        this.str_project_type = str_project_type;
    }

    public String getStr_category() {
        return str_category;
    }

    public void setStr_category(String str_category) {
        this.str_category = str_category;
    }

    public String getStr_current_project() {
        return str_current_project;
    }

    public void setStr_current_project(String str_current_project) {
        this.str_current_project = str_current_project;
    }

    public String getStr_no_doc_expired() {
        return str_no_doc_expired;
    }

    public void setStr_no_doc_expired(String str_no_doc_expired) {
        this.str_no_doc_expired = str_no_doc_expired;
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

