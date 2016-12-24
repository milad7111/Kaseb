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

import mjkarbasian.moshtarimadar.adapters.PaymentAdapter;
import mjkarbasian.moshtarimadar.helper.Utility;

/**
 * Created by Unique on 20/12/2016.
 */
public class CardViewPayments extends Fragment {
    ListView paymentListView;
    PaymentAdapter mPaymentAdapter;
    ArrayList<Map<String, String>> mPaymentListHashMap;
    View view;
    String activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_card_view_payments, container, false);
        activity = getArguments().getString("activity").toString();

        mPaymentAdapter = new PaymentAdapter(getActivity(), mPaymentListHashMap);
        paymentListView = (ListView) view.findViewById(R.id.list_view_fragment_card_view_payments);
        paymentListView.setAdapter(mPaymentAdapter);

        //region PaymentListView OnItemClickListener
        paymentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map<String, String> cursor = (Map<String, String>) parent.getItemAtPosition(position);
                if (cursor != null) {
                    String _id = cursor.get("id");
                    mPaymentListHashMap.remove(Utility.indexOfRowsInMap(mPaymentListHashMap, "id", _id));
                    paymentListView.setAdapter(mPaymentAdapter);

                    if (activity.equals("insert"))
                        ((DetailSaleInsert) getActivity()).setValuesOfFactor();
                    else if (activity.equals("view"))
                        ((DetailSaleView) getActivity()).setValuesOfFactor();
                }
            }
        });
        //endregion PaymentListView OnItemClickListener

        //region PaymentListView OnItemLongClickListener
        //endregion PaymentListView OnItemLongClickListener

        return view;
    }

    public void getPaymentAdapterForView(ArrayList<Map<String, String>> list) {
        mPaymentListHashMap = list;

        if (activity.equals("insert"))
            ((DetailSaleInsert) getActivity()).setValuesOfFactor();
        else if (activity.equals("view"))
            ((DetailSaleView) getActivity()).setValuesOfFactor();
    }

    public void getPaymentAdapterForInsert(ArrayList<Map<String, String>> list) {
        mPaymentListHashMap = list;
        paymentListView.setAdapter(mPaymentAdapter);

        if (activity.equals("insert"))
            ((DetailSaleInsert) getActivity()).setValuesOfFactor();
        else if (activity.equals("view"))
            ((DetailSaleView) getActivity()).setValuesOfFactor();
    }

    public void getPaymentAdapter(ArrayList<Map<String, String>> list) {
        mPaymentListHashMap = list;
        mPaymentAdapter = new PaymentAdapter(getActivity(), mPaymentListHashMap);
        paymentListView.setAdapter(mPaymentAdapter);

        if (activity.equals("insert"))
            ((DetailSaleInsert) getActivity()).setValuesOfFactor();
        else if (activity.equals("view"))
            ((DetailSaleView) getActivity()).setValuesOfFactor();
    }
}
