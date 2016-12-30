package mjkarbasian.moshtarimadar.Others;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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
import android.widget.Spinner;
import android.widget.Toast;

import mjkarbasian.moshtarimadar.Adapters.CostSaleProductAdapter;
import mjkarbasian.moshtarimadar.Adapters.TypesSettingAdapter;
import mjkarbasian.moshtarimadar.Data.KasebContract;
import mjkarbasian.moshtarimadar.Data.KasebDbHelper;
import mjkarbasian.moshtarimadar.Data.KasebProvider;
import mjkarbasian.moshtarimadar.Helpers.Utility;
import mjkarbasian.moshtarimadar.Products.DetailProducts;
import mjkarbasian.moshtarimadar.R;
import mjkarbasian.moshtarimadar.Sales.DetailSaleView;

/**
 * Created by Unique on 10/11/2016.
 */
public class CostSaleProductList extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    final static int FRAGMENT_COST_SALE_PRODUCT_LOADER = 1;
    private final String LOG_TAG = CostSaleProductList.class.getSimpleName();
    FragmentManager fragmentManager;
    Dialog dialog;

    DetailProducts productHistory = new DetailProducts();
    Bundle productHistoryBundle = new Bundle();

    KasebDbHelper mOpenHelper;
    SQLiteDatabase mDb;

    CostSaleProductAdapter mAdapter = null;
    ListView mListView;
    String[] mProjection;
    Cursor mCursor;
    Spinner costType;
    EditText costName;
    EditText costAmount;
    EditText costCode;
    EditText costDate;
    EditText costDescription;
    TypesSettingAdapter cursorAdapter = null;
    ContentValues costValues = new ContentValues();
    ContentValues saleValues = new ContentValues();
    Button cancelButton;
    Button updateButton;
    private String searchQuery;
    private String sortOrder;
    private int sortId;

    public CostSaleProductList() {
        super();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        switch (getArguments().getString("witchActivity")) {
            case "cost": {
                mProjection = new String[]{
                        null,
                        KasebContract.Costs.COLUMN_COST_NAME,
                        KasebContract.Costs.COLUMN_AMOUNT,
                        KasebContract.Costs.COLUMN_COST_CODE,
                        KasebContract.Costs.COLUMN_DESCRIPTION,
                        KasebContract.Costs.COLUMN_COST_TYPE_ID,
                        KasebContract.Costs.COLUMN_DATE};
                break;
            }
            case "sale": {
                mProjection = new String[]{
                        null,
                        KasebContract.Sales.COLUMN_CUSTOMER_ID,
                        KasebContract.Sales.COLUMN_SALE_CODE};
                break;
            }
            case "product": {
                mProjection = new String[]{
                        null,
                        KasebContract.Products.COLUMN_PRODUCT_NAME,
                        KasebContract.Products.COLUMN_PRODUCT_CODE};

                mOpenHelper = KasebProvider.mOpenHelper;
                mDb = mOpenHelper.getWritableDatabase();
                break;
            }
            default:
                break;
        }

        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mAdapter = new CostSaleProductAdapter(
                getActivity(),
                null,
                0,
                getArguments().getString("witchActivity"));

        View rootView = inflater.inflate(R.layout.fragment_cost_sale_product, container, false);
        mListView = (ListView) rootView.findViewById(R.id.list_view_cost_sale_product);
        mListView.setAdapter(mAdapter);

        //region mListView setOnItemClickListener
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (getArguments().getString("witchActivity")) {
                    case "cost": {
                        //region Cost
                        mCursor = (Cursor) parent.getItemAtPosition(position);
                        if (mCursor != null) {
                            dialog = Utility.dialogBuilder(getActivity()
                                    , R.layout.dialog_edit_cost
                                    , R.string.title_edit_cost);

                            costType = (Spinner) dialog.findViewById(R.id.dialog_edit_cost_type_spinner);
                            costName = (EditText) dialog.findViewById(R.id.dialog_edit_cost_name);
                            costAmount = (EditText) dialog.findViewById(R.id.dialog_edit_cost_amount);
                            costCode = (EditText) dialog.findViewById(R.id.dialog_edit_cost_code);
                            costDate = (EditText) dialog.findViewById(R.id.dialog_edit_cost_date);
                            costDescription = (EditText) dialog.findViewById(R.id.dialog_edit_cost_description);

                            costName.setText(mCursor.getString(mCursor.getColumnIndex(KasebContract.Costs.COLUMN_COST_NAME)));
                            costAmount.setText(mCursor.getString(mCursor.getColumnIndex(KasebContract.Costs.COLUMN_AMOUNT)));
                            costCode.setText(mCursor.getString(mCursor.getColumnIndex(KasebContract.Costs.COLUMN_COST_CODE)));
                            costDate.setText(mCursor.getString(mCursor.getColumnIndex(KasebContract.Costs.COLUMN_DATE)));
                            costDescription.setText(mCursor.getString(mCursor.getColumnIndex(KasebContract.Costs.COLUMN_DESCRIPTION)));
                            String costTypeid = mCursor.getString(mCursor.getColumnIndex(KasebContract.Costs.COLUMN_COST_TYPE_ID));

                            Cursor mCursor1 = getContext().getContentResolver().query(
                                    KasebContract.CostTypes.CONTENT_URI,
                                    new String[]{
                                            KasebContract.CostTypes._ID,
                                            KasebContract.CostTypes.COLUMN_COST_TYPE_POINTER},
                                    null, null, null);

                            cursorAdapter = new TypesSettingAdapter(
                                    getContext(), mCursor1, 0,
                                    KasebContract.CostTypes.COLUMN_COST_TYPE_POINTER);

                            costType.setAdapter(cursorAdapter);

                            for (int i = 0; i < costType.getCount(); i++) {
                                Cursor nCursor = (Cursor) costType.getItemAtPosition(i);
                                if (nCursor != null &&
                                        nCursor.getString(
                                                nCursor.getColumnIndex(
                                                        KasebContract.CostTypes._ID))
                                                .equals(costTypeid)) {
                                    costType.setSelection(i);
                                    break;
                                }
                            }

                            costType.setOnItemSelectedListener(
                                    new AdapterView.OnItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(AdapterView<?> arg0, View
                                                arg1, int arg2, long arg3) {

                                            costValues.put(KasebContract.Costs.COLUMN_COST_TYPE_ID, costType.getSelectedItemPosition() + 1);
                                        }

                                        @Override
                                        public void onNothingSelected(AdapterView<?> arg0) {
                                        }

                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        }
                                    });

                            //region update Button
                            updateButton = (Button) dialog.findViewById(R.id.dialog_edit_cost_button_update);
                            updateButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (CheckForValidity(
                                            costName.getText().toString(),
                                            costAmount.getText().toString(),
                                            costDate.getText().toString()
                                    )) {
                                        costValues.put(KasebContract.Costs.COLUMN_COST_NAME, costName.getText().toString());
                                        costValues.put(KasebContract.Costs.COLUMN_COST_CODE, costCode.getText().toString());
                                        costValues.put(KasebContract.Costs.COLUMN_AMOUNT, costAmount.getText().toString());
                                        costValues.put(KasebContract.Costs.COLUMN_DATE, costDate.getText().toString());
                                        costValues.put(KasebContract.Costs.COLUMN_DESCRIPTION, costDescription.getText().toString());

                                        getActivity().getContentResolver().update(
                                                KasebContract.Costs.CONTENT_URI,
                                                costValues,
                                                KasebContract.Costs._ID + " = ? ",
                                                new String[]{mCursor.getString(mCursor.getColumnIndex(KasebContract.Costs._ID))}
                                        );

                                        //just a message to show everything are under control
                                        Toast.makeText(getContext(),
                                                getContext().getResources().getString(R.string.msg_delete_succeed), Toast.LENGTH_LONG).show();

                                        dialog.dismiss();
                                    }
                                }
                            });
                            //endregion update Button

                            //region Cancel Button
                            cancelButton = (Button) dialog
                                    .findViewById(R.id.dialog_edit_cost_button_cancel);

                            cancelButton.setOnClickListener(
                                    new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialog.dismiss();
                                        }
                                    }

                            );
                            //endregion Cancel Button

                            dialog.show();
                        }
                        //endregion Cost
                        break;
                    }
                    case "sale": {
                        //region Sale
                        mCursor = (Cursor) parent.getItemAtPosition(position);
                        if (mCursor != null) {
                            Intent detailSale;
                            detailSale = new Intent(getActivity(), DetailSaleView.class);
                            detailSale.putExtra("forViewAndUpdateSales", true);
                            detailSale.putExtra("saleId", mCursor.getString(mCursor.getColumnIndex(KasebContract.Sales._ID)));
                            detailSale.putExtra("saleCode", mCursor.getString(mCursor.getColumnIndex(KasebContract.Sales.COLUMN_SALE_CODE)));
                            detailSale.putExtra("customerId", mCursor.getLong(mCursor.getColumnIndex(KasebContract.Sales.COLUMN_CUSTOMER_ID)));
                            startActivity(detailSale);
                        }
                        //endregion Sale
                        break;
                    }
                    case "product": {
                        //region Product
                        mCursor = (Cursor) parent.getItemAtPosition(position);
                        if (mCursor != null) {
                            productHistoryBundle.putString("productId", mCursor.getString(mCursor.getColumnIndex(KasebContract.Products._ID)));
                            productHistory.setArguments(productHistoryBundle);
                            fragmentManager = getActivity().getSupportFragmentManager();
                            fragmentManager.beginTransaction().addToBackStack(null).replace(R.id.container, productHistory).commit();
                        }
                        //endregion Product
                        break;
                    }
                    default:
                        break;
                }
            }
        });
        //endregion mListView setOnItemClickListener

        //region mListView setOnItemLongClickListener
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                switch (getArguments().getString("witchActivity")) {
                    case "cost": {
                        //region Cost
                        mCursor = (Cursor) parent.getItemAtPosition(position);
                        if (mCursor != null) {
                            new AlertDialog.Builder(getActivity())
                                    .setTitle("Confirmation ...")
                                    .setMessage("Do You Really Want to Delete This COST?\n\nCost Name : " +
                                            mCursor.getString(mCursor.getColumnIndex(KasebContract.Costs.COLUMN_COST_NAME)))
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int whichButton) {
                                            getActivity().getContentResolver().delete(
                                                    KasebContract.Costs.CONTENT_URI,
                                                    KasebContract.Costs._ID + " = ? ",
                                                    new String[]{mCursor.getString(mCursor.getColumnIndex(KasebContract.Costs._ID))}
                                            );

                                            //just a message to show everything are under control
                                            Toast.makeText(getContext(),
                                                    getContext().getResources().getString(R.string.msg_delete_succeed), Toast.LENGTH_LONG).show();
                                        }
                                    })
                                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int whichButton) {
                                        }
                                    }).show();
                        }
                        //endregion Cost
                        break;
                    }
                    case "sale": {
                        //region Sale
                        mCursor = (Cursor) parent.getItemAtPosition(position);
                        if (mCursor != null) {
                            new AlertDialog.Builder(getActivity())
                                    .setTitle("Confirmation ...")
                                    .setMessage("Do You Really Want to Delete This Sale?\n\nSale Code : " +
                                            mCursor.getString(mCursor.getColumnIndex(KasebContract.Sales.COLUMN_SALE_CODE)))
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int whichButton) {
                                            saleValues.put(KasebContract.Sales.COLUMN_IS_DELETED, 1);

                                            getContext().getContentResolver().update(
                                                    KasebContract.Sales.CONTENT_URI,
                                                    saleValues,
                                                    KasebContract.Sales._ID + " = ? ",
                                                    new String[]{
                                                            mCursor.getString(
                                                                    mCursor.getColumnIndex(KasebContract.Sales._ID))}
                                            );

                                            //just a message to show everything are under control
                                            Toast.makeText(getContext(),
                                                    getContext().getResources().getString(R.string.msg_delete_succeed), Toast.LENGTH_LONG).show();
                                        }
                                    })
                                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int whichButton) {
                                        }
                                    }).show();
                        }
                        //endregion Sale
                        break;
                    }
                    case "product": {
                        //region Product
                        mCursor = (Cursor) parent.getItemAtPosition(position);
                        if (mCursor != null) {
                            long productId = mCursor.getLong(mCursor.getColumnIndex(KasebContract.Products._ID));

                            int numberUseOfThisProduct = getContext().getContentResolver().query(
                                    KasebContract.DetailSaleProducts.uriDetailSaleProductsWithProductId(productId),
                                    new String[]{
                                            KasebContract.DetailSaleProducts._ID},
                                    null, null, null).getCount();

                            if (numberUseOfThisProduct > 0)
                                Toast.makeText(getContext(),
                                        "This Product Is INUSE In "
                                                + numberUseOfThisProduct
                                                + " Factor(s)!", Toast.LENGTH_LONG).show();
                            else {
                                new AlertDialog.Builder(getActivity())
                                        .setTitle("Confirmation ...")
                                        .setMessage("Do You Really Want to Delete This PRODUCT?\n\nProduct Name : " +
                                                mCursor.getString(mCursor.getColumnIndex(KasebContract.Products.COLUMN_PRODUCT_NAME)))
                                        .setIcon(android.R.drawable.ic_dialog_alert)
                                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int whichButton) {

                                                mDb.beginTransaction();
                                                try {
                                                    getActivity().getContentResolver().delete(
                                                            KasebContract.ProductHistory.CONTENT_URI,
                                                            KasebContract.ProductHistory.COLUMN_PRODUCT_ID + " = ? ",
                                                            new String[]{mCursor.getString(mCursor.getColumnIndex(KasebContract.Products._ID))}
                                                    );

                                                    getActivity().getContentResolver().delete(
                                                            KasebContract.Products.CONTENT_URI,
                                                            KasebContract.Products._ID + " = ? ",
                                                            new String[]{mCursor.getString(mCursor.getColumnIndex(KasebContract.Products._ID))}
                                                    );

                                                    mDb.setTransactionSuccessful();
                                                    mDb.endTransaction();
                                                } catch (Exception e) {
                                                    mDb.endTransaction();
                                                }
                                                //just a message to show everything are under control
                                                Toast.makeText(getContext(),
                                                        getContext().getResources().getString(R.string.msg_delete_succeed), Toast.LENGTH_LONG).show();
                                            }
                                        })
                                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int whichButton) {
                                            }
                                        }).show();
                            }

                        }
                        //endregion Product
                        break;
                    }
                    default:
                        break;
                }

                return true;
            }
        });
        //endregion mListView setOnItemLongClickListener

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        switch (getArguments().getString("witchActivity")) {
            case "cost": {
                inflater.inflate(R.menu.menu_costs, menu);
                break;
            }
            case "sale": {
                inflater.inflate(R.menu.menu_sales, menu);
                break;
            }
            case "product": {
                inflater.inflate(R.menu.menu_products, menu);
                break;
            }
            default:
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        Log.d(LOG_TAG, "onStart");
        super.onStart();
        updateList();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onActivityCreated");
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(FRAGMENT_COST_SALE_PRODUCT_LOADER, null, this);
    }

    public void getSearchQuery(String query) {
        searchQuery = query;
        updateList();
    }

    public void getSortOrder(int id) {
        sortId = id;
        updateList();
    }

    private void updateList() {
        getLoaderManager().restartLoader(FRAGMENT_COST_SALE_PRODUCT_LOADER, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.d(LOG_TAG, "onCreateLoader");
        Uri cursorUri = null;
        String _idColumn = null;
        String whereClause = null;
        String[] selectArg = null;
        switch (getArguments().getString("witchActivity")) {
            case "cost": {
                cursorUri = KasebContract.Costs.CONTENT_URI;
                _idColumn = KasebContract.Costs._ID;
                if (searchQuery != null) {
                    whereClause = mProjection[1] + " LIKE ? " + " OR " + mProjection[3] + " LIKE ? ";
                    selectArg = new String[2];
                    selectArg[0] = "%" + searchQuery + "%";
                    selectArg[1] = "%" + searchQuery + "%";
                }
                switch (sortId) {
                    case R.id.menu_sort_code:
                        sortOrder = KasebContract.Costs.COLUMN_COST_CODE + " ASC," + KasebContract.Costs.COLUMN_COST_NAME + " ASC";
                        break;
                    case R.id.menu_sort_date:
                        sortOrder = KasebContract.Costs.COLUMN_DATE + " ASC," + KasebContract.Costs.COLUMN_COST_NAME + " ASC";
                        break;
                }
                break;
            }
            case "sale": {
                cursorUri = KasebContract.Sales.CONTENT_URI;
                _idColumn = KasebContract.Sales._ID;
                whereClause = KasebContract.Sales.COLUMN_IS_DELETED + " = ? ";
                selectArg = new String[]{"0"};
                if (searchQuery != null) {
                    whereClause = KasebContract.Sales.COLUMN_IS_DELETED + " = ? and " + mProjection[2] + " LIKE ? ";
                    selectArg = new String[2];
                    selectArg[0] = "0";
                    selectArg[1] = "%" + searchQuery + "%";
                }
                switch (sortId) {
                    case R.id.menu_sort_code:
                        sortOrder = KasebContract.Sales.COLUMN_SALE_CODE + " ASC";
                        break;
                    case R.id.menu_sort_date:
                        sortOrder = null;
                        break;
                }
                break;
            }
            case "product": {
                cursorUri = KasebContract.Products.CONTENT_URI;
                _idColumn = KasebContract.Products._ID;
                if (searchQuery != null) {
                    whereClause = mProjection[1] + " LIKE ? " + " OR " + mProjection[2] + " LIKE ? ";
                    selectArg = new String[2];
                    selectArg[0] = "%" + searchQuery + "%";
                    selectArg[1] = "%" + searchQuery + "%";
                }
                switch (sortId) {
                    case R.id.menu_sort_code:
                        sortOrder = KasebContract.Products.COLUMN_PRODUCT_CODE + " ASC";
                        break;
                    case R.id.menu_sort_name:
                        sortOrder = KasebContract.Products.COLUMN_PRODUCT_NAME + " ASC";
                        break;
                }
                break;
            }
            default:
                new UnsupportedOperationException("Cost/Sale/Product Code Not Match..!");
        }

        mProjection[0] = _idColumn;
        return new CursorLoader(getActivity(), cursorUri, mProjection, whereClause, selectArg, sortOrder);
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
    private boolean CheckForValidity(String costName, String costAmount, String costDate) {
        if (costName.equals("") || costName.equals(null)) {
            Toast.makeText(getActivity(), "Choose apropriate name for COST.", Toast.LENGTH_SHORT).show();
            return false;
        } else if (costAmount.equals("") || costAmount.equals(null)) {
            Toast.makeText(getActivity(), "Choose apropriate amount for COST.", Toast.LENGTH_SHORT).show();
            return false;
        } else if (costDate.equals("") || costDate.equals(null)) {
            Toast.makeText(getActivity(), "Choose apropriate date for COST.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
