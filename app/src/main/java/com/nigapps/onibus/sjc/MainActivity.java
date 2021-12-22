package com.nigapps.onibus.sjc;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.multidex.MultiDex;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.nigapps.onibus.sjc.adapters.ListViewAdapter;
import com.nigapps.onibus.sjc.entities.BusItem;
import com.nigapps.onibus.sjc.entities.NewsObject;
import com.nigapps.onibus.sjc.entities.SiteResponse;
import com.nigapps.onibus.sjc.manager.AppManager;
import com.nigapps.onibus.sjc.parsers.BlogPostParser;
import com.nigapps.onibus.sjc.utils.IntersticialAdUtils;
import com.nigapps.onibus.sjc.utils.StringUtils;
import com.nigapps.onibus.sjc.webrequesthandler.BaseHttpRequest;
import com.nigapps.onibus.sjc.webrequesthandler.IAsyncCallback;
import com.nigapps.onibus.sjc.webrequesthandler.WebResponse;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.facebook.ads.*;

//import ir.mtajik.android.advancedPermissionsHandler.PermissionHandlerActivity;

public class MainActivity extends AppCompatActivity {//PermissionHandlerActivity {

    private String TAG = MainActivity.class.getSimpleName();

    private TextView mTextMessage;
    private ProgressDialog progressDialog;
    private TextView btnShowBusLignes;
    private boolean conected, isBtnsMoreShowed;
    private FrameLayout containerScreen, moreContainer;
    private SCREEN currentScreen = SCREEN.MAIN;
   // private View ;
    private int exitControl = 0;
    private BusItem[] busItemsFavs = AppManager.BUS_ITEM_FAVS;
    private int busItemIndex;
    private int popup = 0;
    private LinearLayout favoriteContainer, containerFavItems;
    private ArrayList<BusItem> busItemsFav = new ArrayList<>();
 //   AdLoader mAdLoader;
    ViewGroup nativeExpressAdViewContainer, containerMore, screenView;
    BottomNavigationView navigation;
    View avisoView;

//    private AdLayout adView;

//    private AdView adView;
//    VideoController mVideoController;
    private Animation animation;
//    private InterstitialAd interstitialAd;

    //NEWS SJC
    String jsolNewsLink = "https://nigapps.com/json/news_sjc.json";
    ListView listView;
    List<NewsObject> newsObjectList;
    ProgressBar progressBar;
    private int interstitialAdCount = 0;

    enum SCREEN {
        MAIN, MORE, APPS
    }

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    if(!currentScreen.equals(SCREEN.MAIN)) {
                        if(isBtnsMoreShowed) hideBtnsMore();
                        currentScreen = SCREEN.MAIN;
                        showScreenMain();
                    }
                    return true;
               /* case R.id.navigation_apps:
                    if(!currentScreen.equals(SCREEN.APPS)) {
                        if(isBtnsMoreShowed) hideBtnsMore();
                        currentScreen = SCREEN.APPS;
                        showScreenMoreApps();
                    }
                    return true;*/
                case R.id.navigation_more:
                    if(!currentScreen.equals(SCREEN.MORE)) {
                        currentScreen = SCREEN.MORE;
                        showScreenNews();
                    }
                    //manageBtnsMore();
                    return true;
            }
            return false;
        }
    };

    String[] permissions = new String[] {
            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    boolean stickyMode = true;
    private AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MultiDex.install(this);

        clearAppCache();

        conected = AppManager.isConected(MainActivity.this);

        adView = new AdView(this, "YOUR_PLACEMENT_ID", AdSize.BANNER_HEIGHT_50);

        // Find the Ad Container
        LinearLayout adContainer = (LinearLayout) findViewById(R.id.banner_container);

        // Add the ad view to your activity layout
        adContainer.addView(adView);

        // Request an ad
        adView.loadAd();
//        AppManager.checkPermission(MainActivity.this);
/*
        if(conected) {
            adView = findViewById(R.id.adView);

            AdRequest adRequest;
            if (AppManager.isDebugMode()) {
                adRequest = new AdRequest.Builder()
                        .addTestDevice(Constants.AD_TEST_DEVICE)
                        .build();
            } else {
                adRequest = new AdRequest.Builder()
                        .build();
            }
            adView.loadAd(adRequest);
        }*/
        AppManager.initActivity(MainActivity.this);

        progressDialog = new ProgressDialog(MainActivity.this);

        navigation = findViewById(R.id.navigation);
        navigation.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.show_bottom_from_up));
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        moreContainer = findViewById(R.id.container_more);
        containerScreen = findViewById(R.id.container_screen);

        if (getIntent().getExtras() != null) {
            for (String key : getIntent().getExtras().keySet()) {
                Object value = getIntent().getExtras().get(key);
                Log.d(TAG, "[ =====> Key: " + key + " | Value: " + value + "<===== ]");
            }
        }

        showScreenMain();

        if(conected) verifySite();
