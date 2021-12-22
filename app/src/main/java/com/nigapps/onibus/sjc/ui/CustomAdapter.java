package com.nigapps.onibus.sjc.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.ArrayAdapter;

import com.nigapps.onibus.sjc.R;
import com.nigapps.onibus.sjc.entities.BusItem;

import java.util.ArrayList;

/**
 * Created by elvis on 16/09/17.
 */

public class CustomAdapter extends ArrayAdapter<BusItem> {



    public CustomAdapter(@NonNull Context context, ArrayList<BusItem> data) {
        super(context, R.layout.lv_item_layout, data);


    }
}
