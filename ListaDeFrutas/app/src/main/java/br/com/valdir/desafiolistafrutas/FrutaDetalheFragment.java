package br.com.valdir.desafiolistafrutas;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import br.com.valdir.desafiolistafrutas.data.FrutasContract;
import br.com.valdir.desafiolistafrutas.jni.CalculeNative;

public class FrutaDetalheFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    ItemFruta itemFruta;

    private Uri frutaUri;

    private TextView nameView;

    private TextView priceView;

    private TextView priceViewReal;

    private ImageView imageView;

    private static final int FRUTA_DETALHE_LOADER = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            frutaUri = (Uri) getArguments().getParcelable(MainActivity.FRUTA_DETALHE_URI);
        }

        getLoaderManager().initLoader(FRUTA_DETALHE_LOADER, null, this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_fruta_detalhe, container,false);

        nameView = view.findViewById(R.id.item_name);

        priceView = view.findViewById(R.id.item_price);

        priceViewReal = view.findViewById(R.id.item_price_real);

        imageView = view.findViewById(R.id.item_image);

        return view;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                FrutasContract.FrutaEntry._ID,
                FrutasContract.FrutaEntry.COLUMN_NAME,
                FrutasContract.FrutaEntry.COLUMN_PRICE,
                FrutasContract.FrutaEntry.COLUMN_IMAGE_PATH
        };

        return new CursorLoader(getContext(), frutaUri, projection, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }

        if (cursor.moveToFirst()) {

            // Criação do objeto que vai chamar o códigonativo
            CalculeNative calculeNative = new CalculeNative();

            int idIndex = cursor.getColumnIndex(FrutasContract.FrutaEntry._ID);
            int nameIndex = cursor.getColumnIndex(FrutasContract.FrutaEntry.COLUMN_NAME);
            int priceIndex = cursor.getColumnIndex(FrutasContract.FrutaEntry.COLUMN_PRICE);
            int imageIndex = cursor.getColumnIndex(FrutasContract.FrutaEntry.COLUMN_IMAGE_PATH);

            String idProduto = cursor.getString(idIndex);
            String name = cursor.getString(nameIndex);
            String price = cursor.getString(priceIndex);
            String image = cursor.getString(imageIndex);

            itemFruta = new ItemFruta(Long.valueOf(idProduto), name,
                    image, Double.valueOf(price));

            nameView.setText(name);
            priceView.setText(getString(R.string.txt_dollar_simbolo) + String.format(" %.2f", Double.valueOf(price)));
            priceViewReal.setText(getString(R.string.txt_real_simbolo) + String.format(" %.2f", Double.valueOf(calculeNative.asyncConvertToReal(Double.valueOf(price)))));

            Picasso.with(getContext()).load(image).into(imageView);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
