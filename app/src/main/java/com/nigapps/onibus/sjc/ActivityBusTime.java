package com.nigapps.onibus.sjc;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nigapps.onibus.sjc.entities.BusItem;
import com.nigapps.onibus.sjc.entities.SiteResponse;
import com.nigapps.onibus.sjc.manager.AppManager;

import java.util.List;

public class ActivityBusTime extends Activity {

    private static final String TAG = ActivityBusTime.class.getSimpleName();
/*
    AdView adView;
    InterstitialAd interstitialAd;
    NativeContentAdView nativeAdView;
*/
    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_time);

//        refreshAd(false, true);

 /*       adView = findViewById(R.id.adViewBusTime);
        AdRequest adRequest;
        if(AppManager.isDebugMode()) {
            adRequest = new AdRequest.Builder()
                    .addTestDevice(Constants.AD_TEST_DEVICE)
                    .build();
        } else {
            adRequest = new AdRequest.Builder()
                    .build();
        }
        adView.loadAd(adRequest);

        if (!AppManager.intersticialAdShowed) {
            interstitialAd = new InterstitialAd(ActivityBusTime.this);//AppManager.loadIntesrtitialAd(ActivityBusTime.this);
            interstitialAd.setAdUnitId("ca-app-pub-4768510961285493/8208937098");
            if(AppManager.isDebugMode()) {
                adRequest = new AdRequest.Builder()
                        .addTestDevice(Constants.AD_TEST_DEVICE)
                        .build();
            } else {
                adRequest = new AdRequest.Builder()
                        .build();
            }
            interstitialAd.loadAd(adRequest);
        }
*/
        //AppManager.loadAdView(adView);

        AppCompatImageView btnBack = findViewById(R.id.btn_back_);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        showBusHistory();
    }

    private void showBusHistory() {
        TextView numDaLinhaTitle = findViewById(R.id.num_da_linha_title);
        TextView numDaLinha = findViewById(R.id.num_da_linha);
        TextView nomeDaLinhaTitle = findViewById(R.id.nome_da_linha_title);
        TextView nomeDaLinha = findViewById(R.id.nome_da_linha);
        TextView sentidoTitle = findViewById(R.id.sentido_title);
        TextView sentido = findViewById(R.id.sentido);
        TextView itinerarioTitle = findViewById(R.id.itinerario_title);
        TextView itinerario = findViewById(R.id.itinerario);
        TextView diasUteisTitle = findViewById(R.id.segunda_a_sexta_title);
        TextView sabadoTitle = findViewById(R.id.sabado_title);
        TextView domingoTitle = findViewById(R.id.dom_title);
        ViewGroup segSexGridContainer = findViewById(R.id.seg_a_sex_grid_container);
        ViewGroup sabGridContainer = findViewById(R.id.sab_grid_container);
        ViewGroup domGridContainer = findViewById(R.id.dom_grid_container);
        TextView obsText = findViewById(R.id.obs_text);

        int gridCreated = 0;
        int i;
        SiteResponse blogPosts = AppManager.CURRENT_BUS_DATA;
        BusItem post;
        int postIndex;
        for(i = 0; i < blogPosts.getPosts().size(); i++) {
            post = blogPosts.getPosts().get(i);
            switch (post.getText()) {
                case Constants.NUMERO_DA_LINHA:
                    postIndex = i + 1;
                    String postText = "Ônibus " + blogPosts.getPosts().get(postIndex).getText();
                    numDaLinhaTitle.setText(post.getText());
                    numDaLinha.setText(blogPosts.getPosts().get(i + 1).getText());
                    ((TextView) findViewById(R.id.bus_time_title)).setText(postText);
                    break;
                case Constants.NOME_DA_LINHA:
                    postIndex = i + 1;
                    nomeDaLinhaTitle.setText(post.getText());
                    nomeDaLinha.setText(blogPosts.getPosts().get(postIndex).getText());
                    break;
                case Constants.SENTIDO:
                    postIndex = i + 1;
                    sentidoTitle.setText(post.getText());
                    sentido.setText(blogPosts.getPosts().get(postIndex).getText());
                    break;
                case Constants.ITINERARIO:
                    postIndex = i + 1;
                    itinerarioTitle.setText(post.getText());
                    itinerario.setText(blogPosts.getPosts().get(postIndex).getText());
                    break;
            }
        }
        for (i = 0; i < blogPosts.getDiasUteisList().size(); i++) {
            post = blogPosts.getDiasUteisList().get(i);
            if(post.getText().equals(Constants.DE_SEGUNDA_A_SEXTA)) {
                diasUteisTitle.setText(post.getText());
            } else if (i >= 2) {
                if(gridCreated == 0) {
                    gridCreated = 1;
                    int cols = 4;
                    int rows = (blogPosts.getDiasUteisList().size() - 2) / 4;
                 //   Log.d(TAG, "====> DIAS UTEIS ROWS SIZE : " + String.valueOf(rows));

                    ViewGroup rowView = null;
                    for (int j = 0; j < rows; j++) {
                        rowView = (ViewGroup) getLayoutInflater().inflate(R.layout.bus_row, segSexGridContainer);
                 //       Log.d(TAG, "====> DIAS UTEIS CONTAINER CHILDS ====> : " + rowView.getChildCount());
                    }
                    if(rowView != null) {
                        ViewGroup containerGrid;
                        for (int k = 0; k < rowView.getChildCount(); k++) {
                            containerGrid = (ViewGroup) rowView.getChildAt(k);
                            ((TextView) containerGrid.getChildAt(0)).
                                    setText(blogPosts.getDiasUteisList().get(k * cols + 2).getText());
                            ((TextView) containerGrid.getChildAt(1)).
                                    setText(blogPosts.getDiasUteisList().get(k * cols + 3).getText());
                            ((TextView) containerGrid.getChildAt(2)).
                                    setText(blogPosts.getDiasUteisList().get(k * cols + 4).getText());
                            ((TextView) containerGrid.getChildAt(3)).
                                    setText(blogPosts.getDiasUteisList().get(k * cols + 5).getText());
                        }
                        containerGrid = (ViewGroup) rowView.getChildAt(0);
                        ((TextView) containerGrid.getChildAt(0)).setTypeface(Typeface.DEFAULT_BOLD);
                        ((TextView) containerGrid.getChildAt(1)).setTypeface(Typeface.DEFAULT_BOLD);
                        ((TextView) containerGrid.getChildAt(2)).setTypeface(Typeface.DEFAULT_BOLD);
                        ((TextView) containerGrid.getChildAt(3)).setTypeface(Typeface.DEFAULT_BOLD);
                    }
                }
              //  Log.d(TAG, "=====> DIAS UTEIS SAÍDA [" + Constants.DE_SEGUNDA_A_SEXTA +"] =====> : " + post.getText());
            }
        }

        gridCreated = 0;
        if(blogPosts.getSabadosList().size() == 0) sabadoTitle.setPadding(0,0,0,0);
        for (i = 0; i < blogPosts.getSabadosList().size(); i++) {
            post = blogPosts.getSabadosList().get(i);
            if(post.getText().equals(Constants.AOS_SABADOS)) {
                sabadoTitle.setText(post.getText());
            } else if (i >= 2) {
                if(gridCreated == 0) {
                    gridCreated = 1;
                    int cols = 4;
                    int rows = (blogPosts.getSabadosList().size() - 2) / 4;
              //      Log.d(TAG, "====> SABADO ROWS SIZE : " + String.valueOf(rows));

                    ViewGroup rowView = null;
                    for (int j = 0; j < rows; j++) {
                        rowView = (ViewGroup) getLayoutInflater().inflate(R.layout.bus_row, sabGridContainer);
                //        Log.d(TAG, "====> SABADO CONTAINER CHILDS ====> : " + rowView.getChildCount());
                    }
                    if (rowView != null) {
                        ViewGroup containerGrid;
                        for (int k = 0; k < rowView.getChildCount(); k++) {
                            containerGrid = (ViewGroup) rowView.getChildAt(k);
                            ((TextView) containerGrid.getChildAt(0)).
                                    setText(blogPosts.getSabadosList().get(k * cols + 2).getText());
                            ((TextView) containerGrid.getChildAt(1)).
                                    setText(blogPosts.getSabadosList().get(k * cols + 3).getText());
                            ((TextView) containerGrid.getChildAt(2)).
                                    setText(blogPosts.getSabadosList().get(k * cols + 4).getText());
                            ((TextView) containerGrid.getChildAt(3)).
                                    setText(blogPosts.getSabadosList().get(k * cols + 5).getText());
                        }
                        containerGrid = (ViewGroup) rowView.getChildAt(0);
                        ((TextView) containerGrid.getChildAt(0)).setTypeface(Typeface.DEFAULT_BOLD);
                        ((TextView) containerGrid.getChildAt(1)).setTypeface(Typeface.DEFAULT_BOLD);
                        ((TextView) containerGrid.getChildAt(2)).setTypeface(Typeface.DEFAULT_BOLD);
                        ((TextView) containerGrid.getChildAt(3)).setTypeface(Typeface.DEFAULT_BOLD);
                    }
                }
            //    Log.d(TAG, "=====> SABADO SAÍDA [" + Constants.AOS_SABADOS +"] =====> : " + post.getText());
            }
        }
        gridCreated = 0;
        if(blogPosts.getDomingosList().size() == 0) domingoTitle.setPadding(0,0,0,0);
        for (i = 0; i < blogPosts.getDomingosList().size(); i++) {
            post = blogPosts.getDomingosList().get(i);
            if(post.getText().equals(Constants.AOS_DOMINGOS_E_FERIADOS)) {
                domingoTitle.setText(post.getText());
            } else if (i >= 2) {
                if(gridCreated == 0) {
                    gridCreated = 1;
                    int cols = 4;
                    int rows = (blogPosts.getDomingosList().size() - 2) / 4;
                //    Log.d(TAG, "====> DOMINGOS ROWS SIZE : " + String.valueOf(rows));

                    ViewGroup rowView = null;
                    for (int j = 0; j < rows; j++) {
                        rowView = (ViewGroup) getLayoutInflater().inflate(R.layout.bus_row, domGridContainer);
               //         Log.d(TAG, "====> DOMINGOS CONTAINER CHILDS ====> : " + rowView.getChildCount());
                    }
                    if (rowView != null) {
                        ViewGroup containerGrid;
                        for (int k = 0; k < rowView.getChildCount(); k++) {
                            containerGrid = (ViewGroup) rowView.getChildAt(k);
                            ((TextView) containerGrid.getChildAt(0)).
                                    setText(blogPosts.getDomingosList().get(k * cols + 2).getText());
                            ((TextView) containerGrid.getChildAt(1)).
                                    setText(blogPosts.getDomingosList().get(k * cols + 3).getText());
                            ((TextView) containerGrid.getChildAt(2)).
                                    setText(blogPosts.getDomingosList().get(k * cols + 4).getText());
                            ((TextView) containerGrid.getChildAt(3)).
                                    setText(blogPosts.getDomingosList().get(k * cols + 5).getText());
                        }
                        containerGrid = (ViewGroup) rowView.getChildAt(0);
                        ((TextView) containerGrid.getChildAt(0)).setTypeface(Typeface.DEFAULT_BOLD);
                        ((TextView) containerGrid.getChildAt(1)).setTypeface(Typeface.DEFAULT_BOLD);
                        ((TextView) containerGrid.getChildAt(2)).setTypeface(Typeface.DEFAULT_BOLD);
                        ((TextView) containerGrid.getChildAt(3)).setTypeface(Typeface.DEFAULT_BOLD);
                    }
                }
            //    Log.d(TAG, "=====> DOMINGOS SAÍDA [" + Constants.AOS_DOMINGOS_E_FERIADOS +"] =====> : " + post.getText());
            }
        }
        gridCreated = 0;
        for (i = 0; i < blogPosts.getSabDomFeriadosList().size(); i++) {
            post = blogPosts.getSabDomFeriadosList().get(i);
            if(post.getText().equals(Constants.AOS_SABADOS_DOMINGOS_E_FERIADOS)) {
                sabadoTitle.setText(post.getText());
                sabadoTitle.setPadding(8,16,8,16);
            } else if (i >= 2) {
                if(gridCreated == 0) {
                    gridCreated = 1;
                    int cols = 4;
                    int rows = (blogPosts.getSabDomFeriadosList().size() - 2) / 4;
             //       Log.d(TAG, "====> SAB DOM FER ROWS SIZE : " + String.valueOf(rows));

                    ViewGroup rowView = null;
                    for (int j = 0; j < rows; j++) {
                        rowView = (ViewGroup) getLayoutInflater().inflate(R.layout.bus_row, sabGridContainer);
             //           Log.d(TAG, "====> DOMINGOS e FERIADOS CONTAINER CHILDS ====> : " + rowView.getChildCount());
                    }
                    if (rowView != null) {
                        ViewGroup containerGrid;
                        for (int k = 0; k < rowView.getChildCount(); k++) {
                            containerGrid = (ViewGroup) rowView.getChildAt(k);
                            ((TextView) containerGrid.getChildAt(0)).
                                    setText(blogPosts.getSabDomFeriadosList().get(k * cols + 2).getText());
                            ((TextView) containerGrid.getChildAt(1)).
                                    setText(blogPosts.getSabDomFeriadosList().get(k * cols + 3).getText());
                            ((TextView) containerGrid.getChildAt(2)).
                                    setText(blogPosts.getSabDomFeriadosList().get(k * cols + 4).getText());
                            ((TextView) containerGrid.getChildAt(3)).
                                    setText(blogPosts.getSabDomFeriadosList().get(k * cols + 5).getText());
                        }
                        containerGrid = (ViewGroup) rowView.getChildAt(0);
                        ((TextView) containerGrid.getChildAt(0)).setTypeface(Typeface.DEFAULT_BOLD);
                        ((TextView) containerGrid.getChildAt(1)).setTypeface(Typeface.DEFAULT_BOLD);
                        ((TextView) containerGrid.getChildAt(2)).setTypeface(Typeface.DEFAULT_BOLD);
                        ((TextView) containerGrid.getChildAt(3)).setTypeface(Typeface.DEFAULT_BOLD);
                    }
                }
              //  Log.d(TAG, "=====> DOMINGOS SAÍDA [" + Constants.AOS_SABADOS_DOMINGOS_E_FERIADOS +"] =====> : " + post.getText());
            }
        }
        gridCreated = 0;
        for (i = 0; i < blogPosts.getSegDomFeriadosList().size(); i++) {
            post = blogPosts.getSegDomFeriadosList().get(i);
            if(post.getText().equals(Constants.DE_SEGUNDA_A_DOMINGO_E_FERIADOS)) {
                diasUteisTitle.setText(post.getText());
                diasUteisTitle.setPadding(8,16,8,16);
            } else if (i >= 2) {
                if(gridCreated == 0) {
                    gridCreated = 1;
                    int cols = 4;
                    int rows = (blogPosts.getSegDomFeriadosList().size() - 2) / 4;
                 //   Log.d(TAG, "====> SEG DOM FER ROWS SIZE : " + String.valueOf(rows));

                    ViewGroup rowView = null;
                    for (int j = 0; j < rows; j++) {
                        rowView = (ViewGroup) getLayoutInflater().inflate(R.layout.bus_row, segSexGridContainer);
                //        Log.d(TAG, "====> SEG, DOMINGOS e FERIADOS CONTAINER CHILDS ====> : " + rowView.getChildCount());
                    }
                    if (rowView != null) {
                        ViewGroup containerGrid;
                        for (int k = 0; k < rowView.getChildCount(); k++) {
                            containerGrid = (ViewGroup) rowView.getChildAt(k);
                            ((TextView) containerGrid.getChildAt(0)).
                                    setText(blogPosts.getSegDomFeriadosList().get(k * cols + 2).getText());
                            ((TextView) containerGrid.getChildAt(1)).
                                    setText(blogPosts.getSegDomFeriadosList().get(k * cols + 3).getText());
                            ((TextView) containerGrid.getChildAt(2)).
                                    setText(blogPosts.getSegDomFeriadosList().get(k * cols + 4).getText());
                            ((TextView) containerGrid.getChildAt(3)).
                                    setText(blogPosts.getSegDomFeriadosList().get(k * cols + 5).getText());
                        }
                        containerGrid = (ViewGroup) rowView.getChildAt(0);
                        ((TextView) containerGrid.getChildAt(0)).setTypeface(Typeface.DEFAULT_BOLD);
                        ((TextView) containerGrid.getChildAt(1)).setTypeface(Typeface.DEFAULT_BOLD);
                        ((TextView) containerGrid.getChildAt(2)).setTypeface(Typeface.DEFAULT_BOLD);
                        ((TextView) containerGrid.getChildAt(3)).setTypeface(Typeface.DEFAULT_BOLD);
                    }
                }
             //   Log.d(TAG, "=====> DOMINGOS SAÍDA [" + Constants.DE_SEGUNDA_A_DOMINGO_E_FERIADOS +"] =====> : " + post.getText());
            }
        }
        StringBuilder textObs = new StringBuilder();
        for (i = 0; i < blogPosts.getObsList().size(); i++) {
            post = blogPosts.getObsList().get(i);
            textObs.append(post.getText());
        }
            /*
            if(post.getText().equals(Constants.DE_SEGUNDA_A_DOMINGO_E_FERIADOS)) {
                diasUteisTitle.setText(post.getText());
                diasUteisTitle.setPadding(8,16,8,16);
            } else if (i >= 2) {
                if(gridCreated == 0) {
                    gridCreated = 1;
                    int cols = 4;
                    int rows = (blogPosts.getSegDomFeriadosList().size() - 2) / 4;
                    //   Log.d(TAG, "====> SEG DOM FER ROWS SIZE : " + String.valueOf(rows));

                    ViewGroup rowView = null;
                    for (int j = 0; j < rows; j++) {
                        rowView = (ViewGroup) getLayoutInflater().inflate(R.layout.bus_row, segSexGridContainer);
                        //        Log.d(TAG, "====> SEG, DOMINGOS e FERIADOS CONTAINER CHILDS ====> : " + rowView.getChildCount());
                    }
                    if (rowView != null) {
                        ViewGroup containerGrid;
                        for (int k = 0; k < rowView.getChildCount(); k++) {
                            containerGrid = (ViewGroup) rowView.getChildAt(k);
                            ((TextView) containerGrid.getChildAt(0)).
                                    setText(blogPosts.getSegDomFeriadosList().get(k * cols + 2).getText());
                            ((TextView) containerGrid.getChildAt(1)).
                                    setText(blogPosts.getSegDomFeriadosList().get(k * cols + 3).getText());
                            ((TextView) containerGrid.getChildAt(2)).
                                    setText(blogPosts.getSegDomFeriadosList().get(k * cols + 4).getText());
                            ((TextView) containerGrid.getChildAt(3)).
                                    setText(blogPosts.getSegDomFeriadosList().get(k * cols + 5).getText());
                        }
                        containerGrid = (ViewGroup) rowView.getChildAt(0);
                        ((TextView) containerGrid.getChildAt(0)).setTypeface(Typeface.DEFAULT_BOLD);
                        ((TextView) containerGrid.getChildAt(1)).setTypeface(Typeface.DEFAULT_BOLD);
                        ((TextView) containerGrid.getChildAt(2)).setTypeface(Typeface.DEFAULT_BOLD);
                        ((TextView) containerGrid.getChildAt(3)).setTypeface(Typeface.DEFAULT_BOLD);
                    }
                }
                //   Log.d(TAG, "=====> DOMINGOS SAÍDA [" + Constants.DE_SEGUNDA_A_DOMINGO_E_FERIADOS +"] =====> : " + post.getText());
            }*/
        obsText.setText(textObs.toString());

 /*       nativeAdView = AppManager.getInstance().getNativeContentAdView();
        //nativeAdView = AppManager.getInstance().getNativeAppInstallAdView();
        if(nativeAdView != null) {
            if(nativeAdView.getParent() != null) {
                ((ViewGroup) nativeAdView.getParent()).removeView(nativeAdView);
            }
            ((FrameLayout) findViewById(R.id.native_ad_container)).addView(nativeAdView);
        }*/
    }

    public void closeScreen(View view) {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
/*        if(!AppManager.intersticialAdShowed) {
            if (interstitialAd != null && interstitialAd.isLoaded()) {
                interstitialAd.setAdListener(new InterstitalAdListener());
                interstitialAd.show();
            }
        }*/
        super.onBackPressed();
    }
/*
    private class InterstitalAdListener extends AdListener{

        @Override
        public void onAdClosed() {
//            onBackPressed();
            AppManager.intersticialAdShowed = true;
 //           finish();
            super.onAdClosed();
        }
    }
*/
    @Override
    protected void onResume() {
        super.onResume();

//        if(adView != null) adView.resume();
    }

    @Override
    protected void onPause() {
 //       if(adView != null) adView.pause();

        super.onPause();
    }

    @Override
    protected void onDestroy() {
//        if(adView != null) adView.destroy();

        super.onDestroy();
    }
}