/*
        askForPermission(permissions , stickyMode, new PermissionHandlerActivity.PermissionCallBack() {

            @Override
            public void onPermissionsGranted() {
                Log.i(TAG, "[ ======> onPermissionsGranted: <====== ]");
            }

            @Override
            public void onPermissionsDenied(String[] permissions) {
                Log.i(TAG, "[ ======> onPermissionsDenied: <====== ]");
            }
        });
*/
//        if(conected) refreshAd(true, false);
    }

    private void clearAppCache() {
        FileUtils.deleteQuietly(getCacheDir());
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);

 /*       if(requestCode == 1000) {
            if(AppManager.isConected(MainActivity.this) && interstitialAdCount == 0) {
                interstitialAd = new InterstitialAd(MainActivity.this);
                interstitialAd.setAdUnitId("ca-app-pub-4768510961285493/8208937098");
                interstitialAd.setAdListener(new InterstitalAdListener());
                AdRequest adRequest;
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
        }*/
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

/*        if(requestCode == 1000) {
            if (interstitialAd != null && interstitialAd.isLoaded() && interstitialAdCount == 0) {
                interstitialAd.show();
            }
            interstitialAdCount++;
            if(interstitialAdCount >= 2) {
                interstitialAdCount = 0;
            }
        }*/
    }

    @Override
    protected void onResume() {
        super.onResume();

 //       if(adView != null) adView.resume();
    }

    @Override
    protected void onPause() {
 //       if(adView != null) adView.pause();

        super.onPause();
    }

    @Override
    protected void onDestroy() {
        AppManager.closeDataHandler();

        if(adView != null) adView.destroy();

        if(IntersticialAdUtils.getInstance().getInterstitialAd() != null)
            IntersticialAdUtils.getInstance().getInterstitialAd().destroy();

        System.exit(0);
        super.onDestroy();
    }

    private void showDashboardScreen() {
        exitControl = 0;
        clearScreen();
        new FetchData().execute();
    }

    private void showScreenMain() {
        exitControl = 0;
        clearScreen();
        screenView = (ViewGroup) getLayoutInflater().inflate(R.layout.screen_main, containerScreen);
        favoriteContainer = screenView.findViewById(R.id.favorite_container);
        btnShowBusLignes = screenView.findViewById(R.id.btn_show_bus_lignes);
        btnShowBusLignes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideViews();
                if(isBtnsMoreShowed) hideBtnsMore();
                showBusOptionsIntent();
                currentScreen = SCREEN.MAIN;
                navigation.setSelectedItemId(R.id.navigation_home);
            }
        });

        loadLinhasFavoritas();
    }

    private void showScreenNews() {
        exitControl = 0;
        clearScreen();
        screenView = (ViewGroup) getLayoutInflater().inflate(R.layout.screen_news, containerScreen);

        listView = screenView.findViewById(R.id.listView);
        progressBar = screenView.findViewById(R.id.progressBar);
        newsObjectList = new ArrayList<>();
        loadNewsItens();
    }

    private void loadNewsItens() {
        if(!AppManager.isConected(MainActivity.this)) {
            Log.i(TAG, "APP NOT CONNECTED");
        } else {
            View tvConnectMsg = findViewById(R.id.tv_connect_msg);
            ViewGroup msgParent = (ViewGroup) tvConnectMsg.getParent();
            msgParent.removeView(tvConnectMsg);

            progressBar.setVisibility(View.VISIBLE);

            StringRequest stringRequest = new StringRequest(Request.Method.GET, jsolNewsLink,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressBar.setVisibility(View.INVISIBLE);

                            try {
                                response = StringUtils.jsonDecode(response);
                                JSONObject obj = new JSONObject(response);

                                //              LogUtils.i(MainActivity.class, response);

                                JSONArray newsArray = obj.getJSONArray("news");

                                for (int i = 0; i < newsArray.length(); i++) {
                                    JSONObject newsItem = newsArray.getJSONObject(i);

                                    String title = newsItem.getString("title");
                                    String resume = newsItem.getString("resume");
                                    String source = getString(R.string.source_title) +
                                            newsItem.getString("source");
                                    String url = newsItem.getString("url");
                                    String iconUrl = newsItem.getString("icon");

                                    NewsObject newsObject = new NewsObject(title, resume, source, url, iconUrl);
                                    // dataModel.setRequestCreator(Picasso.with(getApplicationContext()).load(iconUrl));

                                    newsObjectList.add(newsObject);
                                }

                                ListViewAdapter adapter = new ListViewAdapter(newsObjectList, MainActivity.this);

                                listView.setAdapter(adapter);
                                listView.setDividerHeight(0);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

            RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);

            requestQueue.add(stringRequest);
        }
    }

    private void showScreenMoreApps() {
        exitControl = 0;
        clearScreen();
        screenView = (ViewGroup) getLayoutInflater().inflate(R.layout.screen_more_apps, containerScreen);
 //       NativeContentAdView nativeAdView = AppManager.getInstance().getNativeContentAdView();
        //NativeAppInstallAdView nativeAdView = AppManager.getInstance().getNativeAppInstallAdView();
 /*       if(nativeAdView != null) {
            if(nativeAdView.getParent() != null) {
                ((ViewGroup) nativeAdView.getParent()).removeView(nativeAdView);
            }
            ((FrameLayout) screenView.findViewById(R.id.native_ad_container_view)).addView(nativeAdView);
        }*/
/*        screenView.findViewById(R.id.scroll_container).getViewTreeObserver().addOnScrollChangedListener(
                new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                if(isBtnsMoreShowed) hideBtnsMore();
                if(navigation.getSelectedItemId() != R.id.navigation_apps) {
                    navigation.setSelectedItemId(R.id.navigation_apps);
                    currentScreen = SCREEN.APPS;
                }
            }
        });

        ((ViewGroup)screenView.findViewById(R.id.container_choose)).
                addView(nativeExpressAdViewContainer, 0);

        NativeExpressAdView nativeExpressAdView = screenView.findViewById(R.id.native_ad_view);
        nativeExpressAdView.setVideoOptions(new VideoOptions.Builder()
                .setStartMuted(true)
                .build());
        mVideoController = nativeExpressAdView.getVideoController();
        mVideoController.setVideoLifecycleCallbacks(new VideoController.VideoLifecycleCallbacks() {
            @Override
            public void onVideoEnd() {
          //      Log.d(TAG, "Video playback is finished.");
                super.onVideoEnd();
            }
        });
        nativeExpressAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                if (mVideoController.hasVideoContent()) {
                    Log.d(TAG, "Received an ad that contains a video asset.");
                } else {
                    Log.d(TAG, "Received an ad that does not contain a video asset.");
                }
            }
        });

        AdRequest adRequest;
        if(AppManager.isDebugMode()) {
            adRequest = new AdRequest.Builder()
                    .addTestDevice(Constants.AD_TEST_DEVICE)
                    .build();
        } else {
            adRequest =new AdRequest.Builder().build();
        }

        nativeExpressAdView.loadAd(adRequest);
*/
    }

    public void loadLinhasFavoritas() {
        Cursor cursor = AppManager.getDataHandler().returnBusSjcTable();
        cursor.moveToFirst();
        int rows = cursor.getCount();
        if(rows > 0) {
            int i = 0;
            do {
                BusItem item = AppManager.getDataHandler().parseBusItem(cursor);
                AppManager.getBusItems().get(item.getIndexDaLinha()).setFavBgResId(R.drawable.ic_star_black_24dp);
                busItemsFavs[item.getIndexDaLinha()] = item;
                cursor.moveToNext();
                i++;
            } while (i < rows);
        }
        cursor.close();

        busItemsFav.clear();
        for(BusItem busItem : busItemsFavs) {
            if(busItem != null) {
                busItemsFav.add(busItem);
            }
        }

        if(busItemsFav.size() > 0) {
            ListView listView = favoriteContainer.findViewById(R.id.list_bus_items_fav);
            final LinhasArrayAdapter adapter = new LinhasArrayAdapter(MainActivity.this,
                    R.layout.favorite_bus_row_view, busItemsFav);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new BusClickListener());
        }
        showViews();
    }

    private void showViews() {
        btnShowBusLignes.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.show_up_from_bottom));

        btnShowBusLignes.setVisibility(View.VISIBLE);

        favoriteContainer.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.show_bottom_from_up));

        favoriteContainer.setVisibility(View.VISIBLE);
    }

    private void hideViews() {
        Animation bottomFromUpAnim = AnimationUtils.loadAnimation(MainActivity.this, R.anim.hide_bottom_to_up);
        bottomFromUpAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                btnShowBusLignes.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        btnShowBusLignes.startAnimation(bottomFromUpAnim);

        Animation upFromBottomAnim = AnimationUtils.loadAnimation(MainActivity.this, R.anim.hide_up_from_bottom);
        upFromBottomAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                favoriteContainer.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        favoriteContainer.startAnimation(upFromBottomAnim);

    }

    private void showBusOptionsIntent() {
        exitControl = 0;
        Intent intent = new Intent(MainActivity.this, ActivityBusOptions.class);
        startActivity(intent);
        hideViews();
    }

    private void clearScreen() {
        if(nativeExpressAdViewContainer != null && nativeExpressAdViewContainer.getParent() != null) {
            ((ViewGroup) nativeExpressAdViewContainer.getParent()).removeView(nativeExpressAdViewContainer);
        }
        containerScreen.removeAllViews();
    }

    @Override
    public void onBackPressed() {
        if(exitControl == 0) {
            exitControl = 1;
            Toast.makeText(MainActivity.this, "Pressione novamente para sair.", Toast.LENGTH_SHORT).show();
        } else {
            super.onBackPressed();
        }
    }

    private class BusClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
            exitControl = 0;
            popup = 1;
            final int indexDaLinha = busItemsFav.get(position).getIndexDaLinha();
            AppManager.CURRENT_BUS_ID = Constants.BUS_LIST[indexDaLinha][0];

            LayoutInflater li = LayoutInflater.from(MainActivity.this);
            final View showDialogView = li.inflate(R.layout.show_dialog, null);
            TextView outputTextView = showDialogView.findViewById(R.id.text_view_dialog);
            outputTextView.setText("Sentido");

            final ListView listView = showDialogView.findViewById(R.id.list_sentidos);

            ArrayList<String> sentidos = new ArrayList<>();
            Collections.addAll(sentidos, Constants.SENTIDOS[indexDaLinha]);
            final StableArrayAdapter adapter = new StableArrayAdapter(
                    MainActivity.this, R.layout.list_item, sentidos);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    AppManager.CURRENT_BUS_ID = Constants.BUS_LIST[indexDaLinha][position];
                    view.setSelected(true);
                    conected = AppManager.isConected(MainActivity.this);
                    if(conected) {
                        showBusTimeScreen();
                    } else {
                        Toast.makeText(MainActivity.this, "Conecte-se a internet", Toast.LENGTH_LONG).show();
                    }
                }
            });

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
            alertDialogBuilder.setView(showDialogView);
            alertDialogBuilder.setNegativeButton("Voltar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                }
            })
                    .setCancelable(true)
                    .create();

            alertDialogBuilder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    popup = 0;
                }
            });
            alertDialogBuilder.show();
        }
    }

    private void showConnectionAlert() {
        LayoutInflater li = LayoutInflater.from(MainActivity.this);
        final View showDialogView = li.inflate(R.layout.alert_dialog_view, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilder.setView(showDialogView);
        alertDialogBuilder.setPositiveButton("Entendi", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        })
                .setCancelable(true)
                .create();
    }

    private class StableArrayAdapter extends ArrayAdapter<String> {
        HashMap<String, Integer> mIdMap = new HashMap<>();

        int resId;

        StableArrayAdapter(Context context, int pResId, List<String> objects) {
            super(context, pResId, objects);
            resId = pResId;
            for (int i = 0; i < objects.size(); i++) {
                mIdMap.put(objects.get(i), i);
            }
        }

        @Override
        public long getItemId(int position) {
            String item = getItem(position);
            return mIdMap.get(item);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            String s = getItem(position);
            ViewHolder viewHolder;
            if(convertView == null) {
                convertView = getLayoutInflater().inflate(resId, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.txtTitle = convertView.findViewById(R.id.title_view);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.txtTitle.setText(s);

            return convertView;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }

    private static class ViewHolder {
        TextView txtTitle;
        TextView txtSubtitle;
        ImageView icFavorite;
        LinearLayout containerAdView;
        int bgResId;
    }

    private void showBusTimeScreen() {
        exitControl = 0;
        try {
            BaseHttpRequest request = new BaseHttpRequest(MainActivity.this,
                    Constants.LINK_ONIBUS + AppManager.CURRENT_BUS_ID, BaseHttpRequest.REQUEST_BUS);
            final BlogPostParser blogPostParser = new BlogPostParser();
            request.setHtmlParser(blogPostParser);
            IAsyncCallback callback = new IAsyncCallback() {
                @Override
                public void onComplete(WebResponse responseContent) {
                    AppManager.CURRENT_BUS_DATA = (SiteResponse) responseContent.getTypedObject();

                    Intent intent = new Intent(MainActivity.this, ActivityBusTime.class);
                    startActivity(intent);
                }

                @Override
                public void onError(String errorData) {
                    Toast.makeText(MainActivity.this, "Sistema caiu. Tente novamente.", Toast.LENGTH_LONG).show();
                }
            };
            request.execute(callback, AppSingleton.getInstance(MainActivity.this).getRequestQueue());
        } catch (Exception e) {
            Toast.makeText(MainActivity.this, "Ocorreu um erro.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private class LinhasArrayAdapter extends ArrayAdapter<BusItem> {

        HashMap<BusItem, Integer> idMaps = new HashMap<>();
 //       NativeContentAdView nativeAdView = AppManager.getInstance().getNativeContentAdView();
        //NativeAppInstallAdView nativeAdView = AppManager.getInstance().getNativeAppInstallAdView();
        private boolean nativeAdded = false;

        LinhasArrayAdapter(Context pContext, int pResId, List<BusItem> pPosts){
            super(pContext, pResId, pPosts);

            for(int i = 0; i < pPosts.size(); i++) {
                idMaps.put(pPosts.get(i), i);
            }
        }

        @Override
        public long getItemId(int position) {
            BusItem item = getItem(position);
            return idMaps.get(item);
        }

        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            final BusItem item = getItem(position);
            final ViewHolder viewHolder;
            if(convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.favorite_bus_row_view, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.txtTitle = convertView.findViewById(R.id.num_da_linha_fav);
                viewHolder.txtSubtitle = convertView.findViewById(R.id.nome_da_linha_fav);
//                viewHolder.containerAdView = convertView.findViewById(R.id.native_ad_container_view_holder);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.txtTitle.setText(item != null ? item.getNumLinha() : "");
            viewHolder.txtSubtitle.setText(item != null ? item.getNomeDaLinha() : "");
            // NATIVE ADS ADD
 /*           if(nativeAdView != null) {
                if(nativeAdView.getParent() != null && !nativeAdded) {
                    ((ViewGroup) nativeAdView.getParent()).removeView(nativeAdView);
                    viewHolder.containerAdView.addView(nativeAdView);
                    viewHolder.containerAdView.setLayoutParams(nativeAdView.getLayoutParams());
                    nativeAdded = true;
                }
            }
*/
            return convertView;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }

    public void openPlayStore(View view) {
        //Log.d(TAG, "====> Clicked com package: " + view.getContentDescription());
        String packageName = view.getContentDescription().toString();
        String url;
        try {
            getPackageManager().getPackageInfo("com.android.vending", 0);

            url = "market://details?id=" + packageName;
        } catch ( final Exception e ) {
            url = "https://play.google.com/store/apps/details?id=" + packageName;
        }

        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);

       // navigation.setSelectedItemId(R.id.navigation_apps);
    }

    private void manageBtnsMore() {
        if(isBtnsMoreShowed) {
            hideBtnsMore();
        } else {
            showBtnsMore();
        }
    }

    private void hideBtnsMore() {
        animation = AnimationUtils.loadAnimation(this, R.anim.hide_up_from_bottom);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                moreContainer.removeAllViews();
                containerMore = null;
                isBtnsMoreShowed = false;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        moreContainer.startAnimation(animation);
    }

    private void showBtnsMore() {
        animation = AnimationUtils.loadAnimation(this, R.anim.show_bottom_from_up);
        moreContainer.startAnimation(animation);
        containerMore = (ViewGroup) getLayoutInflater().inflate(R.layout.btns_more_layout, moreContainer);
        containerMore.findViewById(R.id.btn_share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showShareOptions();
            }
        });
        containerMore.findViewById(R.id.btn_rate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                showRateApp();
            }
        });
        isBtnsMoreShowed = true;
    }
/*
    private void showRateApp() {
        String packageName = getPackageName();
        String url;
        try {
            getPackageManager().getPackageInfo("com.android.vending", 0);

            url = "market://details?id=" + packageName;
        } catch ( final Exception e ) {
            url = "https://play.google.com/store/apps/details?id=" + packageName;
        }

        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }
*/
    private void showShareOptions() {
        String message = "\n\nBaixe você também o app " + getString(R.string.app_name) +
                " e tenha sempre o horário atualizado do ônibus circular de São José dos Campos na mão.\n " +
                "Grátis na Google Play Store.\n Compartilhe com seus amigos:\n\n " +
                "https://play.google.com/store/apps/details?id=" + getPackageName();

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, message);
        sendIntent.setType("text/plain");

        startActivity(sendIntent);
    }

    private void verifySite() {
        exitControl = 0;
        try {
            BaseHttpRequest request = new BaseHttpRequest(MainActivity.this,
                    Constants.LINK_ONIBUS + "169", BaseHttpRequest.VERIFY_SITE);
            final BlogPostParser blogPostParser = new BlogPostParser();
            request.setHtmlParser(blogPostParser);
            IAsyncCallback callback = new IAsyncCallback() {
                @Override
                public void onComplete(WebResponse responseContent) {
                    /*AppManager.CURRENT_BUS_DATA = (SiteResponse) responseContent.getTypedObject();

                    Intent intent = new Intent(MainActivity.this, ActivityBusTime.class);
                    startActivity(intent);*/
                }

                @Override
                public void onError(String errorData) {
                    avisoView = getLayoutInflater().inflate(R.layout.aviso,
                            (FrameLayout)findViewById(R.id.aviso_container));
                    //Toast.makeText(MainActivity.this, "Sistema caiu. Tente novamente.", Toast.LENGTH_LONG).show();
                }
            };
            request.execute(callback, AppSingleton.getInstance(MainActivity.this).getRequestQueue());
        } catch (Exception e) {
            /*Toast.makeText(MainActivity.this, "Ocorreu um erro.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();*/
        }
    }

    public void fecharAviso(View view) {
        ((FrameLayout)findViewById(R.id.aviso_container)).removeAllViews();
    }

    private void clearApplicationData() {
        File cache = getCacheDir();
        File appDir = new File(cache.getParent());
        if(appDir.exists()){
            String[] children = appDir.list();
            for(String s : children){
                if(!s.equals("lib")){
                    deleteDir(new File(appDir, s));

                    Log.i(MainActivity.class.getSimpleName(),
                            "=====> File /data/data/com.nigapps.onibus.sjc/" + s +" DELETED <=====");
                }
            }
        }
    }

    private boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (String aChildren : children) {
                boolean success = deleteDir(new File(dir, aChildren));
                if (!success) {
                    return false;
                }
            }
        }

        return dir != null && dir.delete();
    }

