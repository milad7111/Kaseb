package mjkarbasian.moshtarimadar.Products;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import mjkarbasian.moshtarimadar.Costs.CostInsert;
import mjkarbasian.moshtarimadar.Data.KasebContract;
import mjkarbasian.moshtarimadar.Helpers.Utility;
import mjkarbasian.moshtarimadar.R;


/**
 * Created by Unique on 10/25/2016.
 */
public class ProductInsert extends Fragment {
    private static final String LOG_TAG = CostInsert.class.getSimpleName();
    View rootView;
    ContentValues productValues = new ContentValues();
    ContentValues productHistoryValues = new ContentValues();
    EditText productName;
    EditText productCode;
    EditText unit;
    EditText productDescription;
    EditText buyPrice;
    EditText quantity;
    EditText salePrice;
    EditText buyDate;
    EditText discountAmount;
    EditText discountPercent;
    private Uri insertUri;

    public ProductInsert() {
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_product_insert, container, false);

        productName = (EditText) rootView.findViewById(R.id.input_product_name);
        productCode = (EditText) rootView.findViewById(R.id.input_product_code);
        productCode.setText(Utility.preInsertProductCode(getActivity()));
        unit = (EditText) rootView.findViewById(R.id.input_product_unit);
        productDescription = (EditText) rootView.findViewById(R.id.input_product_description);
        buyPrice = (EditText) rootView.findViewById(R.id.input_buy_price);
        quantity = (EditText) rootView.findViewById(R.id.input_quantity);
        salePrice = (EditText) rootView.findViewById(R.id.input_sale_price);
        buyDate = (EditText) rootView.findViewById(R.id.input_buy_date);
        discountAmount = (EditText) rootView.findViewById(R.id.input_discount_amount);
        discountPercent = (EditText) rootView.findViewById(R.id.input_discount_percent);

        buyDate.setText(Utility.preInsertDate(getActivity()));

        discountAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    Float mDiscountAmount = Float.valueOf(discountAmount.getText().toString());
                    Float mBuyPrice = Float.valueOf(buyPrice.getText().toString());

                    if (mDiscountAmount > mBuyPrice)
                        discountAmount.setText(buyPrice.getText().toString());
                } catch (Exception e) {
                    salePrice.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    Float mDiscountAmount = Float.valueOf(discountAmount.getText().toString());
                    Float mBuyPrice = Float.valueOf(buyPrice.getText().toString());

                    salePrice.setText(String.format("%.0f", Float.valueOf(mBuyPrice - mDiscountAmount)));
                } catch (Exception e) {
                    salePrice.setText("");
                }
            }
        });

        discountPercent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    Float mDiscountPercent = Float.valueOf(discountPercent.getText().toString());

                    if (mDiscountPercent > 100)
                        discountPercent.setText("100");
                } catch (Exception e) {
                    salePrice.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    Float mDiscountPercent = Float.valueOf(discountPercent.getText().toString());
                    Float mBuyPrice = Float.valueOf(buyPrice.getText().toString());

                    salePrice.setText(String.format("%.0f", Float.valueOf((100 - mDiscountPercent) * mBuyPrice / 100)));
                } catch (Exception e) {
                    salePrice.setText("");
                }
            }
        });

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.removeItem(R.id.sort_button);
        menu.removeItem(R.id.search_button);
        inflater.inflate(R.menu.fragments_for_insert, menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_inputs: {
                if (CheckForValidity(
                        productName.getText().toString(),
                        buyPrice.getText().toString(),
                        quantity.getText().toString(),
                        salePrice.getText().toString(),
                        buyDate.getText().toString())) {

                    productValues.put(KasebContract.Products.COLUMN_PRODUCT_NAME, productName.getText().toString());
                    productValues.put(KasebContract.Products.COLUMN_PRODUCT_CODE, productCode.getText().toString());
                    productValues.put(KasebContract.Products.COLUMN_UNIT, unit.getText().toString());
                    productValues.put(KasebContract.Products.COLUMN_DESCRIPTION, productDescription.getText().toString());
                    insertUri = getActivity().getContentResolver().insert(
                            KasebContract.Products.CONTENT_URI,
                            productValues
                    );

                    productHistoryValues.put(KasebContract.ProductHistory.COLUMN_COST, buyPrice.getText().toString());
                    productHistoryValues.put(KasebContract.ProductHistory.COLUMN_QUANTITY, quantity.getText().toString());
                    productHistoryValues.put(KasebContract.ProductHistory.COLUMN_SALE_PRICE, salePrice.getText().toString());
                    productHistoryValues.put(KasebContract.ProductHistory.COLUMN_DATE, buyDate.getText().toString());
                    productHistoryValues.put(KasebContract.ProductHistory.COLUMN_PRODUCT_ID, insertUri.getLastPathSegment());

                    insertUri = getActivity().getContentResolver().insert(
                            KasebContract.ProductHistory.CONTENT_URI,
                            productHistoryValues
                    );

                    //region disabling edit
                    productName.setEnabled(false);
                    productCode.setEnabled(false);
                    unit.setEnabled(false);
                    productDescription.setEnabled(false);
                    buyPrice.setEnabled(false);
                    quantity.setEnabled(false);
                    salePrice.setEnabled(false);
                    buyDate.setEnabled(false);

                    //just a message to show everything are under control
                    Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.msg_insert_succeed),
                            Toast.LENGTH_LONG).show();

                    getFragmentManager().popBackStackImmediate();
                }

                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    // this method check the validation and correct entries. its check fill first and then check the validation rules.
    private boolean CheckForValidity(String productName, String buyPrice, String quantity, String salePrice, String buyDate) {
        if (productName.equals("") || productName.equals(null)) {
            Toast.makeText(getActivity(), R.string.validity_error_product_name, Toast.LENGTH_SHORT).show();
            return false;
        } else {
            Cursor mCursor = getContext().getContentResolver().query(
                    KasebContract.Products.CONTENT_URI,
                    new String[]{KasebContract.Products._ID},
                    KasebContract.Products.COLUMN_PRODUCT_NAME + " = ? ",
                    new String[]{productName},
                    null);

            if (mCursor != null) {
                if (mCursor.moveToFirst())
                    if (mCursor.getCount() > 0) {
                        Toast.makeText(getActivity(), R.string.validity_error_product_name_duplicate, Toast.LENGTH_SHORT).show();
                        return false;
                    }
            }
        }

        if (buyPrice.equals("") || buyPrice.equals(null)) {
            Toast.makeText(getActivity(), R.string.validity_error_product_buy_price, Toast.LENGTH_SHORT).show();
            return false;
        } else if (quantity.equals("") || quantity.equals(null)) {
            Toast.makeText(getActivity(), R.string.validity_error_product_qty, Toast.LENGTH_SHORT).show();
            return false;
        } else if (salePrice.equals("") || salePrice.equals(null)) {
            Toast.makeText(getActivity(), R.string.validity_error_product_sale_price, Toast.LENGTH_SHORT).show();
            return false;
        } else if (buyDate.equals("") || buyDate.equals(null)) {
            Toast.makeText(getActivity(), R.string.validity_error_product_buy_date, Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    // this method back to the activity view. this must be a utility method.
    private void backToLastPage() {
        Utility.clearForm((ViewGroup) rootView);
        getFragmentManager().popBackStackImmediate();
    }
}