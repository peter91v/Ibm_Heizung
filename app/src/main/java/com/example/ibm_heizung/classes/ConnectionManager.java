package com.example.ibm_heizung.classes;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.os.Build;

public class ConnectionManager {

    public static String getConnectionType(String sUrl1, String sUrl2, Context context) {
        String selectedUrl = " ";

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkCapabilities capabilities = cm.getNetworkCapabilities(cm.getActiveNetwork());
            if (capabilities != null) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    selectedUrl = sUrl1;
                }
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    selectedUrl = sUrl2;
                }
            }
        }
        return selectedUrl;
    }
}