/*
    private class InterstitalAdListener extends AdListener{

        @Override
        public void onAdClosed() {
            super.onAdClosed();
        }
    }

    // NATIVE ADS
    /**
     * Populates a {@link NativeAppInstallAdView} object with data from a given
     * {@link NativeAppInstallAd}.
     *
     * @param nativeAppInstallAd the object containing the ad's assets
     * @param adView             the view to be populated
     */
/*    private void populateAppInstallAdView(NativeAppInstallAd nativeAppInstallAd,
                                          NativeAppInstallAdView adView) {
        // Get the video controller for the ad. One will always be provided, even if the ad doesn't
        // have a video asset.
        VideoController vc = nativeAppInstallAd.getVideoController();

        // Create a new VideoLifecycleCallbacks object and pass it to the VideoController. The
        // VideoController will call methods on this object when events occur in the video
        // lifecycle.
        vc.setVideoLifecycleCallbacks(new VideoController.VideoLifecycleCallbacks() {
            public void onVideoEnd() {
                // Publishers should allow native ads to complete video playback before refreshing
                // or replacing them with another ad in the same UI location.
                //refresh.setEnabled(true);
                //videoStatus.setText("Video status: Video playback has ended.");
                super.onVideoEnd();
            }
        });

        adView.setHeadlineView(adView.findViewById(R.id.appinstall_headline));
        adView.setBodyView(adView.findViewById(R.id.appinstall_body));
        adView.setCallToActionView(adView.findViewById(R.id.appinstall_call_to_action));
        adView.setIconView(adView.findViewById(R.id.appinstall_app_icon));
        adView.setPriceView(adView.findViewById(R.id.appinstall_price));
        adView.setStarRatingView(adView.findViewById(R.id.appinstall_stars));
        adView.setStoreView(adView.findViewById(R.id.appinstall_store));

        // Some assets are guaranteed to be in every NativeAppInstallAd.
        ((TextView) adView.getHeadlineView()).setText(nativeAppInstallAd.getHeadline());
        ((TextView) adView.getBodyView()).setText(nativeAppInstallAd.getBody());
        ((Button) adView.getCallToActionView()).setText(nativeAppInstallAd.getCallToAction());
        ((ImageView) adView.getIconView()).setImageDrawable(
                nativeAppInstallAd.getIcon().getDrawable());

        MediaView mediaView = adView.findViewById(R.id.appinstall_media);
        ImageView mainImageView = adView.findViewById(R.id.appinstall_image);

        // Apps can check the VideoController's hasVideoContent property to determine if the
        // NativeAppInstallAd has a video asset.
        if (vc.hasVideoContent()) {
            adView.setMediaView(mediaView);
            mainImageView.setVisibility(View.GONE);
           // videoStatus.setText(String.format(Locale.getDefault(),
           //         "Video status: Ad contains a %.2f:1 video asset.",
           //         vc.getAspectRatio()));
        } else {
            adView.setImageView(mainImageView);
            mediaView.setVisibility(View.GONE);

            // At least one image is guaranteed.
            List<NativeAd.Image> images = nativeAppInstallAd.getImages();
            mainImageView.setImageDrawable(images.get(0).getDrawable());

           // refresh.setEnabled(true);
           // videoStatus.setText("Video status: Ad does not contain a video asset.");
        }

        // These assets aren't guaranteed to be in every NativeAppInstallAd, so it's important to
        // check before trying to display them.
        if (nativeAppInstallAd.getPrice() == null) {
            adView.getPriceView().setVisibility(View.INVISIBLE);
        } else {
            adView.getPriceView().setVisibility(View.VISIBLE);
            ((TextView) adView.getPriceView()).setText(nativeAppInstallAd.getPrice());
        }

        if (nativeAppInstallAd.getStore() == null) {
            adView.getStoreView().setVisibility(View.INVISIBLE);
        } else {
            adView.getStoreView().setVisibility(View.VISIBLE);
            ((TextView) adView.getStoreView()).setText(nativeAppInstallAd.getStore());
        }

        if (nativeAppInstallAd.getStarRating() == null) {
            adView.getStarRatingView().setVisibility(View.INVISIBLE);
        } else {
            ((RatingBar) adView.getStarRatingView())
                    .setRating(nativeAppInstallAd.getStarRating().floatValue());
            adView.getStarRatingView().setVisibility(View.VISIBLE);
        }

        // Assign native ad object to the native view.
        adView.setNativeAd(nativeAppInstallAd);
    }

    /*
     * Populates a {@link NativeContentAdView} object with data from a given
     * {@link NativeContentAd}.
     *
     * @param nativeContentAd the object containing the ad's assets
     * @param adView          the view to be populated
     */
