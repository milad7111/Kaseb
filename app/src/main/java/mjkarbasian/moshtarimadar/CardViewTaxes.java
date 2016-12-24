package mjkarbasian.moshtarimadar;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Map;

import mjkarbasian.moshtarimadar.adapters.TaxAdapter;
import mjkarbasian.moshtarimadar.helper.Utility;

/**
 * Created by Unique on 20/12/2016.
 */
public class CardViewTaxes extends Fragment {
    ListView taxListView;
    TaxAdapter mTaxAdapter;
    ArrayList<Map<String, String>> mTaxListHashMap;
    View view;
    String activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_card_view_taxes, container, false);
        activity = getArguments().getString("activity").toString();

        mTaxAdapter = new TaxAdapter(getActivity(), mTaxListHashMap);
        taxListView = (ListView) view.findViewById(R.id.list_view_fragment_card_view_taxes);
        taxListView.setAdapter(mTaxAdapter);

        //region TaxListView OnItemClickListener
        taxListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map<String, String> cursor = (Map<String, String>) parent.getItemAtPosition(position);
                if (cursor != null) {
                    String _id = cursor.get("id");
                    mTaxListHashMap.remove(Utility.indexOfRowsInMap(mTaxListHashMap, "id", _id));
                    taxListView.setAdapter(mTaxAdapter);

                    if (activity.equals("insert"))
                        ((DetailSaleInsert) getActivity()).setValuesOfFactor();
                    else if (activity.equals("view"))
                        ((DetailSaleView) getActivity()).setValuesOfFactor();
                }
            }
        });
        //endregion TaxListView OnItemClickListener

        //region TaxListView OnItemLongClickListener
        //endregion TaxListView OnItemLongClickListener

        return view;
    }

    public void getTaxAdapterForView(ArrayList<Map<String, String>> list) {
        mTaxListHashMap = list;

        if (activity.equals("insert"))
            ((DetailSaleInsert) getActivity()).setValuesOfFactor();
        else if (activity.equals("view"))
            ((DetailSaleView) getActivity()).setValuesOfFactor();
    }

    public void getTaxAdapterForInsert(ArrayList<Map<String, String>> list) {
        mTaxListHashMap = list;
        taxListView.setAdapter(mTaxAdapter);

        if (activity.equals("insert"))
            ((DetailSaleInsert) getActivity()).setValuesOfFactor();
        else if (activity.equals("view"))
            ((DetailSaleView) getActivity()).setValuesOfFactor();
    }

    public void getTaxAdapter(ArrayList<Map<String, String>> list) {
        mTaxListHashMap = list;
        mTaxAdapter = new TaxAdapter(getActivity(), mTaxListHashMap);
        taxListView.setAdapter(mTaxAdapter);

        if (activity.equals("insert"))
            ((DetailSaleInsert) getActivity()).setValuesOfFactor();
        else if (activity.equals("view"))
            ((DetailSaleView) getActivity()).setValuesOfFactor();
    }
}
