package br.com.valdir.desafiolistafrutas.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class FrutasProvider extends ContentProvider {

    private static final UriMatcher URI_MATCHER = buildUriMatcher();

    private FrutasDBHelper dbHelper;

    private static final int FRUTA = 100;

    private static final int FRUTA_ID = 101;

    private static UriMatcher buildUriMatcher() {
        final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(FrutasContract.CONTENT_AUTHORITY, FrutasContract.PATH_FRUTAS, FRUTA);
        uriMatcher.addURI(FrutasContract.CONTENT_AUTHORITY, FrutasContract.PATH_FRUTAS + "/#", FRUTA_ID);

        return uriMatcher;

    }

    @Override
    public boolean onCreate() {
        dbHelper = new FrutasDBHelper(getContext());

        return true;
    }

    @Nullable

    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        SQLiteDatabase readableDatabase = dbHelper.getReadableDatabase();

        Cursor cursor;

        switch(URI_MATCHER.match(uri)) { // O match devolve o id direto
            case FRUTA:
                cursor = readableDatabase.query(FrutasContract.FrutaEntry.TABLE_NAME,
                        projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case FRUTA_ID:
                selection = FrutasContract.FrutaEntry._ID + "=?";
                selectionArgs = new String[] {String.valueOf(FrutasContract.FrutaEntry.getIdFromUri(uri))};

                cursor = readableDatabase.query(FrutasContract.FrutaEntry.TABLE_NAME,
                        projection, selection, selectionArgs, null,null,sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Uri não identificada: " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (URI_MATCHER.match(uri)) {
            case FRUTA:
                return FrutasContract.FrutaEntry.CONTENT_TYPE;
            case FRUTA_ID:
                return FrutasContract.FrutaEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Uri não identificada: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {

        SQLiteDatabase writableDatabase = dbHelper.getWritableDatabase();

        long id;
        switch (URI_MATCHER.match(uri)) {
            case FRUTA:
                id = writableDatabase.insert(FrutasContract.FrutaEntry.TABLE_NAME,null, values);

                if (id == -1) {
                    return null;
                }
                break;
            default:
                throw new IllegalArgumentException("Uri não identificada: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return FrutasContract.FrutaEntry.buildUriForFrutas(id);

    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase writableDatabase = dbHelper.getWritableDatabase();

        int delete = 0;
        switch (URI_MATCHER.match(uri)) {
            case FRUTA:
                delete = writableDatabase.delete(FrutasContract.FrutaEntry.TABLE_NAME, selection, selectionArgs);
            case FRUTA_ID:
                selection = FrutasContract.FrutaEntry._ID + "=?";
                selectionArgs = new String[] {String.valueOf(FrutasContract.FrutaEntry.getIdFromUri(uri))};

                delete = writableDatabase.delete(FrutasContract.FrutaEntry.TABLE_NAME, selection, selectionArgs);
        }

        if (delete != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return delete;

    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase writableDatabase = dbHelper.getWritableDatabase();

        int update = 1;

        switch (URI_MATCHER.match(uri)) {
            case FRUTA:
                update = writableDatabase.update(FrutasContract.FrutaEntry.TABLE_NAME, values, selection, selectionArgs);
            case FRUTA_ID:
                selection = FrutasContract.FrutaEntry._ID + "=?";
                selectionArgs = new String[] {String.valueOf(FrutasContract.FrutaEntry.getIdFromUri(uri))};

                update = writableDatabase.update(FrutasContract.FrutaEntry.TABLE_NAME, values, selection, selectionArgs);
        }

        if (update != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return update;
    }
}
