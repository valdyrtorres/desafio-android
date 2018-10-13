package br.com.valdir.desafiolistafrutas;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public class FrutaDetalheActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fruta_detalhe);

        Intent intent = getIntent();
        Uri uri = intent.getData();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        FrutaDetalheFragment fragment = new FrutaDetalheFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(MainActivity.FRUTA_DETALHE_URI, uri);
        fragment.setArguments(bundle);
        fragmentTransaction.add(R.id.fragment_file_detalhe, fragment);

        // toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Detalhes da Fruta");
        // end toolbar
        fragmentTransaction.commit();
    }

    // usado pela toolbar, pois a toolbar nativa construía uma nova activity
    // que por sua disparava o evento onCreate de novo
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
