package mjkarbasian.moshtarimadar;

import android.app.Dialog;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import android.widget.Button;
import android.widget.ListView;

import mjkarbasian.moshtarimadar.Data.KasebContract;
import mjkarbasian.moshtarimadar.adapters.TypesSettingAdapter;
import mjkarbasian.moshtarimadar.helper.Utility;

import static mjkarbasian.moshtarimadar.Data.KasebContract.CostTypes;
import static mjkarbasian.moshtarimadar.Data.KasebContract.PaymentMethods;
import static mjkarbasian.moshtarimadar.Data.KasebContract.State;
import static mjkarbasian.moshtarimadar.Data.KasebContract.TaxTypes;

/**
 * Created by family on 11/3/2016.
 */
public class TypeSettingFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    static TypesSettingAdapter adapter = null;
    ListView mListView;
    private final String LOG_TAG = TypesSettingAdapter.class.getSimpleName();
    String mColumnName;
    final static String COST_TYPE_CLASS_NAME = KasebContract.CostTypes.class.getSimpleName();
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
        Dialog typeInsert = null;
            switch (mColumnName) {
                case CostTypes.COLUMN_COST_TYPE_POINTER:
                     typeInsert = Utility.dialogBuilder(getActivity(), R.layout.dialog_add_type_setting, R.id.title_dialog_add_cost_type);
                    break;
                case PaymentMethods.COLUMN_PAYMENT_METHOD_POINTER:
                     typeInsert = Utility.dialogBuilder(getActivity(), R.layout.dialog_add_type_setting, R.id.title_dialog_add_payment_methods);
                    break;
                case TaxTypes.COLUMN_TAX_TYPE_POINTER:
                    typeInsert = Utility.dialogBuilder(getActivity(), R.layout.dialog_add_type_setting, R.id.title_dialog_add_tax_types);
                    break;
                case State.COLUMN_STATE_POINTER:
                    typeInsert = Utility.dialogBuilder(getActivity(), R.layout.dialog_add_type_setting, R.id.title_dialog_add_state);
                    break;
            }
            if(typeInsert!=null) {
                typeInsert.show();
                Button dialogButton =(Button)typeInsert.findViewById(R.id.button_add_type_setting);
                final Dialog finalTypeInsert = typeInsert;
                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finalTypeInsert.dismiss();
                    }
                });
            }
            else Log.d(LOG_TAG," Insert Dialog is null..! ");
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
            case (CostTypes.COLUMN_COST_TYPE_POINTER): {
                cursorUri = CostTypes.CONTENT_URI;
                _idColumn = CostTypes._ID;
                break;
            }
            case (TaxTypes.COLUMN_TAX_TYPE_POINTER): {
                cursorUri = TaxTypes.CONTENT_URI;
                _idColumn = TaxTypes._ID;

                break;
            }
            case (PaymentMethods.COLUMN_PAYMENT_METHOD_POINTER): {
                cursorUri = PaymentMethods.CONTENT_URI;
                _idColumn = PaymentMethods._ID;

                break;
            }
            case (State.COLUMN_STATE_POINTER): {
                cursorUri = State.CONTENT_URI;
                _idColumn = State._ID;
                break;
            }
            default:
                new UnsupportedOperationException("Setting Not Match..!");
        }

        String[] mProjection = {_idColumn, mColumnName};
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
