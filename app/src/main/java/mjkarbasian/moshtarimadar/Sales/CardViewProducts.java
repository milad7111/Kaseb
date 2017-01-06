package mjkarbasian.moshtarimadar.Sales;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Map;

import mjkarbasian.moshtarimadar.Adapters.ProductAdapter;
import mjkarbasian.moshtarimadar.Helpers.Utility;
import mjkarbasian.moshtarimadar.R;

/**
 * Created by Unique on 20/12/2016.
 */
public class CardViewProducts extends Fragment {
    ListView productListView;
    ProductAdapter mChosenProductAdapter;
    ArrayList<Map<String, String>> mProductListHashMap;
    View view;
    String _id;
    String _nameOfProduct;
    String activity;
    Long differneceOfBuy_Sale;
    Long mDetailSaleId = 0l;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_card_view_products, container, false);
        activity = getArguments().getString("activity").toString();
        mChosenProductAdapter = new ProductAdapter(getActivity(), mProductListHashMap);
        productListView = (ListView) view.findViewById(R.id.list_view_fragment_card_view_products);
        productListView.setAdapter(mChosenProductAdapter);
        try {
            mDetailSaleId = getArguments().getLong("detailSaleId");
        } catch (Exception e) {
            Log.i(CardViewProducts.class.getName(),
                    "DetailSaleView does not put DetailSaleId in Bundle of CardViewProducts Fragments.");
        }

        //region ProductListView OnItemClickListener
        productListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Map<String, String> cursor = (Map<String, String>) parent.getItemAtPosition(position);
                if (cursor != null) {
                    _id = cursor.get("id");

                    final int index = Utility.indexOfRowsInMap(mProductListHashMap, "id", _id);

                    if (activity.equals("insert"))
                        //in this line does not matter post 0l to second parameter of function because not use in this case
                        differneceOfBuy_Sale = Utility.checkNumberOfProductsForDetailSale(getActivity(),
                                0l, "SaleInsert", Long.parseLong(_id));
                    else if (activity.equals("view"))
                        differneceOfBuy_Sale = Utility.checkNumberOfProductsForDetailSale(getActivity(),
                                mDetailSaleId, "SaleView", Long.parseLong(_id));

                    //region Show Dialog To Edit Number Of Product
                    final Dialog howManyOfThatForEdit = Utility.dialogBuilder(getActivity()
                            , R.layout.dialog_edit_chosen_product_for_sale
                            , R.string.how_many);

                    final EditText howManyEditTextForEdit = (EditText) howManyOfThatForEdit
                            .findViewById(R.id.edit_chosen_product_for_sale_number);

                    howManyEditTextForEdit.setHint("Stock is : " + differneceOfBuy_Sale);

                    Button saveButtonForEdit = (Button) howManyOfThatForEdit
                            .findViewById(R.id.edit_chosen_product_for_sale_save);

                    saveButtonForEdit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Long num = Long.parseLong(howManyEditTextForEdit.getText().toString());

                            if (differneceOfBuy_Sale >= num) {
                                mProductListHashMap.set(index,
                                        Utility.setValueWithIndexInKeyOfMapRow(
                                                cursor,
                                                "quantity",
                                                String.valueOf(num)
                                        ));

                                productListView.setAdapter(mChosenProductAdapter);

                                if (activity.equals("insert"))
                                    ((DetailSaleInsert) getActivity()).setValuesOfFactor();
                                else if (activity.equals("view"))
                                    ((DetailSaleView) getActivity()).setValuesOfFactor();

                                Utility.setHeightOfListView(productListView);

                                howManyOfThatForEdit.dismiss();
                            } else
                                Toast.makeText(getContext(), "There is not enough good in stock.",
                                        Toast.LENGTH_SHORT).show();
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
            }
        });
        //endregion ProductListView OnItemClickListener

        //region ProductListView OnItemLongClickListener
        productListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> parent, View view, final int position, long id) {
                Map<String, String> cursor = (Map<String, String>) parent.getItemAtPosition(position);

                if (cursor != null) {
                    _id = cursor.get("id");
                    _nameOfProduct = cursor.get("name");

                    new AlertDialog.Builder(getActivity())
                            .setTitle(getActivity().getResources().getString(R.string.confirm_title))
                            .setMessage(getActivity().getResources().getString(R.string.confirm_delete_product_list) + _nameOfProduct)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int whichButton) {
                                    mProductListHashMap.remove(Utility.indexOfRowsInMap(mProductListHashMap, "id", _id));
                                    productListView.setAdapter(mChosenProductAdapter);

                                    if (activity.equals("insert"))
                                        ((DetailSaleInsert) getActivity()).setValuesOfFactor();
                                    else if (activity.equals("view"))
                                        ((DetailSaleView) getActivity()).setValuesOfFactor();

                                    Utility.setHeightOfListView(productListView);
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
        //endregion ProductListView OnItemLongClickListener

        mProductListHashMap = null;
        return view;
    }

    public void getChosenProductAdapter(ArrayList<Map<String, String>> list) {
        mProductListHashMap = list;
        mChosenProductAdapter = new ProductAdapter(getActivity(), mProductListHashMap);
        productListView.setAdapter(mChosenProductAdapter);

        if (activity.equals("insert"))
            ((DetailSaleInsert) getActivity()).setValuesOfFactor();
        else if (activity.equals("view"))
            ((DetailSaleView) getActivity()).setValuesOfFactor();

        Utility.setHeightOfListView(productListView);
    }
}
