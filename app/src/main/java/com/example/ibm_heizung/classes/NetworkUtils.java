package com.example.ibm_heizung.classes;

import android.content.Context;
import android.content.res.XmlResourceParser;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.ibm_heizung.R;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NetworkUtils {

    public interface DomainReachabilityCallback {
        void onDomainReachabilityChecked(String domain, boolean isReachable);
    }

    public static List<String> getDomainsFromNetworkSecurityConfig(Context context) {
        List<String> domains = new ArrayList<>();
        XmlResourceParser parser = context.getResources().getXml(R.xml.network_security_config);
        try {
            while (parser.getEventType() != XmlPullParser.END_DOCUMENT) {
                if (parser.getEventType() == XmlPullParser.START_TAG && parser.getName().equals("domain")) {
                    String domain = parser.nextText();
                    if (domain != null) {
                        domains.add(domain);
                    }
                }
                parser.next();
            }
        } catch (IOException | XmlPullParserException e) {
            e.printStackTrace();
        } finally {
            parser.close();
        }
        return domains;
    }

    public static void checkDomainReachability(Context context, final DomainReachabilityCallback callback) {
        List<String> domains = getDomainsFromNetworkSecurityConfig(context);
        ExecutorService executor = Executors.newFixedThreadPool(5); // Begrenzung auf 5 Threads
        for (final String domain : domains) {
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    boolean isReachable = isDomainReachable(domain);
                    notifyMainThread(domain, isReachable, callback);
                }
            });
        }
        executor.shutdown();
    }

    static boolean isDomainReachable(String url) {
        HttpURLConnection connection = null;
        try {
            URL targetUrl = new URL("http://" + url);
            connection = (HttpURLConnection) targetUrl.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.connect();
            int responseCode = connection.getResponseCode();
            return responseCode == HttpURLConnection.HTTP_OK;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    private static void notifyMainThread(final String domain, final boolean isReachable, final DomainReachabilityCallback callback) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                callback.onDomainReachabilityChecked(domain, isReachable);
            }
        });
    }
}
