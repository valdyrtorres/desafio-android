package br.com.valdir.desafiolistafrutas;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import br.com.valdir.desafiolistafrutas.sync.FrutasSyncAdapter;

public class MainActivity extends AppCompatActivity implements MainFragment.Callback {

    public static final String FRUTA_DETALHE_URI = "FRUTA";

    private boolean isTablet = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        setContentView(R.layout.activity_main);

        if(findViewById(R.id.fragment_file_detalhe) != null) {

            if(savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_file_detalhe, new FrutaDetalheFragment())
                        .commit();
            }

            isTablet = true;
        } else {
            isTablet = false;
        }

        MainFragment mainFragment = (MainFragment) getSupportFragmentManager().findFragmentById(R.id.main_fragment);
        //mainFragment.setUseProdutoDestaque(!isTablet);
        mainFragment.setUseFrutaDestaque(false);

        FrutasSyncAdapter.initializeSyncAdapter(this);

    }

    @Override
    public void onItemSelected(Uri uri) {
        if (isTablet) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            FrutaDetalheFragment detalheFragment = new FrutaDetalheFragment();
            Bundle bundle = new Bundle();

            bundle.putParcelable(MainActivity.FRUTA_DETALHE_URI, uri);
            detalheFragment.setArguments(bundle);

            fragmentTransaction.replace(R.id.fragment_file_detalhe, detalheFragment);
            fragmentTransaction.commit();
        } else {

            Intent intent = new Intent(this, FrutaDetalheActivity.class);
            intent.setData(uri);
            startActivity(intent);
        }
    }
}
