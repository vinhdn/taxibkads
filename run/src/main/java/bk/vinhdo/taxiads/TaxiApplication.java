package bk.vinhdo.taxiads;

import android.app.Application;
import android.content.Context;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.maps.model.Marker;

import io.fabric.sdk.android.Fabric;
import java.util.ArrayList;
import java.util.HashMap;

import bk.vinhdo.taxiads.models.Address;

/**
 * Created by Vinh on 1/21/15.
 */
public class TaxiApplication extends Application {

    private static Context mContext;
    private ArrayList<Address> listAddress;

    private HashMap<String, Marker> listMarker;

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        this.mContext = getApplicationContext();
        this.listAddress = new ArrayList<>();
        this.listMarker = new HashMap<>();
    }

    public static Context getAppContext(){
        return mContext;
    }

    public HashMap<String, Marker> getListMarker() {
        return listMarker;
    }

    public void setListMarker(HashMap<String, Marker> listMarker) {
        this.listMarker = listMarker;
    }

    public ArrayList<Address> getListAddress() {
        return listAddress;
    }

    public void setListAddress(ArrayList<Address> listAddress) {
        this.listAddress = listAddress;
    }
}
