package mjkarbasian.moshtarimadar;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import mjkarbasian.moshtarimadar.adapters.CustomerBillAdapter;
import mjkarbasian.moshtarimadar.helper.Samples;

/**
 * Created by family on 7/27/2016.
 */
public class DetailCustomerBill extends Fragment {
    protected final Integer customerPostion;
    public DetailCustomerBill(Integer position) {
        super();
        customerPostion = position;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.customer_bill, container, false);
        //it most change to recyclerview for scrolling http://stackoverflow.com/questions/35577206/listview-inside-viewpager-not-scrolling
        ListView customerBills = (ListView)view.findViewById(R.id.list_view_customer_bills);
        if(Samples.sales.size()==0){
            Samples.setSaleDueDate();
            Samples.setSalesAmount();
            Samples.setSalesCode();
            Samples.setSalesCustomer(getActivity());
            Samples.setSale();}
        CustomerBillAdapter adaper = new CustomerBillAdapter(getActivity(),customerPostion);
        customerBills.setAdapter(adaper);
        return view;
    }
}
