package br.com.valdir.desafiolistafrutas.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class FrutasDBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 3;

    private static final String DATABASE_NAME = "frutas.db";

    public FrutasDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlTableProdutos = "CREATE TABLE " + FrutasContract.FrutaEntry.TABLE_NAME + " (" +
                FrutasContract.FrutaEntry._ID + " INTEGER PRIMARY KEY, " +
                FrutasContract.FrutaEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                FrutasContract.FrutaEntry.COLUMN_IMAGE_PATH + " TEXT NOT NULL, " +
                FrutasContract.FrutaEntry.COLUMN_PRICE + " REAL " +
                ");";

        db.execSQL(sqlTableProdutos);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE " + FrutasContract.FrutaEntry.TABLE_NAME);

        onCreate(db);
    }
}
