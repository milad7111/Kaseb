package mjkarbasian.moshtarimadar;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import mjkarbasian.moshtarimadar.helper.Samples;
import mjkarbasian.moshtarimadar.helper.Utility;

/**
 * Created by family on 7/27/2016.
 */
public class DetailCustomerDash extends Fragment {
    Integer customerPosition;
    public DetailCustomerDash(Integer position) {
        super();
        customerPosition = position;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.customer_dash, container, false);
        ImageView customerMembership = (ImageView) view.findViewById(R.id.customer_dash_membership);
        Utility.setCustomerState(customerMembership, Samples.customerMembership[customerPosition]);


        TextView customerPurchase = (TextView) view.findViewById(R.id.customer_dash_total_purchase);
        customerPurchase.setText(Utility.formatPurchase(getActivity(),Utility.DecimalSeperation(getContext(),Samples.customerPurchaseAmount[customerPosition])));

        TextView customerBalance = (TextView) view.findViewById(R.id.customer_dash_total_balance);

        switch (Samples.customerName[customerPosition])
        {
            case (R.string.sample_ali_ghorbani):
                customerBalance.setText(Utility.formatPurchase(getContext(),Utility.DecimalSeperation(getContext(),Samples.customerDebateBalance[0])));
                break;
            case (R.string.sample_mohammad_alikhani):
                customerBalance.setText(Utility.formatPurchase(getContext(),Integer.toString(0)));
                break;
            case(R.string.sample_sima_saberzadeh):
                customerBalance.setText(Utility.formatPurchase(getContext(),Utility.DecimalSeperation(getContext(), Samples.customerDebateBalance[1])));
                break;
            default:
        }


        TextView customerNumberP = (TextView) view.findViewById(R.id.customer_dash_number_purchase);
        customerNumberP.setText(Utility.doubleFormatter(Samples.numberOfPurchase[customerPosition]));



        return view;
    }
}