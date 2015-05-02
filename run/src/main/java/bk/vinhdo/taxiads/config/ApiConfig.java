package bk.vinhdo.taxiads.config;

/**
 * Created by vinhdo on 4/30/15.
 */
public class ApiConfig {
    private static final String URL_BASE = "http://192.168.1.31/doan/";
    public static final String URL_CREATE_ADDRESS = URL_BASE + "address/create";
    public static final String URL_GET_INFO_ADDRESS = URL_BASE + "address/getinfo";
    public static final String URL_UPDATE_ADDRESS = URL_BASE + "address/update";
    public static final String URL_APPROVE_ADDRESS = URL_BASE + "address/approve";
    public static final String URL_REPORT_ADDRESS = URL_BASE + "address/report";
    public static final String URL_LIKE_ADDRESS = URL_BASE + "address/like";
    public static final String URL_GET_LIST_ADDRESS = URL_BASE + "address/getlist";

    public static final String URL_CREATE_POST = URL_BASE + "post/create";
    public static final String URL_EDIT_POST = URL_BASE + "post/edit";
    public static final String URL_DELETE_POST = URL_BASE + "post/delete";
    public static final String URL_GET_INFO_POST = URL_BASE + "post/getinfo";

    public static final String URL_LOGIN = URL_BASE + "user/login";
    public static final String URL_LOGOUT = URL_BASE + "user/logout";
    public static final String URL_REGISTER = URL_BASE + "user/register";
    public static final String URL_EDIT_PROFILE = URL_BASE + "user/editprofile";
    public static final String URL_FORGOT_PASSWORD = URL_BASE + "user/forgotpassword";
    public static final String URL_GET_PROFILE = URL_BASE + "user/getprofile";

    public static final String PARAM_LAT = "lat";
    public static final String PARAM_LNG = "lng";
    public static final String PARAM_LIMIT = "limit";
    public static final String PARAM_EMAIL = "email";
    public static final String PARAM_PASSWORD = "password";
    public static final String PARAM_ACCESS_TOKEN = "access_token";

    public static final String URL_LOGIN_WITH_FACEBOOK = URL_BASE + "user/loginWithFacebook";;
}
