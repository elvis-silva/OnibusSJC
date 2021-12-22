package com.nigapps.onibus.sjc.manager;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.nigapps.onibus.sjc.Constants;
import com.nigapps.onibus.sjc.MainActivity;
import com.nigapps.onibus.sjc.context.Contexts;
import com.nigapps.onibus.sjc.db.DataHandler;
import com.nigapps.onibus.sjc.entities.BusItem;
import com.nigapps.onibus.sjc.entities.SiteResponse;

import java.util.ArrayList;

public class AppManager {

    private static boolean debugMode;

    private static final String TAG = AppManager.class.getSimpleName();

    public static SiteResponse CURRENT_BUS_DATA;
    public static String CURRENT_BUS_ID;
    public static int CURRENT_BUS_POSITION;
    private static ArrayList<BusItem> busItems = new ArrayList<>(Constants.LINHAS.length);
    public static boolean intersticialAdShowed;
    private static AppManager instance;

    private AppManager(){}

    public static AppManager getInstance() {
        if(instance == null) {
            instance = new AppManager();
        }
        return instance;
    }

    public static boolean isDebugMode() {
        return debugMode;
    }

    public static void setDebugMode(boolean debug) {
        debugMode = debug;
    }

    public static Contexts getContexts() {
        return Contexts.getInstance();
    }

    public static Context getContext() {
        return getContexts().getContext();
    }

    public static Activity getActivity() {
        return getContexts().getCurrentActivity();
    }

    public static void initActivity(Activity activity) {
        getContexts().initActivity(activity);
    }

    public static DataHandler getDataHandler() {
        return getContexts().getDataHandler();
    }

    public static void closeDataHandler() {
        getDataHandler().close();
    }

    public static MainActivity getMainActivity() {
        return (MainActivity) getActivity();
    }

    public static ArrayList<BusItem> getBusItems() {
        return busItems;
    }

    public static boolean isConected(Context context) {
        ConnectivityManager cn = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo nf = null;
        if (cn != null) {
            nf = cn.getActiveNetworkInfo();
        }
        if (nf != null && nf.isConnected()) {
            Log.d(TAG,"Internet connection ok.!");
            return true;
        } else {
            Log.d(TAG, "No internet connection.!");
            return false;
        }
    }

    public static BusItem[] BUS_ITEM_FAVS = new BusItem[Constants.LINHAS.length];
/*
    public static AdRequest getAdRequest() {
        return adRequest;
    }

    public static void loadAdView(AdView adView) {
        initAdRequest();
        adView.loadAd(adRequest);
    }

    public static void loadNativeAdView(NativeExpressAdView nativeExpressAdView) {
        initAdRequest();
        nativeExpressAdView.loadAd(adRequest);
    }

    public static void loadNativeAdView(NativeContentAdView nativeExpressAdView, AdLoader pAdLoader) {
        initAdRequest();
        pAdLoader.loadAd(adRequest);
    }

    public static InterstitialAd loadIntesrtitialAd(Context context) {
        InterstitialAd interstitialAd = new InterstitialAd(context);
        interstitialAd.setAdUnitId("ca-app-pub-4768510961285493/8208937098");
        initAdRequest();
        interstitialAd.loadAd(adRequest);
        return interstitialAd;
    }
*/
    public static void checkPermission(Activity context) {
        // Verifica necessidade de verificacao de permissao
/*        if (ContextCompat.checkSelfPermission(context,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // Verifica necessidade de explicar necessidade da permissao
            if (ActivityCompat.shouldShowRequestPermissionRationale(context,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Toast.makeText(context, "É necessário dar acesso de escrita!", Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(context,
                        new String[]{
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                        }, 0);
            } else {
                // Solicita permissao
                ActivityCompat.requestPermissions(context,
                        new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        0);
            }
        }
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // Verifica necessidade de explicar necessidade da permissao
            if (ActivityCompat.shouldShowRequestPermissionRationale(context,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Toast.makeText(context, "É necessário dar acesso de escrita!", Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(context,
                        new String[]{
                                android.Manifest.permission.READ_EXTERNAL_STORAGE
                        }, 0);
            } else {
                // Solicita permissao
                ActivityCompat.requestPermissions(context,
                        new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                        0);
            }
        }*/
    }
/*
    public NativeAppInstallAdView getAdView() {
        return adView;
    }

    public NativeAppInstallAdView getNativeAppInstallAdView() {
        return nativeAppInstallAdView;
    }

    public NativeContentAdView getNativeContentAdView() {
        if(nativeContentAdView != null) {
            if (nativeContentAdView.getParent() == null) {
                FrameLayout container = new FrameLayout(getContext());
                container.addView(nativeContentAdView);
            }
        }
        return nativeContentAdView;
    }

    public void setNativeAppInstallAdView(NativeAppInstallAdView nativeAppInstallAdView) {
        this.nativeAppInstallAdView = nativeAppInstallAdView;
    }

    public void setNativeContentAdView(NativeContentAdView nativeContentAdView) {
        this.nativeContentAdView = nativeContentAdView;
    }*/
}