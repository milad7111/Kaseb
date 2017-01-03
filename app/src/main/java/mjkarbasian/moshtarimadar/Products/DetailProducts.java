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
import android.text.Editable;
import android.text.TextWatcher;
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

    //region Declare Values & Views
    final static int FRAGMENT_PRODUCT_HISTORY_LOADER = 5;
    private final String LOG_TAG = DetailProducts.class.getSimpleName();
    String[] mProjection;
    Cursor mCursor;
    ListView mListView;
    DetailProductAdapter mAdapter = null;
    Long productId;
    FloatingActionButton fab;
    ContentValues productHistoryValues = new ContentValues();
    ContentValues productValues = new ContentValues();
    EditText productName;
    EditText productCode;
    EditText productUnit;
    EditText productDescription;
    EditText buyPrice;
    EditText quantity;
    EditText salePrice;
    EditText buyDate;
    EditText discountAmount;
    EditText discountPercent;
    MenuItem saveItem;
    MenuItem editItem;
    private Uri insertUri;
    //endregion Declare Values & Views

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

                //region Declare Views
                buyPrice = (EditText) dialog.findViewById(R.id.add_product_history_buy_price);
                quantity = (EditText) dialog.findViewById(R.id.add_product_history_quantity);
                salePrice = (EditText) dialog.findViewById(R.id.add_product_history_sale_price);
                buyDate = (EditText) dialog.findViewById(R.id.add_product_history_buy_date);
                discountAmount = (EditText) dialog.findViewById(R.id.add_product_history_discount_amount);
                discountPercent = (EditText) dialog.findViewById(R.id.add_product_history_discount_percent);
                //endregion Declare Views

                buyDate.setText(Utility.preInsertDate(getActivity()));

                discountAmount.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        try {
                            Float mDiscountAmount = Float.valueOf(discountAmount.getText().toString());
                            Float mBuyPrice = Float.valueOf(buyPrice.getText().toString());

                            if (mDiscountAmount > mBuyPrice)
                                discountAmount.setText(buyPrice.getText().toString());
                        } catch (Exception e) {
                            salePrice.setText("");
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        try {
                            Float mDiscountAmount = Float.valueOf(discountAmount.getText().toString());
                            Float mBuyPrice = Float.valueOf(buyPrice.getText().toString());

                            salePrice.setText(String.format("%.0f", Float.valueOf(mBuyPrice - mDiscountAmount)));
                        } catch (Exception e) {
                            salePrice.setText("");
                        }
                    }
                });

                discountPercent.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        try {
                            Float mDiscountPercent = Float.valueOf(discountPercent.getText().toString());

                            if (mDiscountPercent > 100)
                                discountPercent.setText("100");
                        } catch (Exception e) {
                            salePrice.setText("");
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        try {
                            Float mDiscountPercent = Float.valueOf(discountPercent.getText().toString());
                            Float mBuyPrice = Float.valueOf(buyPrice.getText().toString());

                            salePrice.setText(String.format("%.0f", Float.valueOf((100 - mDiscountPercent) * mBuyPrice / 100)));
                        } catch (Exception e) {
                            salePrice.setText("");
                        }
                    }
                });

                //region button save
                Button dialogButton = (Button) dialog.findViewById(R.id.add_product_history_save);

                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (CheckForValidityInsertProductHistory(
                                buyPrice.getText().toString(),
                                quantity.getText().toString(),
                                salePrice.getText().toString(),
                                buyDate.getText().toString())) {
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
                    }
                });
                //endregion button save

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
        saveItem = (MenuItem) menu.findItem(R.id.action_detail_product_save);
        editItem = (MenuItem) menu.findItem(R.id.action_detail_product_edit);
        saveItem.setVisible(isHidden());
        MenuItem sort = menu.findItem(R.id.sort_button);
        sort.setVisible(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case R.id.action_detail_product_edit:
                saveItem.setVisible(isVisible());
                editItem.setVisible(isHidden());

                productName.setEnabled(true);
                productCode.setEnabled(true);
                productUnit.setEnabled(true);
                productDescription.setEnabled(true);
                fab.show();

                break;
            case R.id.action_detail_product_save:
                if (CheckForValidityEditProduct(productName.getText().toString())) {
                    editItem.setVisible(isVisible());
                    saveItem.setVisible(isHidden());

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

                    productName.setEnabled(false);
                    productCode.setEnabled(false);
                    productUnit.setEnabled(false);
                    productDescription.setEnabled(false);
                    fab.hide();

//                    getFragmentManager().popBackStackImmediate();
                }
                break;
//            case R.id.action_detail_product_share: {
//            }
            default:
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

    // this method check the validation and correct entries. its check fill first and then check the validation rules.
    private boolean CheckForValidityEditProduct(String productName) {
        if (productName.equals("") || productName.equals(null)) {
            Toast.makeText(getActivity(), "Choose apropriate name for PRODUCT.", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            Cursor mCursor = getContext().getContentResolver().query(
                    KasebContract.Products.CONTENT_URI,
                    new String[]{KasebContract.Products._ID},
                    KasebContract.Products.COLUMN_PRODUCT_NAME + " = ? and " + KasebContract.Products._ID + " != ? ",
                    new String[]{productName, String.valueOf(productId)},
                    null);

            if (mCursor != null) {
                if (mCursor.moveToFirst())
                    if (mCursor.getCount() > 0) {
                        Toast.makeText(getActivity(), "Choose apropriate (Not Itterative) name for PRODUCT.", Toast.LENGTH_SHORT).show();
                        return false;
                    }
            }
        }

        return true;
    }

    // this method check the validation and correct entries. its check fill first and then check the validation rules.
    private boolean CheckForValidityInsertProductHistory(String buyPrice, String quantity, String salePrice, String buyDate) {
        if (buyPrice.equals("") || buyPrice.equals(null)) {
            Toast.makeText(getActivity(), "Choose apropriate buy price for PRODUCT.", Toast.LENGTH_SHORT).show();
            return false;
        } else if (quantity.equals("") || quantity.equals(null)) {
            Toast.makeText(getActivity(), "Choose apropriate quantity for PRODUCT.", Toast.LENGTH_SHORT).show();
            return false;
        } else if (salePrice.equals("") || salePrice.equals(null)) {
            Toast.makeText(getActivity(), "Choose apropriate sale price for PRODUCT.", Toast.LENGTH_SHORT).show();
            return false;
        } else if (buyDate.equals("") || buyDate.equals(null)) {
            Toast.makeText(getActivity(), "Choose apropriate buy date for PRODUCT.", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}
