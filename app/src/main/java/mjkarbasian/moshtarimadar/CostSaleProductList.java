package mjkarbasian.moshtarimadar;


import android.content.Intent;
import android.database.Cursor;
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
import android.widget.ListView;

import mjkarbasian.moshtarimadar.Data.KasebContract;
import mjkarbasian.moshtarimadar.adapters.CostSaleProductAdapter;

/**
 * Created by Unique on 10/11/2016.
 */
public class CostSaleProductList extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    final static int FRAGMENT_COST_SALE_PRODUCT_LOADER = 1;
    private final String LOG_TAG = CostSaleProductList.class.getSimpleName();
    FragmentManager fragmentManager;

    Fragment productHistory = new DetailProducts();
    Bundle productHistoryBundle = new Bundle();

    CostSaleProductAdapter mAdapter = null;
    ListView mListView;
    String[] mProjection;
    Cursor mCursor;
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

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (getArguments().getString("witchActivity")) {
                    case "cost": {
                        // rise detail cost
                        break;
                    }
                    case "sale": {
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
                        break;
                    }
                    case "product": {
                        mCursor = (Cursor) parent.getItemAtPosition(position);
                        if (mCursor != null) {
                            productHistoryBundle.putString("productId", mCursor.getString(mCursor.getColumnIndex(KasebContract.Products._ID)));
                            productHistory.setArguments(productHistoryBundle);
                            fragmentManager = getActivity().getSupportFragmentManager();
                            fragmentManager.beginTransaction().replace(R.id.container, productHistory).commit();
                        }
                        mCursor.close();
                        break;
                    }
                    default:
                        break;
                }
            }
        });

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
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//        Dialog typeInsert = null;
//        switch (mColumnName) {
//            case KasebContract.CostTypes.COLUMN_COST_TYPE_POINTER:
//                typeInsert = Utility.dialogBuilder(getActivity(), R.layout.dialog_add_type_setting, R.id.title_dialog_add_cost_type);
//                break;
//            case KasebContract.PaymentMethods.COLUMN_PAYMENT_METHOD_POINTER:
//                typeInsert = Utility.dialogBuilder(getActivity(), R.layout.dialog_add_type_setting, R.id.title_dialog_add_payment_methods);
//                break;
//            case KasebContract.TaxTypes.COLUMN_TAX_TYPE_POINTER:
//                typeInsert = Utility.dialogBuilder(getActivity(), R.layout.dialog_add_type_setting, R.id.title_dialog_add_tax_types);
//                break;
//            case KasebContract.State.COLUMN_STATE_POINTER:
//                typeInsert = Utility.dialogBuilder(getActivity(), R.layout.dialog_add_type_setting, R.id.title_dialog_add_state);
//                break;
//        }
//        if (typeInsert != null) {
//            typeInsert.show();
//            Button dialogButton = (Button) typeInsert.findViewById(R.id.button_add_type_setting);
//            final Dialog finalTypeInsert = typeInsert;
//            dialogButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    finalTypeInsert.dismiss();
//                }
//            });
//        } else Log.d(LOG_TAG, " Insert Dialog is null..! ");
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
                if (searchQuery != null) {
                    whereClause = mProjection[2] + " LIKE ? ";
                    selectArg = new String[1];
                    selectArg[0] = "%" + searchQuery + "%";
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
}
