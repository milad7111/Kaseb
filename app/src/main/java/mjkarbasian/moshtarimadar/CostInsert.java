package mjkarbasian.moshtarimadar;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

/**
 * Created by family on 10/19/2016.
 */
public class CostInsert extends Fragment {
    EditText costName;
    EditText costCode;
    EditText costAmount;
    Spinner costType;
    EditText costDate;
    EditText costDescription;
    public CostInsert() {
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.fragment_cost_insert, container, false);

         costType = (Spinner) rootView.findViewById(R.id.input_cost_type_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.cost_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        costType.setAdapter(adapter);

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(
            Menu menu, MenuInflater inflater) {
        menu.removeItem(R.id.sort_button);
        menu.removeItem(R.id.search_button);
        inflater.inflate(R.menu.fragments_for_insert, menu);
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save:{

               break;
            }

        }
        return super.onOptionsItemSelected(item);
    }
}

