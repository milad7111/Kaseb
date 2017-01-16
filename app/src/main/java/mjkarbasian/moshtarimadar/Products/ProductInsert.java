package mjkarbasian.moshtarimadar.Products;

import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
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

    //region declare values
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
    TextInputLayout productNameTextInputLayout;
    TextInputLayout salePriceTextInputLayout;
    TextInputLayout quantityTextInputLayout;
    TextInputLayout buyPriceTextInputLayout;
    TextInputLayout buyDateTextInputLayout;
    TextInputLayout productCodeTextInputLayout;
    TextInputLayout productUnitTextInputLayout;
    TextInputLayout productDDescriptionTextInputLayout;
    private Uri insertUri;
    //endregion declare values

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

        productNameTextInputLayout = (TextInputLayout) rootView.findViewById(R.id.text_input_layout_input_product_name);
        salePriceTextInputLayout = (TextInputLayout) rootView.findViewById(R.id.text_input_layout_input_sale_price);
        quantityTextInputLayout = (TextInputLayout) rootView.findViewById(R.id.text_input_layout_input_quantity);
        buyPriceTextInputLayout = (TextInputLayout) rootView.findViewById(R.id.text_input_layout_input_buy_price);
        buyDateTextInputLayout = (TextInputLayout) rootView.findViewById(R.id.text_input_layout_input_buy_date);
        productCodeTextInputLayout = (TextInputLayout) rootView.findViewById(R.id.text_input_layout_input_product_code);
        productUnitTextInputLayout = (TextInputLayout) rootView.findViewById(R.id.text_input_layout_input_product_unit);
        productDDescriptionTextInputLayout = (TextInputLayout) rootView.findViewById(R.id.text_input_layout_input_product_description);

        buyDate.setText(Utility.preInsertDate(getActivity()));

        setHelperText();

        discountAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    Float mDiscountAmount = Float.valueOf(discountAmount.getText().toString());
                    Float mSalePrice = Float.valueOf(salePrice.getText().toString());

                    if (mDiscountAmount > mSalePrice) {
                        discountAmount.setText(salePrice.getText().toString());
                        Utility.setErrorForEditText(getActivity(), discountAmount,
                                getResources().getString(R.string.not_more_than_sale_price));
                        discountAmount.setSelectAllOnFocus(true);
                        discountAmount.selectAll();
                    }
                } catch (Exception e) {
                    if (salePrice.getText().toString().equals(null) || salePrice.getText().toString().equals(""))
                        Utility.setErrorForEditText(getActivity(), salePrice, "");
                    else
                        Utility.setErrorForEditText(getActivity(), discountAmount, "");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    Float mDiscountAmount = Float.valueOf(discountAmount.getText().toString());
                    Float mSalePrice = Float.valueOf(salePrice.getText().toString());

                    buyPrice.setText(String.format("%.2f", Float.valueOf(mSalePrice - mDiscountAmount)));
                } catch (Exception e) {
                    if (salePrice.getText().toString().equals(null) || salePrice.getText().toString().equals(""))
                        Utility.setErrorForEditText(getActivity(), salePrice, "");
                    else
                        Utility.setErrorForEditText(getActivity(), discountAmount, "");
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
                    Float mSalePrice = Float.valueOf(salePrice.getText().toString());

                    if (mDiscountPercent > 100) {
                        discountAmount.setText(String.format("%.2f", Float.valueOf(mDiscountPercent * mSalePrice / 100)));

                        discountPercent.setText(Utility.doubleFormatter(100));
                        Utility.setErrorForEditText(getActivity(), discountPercent,
                                getResources().getString(R.string.not_more_hundred));
                        discountPercent.setSelectAllOnFocus(true);
                        discountPercent.selectAll();
                    }
                } catch (Exception e) {
                    if (salePrice.getText().toString().equals(null) || salePrice.getText().toString().equals(""))
                        Utility.setErrorForEditText(getActivity(), salePrice, "");
                    else
                        Utility.setErrorForEditText(getActivity(), discountPercent, "");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    Float mDiscountPercent = Float.valueOf(discountPercent.getText().toString());
                    Float mSalePrice = Float.valueOf(salePrice.getText().toString());

                    buyPrice.setText(String.format("%.2f",
                            Float.valueOf((float) ((Float.valueOf(100 - mDiscountPercent) / 100.0) * mSalePrice))));
                    discountAmount.setText(String.format("%.2f", Float.valueOf(mDiscountPercent * mSalePrice / 100)));
                } catch (Exception e) {
                    if (salePrice.getText().toString().equals(null) || salePrice.getText().toString().equals(""))
                        Utility.setErrorForEditText(getActivity(), salePrice, "");
                    else
                        Utility.setErrorForEditText(getActivity(), discountPercent, "");
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
                if (Utility.checkForValidityForEditTextNullOrEmptyAndItterative(
                        getActivity(), productName, productNameTextInputLayout, KasebContract.Products.CONTENT_URI,
                        KasebContract.Products.COLUMN_PRODUCT_NAME + " = ? ",
                        KasebContract.Products._ID, new String[]{productName.getText().toString()})
                        && checkValidityWithChangeColorOfHelperText()) {

                    productValues.put(KasebContract.Products.COLUMN_PRODUCT_NAME, productName.getText().toString());
                    productValues.put(KasebContract.Products.COLUMN_PRODUCT_CODE, productCode.getText().toString());
                    productValues.put(KasebContract.Products.COLUMN_UNIT, unit.getText().toString());
                    productValues.put(KasebContract.Products.COLUMN_DESCRIPTION, productDescription.getText().toString());
                    insertUri = getActivity().getContentResolver().insert(
                            KasebContract.Products.CONTENT_URI,
                            productValues
                    );

                    productHistoryValues.put(KasebContract.ProductHistory.COLUMN_COST,
                            Utility.convertFarsiNumbersToDecimal(buyPrice.getText().toString()));
                    productHistoryValues.put(KasebContract.ProductHistory.COLUMN_QUANTITY,
                            Utility.convertFarsiNumbersToDecimal(quantity.getText().toString()));
                    productHistoryValues.put(KasebContract.ProductHistory.COLUMN_SALE_PRICE,
                            Utility.convertFarsiNumbersToDecimal(salePrice.getText().toString()));
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

                    backToLastPage();
                }

                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    // this method back to the activity view. this must be a utility method.
    private void backToLastPage() {
        Utility.clearForm((ViewGroup) rootView);
        getFragmentManager().popBackStackImmediate();
    }

    private void setHelperText() {

        productNameTextInputLayout.setError(getResources().getString(R.string.choose_appropriate_data)
                + getResources().getString(R.string.not_itterative));

        salePriceTextInputLayout.setError(getResources().getString(R.string.choose_appropriate_data));
        quantityTextInputLayout.setError(getResources().getString(R.string.choose_appropriate_data));
        buyPriceTextInputLayout.setError(getResources().getString(R.string.choose_appropriate_data));
        productCodeTextInputLayout.setError(getResources().getString(R.string.choose_appropriate_data));
        productUnitTextInputLayout.setError(getResources().getString(R.string.choose_appropriate_data));
        productDDescriptionTextInputLayout.setError(getResources().getString(R.string.choose_appropriate_data));

        buyDateTextInputLayout.setError(getResources().getString(R.string.choose_appropriate_data)
                + getResources().getString(R.string.date_format_error));
    }

    // this method check the validation and correct entries. its check fill first and then check the validation rules.
    private boolean checkValidityWithChangeColorOfHelperText() {

        if (!Utility.checkForValidityForEditTextNullOrEmpty(getActivity(), productCode)) {
            Utility.changeColorOfHelperText(getActivity(), productCodeTextInputLayout, R.color.colorRed);
            productCode.requestFocus();
            return false;
        } else
            Utility.changeColorOfHelperText(getActivity(), productCodeTextInputLayout, R.color.colorPrimaryLight);

        if (!Utility.checkForValidityForEditTextNullOrEmpty(getActivity(), salePrice)) {
            Utility.changeColorOfHelperText(getActivity(), salePriceTextInputLayout, R.color.colorRed);
            salePrice.requestFocus();
            return false;
        } else
            Utility.changeColorOfHelperText(getActivity(), salePriceTextInputLayout, R.color.colorPrimaryLight);

        if (!Utility.checkForValidityForEditTextNullOrEmpty(getActivity(), quantity)) {
            Utility.changeColorOfHelperText(getActivity(), quantityTextInputLayout, R.color.colorRed);
            quantity.requestFocus();
            return false;
        } else
            Utility.changeColorOfHelperText(getActivity(), quantityTextInputLayout, R.color.colorPrimaryLight);

        if (!Utility.checkForValidityForEditTextNullOrEmpty(getActivity(), buyPrice)) {
            Utility.changeColorOfHelperText(getActivity(), buyPriceTextInputLayout, R.color.colorRed);
            buyPrice.requestFocus();
            return false;
        } else
            Utility.changeColorOfHelperText(getActivity(), buyPriceTextInputLayout, R.color.colorPrimaryLight);

        if (!Utility.checkForValidityForEditTextDate(getActivity(), buyDate)) {
            Utility.changeColorOfHelperText(getActivity(), buyDateTextInputLayout, R.color.colorRed);
            buyDate.requestFocus();
            return false;
        } else
            Utility.changeColorOfHelperText(getActivity(), buyDateTextInputLayout, R.color.colorPrimaryLight);

        return true;
    }
}