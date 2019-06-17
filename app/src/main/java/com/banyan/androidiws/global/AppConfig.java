package com.banyan.androidiws.global;

public class AppConfig {

//    public static final String BASE_URL = "http://epictech.in/androidiws/Apidata/"; // live url
    public static final String BASE_URL = "http://epictech.in/iwsone/Apidata/"; // testing url

    public static final String URL_FORGOT_PASSWORD = BASE_URL + "forgot";
    public static final String URL_LOGIN = BASE_URL + "login";
    public static final String URL_LOGOUT = BASE_URL + "logout";

    public static final String URL_ATTENDANCE  = BASE_URL + "checkin";

    public static final String URL_LEAVE_BALANCE  = BASE_URL + "leavelist";
    public static final String URL_LEAVE_FILTER  = BASE_URL + "leavefilter";
    public static final String URL_LEAVE_TYPE  = BASE_URL + "leavetype";
    public static final String URL_LEAVE_APPLY  = BASE_URL + "leaveapply";

    public static final String URL_PROFILE  = BASE_URL + "profilelists";

    public static final String URL_CHANGE_PASSWORD = BASE_URL + "changepassword";

    public static final String URL_ATTENDANCE_STATUS = BASE_URL + "getstatus";

    public static final String URL_ATTENDANCE_REPORT = BASE_URL + "attendancelists";
    public static final String URL_ATTENDANCE_REPORT_FOR_MONTH = BASE_URL + "mnthattendancestatus";
}