/*    private void populateContentAdView(NativeContentAd nativeContentAd,
                                       NativeContentAdView adView) {
//        videoStatus.setText("Video status: Ad does not contain a video asset.");
//        refresh.setEnabled(true);

        adView.setHeadlineView(adView.findViewById(R.id.contentad_headline));
        adView.setImageView(adView.findViewById(R.id.contentad_image));
        adView.setBodyView(adView.findViewById(R.id.contentad_body));
        adView.setCallToActionView(adView.findViewById(R.id.contentad_call_to_action));
        adView.setLogoView(adView.findViewById(R.id.contentad_logo));
        adView.setAdvertiserView(adView.findViewById(R.id.contentad_advertiser));

        // Some assets are guaranteed to be in every NativeContentAd.
        ((TextView) adView.getHeadlineView()).setText(nativeContentAd.getHeadline());
        ((TextView) adView.getBodyView()).setText(nativeContentAd.getBody());
        ((TextView) adView.getCallToActionView()).setText(nativeContentAd.getCallToAction());
        ((TextView) adView.getAdvertiserView()).setText(nativeContentAd.getAdvertiser());

        List<NativeAd.Image> images = nativeContentAd.getImages();

        if (images.size() > 0) {
            ((ImageView) adView.getImageView()).setImageDrawable(images.get(0).getDrawable());
        }

        // Some aren't guaranteed, however, and should be checked.
        NativeAd.Image logoImage = nativeContentAd.getLogo();

        if (logoImage == null) {
            adView.getLogoView().setVisibility(View.INVISIBLE);
        } else {
            ((ImageView) adView.getLogoView()).setImageDrawable(logoImage.getDrawable());
            adView.getLogoView().setVisibility(View.VISIBLE);
        }

        // Assign native ad object to the native view.
        adView.setNativeAd(nativeContentAd);
    }

    /**
     * Creates a request for a new native ad based on the boolean parameters and calls the
     * corresponding "populate" method when one is successfully returned.
     *
     * @param requestAppInstallAds indicates whether app install ads should be requested
     * @param requestContentAds    indicates whether content ads should be requested
     */
