package com.nigapps.onibus.sjc;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.AppCompatImageView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import com.nigapps.onibus.sjc.db.DataHandler;
import com.nigapps.onibus.sjc.entities.BusItem;
import com.nigapps.onibus.sjc.entities.SiteResponse;
import com.nigapps.onibus.sjc.manager.AppManager;
import com.nigapps.onibus.sjc.parsers.BlogPostParser;
import com.nigapps.onibus.sjc.webrequesthandler.BaseHttpRequest;
import com.nigapps.onibus.sjc.webrequesthandler.IAsyncCallback;
import com.nigapps.onibus.sjc.webrequesthandler.WebResponse;

public class ActivityBusOptions extends Activity {

    private static final String TAG = ActivityBusOptions.class.getSimpleName();
    private int currentBusPosition;
    private int lastPosition = -1;
    private int popup = 0;

 //   AdView adView;

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

 //   private NativeAppInstallAdView nativeAdInstallView;
    private boolean nativeAdViewAdded = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bus_options_screen);

        //refreshAd(false, true);

 /*       adView = findViewById(R.id.adViewBusOptions);
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
*/
        ArrayList<BusItem> busItems = new ArrayList<>();
        for(int i = 0; i < Constants.LINHAS.length; i++) {
            BusItem item = new BusItem();
            item.setNumDaLinha(Constants.LINHAS[i][0]);
            item.setNomeDaLinha(Constants.LINHAS[i][1]);
            busItems.add(item);
        }
        ListView listView = findViewById(R.id.list_bus);
        final LinhasArrayAdapter adapter = new LinhasArrayAdapter(ActivityBusOptions.this,
                R.layout.lv_item_layout, busItems);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                currentBusPosition = position;

                showBusOptionsPopup();
            }
        });

        AppCompatImageView btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void showBusOptionsPopup() {
        popup = 1;
        AppManager.CURRENT_BUS_ID = Constants.BUS_LIST[currentBusPosition][0];

        LayoutInflater li = LayoutInflater.from(ActivityBusOptions.this);
        final View showDialogView = li.inflate(R.layout.show_dialog, null);
        TextView outputTextView = showDialogView.findViewById(R.id.text_view_dialog);
        outputTextView.setText("Sentido");

        final ListView listView = showDialogView.findViewById(R.id.list_sentidos);

        ArrayList<String> sentidos = new ArrayList<>();
        Collections.addAll(sentidos, Constants.SENTIDOS[currentBusPosition]);
        final StableArrayAdapter adapter = new StableArrayAdapter(
                ActivityBusOptions.this, R.layout.list_item, sentidos);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                AppManager.CURRENT_BUS_ID = Constants.BUS_LIST[currentBusPosition][position];
                view.setSelected(true);
                showBusTimeScreen();
            }
        });

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ActivityBusOptions.this);
        alertDialogBuilder.setView(showDialogView);
        alertDialogBuilder.setNegativeButton("Voltar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //showBusTimeScreen();
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

    private void showBusTimeScreen() {
        if(AppManager.isConected(ActivityBusOptions.this)) {
            try {
                BaseHttpRequest request = new BaseHttpRequest(ActivityBusOptions.this,
                        Constants.LINK_ONIBUS + AppManager.CURRENT_BUS_ID, BaseHttpRequest.REQUEST_BUS);
                final BlogPostParser blogPostParser = new BlogPostParser();
                request.setHtmlParser(blogPostParser);
                IAsyncCallback callback = new IAsyncCallback() {
                    @Override
                    public void onComplete(WebResponse responseContent) {
                        AppManager.CURRENT_BUS_DATA = (SiteResponse) responseContent.getTypedObject();

                        Intent intent = new Intent(ActivityBusOptions.this, ActivityBusTime.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onError(String errorData) {
                        Log.d(TAG, errorData);
                     //   Toast.makeText(ActivityBusOptions.this, errorData, Toast.LENGTH_SHORT).show();
                    }
                };
                request.execute(callback, AppSingleton.getInstance(ActivityBusOptions.this).getRequestQueue());
            } catch (Exception e) {
                Toast.makeText(ActivityBusOptions.this, "Ocorreu um erro.", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        } else {
            Toast.makeText(ActivityBusOptions.this, "Conecte-se a internet", Toast.LENGTH_LONG).show();
        }
    }

    public void closeScreen(View view) {
        onBackPressed();
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

    private class LinhasArrayAdapter extends ArrayAdapter<BusItem> {

        HashMap<BusItem, Integer> idMaps = new HashMap<>();

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
            assert item != null;
            item.setIndexDaLinha(position);
            int bgResId = AppManager.getBusItems().get(position).getFavBgResId();
            item.setFavBgResId(bgResId);
            final ViewHolder viewHolder;
            if(convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.lv_item_layout, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.txtTitle = convertView.findViewById(R.id.title_view);
                viewHolder.txtSubtitle = convertView.findViewById(R.id.subtitle_view);
                viewHolder.icFavorite = convertView.findViewById(R.id.favorite_icon);
 //               viewHolder.nativeAdContainer = convertView.findViewById(R.id.nat_ad_container);
                viewHolder.bgResId = bgResId;
                convertView.setTag(viewHolder);

                Animation animation = AnimationUtils.makeInAnimation(ActivityBusOptions.this, false);
                convertView.startAnimation(animation);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.txtTitle.setText(item.getNumLinha());
            viewHolder.txtSubtitle.setText(item.getNomeDaLinha());
            viewHolder.icFavorite.setBackgroundResource(bgResId);

            // NATIVE ADS ADD
            /*NativeContentAdView nativeAdView = AppManager.getInstance().getNativeContentAdView();
            //NativeAppInstallAdView nativeAdView = AppManager.getInstance().getNativeAppInstallAdView();
            if(nativeAdView != null) {
                if(nativeAdView.getParent() != null && !nativeAdViewAdded) {
                    ((ViewGroup) nativeAdView.getParent()).removeView(nativeAdView);
                    viewHolder.nativeAdContainer.addView(nativeAdView);
                    nativeAdViewAdded = true;
                }
            }
*/
/*            if(nativeAdInstallView != null && nativeAdInstallView.getParent() == null) {
                viewHolder.nativeAdContainer.addView(nativeAdInstallView);
            }*/
            viewHolder.icFavorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {

                    favoritar(item);

                    final int bgFavResId = getItem(position).getFavBgResId() == R.drawable.ic_star_border_black_24dp ?
                            R.drawable.ic_star_black_24dp : R.drawable.ic_star_border_black_24dp;

                    AppManager.getBusItems().get(position).setFavBgResId(bgFavResId);

                    Animation animBack = AnimationUtils.loadAnimation(ActivityBusOptions.this, R.anim.image_anim_back);
                    animBack.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            view.setBackgroundResource(bgFavResId);
                            getItem(position).setFavBgResId(bgFavResId);
                            Animation animIn = AnimationUtils.loadAnimation(ActivityBusOptions.this, R.anim.image_anim_in);
                            view.startAnimation(animIn);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {
                        }
                    });
                    view.startAnimation(animBack);
                }
            });

            return convertView;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }

    private void favoritar(BusItem item) {
     //   Log.d(TAG, " ====> [ TITLE : " + item.getNumLinha() + " ] <==== / ====> [ SUBTITLE : " + item.getNomeDaLinha() + " ] <====");
        DataHandler dataHandler = AppManager.getDataHandler();
        if(!dataHandler.hasBusItem(item)) {
            AppManager.BUS_ITEM_FAVS[item.getIndexDaLinha()] = item;
            dataHandler.addBusItem(item.getNumLinha(), item.getNomeDaLinha(), item.getIndexDaLinha());
        } else {
            AppManager.BUS_ITEM_FAVS[item.getIndexDaLinha()] = null;
            dataHandler.deleteBusItem(item);
        }
    }

    private static class ViewHolder {
        TextView txtTitle;
        TextView txtSubtitle;
        AppCompatImageView icFavorite;
        int bgResId;
        FrameLayout nativeAdContainer;
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
        AppManager.getMainActivity().loadLinhasFavoritas();

//        if(adView != null) adView.destroy();

        super.onDestroy();
    }
/*
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
                    nativeAdInstallView = (NativeAppInstallAdView) getLayoutInflater()
                            .inflate(R.layout.ad_app_install, null);
                    populateAppInstallAdView(ad, nativeAdInstallView);
                    AppManager.getInstance().setNativeAppInstallAdView(nativeAdInstallView);
                }
            });
        }

        if (requestContentAds) {
            builder.forContentAd(new NativeContentAd.OnContentAdLoadedListener() {
                @Override
                public void onContentAdLoaded(NativeContentAd ad) {
                    FrameLayout frameLayout =
                            findViewById(R.id.fl_adplaceholder);
                    NativeContentAdView nativeAdContentView = (NativeContentAdView) getLayoutInflater()
                            .inflate(R.layout.ad_content, null);
                    populateContentAdView(ad, nativeAdContentView);
                    AppManager.getInstance().setNativeContentAdView(nativeAdContentView);
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
                Toast.makeText(ActivityBusOptions.this, "Failed to load native ad: "
                        + errorCode, Toast.LENGTH_SHORT).show();
            }
        }).build();

        adLoader.loadAd(new AdRequest.Builder().build());

//        videoStatus.setText("");
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

    /**
     * Populates a {@link NativeContentAdView} object with data from a given
     * {@link NativeContentAd}.
     *
     * @param nativeContentAd the object containing the ad's assets
     * @param adView          the view to be populated
     */
 /*   private void populateContentAdView(NativeContentAd nativeContentAd,
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