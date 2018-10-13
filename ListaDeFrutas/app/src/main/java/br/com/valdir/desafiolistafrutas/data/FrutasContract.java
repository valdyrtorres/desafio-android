package br.com.valdir.desafiolistafrutas.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class FrutasContract {

    public static final String CONTENT_AUTHORITY = "br.com.valdir.desafiolistafrutas";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_FRUTAS = "frutas";

    private FrutasContract() {}

    public static abstract class FrutaEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_FRUTAS).build();

        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FRUTAS;

        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FRUTAS;

        public static final String TABLE_NAME = "frutas";

        public static final String _ID = "_id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_PRICE = "price";
        public static final String COLUMN_IMAGE_PATH = "image";

        public static Uri buildUriForFrutas() {
            return CONTENT_URI.buildUpon().build();
        }

        public static Uri buildUriForFrutas(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static long getIdFromUri(Uri uri) {
            return Long.parseLong(uri.getPathSegments().get(1));
        }
    }

}
