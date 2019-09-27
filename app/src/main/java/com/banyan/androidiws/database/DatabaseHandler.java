package com.banyan.androidiws.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;


public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "iws";

    private static final String TABLE_PTW_STATUS = "ptw_status"; // to know request ptw status
    private static final String KEY_ID = "id";
    private static final String KEY_DPR_ID = "dpr_id";

    private static final String TABLE_PHOTO_UPLOAD = "photo_upload"; // to know request ptw status
    private static final String KEY_PHOTO_UPLOAD_ID = "photo_upload_id";
    private static final String KEY_PHOTO_UPLOAD_DPR_ID = "photo_upload_dpr_id";

    private static final String TABLE_PROFILE = "profile";
    private static final String KEY_FIRST_NAME = "first_name";
    private static final String KEY_LAST_NAME = "last_name";
    private static final String KEY_DESIGNATION = "designation";
    private static final String KEY_PHOTO = "photo";
    private static final String KEY_LEVEL = "level";

    private static final String TABLE_DASHBOARD = "dashboard";
    private static final String KEY_NO_DOC_EXPIRED = "no_doc_expired";
    private static final String KEY_CURRENT_PROJECT = "current_project";
    private static final String KEY_CATEGORY = "category";
    private static final String KEY_CIRCLE = "circle";
    private static final String KEY_PROJECT_TYPE = "project_type";
    private static final String KEY_REPORTING_MANAGER = "reporting_manager";
    private static final String KEY_LINE_MANAGER = "line_manager";

    private static final String TABLE_IN_PROGRESS_PROJECT = "in_progress_project";
    //KEY_DPR_ID
    private static final String KEY_SITE_NAME = "site_name";
    private static final String KEY_ZONE = "zone";
    //KEY_CIRCLE
    private static final String KEY_PROJECT = "project";
    //KEY_CATEGORY
    private static final String KEY_DATE = "date";
    private static final String KEY_WP = "wp";
    private static final String KEY_ASSIGNED_BY = "assigned_by";

    // reach site
    private static final String TABLE_REACH_SITE = "reach_site";

    //declaration
    private static final String TABLE_DECLARATION = "declaration";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_DECLARATION_TYPE = "declaration_type";
    private static final String KEY_DECLARATION = "declaration";
    private static final String KEY_TEXT = "text";
    private static final String KEY_CHECK_BOX = "chekbox";
    private static final String KEY_IMAGE = "image";
    private static final String KEY_COMMENTS = "comments";
    private static final String KEY_CUSTOMER_PTW = "customer_ptw";

    //ptw request
    private static final String TABLE_PTW_REQUEST = "ptw_request";
    private static final String KEY_PTW_REQUIRED = "ptw_required";
    private static final String KEY_PTW_NO = "ptw_no";
    private static final String KEY_PTW_COPY = "ptw_copy";

    //start work
    private static final String TABLE_START_WORK = "start_work";

    //ohs work photo
    private static final String TABLE_OHS_WORK_PHOTO = "ohs_work_photo";
    private static final String KEY_TIME = "time";

    // work status
    private static final String TABLE_WORK_STATUS = "work_status";
    private static final String KEY_FINAL_AT = "final_at";
    private static final String KEY_FIELD_WORK = "field_work";

    //left site
    private static final String TABLE_LEFT_SITE = "left_site";

    // expense
    private static final String TABLE_EXPENSE = "expense";
    private static final String KEY_TRAVEL_COST = "travelcost";
    private static final String KEY_VEHICLE_COST = "vechiclecost";

    //work completed
    private static final String TABLE_WORK_COMPLETED = "work_completed";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        //3rd argument to be passed is CursorFactory instance
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_PTW_STATUS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_DPR_ID + " TEXT" + ")";

        String CREATE_PROFILE_TABLE = "CREATE TABLE " + TABLE_PROFILE + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_FIRST_NAME + " TEXT, "
                + KEY_LAST_NAME + " TEXT,"
                + KEY_DESIGNATION + " TEXT,"
                + KEY_PHOTO + " TEXT,"
                + KEY_LEVEL + " TEXT"
                + ")";

        String CREATE_DASHBOARD_TABLE = "CREATE TABLE " + TABLE_DASHBOARD + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_FIRST_NAME + " TEXT, "
                + KEY_LAST_NAME + " TEXT,"
                + KEY_DESIGNATION + " TEXT,"
                + KEY_PHOTO + " TEXT,"
                + KEY_LEVEL + " TEXT,"
                + KEY_NO_DOC_EXPIRED + " TEXT,"
                + KEY_CURRENT_PROJECT + " TEXT,"
                + KEY_CATEGORY + " TEXT,"
                + KEY_CIRCLE + " TEXT,"
                + KEY_PROJECT_TYPE + " TEXT,"
                + KEY_REPORTING_MANAGER + " TEXT,"
                + KEY_LINE_MANAGER + " TEXT"
                + ")";

        String CREATE_IN_PROGRESS_PROJECT_TABLE = "CREATE TABLE " + TABLE_IN_PROGRESS_PROJECT + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_DPR_ID + " TEXT, "
                + KEY_SITE_NAME + " TEXT, "
                + KEY_ZONE + " TEXT, "
                + KEY_CIRCLE + " TEXT, "
                + KEY_PROJECT + " TEXT, "
                + KEY_CATEGORY + " TEXT, "
                + KEY_DATE + " TEXT, "
                + KEY_WP + " TEXT, "
                + KEY_ASSIGNED_BY + " TEXT"
                + ")";

        String CREATE_REACH_SITE = "CREATE TABLE " + TABLE_REACH_SITE + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_DPR_ID + " TEXT "
                + ")";

        String CREATE_DECLARATION = "CREATE TABLE " + TABLE_DECLARATION+ "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_DPR_ID + " TEXT, "
                + KEY_USER_ID + " TEXT, "
                + KEY_DECLARATION_TYPE + " TEXT, "
                + KEY_DECLARATION + " TEXT, "
                + KEY_TEXT + " TEXT, "
                + KEY_CHECK_BOX + " TEXT, "
                + KEY_IMAGE + " BLOB, "
                + KEY_COMMENTS + " TEXT,"
                + KEY_CUSTOMER_PTW + " TEXT"
                + ")";

        String CREATE_PTW_REQUEST = "CREATE TABLE " + TABLE_PTW_REQUEST+ "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_DPR_ID + " TEXT, "
                + KEY_PTW_REQUIRED + " TEXT, "
                + KEY_PTW_NO + " TEXT, "
                + KEY_PTW_COPY + " BLOB "
                + ")";

        String CREATE_START_WORK = "CREATE TABLE " + TABLE_START_WORK + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_DPR_ID + " TEXT "
                + ")";

        String CREATE_OHS_WORK_PHOTO = "CREATE TABLE " + TABLE_OHS_WORK_PHOTO + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_DPR_ID + " TEXT, "
                + KEY_USER_ID + " TEXT, "
                + KEY_IMAGE + " BLOB, "
                + KEY_TIME + " TEXT "
                + ")";

        String CREATE_WORK_STATUS = "CREATE TABLE " + TABLE_WORK_STATUS + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_DPR_ID + " TEXT, "
                + KEY_FINAL_AT + " TEXT, "
                + KEY_FIELD_WORK + " TEXT "
                + ")";

        String CREATE_LEFT_SITE = "CREATE TABLE " + TABLE_LEFT_SITE + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_DPR_ID + " TEXT "
                + ")";

        String CREATE_EXPENSE = "CREATE TABLE " + TABLE_EXPENSE + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_DPR_ID + " TEXT, "
                + KEY_TRAVEL_COST + " TEXT, "
                + KEY_VEHICLE_COST + " TEXT "
                + ")";

        String CREATE_WORK_COMPLETED = "CREATE TABLE " + TABLE_WORK_COMPLETED + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_DPR_ID + " TEXT "
                + ")";

        db.execSQL(CREATE_CONTACTS_TABLE);
        db.execSQL(CREATE_PROFILE_TABLE);
        db.execSQL(CREATE_DASHBOARD_TABLE);
        db.execSQL(CREATE_IN_PROGRESS_PROJECT_TABLE);

        db.execSQL(CREATE_REACH_SITE);
        db.execSQL(CREATE_DECLARATION);
        db.execSQL(CREATE_PTW_REQUEST);
        db.execSQL(CREATE_START_WORK);
        db.execSQL(CREATE_OHS_WORK_PHOTO);
        db.execSQL(CREATE_WORK_STATUS);
        db.execSQL(CREATE_LEFT_SITE);
        db.execSQL(CREATE_EXPENSE);
        db.execSQL(CREATE_WORK_COMPLETED);

    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PTW_STATUS);

        // Create tables again
        onCreate(db);
    }

    /******************************
    *  PROFILE
    *******************************/

    public void Function_Add_Profile(Model_Profile profile) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_FIRST_NAME, profile.getStr_first_name()); // Model_PTW_Status Name
        values.put(KEY_LAST_NAME, profile.getStr_last_name()); // Model_PTW_Status Name
        values.put(KEY_DESIGNATION, profile.getStr_designation()); // Model_PTW_Status Name
        values.put(KEY_PHOTO, profile.getStr_photo()); // Model_PTW_Status Name
        values.put(KEY_LEVEL, profile.getStr_level()); // Model_PTW_Status Name

        // Inserting Row
        db.insert(TABLE_PROFILE, null, values);
        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection

    }

    public void Function_Delete_Profile(Model_Profile profile) {

        System.out.println("### Function_Delete_Profile profile.get_id() : "+profile.get_id());

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PROFILE, KEY_ID + " = ?",
                new String[] { String.valueOf(profile.get_id()) });
        db.close();
    }

    public int Function_Get_Profile_Count() {

        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_PROFILE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // return contact list
        return cursor.getCount();
    }

    // code to get the single contact
    public Model_Profile Function_Get_Profile(int id) {

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_PROFILE, new String[] { KEY_ID,
                        KEY_FIRST_NAME, KEY_LAST_NAME, KEY_DESIGNATION, KEY_PHOTO, KEY_LEVEL }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Model_Profile profile = new Model_Profile(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getString(4),
                cursor.getString(5));

        // return contact
        return profile;
    }


    public int Function_Update_Profile(Model_Profile profile) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, profile.get_id()); // Model_PTW_Status Name
        values.put(KEY_FIRST_NAME, profile.getStr_first_name()); // Model_PTW_Status Name
        values.put(KEY_LAST_NAME, profile.getStr_last_name()); // Model_PTW_Status Name
        values.put(KEY_DESIGNATION, profile.getStr_designation()); // Model_PTW_Status Name
        values.put(KEY_PHOTO, profile.getStr_photo()); // Model_PTW_Status Name
        values.put(KEY_LEVEL, profile.getStr_level()); // Model_PTW_Status Name


        // updating row
        return db.update(TABLE_PROFILE, values, KEY_ID + " = ?",
                new String[] { String.valueOf(profile.get_id()) });
    }

    // code to get the single contact
    public int Function_Get_Last_Profile_id() {

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_PROFILE, new String[] { KEY_ID }, null,
                null, null, null, KEY_ID+ " DESC", "1");

        Integer int_profile_id = -1;
        if (cursor != null) {

            cursor.moveToFirst();
            int_profile_id = Integer.parseInt(cursor.getString(0));
        }

        // return contact
        return int_profile_id;

    }

    /******************************
     *  DASHBOARD
     *******************************/

    public void Function_Add_Dashboard(Model_Dashboard dashboard) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_FIRST_NAME, dashboard.getStr_first_name()); // Model_PTW_Status Name
        values.put(KEY_LAST_NAME, dashboard.getStr_last_name()); // Model_PTW_Status Name
        values.put(KEY_DESIGNATION, dashboard.getStr_designation()); // Model_PTW_Status Name
        values.put(KEY_PHOTO, dashboard.getStr_photo()); // Model_PTW_Status Name
        values.put(KEY_LEVEL, dashboard.getStr_level()); // Model_PTW_Status Name

        values.put(KEY_NO_DOC_EXPIRED, dashboard.getStr_no_doc_expired()); // Model_PTW_Status Name
        values.put(KEY_CURRENT_PROJECT, dashboard.getStr_current_project()); // Model_PTW_Status Name
        values.put(KEY_CATEGORY, dashboard.getStr_category()); // Model_PTW_Status Name
        values.put(KEY_CIRCLE, dashboard.getStr_circle()); // Model_PTW_Status Name
        values.put(KEY_PROJECT_TYPE, dashboard.getStr_project_type()); // Model_PTW_Status Name
        values.put(KEY_REPORTING_MANAGER, dashboard.getStr_reporting_manager()); // Model_PTW_Status Name
        values.put(KEY_LINE_MANAGER, dashboard.getStr_line_manager()); // Model_PTW_Status Name

        // Inserting Row
        db.insert(TABLE_DASHBOARD, null, values);
        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection

    }

    public void Function_Delete_Dashboard(Model_Dashboard dashboard) {

        System.out.println("### Function_Delete_Dashboard dashboard.get_id() : "+dashboard.get_id());

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_DASHBOARD, KEY_ID + " = ?",
                new String[] { String.valueOf(dashboard.get_id()) });
        db.close();
    }

    public int Function_Get_Dashboard_Count() {

        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_DASHBOARD;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // return contact list
        return cursor.getCount();
    }

    public Model_Dashboard Function_Get_Dashboard(int id) {

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_DASHBOARD, new String[] { KEY_ID,
                        KEY_FIRST_NAME, KEY_LAST_NAME, KEY_DESIGNATION, KEY_PHOTO, KEY_LEVEL,
                        KEY_NO_DOC_EXPIRED, KEY_CURRENT_PROJECT, KEY_CATEGORY, KEY_CIRCLE,
                        KEY_PROJECT_TYPE, KEY_REPORTING_MANAGER, KEY_LINE_MANAGER}, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Model_Dashboard dashboard = new Model_Dashboard(Integer.parseInt(
                cursor.getString(0)),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getString(4),
                cursor.getString(5),
                cursor.getString(6),
                cursor.getString(7),
                cursor.getString(8),
                cursor.getString(9),
                cursor.getString(10),
                cursor.getString(11),
                cursor.getString(12));

        // return contact
        return dashboard;
    }


    public int Function_Update_Dashboard(Model_Dashboard dashboard) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, dashboard.get_id()); // Model_PTW_Status Name
        values.put(KEY_FIRST_NAME, dashboard.getStr_first_name()); // Model_PTW_Status Name
        values.put(KEY_LAST_NAME, dashboard.getStr_last_name()); // Model_PTW_Status Name
        values.put(KEY_DESIGNATION, dashboard.getStr_designation()); // Model_PTW_Status Name
        values.put(KEY_PHOTO, dashboard.getStr_photo()); // Model_PTW_Status Name
        values.put(KEY_LEVEL, dashboard.getStr_level()); // Model_PTW_Status Name

        values.put(KEY_NO_DOC_EXPIRED, dashboard.getStr_no_doc_expired()); // Model_PTW_Status Name
        values.put(KEY_CURRENT_PROJECT, dashboard.getStr_current_project()); // Model_PTW_Status Name
        values.put(KEY_CATEGORY, dashboard.getStr_category()); // Model_PTW_Status Name
        values.put(KEY_CIRCLE, dashboard.getStr_circle()); // Model_PTW_Status Name
        values.put(KEY_PROJECT_TYPE, dashboard.getStr_project_type()); // Model_PTW_Status Name
        values.put(KEY_REPORTING_MANAGER, dashboard.getStr_reporting_manager()); // Model_PTW_Status Name
        values.put(KEY_LINE_MANAGER, dashboard.getStr_line_manager()); // Model_PTW_Status Name

        // updating row
        return db.update(TABLE_DASHBOARD, values, KEY_ID + " = ?",
                new String[] { String.valueOf(dashboard.get_id()) });
    }

    // code to get the single contact
    public int Function_Get_Last_Dashboard_id() {

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_DASHBOARD, new String[] { KEY_ID }, null,
                null, null, null, KEY_ID+ " DESC", "1");

        Integer int_dashboard_id = -1;
        if (cursor != null) {

            cursor.moveToFirst();
            int_dashboard_id = Integer.parseInt(cursor.getString(0));
        }

        // return contact
        return int_dashboard_id;

    }

    /******************************
     *  IN PROGRESSS PROJECT
     *******************************/

    public void Function_Add_In_Progress_Project(Model_Project project) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_DPR_ID, project.getDpr_id());
        values.put(KEY_SITE_NAME, project.getSite_name());
        values.put(KEY_ZONE, project.getZone());
        values.put(KEY_CIRCLE, project.getCircle());
        values.put(KEY_PROJECT, project.getProject());
        values.put(KEY_CATEGORY, project.getCategory());
        values.put(KEY_DATE, project.getDate());
        values.put(KEY_WP, project.getWp());
        values.put(KEY_ASSIGNED_BY, project.getAssigned_by());

        // Inserting Row
        db.insert(TABLE_IN_PROGRESS_PROJECT, null, values);
        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection

    }

    public void Function_Delete_In_Progress_Project(Model_Project project) {

        System.out.println("### Function_Delete_Dashboard dashboard.get_id() : "+project.get_id());

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_IN_PROGRESS_PROJECT, KEY_ID + " = ?",
                new String[] { String.valueOf(project.get_id()) });
        db.close();
    }
    public void Function_Delete_All_In_Progress_Project() {

        System.out.println("### Function_Delete_All_In_Progress_Project ");

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_IN_PROGRESS_PROJECT, null,
                null);
        db.close();
    }

    public int Function_Get_In_Progress_Project_Count() {

        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_IN_PROGRESS_PROJECT;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // return contact list
        return cursor.getCount();
    }


    public Model_Project Function_Get_In_Progress_Project(int id) {

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_IN_PROGRESS_PROJECT, new String[] { KEY_ID,
                        KEY_DPR_ID, KEY_SITE_NAME, KEY_ZONE, KEY_CIRCLE,
                        KEY_PROJECT,
                        KEY_CATEGORY, KEY_DATE, KEY_WP,
                        KEY_ASSIGNED_BY}, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Model_Project project = new Model_Project(Integer.parseInt(
                cursor.getString(0)),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getString(4),
                cursor.getString(5),
                cursor.getString(6),
                cursor.getString(7),
                cursor.getString(8),
                cursor.getString(9));

        // return contact
        return project;
    }


    public int Function_Update_In_Progress_Project(Model_Project project) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_DPR_ID, project.getDpr_id());
        values.put(KEY_SITE_NAME, project.getSite_name());
        values.put(KEY_ZONE, project.getZone());
        values.put(KEY_CIRCLE, project.getCircle());
        values.put(KEY_PROJECT, project.getProject());

        values.put(KEY_CATEGORY, project.getCategory());
        values.put(KEY_DATE, project.getDate());
        values.put(KEY_WP, project.getWp());
        values.put(KEY_ASSIGNED_BY, project.getAssigned_by());

        // updating row
        return db.update(TABLE_IN_PROGRESS_PROJECT, values, KEY_ID + " = ?",
                new String[] { String.valueOf(project.get_id()) });
    }

    // code to get the single contact
    public int Function_Get_Last_In_Progress_Project_id() {

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_IN_PROGRESS_PROJECT, new String[] { KEY_ID }, null,
                null, null, null, KEY_ID+ " DESC", "1");

        Integer int_in_progress_project_id = -1;
        if (cursor != null) {

            cursor.moveToFirst();
            int_in_progress_project_id = Integer.parseInt(cursor.getString(0));
        }

        // return contact
        return int_in_progress_project_id;

    }

    // code to get all contacts in a list view
    public List<Model_Project> Function_Get_All_In_Progress_Project() {
        List<Model_Project> in_progress_project = new ArrayList<Model_Project>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_IN_PROGRESS_PROJECT;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {

            do {
                Model_Project project = new Model_Project(Integer.parseInt(cursor.getString(0)),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6),
                        cursor.getString(7),
                        cursor.getString(8),
                        cursor.getString(9));

                // Adding contact to list
                in_progress_project.add(project);
            } while (cursor.moveToNext());

        }

        // return contact list
        return in_progress_project;
    }



    /******************************
     *  REACH SITE
     *******************************/

    public long Function_Add_Reach_Site(Model_DPR_ID model_dpr_id) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_DPR_ID, model_dpr_id.getDPR_ID()); // Model_PTW_Status Name

        // Inserting Row
        Long long_result = db.insert(TABLE_REACH_SITE, null, values);
        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection

        return long_result;
    }

    public int Function_Get_Reached_Site_Count() {

        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_REACH_SITE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // return contact list
        return cursor.getCount();
    }

    public boolean Function_Is_Reach_Site_Exist(String str_dpr_id) {

        boolean bol_is_exist;
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_REACH_SITE + " WHERE "+KEY_DPR_ID + " = "+str_dpr_id;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        return cursor.moveToFirst();
    }

    public List<Model_DPR_ID> Function_Get_All_Reach_Site() {
        List<Model_DPR_ID> list_reached_site = new ArrayList<Model_DPR_ID>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_REACH_SITE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {

            do {
                Model_DPR_ID project = new Model_DPR_ID(Integer.parseInt(cursor.getString(0)),
                        cursor.getString(1));

                // Adding contact to list
                list_reached_site.add(project);
            } while (cursor.moveToNext());

        }

        // return contact list
        return list_reached_site;
    }


    public Integer Function_Delete_Reached_Site(Model_DPR_ID model_dpr_id) {

        System.out.println("### Function_Delete_Reached_Site ");

        SQLiteDatabase db = this.getWritableDatabase();
        Integer int_result = db.delete(TABLE_REACH_SITE, KEY_ID + " = ?",
                new String[] { String.valueOf(model_dpr_id.getID()) });
        db.close();
        return int_result;
    }

    /******************************
     *  DECLARATION
     *******************************/

    public Long Function_Add_Declaration(Model_DB_Declaration model_declaration) {

        byte[] data = getBitmapAsByteArray(model_declaration.getImage());

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_DPR_ID, model_declaration.getDPR_ID());
        values.put(KEY_USER_ID, model_declaration.getUserid());
        values.put(KEY_DECLARATION_TYPE, model_declaration.getDeclaration_type());
        values.put(KEY_DECLARATION, model_declaration.getDeclaration());
        values.put(KEY_TEXT, model_declaration.getText());
        values.put(KEY_CHECK_BOX, model_declaration.getCheckbox());
        values.put(KEY_IMAGE, data);
        values.put(KEY_COMMENTS, model_declaration.getComments());
        values.put(KEY_CUSTOMER_PTW, model_declaration.getCustomer_ptw());

        // Inserting Row
        Long long_result = db.insert(TABLE_DECLARATION, null, values);
        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection

        return long_result;

    }

    public int Function_Update_Declaration(Model_DB_Declaration model_declaration) {

        System.out.println("### Function_Update_Declaration ");
        System.out.println("### DPR ID " + model_declaration.getDPR_ID());
        System.out.println("### KEY_DECLARATION " + model_declaration.getDeclaration());

        Bitmap bitmpa = model_declaration.getImage();
        byte[] data = null;
        if (bitmpa != null){
            data = getBitmapAsByteArray(bitmpa);
        }


        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_DPR_ID, model_declaration.getDPR_ID());
        values.put(KEY_USER_ID, model_declaration.getUserid());
        values.put(KEY_DECLARATION_TYPE, model_declaration.getDeclaration_type());
        values.put(KEY_DECLARATION, model_declaration.getDeclaration());
        values.put(KEY_TEXT, model_declaration.getText());
        values.put(KEY_CHECK_BOX, model_declaration.getCheckbox());
        values.put(KEY_IMAGE, data);
        values.put(KEY_COMMENTS, model_declaration.getComments());
        values.put(KEY_CUSTOMER_PTW, model_declaration.getCustomer_ptw());

        // updating row
        return db.update(TABLE_DECLARATION, values, KEY_DPR_ID + " = ? AND "+ KEY_DECLARATION + " = ? ",
                new String[] { String.valueOf(model_declaration.getDPR_ID()), model_declaration.getDeclaration() });

    }

    public static byte[] getBitmapAsByteArray(Bitmap bitmap) {

        byte [] array_byte = null;
        if (bitmap != null){
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
            array_byte = outputStream.toByteArray();
        }

        return array_byte;
    }

    public int Function_Get_Declaration_Count() {

        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_DECLARATION;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // return contact list
        return cursor.getCount();
    }

    public int Function_Get_Declaration_Count(String str_dpr_id) {

        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_DECLARATION + " WHERE "+KEY_DPR_ID + " = "+str_dpr_id;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // return contact list
        return cursor.getCount();
    }

    public List<Model_DB_Declaration> Function_Get_All_Declaration(String str_dpr_id) {

        System.out.println("### Function_Get_All_Declaration ");

        List<Model_DB_Declaration> list_reached_site = new ArrayList<Model_DB_Declaration>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_DECLARATION + " WHERE "+KEY_DPR_ID +" = "+str_dpr_id + " ORDER BY "+KEY_ID + " ASC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {

            do {

                byte[] imgByte = cursor.getBlob(7);
                //String encodedstring = Base64.encodeToString(imgByte, 0);
                Bitmap bitmap = BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length);

                Model_DB_Declaration project = new Model_DB_Declaration(Integer.parseInt(cursor.getString(0)),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6),
                        null,
                        cursor.getString(8),
                        cursor.getString(9),
                        ""
                        );

                // Adding contact to list
                list_reached_site.add(project);
            } while (cursor.moveToNext());

        }

        // return contact list
        return list_reached_site;
    }

    public List<Model_DB_Declaration> Function_Get_All_Declaration() {

        // to retrieve bitmap as string using image url field

        List<Model_DB_Declaration> list_reached_site = new ArrayList<Model_DB_Declaration>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_DECLARATION ;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {

            do {

                byte[] imgByte = cursor.getBlob(7);

                String encodedstring = "";
                if (imgByte != null){
                    encodedstring = Base64.encodeToString(imgByte, 0);
                }


                Model_DB_Declaration project = new Model_DB_Declaration(Integer.parseInt(cursor.getString(0)),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6),
                        null,
                        cursor.getString(8),
                        cursor.getString(9),
                        encodedstring
                );

                // Adding contact to list
                list_reached_site.add(project);
            } while (cursor.moveToNext());

        }

        // return contact list
        return list_reached_site;
    }


    public Integer Function_Delete_Declaration(Model_DB_Declaration model_declaration) {

        System.out.println("### Function_Delete_Declaration ");

        SQLiteDatabase db = this.getWritableDatabase();
        Integer int_result = db.delete(TABLE_DECLARATION, KEY_ID + " = ?",
                new String[] { String.valueOf(model_declaration.getID()) });
        db.close();

        return int_result;
    }


    /******************************
     *  PTW REQUEST
     *******************************/

    public Long Function_Add_PTW_Request(Model_PTW_Request model_ptw_request) {

        Bitmap bitmap = model_ptw_request.getPtwcopy();

        byte[] data = null;
        if (bitmap != null){
             data = getBitmapAsByteArray(model_ptw_request.getPtwcopy());
        }


        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_DPR_ID, model_ptw_request.getDPR_ID());
        values.put(KEY_PTW_REQUIRED, model_ptw_request.getPtwrequired());
        values.put(KEY_PTW_NO, model_ptw_request.getPtwno());
        values.put(KEY_PTW_COPY, data);

        // Inserting Row
        Long long_result = db.insert(TABLE_PTW_REQUEST, null, values);
        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection

        return long_result;
    }

    public int Function_Get_PTW_Request_Count() {

        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_PTW_REQUEST;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // return contact list
        return cursor.getCount();
    }

    public List<Model_PTW_Request> Function_Get_All_PTW_REQUEST() {
        List<Model_PTW_Request> list_reached_site = new ArrayList<Model_PTW_Request>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_PTW_REQUEST;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {


            do {

                byte[] imgByte = cursor.getBlob(4);

                Bitmap bitmap = null;
                if (imgByte != null){
                     bitmap = BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length);
                }


                Model_PTW_Request project = new Model_PTW_Request(Integer.parseInt(cursor.getString(0)),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        bitmap
                );

                // Adding contact to list
                list_reached_site.add(project);
            } while (cursor.moveToNext());

        }

        // return contact list
        return list_reached_site;
    }

    public Model_PTW_Request Function_Get_PTW_Request(String str_dpr_id) {

        Model_PTW_Request project = null;
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_PTW_REQUEST + " WHERE "+KEY_DPR_ID + " = "+str_dpr_id;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {


            byte[] imgByte = cursor.getBlob(4);
            Bitmap bitmap = null;
            if (imgByte != null){
                bitmap = BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length);
            }

             project = new Model_PTW_Request(Integer.parseInt(cursor.getString(0)),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    bitmap
            );

        }

        // return contact list
        return project;
    }

    public boolean Function_Is_PTW_Request_Exist(String str_dpr_id) {

        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_PTW_REQUEST + " WHERE "+KEY_DPR_ID + " = "+str_dpr_id;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        // return contact list
        return cursor.moveToFirst();
    }


    public Integer Function_Delete_PTW_Request(Model_PTW_Request model_ptw_request) {

        System.out.println("### Function_Delete_PTW_Request ");

        SQLiteDatabase db = this.getWritableDatabase();
        Integer int_result = db.delete(TABLE_PTW_REQUEST, KEY_ID + " = ?",
                new String[] { String.valueOf(model_ptw_request.getID()) });
        db.close();

        return int_result;
    }

    /******************************
     *  START WORK
     *******************************/

    public Long Function_Add_Start_Work(Model_DPR_ID model_dpr_id) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_DPR_ID, model_dpr_id.getDPR_ID()); // Model_PTW_Status Name

        // Inserting Row
        Long long_result = db.insert(TABLE_START_WORK, null, values);
        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection

        return long_result;
    }

    public int Function_Get_Start_Work_Count() {

        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_START_WORK;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // return contact list
        return cursor.getCount();
    }

    public List<Model_DPR_ID> Function_Get_All_Start_Work() {
        List<Model_DPR_ID> list_reached_site = new ArrayList<Model_DPR_ID>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_START_WORK;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {


            do {

                Model_DPR_ID project = new Model_DPR_ID(Integer.parseInt(cursor.getString(0)),
                        cursor.getString(1));

                // Adding contact to list
                list_reached_site.add(project);
            } while (cursor.moveToNext());

        }

        // return contact list
        return list_reached_site;
    }


    public Boolean Function_Is_Start_Work_Exist(String str_dpr_id) {

        Boolean bol_is_exist = false ;
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_START_WORK + " WHERE "+KEY_DPR_ID + " = "+str_dpr_id;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {

            bol_is_exist = true;

        }

        // return contact list
        return bol_is_exist;
    }

    public Integer Function_Delete_Start_Work(Model_DPR_ID model_dpr_id) {

        System.out.println("### Function_Delete_Start_Work ");

        SQLiteDatabase db = this.getWritableDatabase();
        Integer int_result = db.delete(TABLE_START_WORK, KEY_ID + " = ?",
                new String[] { String.valueOf(model_dpr_id.getID()) });
        db.close();
        return int_result;
    }

    /******************************
     *  OHS WORK
     *******************************/

    public Long Function_Add_OHS_Work(Model_OHS_Work model_ohs_work) {

        byte[] data = getBitmapAsByteArray(model_ohs_work.getImage());

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_DPR_ID, model_ohs_work.getDPR_ID());
        values.put(KEY_USER_ID, model_ohs_work.getUserid());
        values.put(KEY_IMAGE, data);
        values.put(KEY_TIME, model_ohs_work.getTime());

        // Inserting Row
        Long long_result = db.insert(TABLE_OHS_WORK_PHOTO, null, values);
        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection

        return long_result;
    }

    public int Function_Get_OHS_Work_Count() {

        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_OHS_WORK_PHOTO;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // return contact list
        return cursor.getCount();
    }

    public List<Model_OHS_Work> Function_Get_All_OHS_Work() {
        List<Model_OHS_Work> list_reached_site = new ArrayList<Model_OHS_Work>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_OHS_WORK_PHOTO;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {

            byte[] imgByte = cursor.getBlob(3);
            Bitmap bitmap = BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length);

            do {
                Model_OHS_Work project = new Model_OHS_Work(Integer.parseInt(cursor.getString(0)),
                        cursor.getString(1),
                        cursor.getString(2),
                        bitmap,
                        cursor.getString(4));

                // Adding contact to list
                list_reached_site.add(project);
            } while (cursor.moveToNext());

        }

        // return contact list
        return list_reached_site;
    }


    public Integer Function_Delete_OHS_Work(Model_OHS_Work model_ohs_work) {

        System.out.println("### Function_Delete_OHS_Work ");

        SQLiteDatabase db = this.getWritableDatabase();
        Integer int_result = db.delete(TABLE_OHS_WORK_PHOTO, KEY_ID + " = ?",
                new String[] { String.valueOf(model_ohs_work.getID()) });
        db.close();
        return int_result;
    }

    /******************************
     *  WORK STATUS
     *******************************/

    public void Function_Add_Work_Status(Model_Work_Status model_work_status) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_DPR_ID, model_work_status.getDPR_ID()); // Model_PTW_Status Name
        values.put(KEY_FINAL_AT, model_work_status.getFinal_at()); // Model_PTW_Status Name
        values.put(KEY_FIELD_WORK, model_work_status.getField_work()); // Model_PTW_Status Name

        // Inserting Row
        db.insert(TABLE_WORK_STATUS, null, values);
        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection

    }

    public int Function_Get_Work_Status_Count() {

        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_WORK_STATUS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // return contact list
        return cursor.getCount();
    }

    public List<Model_Work_Status> Function_Get_All_Work_Status() {
        List<Model_Work_Status> list_reached_site = new ArrayList<Model_Work_Status>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_WORK_STATUS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {

            do {
                Model_Work_Status project = new Model_Work_Status(Integer.parseInt(cursor.getString(0)),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3));

                // Adding contact to list
                list_reached_site.add(project);
            } while (cursor.moveToNext());

        }

        // return contact list
        return list_reached_site;
    }


    public void Function_Delete_Work_Status(Model_Work_Status model_work_status) {

        System.out.println("### Function_Delete_OHS_Work ");

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_WORK_STATUS, KEY_ID + " = ?",
                new String[] { String.valueOf(model_work_status.getID()) });
        db.close();
    }


    /*****************************
    * PTW
    *****************************/

    // code to add the new contact
    public void addPTW(Model_PTW_Status contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_DPR_ID, contact.getDPR_ID());

        System.out.println("### addPTW ptw_id"+contact.getDPR_ID());
        // Inserting Row
        db.insert(TABLE_PTW_STATUS, null, values);
        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection
    }

    // code to get the single contact
    public Model_PTW_Status getPTW(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_PTW_STATUS, new String[] { KEY_ID,
                        KEY_DPR_ID }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Model_PTW_Status contact = new Model_PTW_Status(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1));
        // return contact
        return contact;
    }

    // code to get all contacts in a list view
    public List<Model_PTW_Status> getAllPTW() {
        List<Model_PTW_Status> contactList = new ArrayList<Model_PTW_Status>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_PTW_STATUS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Model_PTW_Status contact = new Model_PTW_Status();
                contact.setID(Integer.parseInt(cursor.getString(0)));
                contact.setDPR_ID(cursor.getString(1));
                // Adding contact to list
                contactList.add(contact);
            } while (cursor.moveToNext());
        }

        // return contact list
        return contactList;
    }

    // code to update the single contact
    public int updatePTW(Model_PTW_Status contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_DPR_ID, contact.getDPR_ID());

        // updating row
        return db.update(TABLE_PTW_STATUS, values, KEY_ID + " = ?",
                new String[] { String.valueOf(contact.getID()) });
    }

    // Deleting single contact
    public void deletePTW(Model_PTW_Status contact) {

        System.out.println("### deletePTW dpr_id : "+contact.getDPR_ID());
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PTW_STATUS, KEY_ID + " = ?",
                new String[] { String.valueOf(contact.getID()) });
        db.close();

    }

    // Getting contacts Count
    public int getPTWCount() {
        String countQuery = "SELECT  * FROM " + TABLE_PTW_STATUS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }


    /****************************
    *  PHOTO UPLOAD
    ****************************/
    // code to add the new contact
    public void addPhotoUpload(Model_15_Minutes_Photo_Upload contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_DPR_ID, contact.getDPR_ID());

        System.out.println("### photo upload addPTW ptw_id"+contact.getDPR_ID());
        // Inserting Row
        db.insert(TABLE_PHOTO_UPLOAD, null, values);
        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection
    }

    // code to get all contacts in a list view
    public List<Model_15_Minutes_Photo_Upload> getAllPhotoUpload() {
        List<Model_15_Minutes_Photo_Upload> contactList = new ArrayList<Model_15_Minutes_Photo_Upload>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_PHOTO_UPLOAD;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Model_15_Minutes_Photo_Upload contact = new Model_15_Minutes_Photo_Upload();
                contact.setID(Integer.parseInt(cursor.getString(0)));
                contact.setDPR_ID(cursor.getString(1));
                // Adding contact to list
                contactList.add(contact);
            } while (cursor.moveToNext());
        }

        // return contact list
        return contactList;
    }

    // Deleting single contact
    public void deletePhotoUpload(Model_15_Minutes_Photo_Upload contact) {

        System.out.println("### deletePhotoUpload ptw_id : " + contact.getDPR_ID());

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PHOTO_UPLOAD, KEY_ID + " = ?",
                new String[] { String.valueOf(contact.getID()) });
        db.close();
    }

}
