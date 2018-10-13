package br.com.valdir.desafiolistafrutas.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by valdyrtorres on 05/12/2017.
 */

public class FrutasSyncService extends Service {

    private static FrutasSyncAdapter frutasSyncAdapter = null;

    private static final Object lock = new Object();

    @Override
    public void onCreate() {
        // Para ser criada uma só vez e sincronizada
        synchronized (lock) {
            if (frutasSyncAdapter == null) {
                frutasSyncAdapter = new FrutasSyncAdapter(getApplicationContext(),true);
            }
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return frutasSyncAdapter.getSyncAdapterBinder();
    }
}
