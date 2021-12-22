package com.nigapps.onibus.sjc.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.nigapps.onibus.sjc.entities.BusItem;

public class DataHandler {

    private static final String TAG = DataHandler.class.getSimpleName();

    static final String DB_NAME = "bussjc.db";
    static final int DB_VERSION = 1;

    static final String DB_BUS_SJC_TABLE = "OnibusSJC";
    private static final String ID = "_id";
    private static final String NUM_DA_LINHA = "num_da_linha";
    private static final String NOME_DA_LINHA = "nome_da_linha";
    private static final String ID_DA_LINHA = "id_da_linha";
    private static final String INDEX_DA_LINHA = "index_da_linha";

    private static final String[] COLUMNS = {ID, NUM_DA_LINHA, NOME_DA_LINHA, INDEX_DA_LINHA};

    static final String DB_BUS_SJC_CREATE   = "CREATE TABLE " +
            DB_BUS_SJC_TABLE                + "(" +
            ID                              + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            NUM_DA_LINHA                    + " TEXT," +
            NOME_DA_LINHA                   + " TEXT," +
            INDEX_DA_LINHA                  + " INTEGER);" ;

    private Context context;
    private DataBaseHelper dataBaseHelper;
    private SQLiteDatabase db;

    public DataHandler(Context context) {
        this.context = context;
        dataBaseHelper = new DataBaseHelper(context);
     //   Log.d(TAG, "====> [ DATABASE CREATED ] <====");
    }

    public DataHandler open() throws SQLException {
        db = dataBaseHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        db.close();
        dataBaseHelper.close();
    }

    public BusItem addBusItem(String numDaLinha, String nomeDaLinha, int indexDaLinha) {
        ContentValues cv = new ContentValues();
        cv.put(NUM_DA_LINHA, numDaLinha);
        cv.put(NOME_DA_LINHA, nomeDaLinha);
        cv.put(INDEX_DA_LINHA, indexDaLinha);

        long rowId = db.insert(DB_BUS_SJC_TABLE, null, cv);

        Cursor cursor = db.query(DB_BUS_SJC_TABLE, COLUMNS, ID + " = " + rowId, null, null, null, null);
        cursor.moveToFirst();

        BusItem busItem = parseBusItem(cursor);

        cursor.close();

        return busItem;
    }

    public BusItem parseBusItem(Cursor cursor) {
        BusItem busItem = new BusItem();
        busItem.setId(cursor.getInt(0));
        busItem.setNumDaLinha(cursor.getString(1));
        busItem.setNomeDaLinha(cursor.getString(2));
        busItem.setIndexDaLinha(cursor.getInt(3));
        return busItem;
    }

    public boolean hasBusItem(BusItem item) {
        Cursor cursor = returnBusSjcTable();
        cursor.moveToFirst();
        int rows = cursor.getCount();
        if(rows > 0) {
            int i = 0;
            do {
                BusItem busItem = parseBusItem(cursor);
                if(item.getNumLinha().equals(busItem.getNumLinha())) {
                    cursor.close();
                    return true;
                }
                cursor.moveToNext();
                i++;
            } while (i < rows);
        }
        cursor.close();
        return false;
    }

    public void deleteBusItem(BusItem item) {
        db = dataBaseHelper.getWritableDatabase();
        Cursor cursor = returnBusSjcTable();
        cursor.moveToFirst();
        int rows = cursor.getCount();
        if(rows > 0) {
            int i = 0;
            do {
                BusItem busItem = parseBusItem(cursor);
                if(item.getNumLinha().equals(busItem.getNumLinha())) {
                    db.delete(DB_BUS_SJC_TABLE, ID + " = " + busItem.getId(), null);
                    cursor.close();
                    return;
                }
                cursor.moveToNext();
                i++;
            } while (i < rows);
        }
        cursor.close();
    }

    public void clearData() {
        db = dataBaseHelper.getWritableDatabase();
        Cursor cursor = returnBusSjcTable();
        cursor.moveToFirst();
        int rows = cursor.getCount();
        if(rows > 0) {
            int i = 0;
            do {
                BusItem item = parseBusItem(cursor);
                db.delete(DB_BUS_SJC_TABLE, ID + " = " + item.getId(), null);
                cursor.moveToNext();
                i++;
            } while (i < rows);
        }
        cursor.close();
    }

    public Cursor returnBusSjcTable() {
        return db.query(DB_BUS_SJC_TABLE, COLUMNS, ID, null, null, null, null);
    }

    private static class DataBaseHelper extends SQLiteOpenHelper {

        private DataBaseHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DB_BUS_SJC_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + DB_BUS_SJC_TABLE);
            onCreate(db);
        }
    }
}
