package com.banyan.androidiws.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
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


        db.execSQL(CREATE_CONTACTS_TABLE);
        db.execSQL(CREATE_PROFILE_TABLE);
        db.execSQL(CREATE_DASHBOARD_TABLE);
        db.execSQL(CREATE_IN_PROGRESS_PROJECT_TABLE);

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