/*    private void refreshAd(boolean requestAppInstallAds, boolean requestContentAds) {
        if (!requestAppInstallAds && !requestContentAds) {
            Toast.makeText(this, "At least one ad format must be checked to request an ad.",
                    Toast.LENGTH_SHORT).show();
            return;
        }

//        refresh.setEnabled(false);
        String nativeAdId;

        if(AppManager.isDebugMode()) {
            nativeAdId = "ca-app-pub-3940256099942544/2247696110";
        } else {
            nativeAdId = "ca-app-pub-4768510961285493/5067306385";
        }

        AdLoader.Builder builder = new AdLoader.Builder(this, nativeAdId);

        if (requestAppInstallAds) {
            builder.forAppInstallAd(new NativeAppInstallAd.OnAppInstallAdLoadedListener() {
                @Override
                public void onAppInstallAdLoaded(NativeAppInstallAd ad) {
                    FrameLayout frameLayout =
                            findViewById(R.id.fl_adplaceholder);
                    NativeAppInstallAdView nativeAppInstallAdView = (NativeAppInstallAdView) getLayoutInflater()
                            .inflate(R.layout.ad_app_install, null);
                    populateAppInstallAdView(ad, nativeAppInstallAdView);
                    AppManager.getInstance().setNativeAppInstallAdView(nativeAppInstallAdView);
             //       frameLayout.removeAllViews();
             //       frameLayout.addView(adView);
                }
            });
        }

        if (requestContentAds) {
            builder.forContentAd(new NativeContentAd.OnContentAdLoadedListener() {
                @Override
                public void onContentAdLoaded(NativeContentAd ad) {
                    FrameLayout frameLayout =
                            findViewById(R.id.fl_adplaceholder);
                    NativeContentAdView nativeContentAdView = (NativeContentAdView) getLayoutInflater()
                            .inflate(R.layout.ad_content, null);
                    populateContentAdView(ad, nativeContentAdView);
                    AppManager.getInstance().setNativeContentAdView(nativeContentAdView);
//                    frameLayout.removeAllViews();
//                    frameLayout.addView(adView);
                }
            });
        }

        VideoOptions videoOptions = new VideoOptions.Builder()
                .setStartMuted(true)//startVideoAdsMuted.isChecked())
                .build();

        NativeAdOptions adOptions = new NativeAdOptions.Builder()
                .setVideoOptions(videoOptions)
                .build();

        builder.withNativeAdOptions(adOptions);

        AdLoader adLoader = builder.withAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(int errorCode) {
//                refresh.setEnabled(true);
                Toast.makeText(MainActivity.this, "Failed to load native ad: "
                        + errorCode, Toast.LENGTH_SHORT).show();
            }
        }).build();

        adLoader.loadAd(new AdRequest.Builder().build());

//        videoStatus.setText("");
    }

    /**
     * Populates a {@link NativeContentAdView} object with data from a given
     * {@link NativeContentAd}.
     *
     * @param nativeContentAd the object containing the ad's assets
     * @param adView          the view to be populated
     */
