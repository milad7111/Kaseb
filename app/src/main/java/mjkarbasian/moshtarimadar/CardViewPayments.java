package mjkarbasian.moshtarimadar;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
    String _id;
    String _amount;

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
            }
        });
        //endregion PaymentListView OnItemClickListener

        //region PaymentListView OnItemLongClickListener
        paymentListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Map<String, String> cursor = (Map<String, String>) parent.getItemAtPosition(position);
                if (cursor != null) {
                    _id = cursor.get("id");
                    _amount = cursor.get("amount");

                    new AlertDialog.Builder(getActivity())
                            .setTitle("Confirmation ...")
                            .setMessage("Do You Really Want to Delete This PAYMENT?\n\nPayment Amount : " + _amount)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int whichButton) {
                                    mPaymentListHashMap.remove(Utility.indexOfRowsInMap(mPaymentListHashMap, "id", _id));
                                    paymentListView.setAdapter(mPaymentAdapter);

                                    if (activity.equals("insert"))
                                        ((DetailSaleInsert) getActivity()).setValuesOfFactor();
                                    else if (activity.equals("view"))
                                        ((DetailSaleView) getActivity()).setValuesOfFactor();

                                    Utility.setHeightOfListView(paymentListView);
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int whichButton) {
                                }
                            }).show();
                }

                return true;
            }
        });
        //endregion PaymentListView OnItemLongClickListener

        return view;
    }

    public void getPaymentAdapter(ArrayList<Map<String, String>> list) {
        mPaymentListHashMap = list;
        mPaymentAdapter = new PaymentAdapter(getActivity(), mPaymentListHashMap);
        paymentListView.setAdapter(mPaymentAdapter);

        if (activity.equals("insert"))
            ((DetailSaleInsert) getActivity()).setValuesOfFactor();
        else if (activity.equals("view"))
            ((DetailSaleView) getActivity()).setValuesOfFactor();

        Utility.setHeightOfListView(paymentListView);
    }
}
