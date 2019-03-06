
package com.example.android.inventory.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;
import com.example.android.inventory.data.Contract.Entry;


public class Provider extends ContentProvider {

    public static final String LOG_TAG = Provider.class.getSimpleName();

    private static final int inventory = 100;

    private static final int inventory_ID = 101;


    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);


    static {

        sUriMatcher.addURI(Contract.CONTENT_AUTHORITY, Contract.inventory_Path, inventory);

        sUriMatcher.addURI(Contract.CONTENT_AUTHORITY, Contract.inventory_Path + "/#", inventory_ID);
    }

    private DbHelper mDbHelper;

    @Override
    public boolean onCreate() {
        mDbHelper = new DbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        SQLiteDatabase database = mDbHelper.getReadableDatabase();

        Cursor cursor;

        int match = sUriMatcher.match(uri);
        switch (match) {
            case inventory:

                cursor = database.query(Contract.Entry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case inventory_ID:

                selection = Entry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };


                cursor = database.query(Entry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case inventory:
                return insertItem(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }


    private Uri insertItem(Uri uri, ContentValues values) {

        if (values.containsKey(Entry.COLUMN_Name)) {
            String name = values.getAsString(Entry.COLUMN_Name);
            if (name == null) {
                throw new IllegalArgumentException("need name");
            }
        }



        if (values.containsKey(Entry.COLUMN_Quantity)) {
            Integer qun = values.getAsInteger(Entry.COLUMN_Quantity);
            if (qun != null && qun < 0) {
                throw new IllegalArgumentException("must be 0 or more ");
            }
        }

        if (values.containsKey(Entry.COLUMN_price)) {
            Integer price = values.getAsInteger(Entry.COLUMN_price);
            if (price != null && price < 0) {
                throw new IllegalArgumentException("must be 0 or more ");
            }
        }



        if (values.containsKey(Entry.COLUMN_Supplier_Name)) {
            String name = values.getAsString(Entry.COLUMN_Supplier_Name);
            if (name == null) {
                throw new IllegalArgumentException("need value");
            }
        }

        if (values.containsKey(Entry.COLUMN_Supplier_Phone_Number)) {
            String name = values.getAsString(Entry.COLUMN_Supplier_Phone_Number);
            if (name == null) {
                throw new IllegalArgumentException("need value");
            }
        }


        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        long id = database.insert(Entry.TABLE_NAME, null, values);

        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }


        getContext().getContentResolver().notifyChange(uri, null);


        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection,
                      String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case inventory:

                return updateItem(uri, contentValues, selection, selectionArgs);
            case inventory_ID:


                selection = Entry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return updateItem(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }


    private int updateItem(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        if (values.containsKey(Entry.COLUMN_Name)) {
            String name = values.getAsString(Entry.COLUMN_Name);
            if (name == null) {
                throw new IllegalArgumentException("need name");
            }
        }



        if (values.containsKey(Entry.COLUMN_Quantity)) {
            Integer qun = values.getAsInteger(Entry.COLUMN_Quantity);
            if (qun != null && qun < 0) {
                throw new IllegalArgumentException("must be 0 or more ");
            }
        }

        if (values.containsKey(Entry.COLUMN_price)) {
            Integer price = values.getAsInteger(Entry.COLUMN_price);
            if (price != null && price < 0) {
                throw new IllegalArgumentException("must be 0 or more ");
            }
        }

        if (values.containsKey(Entry.COLUMN_Supplier_Name)) {
            String name = values.getAsString(Entry.COLUMN_Supplier_Name);
            if (name == null) {
                throw new IllegalArgumentException("need value");
            }
        }

        if (values.containsKey(Entry.COLUMN_Supplier_Phone_Number)) {
            String name = values.getAsString(Entry.COLUMN_Supplier_Phone_Number);
            if (name == null) {
                throw new IllegalArgumentException("need value");
            }
        }


        if (values.size() == 0) {
            return 0;
        }


        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        int rowsUpdated = database.update(Entry.TABLE_NAME, values, selection, selectionArgs);


        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }


    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        int rowsDeleted;

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case inventory:
                rowsDeleted = database.delete(Entry.TABLE_NAME, selection, selectionArgs);
                break;
            case inventory_ID:
                selection = Contract.Entry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                rowsDeleted = database.delete(Contract.Entry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }

        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case inventory:
                return Entry.CONTENT_LIST_TYPE;
            case inventory_ID:
                return Contract.Entry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }
}