/*    private void populateContentAdView(NativeContentAd nativeContentAd,
                                       NativeContentAdView adView) {
//        mVideoStatus.setText("Video status: Ad does not contain a video asset.");
//        mRefresh.setEnabled(true);

        adView.setHeadlineView(adView.findViewById(R.id.contentad_headline));
        adView.setImageView(adView.findViewById(R.id.contentad_image));
        adView.setBodyView(adView.findViewById(R.id.contentad_body));
        adView.setCallToActionView(adView.findViewById(R.id.contentad_call_to_action));
        adView.setLogoView(adView.findViewById(R.id.contentad_logo));
        adView.setAdvertiserView(adView.findViewById(R.id.contentad_advertiser));

        // Some assets are guaranteed to be in every NativeContentAd.
        ((TextView) adView.getHeadlineView()).setText(nativeContentAd.getHeadline());
        ((TextView) adView.getBodyView()).setText(nativeContentAd.getBody());
        ((TextView) adView.getCallToActionView()).setText(nativeContentAd.getCallToAction());
        ((TextView) adView.getAdvertiserView()).setText(nativeContentAd.getAdvertiser());

        List<NativeAd.Image> images = nativeContentAd.getImages();

        if (images.size() > 0) {
            ((ImageView) adView.getImageView()).setImageDrawable(images.get(0).getDrawable());
        }

        // Some aren't guaranteed, however, and should be checked.
        NativeAd.Image logoImage = nativeContentAd.getLogo();

        if (logoImage == null) {
            adView.getLogoView().setVisibility(View.INVISIBLE);
        } else {
            ((ImageView) adView.getLogoView()).setImageDrawable(logoImage.getDrawable());
            adView.getLogoView().setVisibility(View.VISIBLE);
        }

        // Assign native ad object to the native view.
        adView.setNativeAd(nativeContentAd);
    }*/
}
