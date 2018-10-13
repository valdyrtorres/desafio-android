package br.com.valdir.desafiolistafrutas.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SyncRequest;
import android.content.SyncResult;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import br.com.valdir.desafiolistafrutas.FrutaDetalheActivity;
import br.com.valdir.desafiolistafrutas.ItemFruta;
import br.com.valdir.desafiolistafrutas.JsonUtil;
import br.com.valdir.desafiolistafrutas.R;
import br.com.valdir.desafiolistafrutas.api.APIServiceGet;
import br.com.valdir.desafiolistafrutas.data.FrutasContract;
import okhttp3.ResponseBody;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by valdyrtorres on 05/12/2017.
 */

public class FrutasSyncAdapter extends AbstractThreadedSyncAdapter {

    public static final int SYNC_INTERVAL = 60 * 720;

    public static final int SYNC_FLEXTIME = SYNC_INTERVAL/3;

    public static final int NOTIFICATION_PRODUTOS_ID = 1001;

    public FrutasSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());

        try {

            String urlBase = "https://raw.githubusercontent.com/muxidev/desafio-android/master/fruits.json";

            Uri uriApi = Uri.parse(urlBase).buildUpon().build();

            URL url = new URL(uriApi.toString());
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String linha;
            StringBuffer buffer = new StringBuffer();
            while ((linha = reader.readLine()) != null) {
                buffer.append(linha);
                buffer.append("\n");
            }


            JSONObject jObjFrutas = new JSONObject(buffer.toString());

            //List<ItemFruta> itemFrutas = JsonUtil.fromJsonToList(buffer.toString());

            List<ItemFruta> itemFrutas = JsonUtil.fromJsonArrayToList(jObjFrutas.getJSONArray("fruits"));

            if (itemFrutas == null) {
                return;
            }

            for (ItemFruta itemFruta : itemFrutas) {
                ContentValues values = new ContentValues();
                values.put(FrutasContract.FrutaEntry._ID, itemFruta.getId());
                values.put(FrutasContract.FrutaEntry.COLUMN_NAME, itemFruta.getName());
                values.put(FrutasContract.FrutaEntry.COLUMN_PRICE, itemFruta.getPrice());
                values.put(FrutasContract.FrutaEntry.COLUMN_IMAGE_PATH, itemFruta.getImage());

                // Preparar para o futuro caso a lista de produtos aumente no JSON fornecido
                int update = getContext().getContentResolver().update(FrutasContract.FrutaEntry.buildUriForFrutas(itemFruta.getId()),
                        values, null, null);

                if (update == 0) {
                    getContext().getContentResolver().insert(FrutasContract.FrutaEntry.CONTENT_URI, values);
                    notify(itemFruta);
                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public void notify(ItemFruta itemFruta) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String notifyPrefKey = getContext().getString(R.string.prefs_notif_filmes_key);
        String notifyDefault = getContext().getString(R.string.prefs_notif_filmes_default);
        boolean notifyPrefs = sharedPreferences.getBoolean(notifyPrefKey, Boolean.parseBoolean(notifyDefault));

        if(!notifyPrefs) {
            return;
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext()).
                setSmallIcon(R.mipmap.ic_launcher).
                setContentTitle(itemFruta.getName()).
                setContentText(itemFruta.getPrice().toString());

        Intent intent = new Intent(getContext(), FrutaDetalheActivity.class);
        Uri uri = FrutasContract.FrutaEntry.buildUriForFrutas(itemFruta.getId());
//        Uri uri = FrutasContract.ProdutoEntry.buildUriForProdutos();
        intent.setData(uri);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(getContext());
        stackBuilder.addNextIntent(intent);
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_PRODUTOS_ID, builder.build());
    }

    public static void configurePeriodicSync(Context context, int syncInterval, int flextime) {
        Account account = getSyncAccount(context);
        String authority = context.getString(R.string.content_authority);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            SyncRequest syncRequest = new SyncRequest.Builder().
                    syncPeriodic(syncInterval, flextime).
                    setSyncAdapter(account, authority).
                    setExtras(new Bundle()).build();
            ContentResolver.requestSync(syncRequest);
        } else {
            ContentResolver.addPeriodicSync(account, authority, new Bundle(), syncInterval);
        }
    }

    public static void syncImmediately(Context context) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        ContentResolver.requestSync(getSyncAccount(context), context.getString(R.string.content_authority), bundle);
    }

    public static Account getSyncAccount(Context context) {
        AccountManager accountManager = (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);

        Account account = new Account(context.getString(R.string.app_name), context.getString(R.string.sync_account_type));

        if(accountManager.getPassword(account) == null) {
            if(!accountManager.addAccountExplicitly(account, "", null)) {
                return null;
            }

            // conta de mentirinha
            onAccountCreate(account, context);
        }

        return account;
    }

    private static void onAccountCreate(Account account, Context context) {
        FrutasSyncAdapter.configurePeriodicSync(context, SYNC_INTERVAL, SYNC_FLEXTIME);

        ContentResolver.setSyncAutomatically(account, context.getString(R.string.content_authority), true);

        syncImmediately(context);
    }

    public static void initializeSyncAdapter(Context context) {
        getSyncAccount(context);
    }
}
