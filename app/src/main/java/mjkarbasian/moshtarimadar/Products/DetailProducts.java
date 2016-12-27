package mjkarbasian.moshtarimadar.Products;

import android.app.Dialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import mjkarbasian.moshtarimadar.Adapters.DetailProductAdapter;
import mjkarbasian.moshtarimadar.Data.KasebContract;
import mjkarbasian.moshtarimadar.Helpers.Utility;
import mjkarbasian.moshtarimadar.R;

public class DetailProducts extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    final static int FRAGMENT_PRODUCT_HISTORY_LOADER = 5;
    private final String LOG_TAG = DetailProducts.class.getSimpleName();
    EditText productName;
    EditText productCode;
    EditText productUnit;
    EditText productDescription;
    EditText buyPrice;
    EditText quantity;
    EditText salePrice;
    EditText buyDate;
    ContentValues productHistoryValues = new ContentValues();
    String[] mProjection;
    Cursor mCursor;
    ListView mListView;
    DetailProductAdapter mAdapter = null;
    Long productId;
    ContentValues productValues = new ContentValues();
    FloatingActionButton fab;
    private Uri insertUri;

    public DetailProducts() {
        super();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        productId = Long.parseLong(this.getArguments().getString("productId"));

        mProjection = new String[]{
                KasebContract.ProductHistory._ID,
                KasebContract.ProductHistory.COLUMN_COST,
                KasebContract.ProductHistory.COLUMN_DATE,
                KasebContract.ProductHistory.COLUMN_QUANTITY,
                KasebContract.ProductHistory.COLUMN_SALE_PRICE};

        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mAdapter = new DetailProductAdapter(
                getActivity(),
                null,
                0);

        View rootView = inflater.inflate(R.layout.fragment_detail_products, container, false);
        mListView = (ListView) rootView.findViewById(R.id.listview_detail_product);
        mListView.setAdapter(mAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });

        //region Fab Add ProductHistory
        fab = (FloatingActionButton) rootView.findViewById(R.id.fab_detail_product);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = Utility.dialogBuilder(getActivity()
                        , R.layout.dialog_add_product_history
                        , R.string.title_add_product_history);

                buyPrice = (EditText) dialog.findViewById(R.id.add_product_history_buy_price);
                quantity = (EditText) dialog.findViewById(R.id.add_product_history_quantity);
                salePrice = (EditText) dialog.findViewById(R.id.add_product_history_sale_price);
                buyDate = (EditText) dialog.findViewById(R.id.add_product_history_buy_date);
                buyDate.setText(Utility.preInsertDate(getActivity()));

                Button dialogButton = (Button) dialog.findViewById(R.id.add_product_history_button1);

                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        productHistoryValues.put(KasebContract.ProductHistory.COLUMN_COST, buyPrice.getText().toString());
                        productHistoryValues.put(KasebContract.ProductHistory.COLUMN_QUANTITY, quantity.getText().toString());
                        productHistoryValues.put(KasebContract.ProductHistory.COLUMN_SALE_PRICE, salePrice.getText().toString());
                        productHistoryValues.put(KasebContract.ProductHistory.COLUMN_DATE, buyDate.getText().toString());
                        productHistoryValues.put(KasebContract.ProductHistory.COLUMN_PRODUCT_ID, productId);

                        insertUri = getActivity().getContentResolver().insert(
                                KasebContract.ProductHistory.CONTENT_URI,
                                productHistoryValues
                        );
                        Toast.makeText(getActivity(), getResources().getString(R.string.msg_insert_succeed), Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });
        //endregion Fab Add ProductHistory

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_detail_products, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case R.id.action_detail_product_edit:
                productName.setEnabled(true);
                productCode.setEnabled(true);
                productUnit.setEnabled(true);
                productDescription.setEnabled(true);

                break;
            case R.id.action_detail_product_save:
                productValues.put(KasebContract.Products.COLUMN_PRODUCT_NAME, productName.getText().toString());
                productValues.put(KasebContract.Products.COLUMN_PRODUCT_CODE, productCode.getText().toString());
                productValues.put(KasebContract.Products.COLUMN_UNIT, productUnit.getText().toString());
                productValues.put(KasebContract.Products.COLUMN_DESCRIPTION, productDescription.getText().toString());

                getActivity().getContentResolver().update(
                        KasebContract.Products.CONTENT_URI,
                        productValues,
                        KasebContract.Products._ID + " = ? ",
                        new String[]{String.valueOf(productId)}
                );

                //just a message to show everything are under control
                Toast.makeText(getContext(),
                        getContext().getResources().getString(R.string.msg_update_succeed), Toast.LENGTH_LONG).show();


                break;
            case R.id.action_detail_product_share: {
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        Log.d(LOG_TAG, "onStart");

        productName = (EditText) getActivity().findViewById(R.id.detail_product_name);
        productCode = (EditText) getActivity().findViewById(R.id.detail_product_code);
        productUnit = (EditText) getActivity().findViewById(R.id.detail_product_unit);
        productDescription = (EditText) getActivity().findViewById(R.id.detail_product_description);

        mCursor = getContext().getContentResolver().query(
                KasebContract.Products.buildProductsUri(productId),
                new String[]{
                        KasebContract.Products.COLUMN_PRODUCT_NAME,
                        KasebContract.Products.COLUMN_PRODUCT_CODE,
                        KasebContract.Products.COLUMN_DESCRIPTION,
                        KasebContract.Products.COLUMN_UNIT},
                null,
                null,
                null);

        if (mCursor != null)
            if (mCursor.moveToFirst()) {
                productName.setText(mCursor.getString(mCursor.getColumnIndex(KasebContract.Products.COLUMN_PRODUCT_NAME)));
                productCode.setText(mCursor.getString(mCursor.getColumnIndex(KasebContract.Products.COLUMN_PRODUCT_CODE)));
                productUnit.setText(mCursor.getString(mCursor.getColumnIndex(KasebContract.Products.COLUMN_UNIT)));
                productDescription.setText(mCursor.getString(mCursor.getColumnIndex(KasebContract.Products.COLUMN_DESCRIPTION)));
            }

        productName.setEnabled(false);
        productCode.setEnabled(false);
        productUnit.setEnabled(false);
        productDescription.setEnabled(false);
        fab.hide();

        super.onStart();
        updateList();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onActivityCreated");
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(FRAGMENT_PRODUCT_HISTORY_LOADER, null, this);
    }

    private void updateList() {
        getLoaderManager().restartLoader(FRAGMENT_PRODUCT_HISTORY_LOADER, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.d(LOG_TAG, "onCreateLoader");

        return new CursorLoader(
                getActivity(),
                KasebContract.ProductHistory.aProductHistory(productId),
                mProjection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        Log.d(LOG_TAG, "onLoadFinished");
        mAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Log.d(LOG_TAG, "onLoadReset");
        mAdapter.swapCursor(null);
    }
}
