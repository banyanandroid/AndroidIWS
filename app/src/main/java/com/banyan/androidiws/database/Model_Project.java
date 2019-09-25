package com.banyan.androidiws.database;

public class Model_Project {
    int _id;
    String dpr_id, site_name, zone, circle, project,
    category, date, wp, assigned_by;

    public Model_Project(){   }

    public Model_Project(int id, String dpr_id, String site_name, String zone, String circle, String project,
                         String category, String date, String wp, String assigned_by){
        this._id = id;
        this.dpr_id = dpr_id;
        this.site_name = site_name;
        this.zone = zone;
        this.circle = circle;
        this.project = project;
        this.category = category;
        this.date = date;
        this.wp = wp;
        this.assigned_by = assigned_by;

    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getDpr_id() {
        return dpr_id;
    }

    public void setDpr_id(String dpr_id) {
        this.dpr_id = dpr_id;
    }

    public String getSite_name() {
        return site_name;
    }

    public void setSite_name(String site_name) {
        this.site_name = site_name;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public String getCircle() {
        return circle;
    }

    public void setCircle(String circle) {
        this.circle = circle;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getWp() {
        return wp;
    }

    public void setWp(String wp) {
        this.wp = wp;
    }

    public String getAssigned_by() {
        return assigned_by;
    }

    public void setAssigned_by(String assigned_by) {
        this.assigned_by = assigned_by;
    }

}

