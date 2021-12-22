package com.nigapps.onibus.sjc.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.nigapps.onibus.sjc.R;
import com.nigapps.onibus.sjc.context.Contexts;
import com.nigapps.onibus.sjc.entities.NewsObject;
import com.nigapps.onibus.sjc.manager.AppManager;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ListViewAdapter extends ArrayAdapter<NewsObject> {

    private List<NewsObject> dataModels;
    private Context context;
    private boolean adViewAdded = false;

    public ListViewAdapter(List<NewsObject> dataModels, Context context) {
        super(context, R.layout.list_items, dataModels);
        this.dataModels = dataModels;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);

        final ViewHolder viewHolder;
        if(convertView == null) {
            convertView = inflater.inflate(R.layout.list_items, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.icon = convertView.findViewById(R.id.iv_icon);
            viewHolder.title = convertView.findViewById(R.id.tv_title);
            viewHolder.resume = convertView.findViewById(R.id.tv_resume);
            viewHolder.source = convertView.findViewById(R.id.tv_source);
            viewHolder.btnShare = convertView.findViewById(R.id.btn_share_news);
//            viewHolder.btnBookmark = convertView.findViewById(R.id.btn_bookmark);
            viewHolder.adContainer = convertView.findViewById(R.id.ad_container);

            viewHolder.url = dataModels.get(position).getSourceUrl();

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final NewsObject dataModel = dataModels.get(position);

        Picasso.with(context).load(dataModel.getImageUrl()).resize(960,640).into(viewHolder.icon);
        viewHolder.title.setText(dataModel.getTitle());
        viewHolder.resume.setText(dataModel.getResume());
        viewHolder.source.setText(dataModel.getSource());
/*
        //NATIVE ADS ADD
        NativeContentAdView nativeAdView = AppManager.getInstance().getNativeContentAdView();
        //NativeAppInstallAdView nativeAdView = AppManager.getInstance().getNativeAppInstallAdView();
        if(nativeAdView != null) {
            if(nativeAdView.getParent() != null && !adViewAdded) {
                ((ViewGroup) nativeAdView.getParent()).removeView(nativeAdView);
                viewHolder.adContainer.addView(nativeAdView);
                adViewAdded = true;
            }
        }


        viewHolder.btnBookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int bg = viewHolder.bg == R.drawable.ic_bookmark_border ?
                        R.drawable.ic_bookmark : R.drawable.ic_bookmark_border;
                viewHolder.btnBookmark.setBackgroundResource(bg);
                viewHolder.bg = bg;
                AppManager.setOnBookmark(viewHolder);
            }
        });
*/
        viewHolder.btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent sendIntent = new Intent()
                        .setAction(Intent.ACTION_SEND)
                        .putExtra(Intent.EXTRA_TEXT, dataModel.getSourceUrl())
                        .setType("text/plain");

                context.startActivity(sendIntent);
            }
        });

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(dataModel.getSourceUrl()));
                Contexts.getInstance().getCurrentActivity().startActivityForResult(intent, 1000);
            }
        });

        return convertView;
    }

    public class ViewHolder {
        ImageView icon, btnShare, btnBookmark;
        TextView title, resume, source;
        String url;
        FrameLayout adContainer;
//        int bg = R.drawable.ic_bookmark_border;

        public ImageView getBtnBookmark() {
            return btnBookmark;
        }

        public ImageView getBtnShare() {
            return btnShare;
        }

        public ImageView getIcon() {
            return icon;
        }
/*
        public int getBg() {
            return bg;
        }
*/
        public String getUrl() {
            return url;
        }

        public TextView getResume() {
            return resume;
        }

        public TextView getSource() {
            return source;
        }

        public TextView getTitle() {
            return title;
        }
    }
}
