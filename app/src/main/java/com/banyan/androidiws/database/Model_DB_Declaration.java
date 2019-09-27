package com.banyan.androidiws.database;

import android.graphics.Bitmap;

public class Model_DB_Declaration {
    int _id;
    String  userid, declaration_type, declaration, text, checkbox,  comments, _dpr_id, customer_ptw, image_url;
    Bitmap image;

    // image_url is used for show image in declaration adapter
    public Model_DB_Declaration(){   }
    public Model_DB_Declaration(int id, String _dpr_id, String userid, String declaration_type, String declaration, String text, String checkbox,
                                Bitmap image, String comments, String customer_ptw, String image_url){
        this._id = id;
        this._dpr_id = _dpr_id;
        this.userid = userid;
        this.declaration_type = declaration_type;
        this.declaration = declaration;
        this.text = text;
        this.checkbox = checkbox;
        this.image = image;
        this.comments = comments;
        this.customer_ptw = customer_ptw;

    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getDeclaration_type() {
        return declaration_type;
    }

    public void setDeclaration_type(String declaration_type) {
        this.declaration_type = declaration_type;
    }

    public String getCustomer_ptw() {
        return customer_ptw;
    }

    public void setCustomer_ptw(String customer_ptw) {
        this.customer_ptw = customer_ptw;
    }

    public String getDeclaration() {
        return declaration;
    }

    public void setDeclaration(String declaration) {
        this.declaration = declaration;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getCheckbox() {
        return checkbox;
    }

    public void setCheckbox(String checkbox) {
        this.checkbox = checkbox;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Model_DB_Declaration(Integer int_id){
        this._id = int_id;
    }

    public Model_DB_Declaration(String str_dpr_id){
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

