package br.com.valdir.desafiolistafrutas.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by valdyrtorres on 05/12/2017.
 */

public class FrutasAuthenticatorService extends Service {

    private FrutasAuthenticator frutasAuthenticator;

    @Override
    public void onCreate() {
        frutasAuthenticator = new FrutasAuthenticator(this);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return frutasAuthenticator.getIBinder();
    }
}
