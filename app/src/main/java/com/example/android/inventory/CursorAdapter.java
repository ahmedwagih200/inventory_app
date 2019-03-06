package com.example.android.inventory;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.example.android.inventory.data.Contract.Entry;

public class CursorAdapter extends android.widget.CursorAdapter{



    public CursorAdapter(Context context, Cursor c) {
        super(context, c, 0 );
    }



    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);



    }



    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        TextView nameTextView = (TextView) view.findViewById(R.id.name);
        TextView priceTextView = (TextView) view.findViewById(R.id.price);
        TextView QUnTextView = (TextView) view.findViewById(R.id.QUN);

        int nameColumnIndex = cursor.getColumnIndex(Entry.COLUMN_Name);
        int priceColumnIndex = cursor.getColumnIndex(Entry.COLUMN_price);
        int QUnColumnIndex = cursor.getColumnIndex(Entry.COLUMN_Quantity);
        int ID_ColumnIndex = cursor.getColumnIndex(Entry._ID);

        String Name = cursor.getString(nameColumnIndex);
        int price = cursor.getInt(priceColumnIndex);
        final int Qun = cursor.getInt(QUnColumnIndex);
        final int id = cursor.getInt(ID_ColumnIndex);



        Button sell = (Button) view.findViewById(R.id.sell);
        sell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Qun > 0)
                {
                    int ProQun =Qun;
                    ProQun--;
                    ContentValues values = new ContentValues();
                    values.put(Entry.COLUMN_Quantity,ProQun);
                    Uri uri = ContentUris.withAppendedId(Entry.CONTENT_URI, id);
                    CatalogActivity catalogActivity = (CatalogActivity) context;
                    catalogActivity.sell(uri,values);

                }
            }
        });



        nameTextView.setText(Name);
        priceTextView.setText(price+" $");
        QUnTextView.setText(Qun+" Left in Stock");

    }

    public final static int id()
    {
        return id();
    }

}
