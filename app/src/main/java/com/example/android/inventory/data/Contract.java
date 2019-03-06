
package com.example.android.inventory.data;

import android.net.Uri;
import android.content.ContentResolver;
import android.provider.BaseColumns;


public final class Contract {


    private Contract() {
    }


    public static final String CONTENT_AUTHORITY = "com.example.android.inventory";


    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);


    public static final String inventory_Path = "inventory";


    public static final class Entry implements BaseColumns {

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, inventory_Path);


        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + inventory_Path;


        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + inventory_Path;


        public final static String TABLE_NAME = "Inventory";

        public final static String _ID = BaseColumns._ID;

        public final static String COLUMN_Name ="Name";

        public final static String COLUMN_price ="price";

        public final static String COLUMN_Quantity ="Quantity";

        public final static String COLUMN_Supplier_Name ="SupplieName";

        public final static String COLUMN_Supplier_Phone_Number ="SupplierNum";


    }
}
