package com.raghu.thetapracticle.extra;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.raghu.thetapracticle.AppMain;

public class NetworkStatus {
    private static final String TAG = NetworkStatus.class.getSimpleName();
    private static NetworkStatus instance=new NetworkStatus();
    private ConnectivityManager connectivityManager;
    private NetworkInfo wifiInfo, mobileInfo;
    private boolean connected = false;

    public static NetworkStatus getInstance(){
        return instance;
    }

    /*
     * Check Internet connectivity
     * */
    public boolean isConnected(){
        try {
            connectivityManager = (ConnectivityManager) AppMain.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            connected = networkInfo != null && networkInfo.isAvailable() &&
                    networkInfo.isConnected();
            return connected;


        } catch (Exception e) {
            System.out.println("CheckConnectivity Exception: " + e.getMessage());
            Log.d(TAG, "isConnected: "+e.getMessage());
        }
        return connected;
    }
}
