package mjkarbasian.moshtarimadar;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
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
import android.widget.ListView;

import mjkarbasian.moshtarimadar.Data.KasebContract;
import mjkarbasian.moshtarimadar.adapters.CustomerAdapter;

/**
 * A placeholder fragment containing a simple view.
 */
public class CustomersLists extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private final String LOG_TAG = CustomersLists.class.getSimpleName();
    final static int FRAGMENT_CUSTOMER_LOADER = 2;
    String searchQuery;

    CustomerAdapter mCustomerAdapter = null;
    ListView mListView;
    String[] mProjection;
    private String sortOrder = null;


    public CustomersLists() {
        super();
    }

    public void onCreate(Bundle savedInstanceState) {

        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab_customers);

        mProjection = new String[]{
                KasebContract.Customers._ID,
                KasebContract.Customers.COLUMN_FIRST_NAME,
                KasebContract.Customers.COLUMN_LAST_NAME,
                KasebContract.Customers.COLUMN_STATE_ID};

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(getArguments()!=null) searchQuery = getArguments().getString("query");
        mCustomerAdapter = new CustomerAdapter(
                getActivity(),
                null,
                0);

        View rootView = inflater.inflate(R.layout.fragment_customers, container, false);
        mListView = (ListView) rootView.findViewById(R.id.list_view_customers);
        mListView.setAdapter(mCustomerAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = (Cursor) parent.getItemAtPosition(position);
                if (cursor != null) {
                    Intent intent = new Intent(getActivity(), DetailCustomer.class)
                            .setData(KasebContract.Customers.buildCustomerUri(
                                    Long.parseLong(cursor.getString(cursor.getColumnIndex(KasebContract.Customers._ID)))
                            ));
                    startActivity(intent);
                }
            }
        });
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_customers, menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    public void getSearchQuery(String query){
        searchQuery = query;
        updateList();
    }

    public void getSortOrder(int id){
        switch (id){
            case R.id.menu_sort_newest:
                sortOrder =  KasebContract.Customers.COLUMN_FIRST_NAME + " ASC,"  + KasebContract.Customers.COLUMN_LAST_NAME + " ASC";
                break;
            case R.id.menu_sort_rating:
                sortOrder = KasebContract.Customers.COLUMN_STATE_ID + " ASC," +  KasebContract.Customers.COLUMN_FIRST_NAME + " ASC" ;
                break;
        }
        updateList();
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
        getLoaderManager().initLoader(FRAGMENT_CUSTOMER_LOADER, null, this);
    }

    private void updateList() {
        getLoaderManager().restartLoader(FRAGMENT_CUSTOMER_LOADER, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.d(LOG_TAG, "onCreateLoader");
        String whereClause;
        String[] selectArg = null;
        if(searchQuery!=null) {
            whereClause = mProjection[1] + " LIKE ? " + " OR " + mProjection[2] + " LIKE ? ";
            selectArg = new String[2];
            selectArg[0] = "%"+searchQuery + "%" ;
            selectArg[1] = "%"+searchQuery + "%" ;
        }
        else
        {
         whereClause = null;
        }
        return new CursorLoader(getActivity(), KasebContract.Customers.CONTENT_URI, mProjection, whereClause, selectArg, sortOrder);
}

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        Log.d(LOG_TAG, "onLoadFinished");
        mCustomerAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Log.d(LOG_TAG, "onLoadReset");
        mCustomerAdapter.swapCursor(null);
    }


}
