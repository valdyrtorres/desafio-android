package br.com.valdir.desafiolistafrutas;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import br.com.valdir.desafiolistafrutas.data.FrutasContract;
import br.com.valdir.desafiolistafrutas.sync.FrutasSyncAdapter;

public class MainFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private int posicaoItem = ListView.INVALID_POSITION;

    private static final String KEY_POSICAO = "SELECIONADO";

    private ListView list;

    private FrutasAdapter adapter;

    private boolean useFrutaDestaque = false;

    private static final int FRUTAS_LOADER = 0;

    private ProgressDialog progressDialog;

    Button btnLogin;

    Button btnLogout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        list = view.findViewById(R.id.list_produtos);

        adapter = new FrutasAdapter(getContext(),null);
        adapter.setUseFrutaDestaque(useFrutaDestaque);

        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Uri uri = FrutasContract.FrutaEntry.buildUriForFrutas(id);
                Callback callback = (Callback) getActivity();
                callback.onItemSelected(uri);
                posicaoItem = position;
            }
        });

        if(savedInstanceState != null && savedInstanceState.containsKey(KEY_POSICAO)) {
            posicaoItem = savedInstanceState.getInt(KEY_POSICAO);
        }

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle(getString(R.string.pd_carregando_titulo));
        progressDialog.setMessage(getString(R.string.pd_carregando_mensagem));
        progressDialog.setCancelable(false);

        getLoaderManager().initLoader(FRUTAS_LOADER, null, this);

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (posicaoItem != ListView.INVALID_POSITION) {
            outState.putInt(KEY_POSICAO, posicaoItem);
        }

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            list.smoothScrollToPosition(savedInstanceState.getInt(KEY_POSICAO));
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.menu_atualizar:
                FrutasSyncAdapter.syncImmediately(getContext());
                Toast.makeText(getContext(), "Atualizando as frutas...", Toast.LENGTH_LONG).show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        getLoaderManager().restartLoader(FRUTAS_LOADER, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        progressDialog.show();

        String[] projection = {
                FrutasContract.FrutaEntry._ID,
                FrutasContract.FrutaEntry.COLUMN_NAME,
                FrutasContract.FrutaEntry.COLUMN_PRICE,
                FrutasContract.FrutaEntry.COLUMN_IMAGE_PATH,
        };

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String ordem = preferences.getString(getString(R.string.prefs_ordem_key),"name");
        String popularValue = getResources().getStringArray(R.array.prefs_ordem_values)[0];

        String orderBy = null;
        if (ordem.equals(popularValue)) {
            orderBy = FrutasContract.FrutaEntry.COLUMN_NAME + " ASC";
        } else {
            orderBy = FrutasContract.FrutaEntry.COLUMN_PRICE + " ASC";
        }

        return new CursorLoader(getContext(), FrutasContract.FrutaEntry.CONTENT_URI, projection,
                null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
         adapter.swapCursor(data);
         progressDialog.dismiss();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
          adapter.swapCursor(null);
    }

    public interface Callback {
        void onItemSelected(Uri uri);
    }

    public void setUseFrutaDestaque(boolean useFrutaDestaque) {
        this.useFrutaDestaque = useFrutaDestaque;

        if (adapter != null) {
            adapter.setUseFrutaDestaque(useFrutaDestaque);
        }
    }

}
