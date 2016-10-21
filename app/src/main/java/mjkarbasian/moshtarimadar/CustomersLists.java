package mjkarbasian.moshtarimadar;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import mjkarbasian.moshtarimadar.adapters.CustomerAdapter;

/**
 * A placeholder fragment containing a simple view.
 */
public class CustomersLists extends Fragment {

    CustomerAdapter mCustomerAdapter;
    ListView mListView;
    public CustomersLists() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mCustomerAdapter = new CustomerAdapter(getActivity());
        View rootView = inflater.inflate(R.layout.fragment_customers, container, false);
        mListView = (ListView)rootView.findViewById(R.id.list_view_customers);
        mListView.setAdapter(mCustomerAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(),DetailCustomer.class).putExtra("position",position);
                startActivity(intent);
            }
        });
        return rootView;
    }
}
