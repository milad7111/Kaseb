package mjkarbasian.moshtarimadar;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Map;

import mjkarbasian.moshtarimadar.adapters.ProductAdapter;
import mjkarbasian.moshtarimadar.helper.Utility;

/**
 * Created by Unique on 20/12/2016.
 */
public class CardViewProducts extends Fragment {
    ListView productListView;
    ProductAdapter mChosenProductAdapter;
    ArrayList<Map<String, String>> mProductListHashMap;
    View view;
    String activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_card_view_products, container, false);
        activity = getArguments().getString("activity").toString();

        mChosenProductAdapter = new ProductAdapter(getActivity(), mProductListHashMap);
        productListView = (ListView) view.findViewById(R.id.list_view_fragment_card_view_products);
        productListView.setAdapter(mChosenProductAdapter);

        //region ProductListView OnItemClickListener
        productListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map<String, String> cursor = (Map<String, String>) parent.getItemAtPosition(position);
                if (cursor != null) {
                    String _id = cursor.get("id");
                    mProductListHashMap.remove(Utility.indexOfRowsInMap(mProductListHashMap, "id", _id));
                    productListView.setAdapter(mChosenProductAdapter);

                    if (activity.equals("insert"))
                        ((DetailSaleInsert) getActivity()).setValuesOfFactor();
                    else if (activity.equals("view"))
                        ((DetailSaleView) getActivity()).setValuesOfFactor();
                }
            }
        });
        //endregion ProductListView OnItemClickListener

        //region ProductListView OnItemLongClickListener
        productListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final Map<String, String> cursor = (Map<String, String>) parent.getItemAtPosition(position);
                if (cursor != null) {
                    String _id = cursor.get("id");

                    final int index = Utility.indexOfRowsInMap(mProductListHashMap, "id", _id);

                    //region Show Dialog To Edit Number Of Product
                    final Dialog howManyOfThatForEdit = Utility.dialogBuilder(getActivity()
                            , R.layout.dialog_edit_chosen_product_for_sale
                            , R.string.how_many);

                    final EditText howManyEditTextForEdit = (EditText) howManyOfThatForEdit
                            .findViewById(R.id.edit_chosen_product_for_sale_number);

                    Button saveButtonForEdit = (Button) howManyOfThatForEdit
                            .findViewById(R.id.edit_chosen_product_for_sale_save);

                    saveButtonForEdit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Long num = Long.parseLong(howManyEditTextForEdit.getText().toString());

                            mProductListHashMap.set(index,
                                    Utility.setValueWithIndexInKeyOfMapRow(
                                            cursor,
                                            "quantity",
                                            String.valueOf(num)
                                    ));

                            productListView.setAdapter(mChosenProductAdapter);

                            String activity = getArguments().getString("activity").toString();
                            if (activity.equals("insert"))
                                ((DetailSaleInsert) getActivity()).setValuesOfFactor();

                            howManyOfThatForEdit.dismiss();
                        }
                    });

                    Button cancelButtonForEdit = (Button) howManyOfThatForEdit
                            .findViewById(R.id.edit_chosen_product_for_sale_cancel);

                    cancelButtonForEdit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            howManyOfThatForEdit.dismiss();
                        }
                    });

                    howManyOfThatForEdit.show();
                    //endregion Show Dialog To Edit Number Of Product
                }
                return true;
            }
        });
        //endregion ProductListView OnItemLongClickListener

        mProductListHashMap = null;
        return view;
    }

    public void getChosenProductAdapterForView(ArrayList<Map<String, String>> list) {
        mProductListHashMap = list;

        if (activity.equals("insert"))
            ((DetailSaleInsert) getActivity()).setValuesOfFactor();
        else if (activity.equals("view"))
            ((DetailSaleView) getActivity()).setValuesOfFactor();
    }

    public void getChosenProductAdapterForInsert(ArrayList<Map<String, String>> list) {
        mProductListHashMap = list;
        productListView.setAdapter(mChosenProductAdapter);

        if (activity.equals("insert"))
            ((DetailSaleInsert) getActivity()).setValuesOfFactor();
        else if (activity.equals("view"))
            ((DetailSaleView) getActivity()).setValuesOfFactor();
    }

    public void getChosenProductAdapter(ArrayList<Map<String, String>> list) {
        mProductListHashMap = list;
        mChosenProductAdapter = new ProductAdapter(getActivity(), mProductListHashMap);
        productListView.setAdapter(mChosenProductAdapter);

        if (activity.equals("insert"))
            ((DetailSaleInsert) getActivity()).setValuesOfFactor();
        else if (activity.equals("view"))
            ((DetailSaleView) getActivity()).setValuesOfFactor();
    }


}
