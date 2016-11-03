package mjkarbasian.moshtarimadar;

import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import mjkarbasian.moshtarimadar.Data.KasebContract;
import mjkarbasian.moshtarimadar.adapters.TypesSettingAdapter;

/**
 * Created by family on 11/3/2016.
 */
public class TypeSettingFragment extends Fragment  implements LoaderManager.LoaderCallbacks<Cursor> {
    static TypesSettingAdapter adapter = null;
    ListView mListView;
    private final String LOG_TAG = TypesSettingAdapter.class.getSimpleName();
    String mColumnName;
    final static int FRAGMENT_TYPE_LOADER = 0;

    public TypeSettingFragment() {
        super();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mColumnName = getArguments().getString("columnName");
        adapter = new TypesSettingAdapter(getActivity(), null, 0, mColumnName);
        View rootView = inflater.inflate(R.layout.fragment_setting_types, container, false);
        mListView = (ListView) rootView.findViewById(R.id.list_view_setting_types);
        mListView.setAdapter(adapter);
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.types_setting, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
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
        getLoaderManager().initLoader(FRAGMENT_TYPE_LOADER, null, this);
    }

    private void updateList() {

        getLoaderManager().restartLoader(FRAGMENT_TYPE_LOADER, null, this);

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.d(LOG_TAG, "onCreateLoader");
        Uri cursorUri = null;
        String _idColumn = null;
        switch (mColumnName) {
            case (KasebContract.CostTypes.COLUMN_COST_TYPE_POINTER): {
                cursorUri = KasebContract.CostTypes.CONTENT_URI;
                _idColumn = KasebContract.CostTypes._ID;
                break;
            }
            case (KasebContract.TaxTypes.COLUMN_TAX_TYPE_POINTER): {
                cursorUri = KasebContract.TaxTypes.CONTENT_URI;
                _idColumn = KasebContract.TaxTypes._ID;

                break;
            }
            case (KasebContract.PaymentMethods.COLUMN_PAYMENT_METHOD_POINTER): {
                cursorUri = KasebContract.PaymentMethods.CONTENT_URI;
                _idColumn = KasebContract.PaymentMethods._ID;

                break;
            }
            case (KasebContract.State.COLUMN_STATE_POINTER): {
                cursorUri = KasebContract.State.CONTENT_URI;
                _idColumn = KasebContract.State._ID;
                break;
            }
            default:
                new UnsupportedOperationException("Setting Not Match..!");
        }

        String[] mProjection = {_idColumn ,mColumnName};
        return new CursorLoader(getActivity(), cursorUri, mProjection, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        Log.d(LOG_TAG, "onLoadFinished");
        adapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Log.d(LOG_TAG, "onLoadReset");
        adapter.swapCursor(null);
    }

}
