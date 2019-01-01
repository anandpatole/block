package com.example.block.app_list;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;

/**
 * Created by Gulashan Faye on 12/5/2016.
 */
public class NetworkConnectivity {

    public static boolean isNetworkAvailable(Context context) {

        try {
            ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Network[] networks = connectivity.getAllNetworks();
                NetworkInfo networkInfo;
                for (Network mNetwork : networks) {
                    networkInfo = connectivity.getNetworkInfo(mNetwork);
                    if (networkInfo.getState().equals(NetworkInfo.State.CONNECTED)) {
                        return true;
                    }
                }
            } else {
                if (connectivity != null) {
                    NetworkInfo[] availableNetworks;
                    availableNetworks = connectivity.getAllNetworkInfo();
                    if (availableNetworks != null)
                        for (int i = 0; i < availableNetworks.length; i++)
                            if (availableNetworks[i].getState() == NetworkInfo.State.CONNECTED) {
                                return true;
                            }

                }
            }
            return false;
        } catch (NullPointerException e) {
            return false;
        }
    }


}
