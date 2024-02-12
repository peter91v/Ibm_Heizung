package com.example.ibm_heizung.classes;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.os.Build;

public class ConnectionManager {

    public static String getConnectionType(String sUrl1, String sUrl2, Context context) {
        String selectedUrl = " ";

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && cm != null) {
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

    public static void main(String[] args) {
        // Beispielaufruf der Methode
        String sUrl1 = "" ;
        String sUrl2 = "https://example.com/mobile";
        // Annahme: context ist ein gültiger Android-Kontext
        Context context = null; // Setzen Sie den gültigen Kontext hier ein
        /*String selectedUrl = getConnectionType(sUrl1, sUrl2, context);*/
/*
        System.out.println("Selected URL: " + selectedUrl);
*/
    }
}
