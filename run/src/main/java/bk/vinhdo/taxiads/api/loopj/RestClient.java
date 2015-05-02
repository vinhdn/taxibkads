package bk.vinhdo.taxiads.api.loopj;

import com.google.android.gms.common.api.Api;
import com.google.android.gms.maps.model.LatLng;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

import bk.vinhdo.taxiads.config.ApiConfig;


public class RestClient {

    static {
        LoopjRestClient.setHandleError(new HandleErrorMessage());
    }

    public static RestClient synchronize() {
        LoopjRestClient.synchronize();
        return new RestClient();
    }

    public static void downloadFile(String url, FileAsyncHttpResponseHandler responseHandler) {
        LoopjRestClient.download(url, responseHandler);
    }

    public static void login(String email, String password, TextHttpResponseHandler responseHandler) {
        RequestParams params = new RequestParams();
        params.put(ApiConfig.PARAM_EMAIL, email);
        params.put(ApiConfig.PARAM_PASSWORD, password);
        LoopjRestClient.post(ApiConfig.URL_LOGIN, params, null, responseHandler);
    }

    public static void loginFacebook(String facebookToken, TextHttpResponseHandler responseHandler) {
        RequestParams params = new RequestParams();
        params.put(ApiConfig.PARAM_ACCESS_TOKEN, facebookToken);
        LoopjRestClient.post(ApiConfig.URL_LOGIN_WITH_FACEBOOK, params, null, responseHandler);
    }

    public static void getListAddress(double lat, double lng, int limit, TextHttpResponseHandler responseHandler){
        RequestParams params = new RequestParams();
        params.put(ApiConfig.PARAM_LAT,lat);
        params.put(ApiConfig.PARAM_LNG,lng);
        params.put(ApiConfig.PARAM_LIMIT,limit);

        LoopjRestClient.post(ApiConfig.URL_GET_LIST_ADDRESS,params,responseHandler);
    }

}
