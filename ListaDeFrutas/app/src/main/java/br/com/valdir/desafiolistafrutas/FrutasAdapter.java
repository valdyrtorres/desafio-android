package br.com.valdir.desafiolistafrutas;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import br.com.valdir.desafiolistafrutas.data.FrutasContract;

public class FrutasAdapter extends CursorAdapter {

    private static final int VIEW_TYPE_DESTAQUE = 0;

    private static final int VIEW_TYPE_ITEM = 1;

    private boolean useFrutaDestaque = false;

    public FrutasAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    public static class ItemFrutaHolder {
        TextView name;
        TextView price;
        ImageView image;

        public ItemFrutaHolder(View view) {
           name = view.findViewById(R.id.item_name);
           price = view.findViewById(R.id.item_price);
           image = view.findViewById(R.id.item_image);
        }
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        int viewType = getItemViewType(cursor.getPosition());
        int layoutId = -1;

        switch (viewType) {
            case VIEW_TYPE_DESTAQUE: {
                layoutId = R.layout.item_fruta_destaque;
                break;
            }
            case VIEW_TYPE_ITEM: {
                layoutId = R.layout.item_fruta;
                break;
            }
        }

        View view = LayoutInflater.from(context).inflate(layoutId, parent, false);

        ItemFrutaHolder holder = new ItemFrutaHolder(view);
        view.setTag(holder);

        return view;

    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        ItemFrutaHolder holder = (ItemFrutaHolder) view.getTag();

        int viewType = getItemViewType(cursor.getPosition());

        int nameIndex = cursor.getColumnIndex(FrutasContract.FrutaEntry.COLUMN_NAME);
        int priceIndex = cursor.getColumnIndex(FrutasContract.FrutaEntry.COLUMN_PRICE);
        int imageIndex = cursor.getColumnIndex(FrutasContract.FrutaEntry.COLUMN_IMAGE_PATH);

        switch (viewType) {
            case VIEW_TYPE_DESTAQUE: {
                holder.name.setText(cursor.getString(nameIndex));
                holder.price.setText("R$ " + String.format("%.2f", cursor.getDouble(priceIndex)));
                Picasso.with(context).load(cursor.getString(imageIndex)).into(holder.image);
                break;
            }
            case VIEW_TYPE_ITEM: {
                holder.name.setText(cursor.getString(nameIndex));
                holder.price.setText("R$ " + String.format("%.2f", cursor.getDouble(priceIndex)));
                Picasso.with(context).load(cursor.getString(imageIndex)).into(holder.image);
                break;
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        return (position == 0 && useFrutaDestaque ? VIEW_TYPE_DESTAQUE : VIEW_TYPE_ITEM);
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    public void setUseFrutaDestaque(boolean useFrutaDestaque) {
        this.useFrutaDestaque = useFrutaDestaque;
    }
}
