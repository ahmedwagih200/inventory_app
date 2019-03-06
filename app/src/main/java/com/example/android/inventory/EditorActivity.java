package com.example.android.inventory;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.android.inventory.data.Contract;
import com.example.android.inventory.data.Contract.Entry;


public class EditorActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    private static final int EXISTING_LOADER = 0;

    private Uri mCurrentUri;

    private EditText NameEditText;

    private EditText QunEditText;

    private EditText PriceEditText;

    private EditText SupNameEditText;

    private EditText SupNumEditText;

    private boolean mHasChanged = false;

    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mHasChanged = true;
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        Intent intent = getIntent();
        mCurrentUri = intent.getData();

        if (mCurrentUri == null) {
            setTitle(getString(R.string.editor_activity_title_new_product));

            invalidateOptionsMenu();
        } else {
            setTitle(getString(R.string.editor_activity_title_edit_product));

            getLoaderManager().initLoader(EXISTING_LOADER, null, this);
        }

        NameEditText = (EditText) findViewById(R.id.edit_name);
        PriceEditText = (EditText) findViewById(R.id.edit_Price);
        QunEditText = (EditText) findViewById(R.id.edit_QUN);
        SupNameEditText = (EditText) findViewById(R.id.edit_Supname);
        SupNumEditText = (EditText) findViewById(R.id.edit_Supnum);

        NameEditText.setOnTouchListener(mTouchListener);
        PriceEditText.setOnTouchListener(mTouchListener);
        QunEditText.setOnTouchListener(mTouchListener);
        SupNameEditText.setOnTouchListener(mTouchListener);
        SupNumEditText.setOnTouchListener(mTouchListener);

        Button increase = (Button) findViewById(R.id.increase);
        increase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int ProQun =Integer.parseInt(QunEditText.getText().toString());
                ProQun++;
                ContentValues values = new ContentValues();
                values.put(Entry.COLUMN_Quantity,ProQun);
                getContentResolver().update(mCurrentUri,values,null,null);
            }
        });

        Button decrease = (Button) findViewById(R.id.decrease);
        decrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int ProQun =Integer.parseInt(QunEditText.getText().toString());
                if(ProQun > 0)
                ProQun--;
                ContentValues values = new ContentValues();
                values.put(Entry.COLUMN_Quantity,ProQun);
                getContentResolver().update(mCurrentUri,values,null,null);
            }
        });

        Button order = (Button) findViewById(R.id.order);
        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+SupNumEditText.getText().toString()));
                startActivity(intent);

            }
        });

    }

    private void save() {
        String nameString = NameEditText.getText().toString().trim();
        String priceString = PriceEditText.getText().toString().trim();
        String qunString = QunEditText.getText().toString().trim();
        String supnameString = SupNameEditText.getText().toString().trim();
        String supnumString = SupNumEditText.getText().toString().trim();

        if (mCurrentUri == null &&
                TextUtils.isEmpty(nameString) && TextUtils.isEmpty(priceString) &&
                TextUtils.isEmpty(qunString) && TextUtils.isEmpty(supnameString) && TextUtils.isEmpty(supnumString)) {
            return;
        }

        ContentValues values = new ContentValues();
        values.put(Entry.COLUMN_Name, nameString);
        values.put(Entry.COLUMN_price, priceString);
        values.put(Entry.COLUMN_Quantity, qunString);
        values.put(Entry.COLUMN_Supplier_Name, supnameString);
        values.put(Entry.COLUMN_Supplier_Phone_Number, supnumString);

        int price = 0;
        if (!TextUtils.isEmpty(priceString)) {
            price = Integer.parseInt(priceString);
        }
        values.put(Entry.COLUMN_price, price);

        int Qun = 0;
        if (!TextUtils.isEmpty(qunString)) {
            Qun = Integer.parseInt(qunString);
        }
        values.put(Entry.COLUMN_Quantity, Qun);

        if (mCurrentUri == null) {

            Uri newUri = getContentResolver().insert(Contract.Entry.CONTENT_URI, values);

            if (newUri == null) {
                Toast.makeText(this, getString(R.string.editor_insert_product_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.editor_insert_product_successful),
                        Toast.LENGTH_SHORT).show();
            }
        } else {

            int rowsAffected = getContentResolver().update(mCurrentUri, values, null, null);

            if (rowsAffected == 0) {
                Toast.makeText(this, getString(R.string.editor_update_product_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.editor_update_product_successful),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        if (mCurrentUri == null) {
            MenuItem menuItem = menu.findItem(R.id.action_delete);
            menuItem.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_save:
                save();
                finish();
                return true;
            case R.id.action_delete:
                showDeleteConfirmationDialog();
                return true;
            case android.R.id.home:
                if (!mHasChanged) {
                    NavUtils.navigateUpFromSameTask(EditorActivity.this);
                    return true;
                }

                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                NavUtils.navigateUpFromSameTask(EditorActivity.this);
                            }
                        };

                showUnsavedChangesDialog(discardButtonClickListener);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        if (!mHasChanged) {
            super.onBackPressed();
            return;
        }


        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                };
        showUnsavedChangesDialog(discardButtonClickListener);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

        String[] projection = {
                Contract.Entry._ID,
                Entry.COLUMN_Name,
                Entry.COLUMN_Quantity,
                Entry.COLUMN_price,
                Entry.COLUMN_Supplier_Phone_Number,
                Entry.COLUMN_Supplier_Name};

        return new CursorLoader(this,
                mCurrentUri,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        if (cursor == null || cursor.getCount() < 1) {
            return;
        }


        if (cursor.moveToFirst()) {
            int nameColumnIndex = cursor.getColumnIndex(Entry.COLUMN_Name);
            int QuantityColumnIndex = cursor.getColumnIndex(Entry.COLUMN_Quantity);
            int PriceColumnIndex = cursor.getColumnIndex(Entry.COLUMN_price);
            int supnameColumnIndex = cursor.getColumnIndex(Entry.COLUMN_Supplier_Name);
            int supnumColumnIndex = cursor.getColumnIndex(Entry.COLUMN_Supplier_Phone_Number);


            String sName = cursor.getString(nameColumnIndex);
            String sQuantity = cursor.getString(QuantityColumnIndex);
            String sPrice = cursor.getString(PriceColumnIndex);
            String ssupname = cursor.getString(supnameColumnIndex);
            String ssupnum = cursor.getString(supnumColumnIndex);

            NameEditText.setText(sName);
            PriceEditText.setText(sPrice);
            QunEditText.setText(sQuantity);
            SupNameEditText.setText(ssupname);
            SupNumEditText.setText(ssupnum);


        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        NameEditText.setText("");
        PriceEditText.setText("");
        QunEditText.setText("");
        SupNameEditText.setText("");
        SupNumEditText.setText("");

    }


    private void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.unsaved_changes_dialog_msg);
        builder.setPositiveButton(R.string.discard, discardButtonClickListener);
        builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    private void showDeleteConfirmationDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_dialog_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                delete();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    private void delete() {
        if (mCurrentUri != null) {

            int rowsDeleted = getContentResolver().delete(mCurrentUri, null, null);

            if (rowsDeleted == 0) {
                Toast.makeText(this, getString(R.string.editor_delete_product_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.editor_delete_product_successful),
                        Toast.LENGTH_SHORT).show();
            }
        }
        finish();
    }
}

