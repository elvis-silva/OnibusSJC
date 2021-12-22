package com.nigapps.onibus.sjc.context;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.nigapps.onibus.sjc.Constants;
import com.nigapps.onibus.sjc.R;
import com.nigapps.onibus.sjc.db.DataHandler;
import com.nigapps.onibus.sjc.entities.BusItem;
import com.nigapps.onibus.sjc.manager.AppManager;

import java.util.ArrayList;

public class Contexts {

    private static final String TAG = Contexts.class.getSimpleName();

    @SuppressLint("StaticFieldLeak")
    static Contexts instance;
    private Context context;
    private Activity currentActivity, lastActivity;
    private DataHandler dataHandler;

    private Contexts(){
    }

    public static Contexts getInstance() {
        if(instance == null) {
            synchronized (Contexts.class) {
                if(instance == null) {
                    instance = new Contexts();
                }
            }
        }
        return instance;
    }

    public boolean initActivity(Activity activity) {
        if(context == null) {
            initApp(activity, activity);
        }
        if(currentActivity != activity) {
            lastActivity = currentActivity;
            currentActivity = activity;
            return true;
        }
     //   Log.d(TAG, "====> CURRENT ACTIVITY: [ " + activity.toString() + " ]");
        return false;
    }

    private synchronized void initApp(Object activity, Context context) {
        if(this.context == null) {
            this.context = context.getApplicationContext();
            currentActivity = (Activity) activity;
            initDataHandler();
            initBusFavoriteControl();
        }
    }

    private void initBusFavoriteControl() {
        for(int i = 0; i < Constants.LINHAS.length; i++) {
            BusItem busItem = new BusItem(Constants.LINHAS[i][0], R.drawable.ic_star_border_black_24dp);
            AppManager.getBusItems().add(busItem);
        }
    }

    private void initDataHandler() {
        dataHandler = new DataHandler(context);
        dataHandler.open();
    }

    public Activity getCurrentActivity() {
        return currentActivity;
    }

    public Context getContext() {
        return context;
    }

    public boolean returToLastActivity() {
        return initActivity(lastActivity);
    }

    public DataHandler getDataHandler() {
        return dataHandler;
    }

    public void closeDataHandler() {
        dataHandler.close();
    }
}
