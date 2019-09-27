package com.banyan.androidiws.global;

public class AppConfig {

    public static final String BASE_URL = "https://erpiws.com/"; // live url
//    public static final String BASE_URL = "http://epictech.in/iwsnew/"; // testing url

    public static final String URL_APP_DATA = BASE_URL+"Apidata/"; // testing url

    public static final String URL_LOGIN = URL_APP_DATA + "employee_login";
    public static final String URL_FORGOT_PASSWORD = URL_APP_DATA + "forgot";
    public static final String URL_LOGOUT = URL_APP_DATA + "logout";

    // check in, out
    public static final String URL_ATTENDANCE_CHECK_IN  = URL_APP_DATA + "addattendanceclockin";
    public static final String URL_ATTENDANCE_CHECK_OUT  = URL_APP_DATA + "addattendanceclockout";
    public static final String URL_ATTENDANCE_CHECK  = URL_APP_DATA + "checkattendance";

    // attendance
    public static final String URL_ATTENDANCE  = URL_APP_DATA + "checkin";
    public static final String URL_ATTENDANCE_STATUS = URL_APP_DATA + "getstatus";
    public static final String URL_ATTENDANCE_REPORT = URL_APP_DATA + "attendancelists";
    public static final String URL_ATTENDANCE_REPORT_FOR_MONTH = URL_APP_DATA + "atttendance_employee";
    public static final String URL_PAYSLIP = BASE_URL + "Payslip/emp_payslip";

    public static final String URL_LEAVE_LIST = URL_APP_DATA + "leave_list";
    public static final String URL_LEAVE_TYPE  = URL_APP_DATA + "leavetype";
    public static final String URL_LEAVE_APPLY  = URL_APP_DATA + "leaveadd";
    public static final String URL_LEAVE_CANCEL  = URL_APP_DATA + "cancelleave";
    public static final String URL_LEAVE_CANCEL_REQUEST  = URL_APP_DATA + "cancelleaverequest";

    public static final String URL_NOTIFICATION_LIST = URL_APP_DATA + "notification";
    public static final String URL_DELETE_NOTIFICATION = URL_APP_DATA + "leave_list";

//    profile

    public static final String URL_UPDATE_PROFILE = URL_APP_DATA + "update_profile";
    public static final String URL_PROFILE  = URL_APP_DATA + "employee_profile";
    public static final String URL_CHANGE_PASSWORD = URL_APP_DATA + "changepasswordemp";

    public static final String URL_LEAVE_TRACK  = URL_APP_DATA + "employee_leavetracker";

    public static final String URL_EXPENSE  = URL_APP_DATA + "expense";
    public static final String URL_EXPENSE_DOWNLOAD  = BASE_URL + "Cron/expense_download/";
    public static final String URL_INVENTORY  = URL_APP_DATA + "inventorydetails";
    public static final String URL_IS_INVENTORY_CONFIRM  = URL_APP_DATA + "kufstatus";
    public static final String URL_INVENTORY_CONFIRM  = URL_APP_DATA + "updateinventory";

    public static final String URL_PRODUCTIVITY_NI_NPO_TK  = URL_APP_DATA + "productivity";
    public static final String URL_PRODUCTIVITY_DOWNLOAD  = BASE_URL + "cron/productivity_download/";

    // PROJECT UPDATE
    public static final String URL_PROJECT_UPDATE_SITE_LIST_COMLETED = URL_APP_DATA + "sites";
    public static final String URL_PROJECT_UPDATE_SITE_LIST_IN_PROGRESS  = URL_APP_DATA + "sites_progress";
    public static final String URL_PROJECT_UPDATE_REACHED_SITE  = URL_APP_DATA + "reachedsite";
    public static final String URL_PROJECT_UPDATE_UPDATE_REACHED_SITE  = URL_APP_DATA + "update_reachedtime";
    public static final String URL_PROJECT_UPDATE_START_TIME  = URL_APP_DATA + "starttime";
    public static final String URL_PROJECT_UPDATE_PTW_DECLARATION_REQUEST  = URL_APP_DATA + "update_ptwrequest";
    public static final String URL_PROJECT_UPDATE_GET_PTW  = URL_APP_DATA + "getptw";
    public static final String URL_PROJECT_UPDATE_UPDATE_PTW  = URL_APP_DATA + "updateptw";
    public static final String URL_PROJECT_UPDATE_UPLOAD_OHS_WORK_IMAGE  = URL_APP_DATA + "updateohspic";
    public static final String URL_PROJECT_UPDATE_UPDATE_WORK_STATUS  = URL_APP_DATA + "update_workstatus";
    public static final String URL_PROJECT_UPDATE_GET_LEFT_SITE  = URL_APP_DATA + "get_lefttime";
    public static final String URL_PROJECT_UPDATE_UPDATE_LEFT_SITE = URL_APP_DATA + "update_lefttime";
    public static final String URL_PROJECT_UPDATE_GET_EXPENSE  = URL_APP_DATA + "get_expense";
    public static final String URL_PROJECT_UPDATE_UPDATE_EXPENSE  = URL_APP_DATA + "update_expense";
    public static final String URL_PROJECT_UPDATE_WORK_COMPLETED  = URL_APP_DATA + "completetime";

    public static final long LONG_IMAGE_UPLOAD_DELAY_TIME  = 900000; //900000 15 minutes

}

