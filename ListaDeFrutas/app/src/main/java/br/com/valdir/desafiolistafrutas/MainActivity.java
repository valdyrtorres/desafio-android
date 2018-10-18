package br.com.valdir.desafiolistafrutas;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import br.com.valdir.desafiolistafrutas.api.APIServiceGet;
import br.com.valdir.desafiolistafrutas.data.FrutasContract;
import br.com.valdir.desafiolistafrutas.sync.FrutasSyncAdapter;
import retrofit2.Call;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Response;
import okhttp3.ResponseBody;
import retrofit2.Callback;
import retrofit2.Retrofit;

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
        mainFragment.setUseFrutaDestaque(false);

        // Esse método não utiliza o retrofit
        //FrutasSyncAdapter.initializeSyncAdapter(this);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://raw.githubusercontent.com/").build();

        APIServiceGet api = retrofit.create(APIServiceGet.class);

        api.getListaFrutas().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JSONObject jObj = new JSONObject(response.body().string());

                    JSONArray jArr = jObj.getJSONArray("fruits");
                    for (int i=0; i < jArr.length(); i++) {
                        JSONObject obj = jArr.getJSONObject(i);
                        Log.d("DesafioListaFrutas", obj.getString("name"));
                        Log.d("DesafioListaFrutas", obj.getString("image"));
                        Log.d("DesafioListaFrutas", obj.getString("price"));
                    }

                    List<ItemFruta> itemFrutas = JsonUtil.fromJsonArrayToList(jArr);

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
                        int update = getContentResolver().update(FrutasContract.FrutaEntry.buildUriForFrutas(itemFruta.getId()),
                                values, null, null);

                        if (update == 0) {
                            getContentResolver().insert(FrutasContract.FrutaEntry.CONTENT_URI, values);
                        }
                    }

//                    Frutas frutas = new Frutas();
//
//                    frutas.setFrutaList();

                    Log.d("RetrofitTutorial", response.body().string());
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });


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
