package com.nigapps.onibus.sjc.utils;

import android.content.Context;
import android.util.Log;

import com.facebook.ads.*;

public class IntersticialAdUtils {

    private static final String TAG = IntersticialAdUtils.class.getSimpleName();

    private Context context;
    private static IntersticialAdUtils instance;
    private InterstitialAd interstitialAd;

    public static IntersticialAdUtils getInstance() {
        if(instance == null) {
            instance = new IntersticialAdUtils();
        }
        return instance;
    }

    public void loadIntersticial(Context context) {
        IntersticialAdUtils.getInstance().context = context;


        AdSettings.addTestDevice("2fe05c7d-2502-49a5-9cbf-4fca849f0f9e");

        interstitialAd = new InterstitialAd(context, "236654850323965_236655036990613");

        interstitialAd.setAdListener(new InterstitialAdListener() {
            @Override
            public void onInterstitialDisplayed(Ad ad) {
                // Interstitial ad displayed callback
                //Log.e(TAG, "Interstitial ad displayed.");
            }

            @Override
            public void onInterstitialDismissed(Ad ad) {
                // Interstitial dismissed callback
                //Log.e(TAG, "Interstitial ad dismissed.");
            }

            @Override
            public void onError(Ad ad, AdError adError) {
                // Ad error callback
                //Log.e(TAG, "Interstitial ad failed to load: " + adError.getErrorMessage());
            }

            @Override
            public void onAdLoaded(Ad ad) {
                // Interstitial ad is loaded and ready to be displayed
                //Log.d(TAG, "Interstitial ad is loaded and ready to be displayed!");
                // Show the ad
                //interstitialAd.show();
            }

            @Override
            public void onAdClicked(Ad ad) {
                // Ad clicked callback
                //Log.d(TAG, "Interstitial ad clicked!");
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                // Ad impression logged callback
                //Log.d(TAG, "Interstitial ad impression logged!");
            }
        });

        // For auto play video ads, it's recommended to load the ad
        // at least 30 seconds before it is shown
        interstitialAd.loadAd();
    }

    public InterstitialAd getInterstitialAd() {
        return interstitialAd;
    }
}
