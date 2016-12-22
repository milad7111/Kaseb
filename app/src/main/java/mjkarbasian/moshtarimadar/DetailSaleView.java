package mjkarbasian.moshtarimadar;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import mjkarbasian.moshtarimadar.Data.KasebContract;
import mjkarbasian.moshtarimadar.Data.KasebDbHelper;
import mjkarbasian.moshtarimadar.Data.KasebProvider;
import mjkarbasian.moshtarimadar.adapters.CostSaleProductAdapter;
import mjkarbasian.moshtarimadar.adapters.CustomerAdapter;
import mjkarbasian.moshtarimadar.adapters.PaymentAdapter;
import mjkarbasian.moshtarimadar.adapters.TaxAdapter;
import mjkarbasian.moshtarimadar.adapters.TypesSettingAdapter;
import mjkarbasian.moshtarimadar.helper.PaymentListModel;
import mjkarbasian.moshtarimadar.helper.TaxListModel;
import mjkarbasian.moshtarimadar.helper.Utility;

public class DetailSaleView extends AppCompatActivity {

    //region declare Values
    private static Context mContext;
    KasebDbHelper mOpenHelper;
    SQLiteDatabase mDb;

    long customerId;
    long itemNumber;

    ListView taxTypeRowListView;
    ListView getProductListView;

    EditText saleCode;
    EditText saleDate;

    ContentValues saleValues = new ContentValues();
    ContentValues detailSaleValues = new ContentValues();
    ContentValues[] itemsValuesArray;
    ContentValues itemsValues;
    ContentValues[] paymentValuesArray;
    ContentValues paymentValues;
    ContentValues[] taxValuesArray;
    ContentValues taxValues;
    FloatingActionButton fab;
    AlertDialog.Builder builder;
    ListView modeList;
    CostSaleProductAdapter mAdapter = null;
    CustomerAdapter mCAdapter = null;
    Dialog dialog;
    Button dialogButton;
    Spinner paymentMethod;
    Spinner taxTypes;
    String[] mProjection;
    String[] mProjectionProductHistory;
    TextView nameCustomer;
    TextView familyCustomer;
    CostSaleProductAdapter mProductAdapter;
    ListView mListView;
    String[] mSelection;
    String mWhereStatement;
    int productNumber = 0;
    int mNumberOfNotChooseProduct = 0;
    List<String> mChosenProductList = new ArrayList<String>();
    List<String> mNotChooseProductList = new ArrayList<String>();
    EditText paymentAmount;
    EditText paymentDueDate;
    EditText taxAmount;
    EditText taxPercent;
    String paymentMethodsId;
    String taxTypeeId;
    String paymentMethodType;
    String taxType;

    List<Long> mNumberOfChosenProduct = new ArrayList<Long>();
    List<Long> mAmountOfChosenProduct = new ArrayList<Long>();

    List<Long> paymentAmountList = new ArrayList<Long>();
    List<String> paymentDueDateList = new ArrayList<String>();
    List<String> paymentMethodTypeList = new ArrayList<String>();
    List<String> paymentMethodIdTypeList = new ArrayList<String>();

    List<Long> taxAmountList = new ArrayList<Long>();
    List<String> taxPercentList = new ArrayList<String>();
    List<String> taxTypeList = new ArrayList<String>();
    List<String> taxTypesIdsList = new ArrayList<String>();

    ListView paymentMethodRowListView;

    Long sTotalAmount = 0l;
    Long sTotalTax = 0l;
    Long sTotalDiscount = 0l;
    Long sFinalAmount = 0l;
    Long sPaidAmount = 0l;
    Long sBalanceAmount = 0l;

    String cost = null;

    Cursor mCursor1;
    Cursor mCursor2;

    TextView totalAmountSummary;
    TextView taxSummary;
    TextView discountSummary;
    TextView finalAmountSummary;
    TextView paidSummary;
    TextView balanceSummary;

    TypesSettingAdapter cursorAdapter = null;

    long whichSaleId;
    long whichDetailSaleId;
    String[] mProjectionInitialize;
    String[] mSelectionInitialize;
    String mWhereStatementInitialize;
    Cursor mCursorInitialize;

    ArrayList<PaymentListModel> paymentArrayValues;
    ArrayList<TaxListModel> taxTypesArrayValues;
    //endregion declare Values

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_sale_insert);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //region Initialize Some Views & Values
        mContext = this;
        mOpenHelper = KasebProvider.mOpenHelper;
        mDb = mOpenHelper.getWritableDatabase();

        totalAmountSummary = (TextView) findViewById(R.id.card_detail_sale_summary_total_amount);
        taxSummary = (TextView) findViewById(R.id.card_detail_sale_summary_tax);
        discountSummary = (TextView) findViewById(R.id.card_detail_sale_summary_discount);
        finalAmountSummary = (TextView) findViewById(R.id.card_detail_sale_summary_final_amount);
        paidSummary = (TextView) findViewById(R.id.card_detail_sale_summary_payed);
        balanceSummary = (TextView) findViewById(R.id.card_detail_sale_summary_balance);
        nameCustomer = (TextView) findViewById(R.id.detail_sales_info_customer_name);
        familyCustomer = (TextView) findViewById(R.id.detail_sales_info_customer_family);

        fab = (FloatingActionButton) findViewById(R.id.fab_detail_sale_insert);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        paymentMethodRowListView = (ListView) findViewById(R.id.listview_sale_payments);
        taxTypeRowListView = (ListView) findViewById(R.id.listview_sale_tax_discount);
        getProductListView = (ListView) findViewById(R.id.listview_sale_items);

        saleCode = (EditText) findViewById(R.id.detail_sales_info_sale_code);
        saleDate = (EditText) findViewById(R.id.detail_sales_info_sale_date);
        //endregion

        //region Disable Views
        saleCode.setEnabled(false);
        saleDate.setEnabled(false);
        fab.setEnabled(false);
        //endregion Disable Views

        //region Get SaleId
        whichSaleId = Long.parseLong(getIntent().getExtras().get("saleId").toString());
        saleCode.setText(getIntent().getExtras().get("saleCode").toString());
        customerId = getIntent().getExtras().getLong("customerId");
        //endregion Get SaleId

        //region Get DetailSaleId
        mProjectionInitialize = new String[]{
                KasebContract.DetailSale._ID,
                KasebContract.DetailSale.COLUMN_DATE};

        mCursorInitialize = getContentResolver().query(
                KasebContract.DetailSale.saleDetailSale(whichSaleId),
                mProjectionInitialize,
                null,
                null,
                null);

        if (mCursorInitialize != null) {
            if (mCursorInitialize.moveToFirst()) {
                whichDetailSaleId = mCursorInitialize.getLong(mCursorInitialize.getColumnIndex(KasebContract.DetailSale._ID));
                saleDate.setText(mCursorInitialize.getString(mCursorInitialize.getColumnIndex(KasebContract.DetailSale.COLUMN_DATE)));
            }
        }
        //endregion Get DetailSaleId

        //region Get ProductIds
        mProjectionInitialize = new String[]{
                KasebContract.DetailSaleProducts._ID,
                KasebContract.DetailSaleProducts.COLUMN_PRODUCT_ID,
                KasebContract.DetailSaleProducts.COLUMN_QUANTITY,
                KasebContract.DetailSaleProducts.COLUMN_AMOUNT};

        mCursorInitialize = getContentResolver().query(
                KasebContract.DetailSaleProducts.productsOfDetailSale(whichDetailSaleId),
                mProjectionInitialize,
                null,
                null,
                null);

        if (mCursorInitialize != null) {
            if (mCursorInitialize.moveToFirst()) {
                for (int i = 0; i < mCursorInitialize.getCount(); i++) {
                    Long amount = mCursorInitialize.getLong(
                            mCursorInitialize.getColumnIndex(
                                    KasebContract.DetailSaleProducts.COLUMN_AMOUNT));

                    Long num = mCursorInitialize.getLong(
                            mCursorInitialize.getColumnIndex(
                                    KasebContract.DetailSaleProducts.COLUMN_QUANTITY));

                    mChosenProductList.add(
                            mCursorInitialize.getString(
                                    mCursorInitialize.getColumnIndex(
                                            KasebContract.DetailSaleProducts.COLUMN_PRODUCT_ID)));

                    mAmountOfChosenProduct.add(amount);
                    mNumberOfChosenProduct.add(num);

                    sTotalAmount += amount * num;
                    mCursorInitialize.moveToNext();
                }
            }
        }
        //endregion Get ProductIds

        //region Get Products
        mProjectionInitialize = new String[]{
                KasebContract.Products._ID,
                KasebContract.Products.COLUMN_PRODUCT_NAME,
                KasebContract.Products.COLUMN_PRODUCT_CODE};

        mWhereStatementInitialize = KasebContract.Products._ID + " IN (" +
                Utility.makePlaceholders((mChosenProductList.size() > 0 ? mChosenProductList.size() : 1)) + ")";

        mSelectionInitialize = new String[(mChosenProductList.size() > 0 ? mChosenProductList.size() : 1)];

        for (int i = 0; i < mChosenProductList.size(); i++) {
            mSelectionInitialize[i] = mChosenProductList.get(i);
        }

        if (mSelectionInitialize[0] == null)
            mSelectionInitialize[0] = "-1";

        mProductAdapter = new CostSaleProductAdapter(
                DetailSaleView.this,
                getContentResolver().query(
                        KasebContract.Products.CONTENT_URI,
                        mProjectionInitialize,
                        mWhereStatementInitialize,
                        mSelectionInitialize,
                        null),
                0,
                "product");

        getProductListView.setAdapter(mProductAdapter);

        mProductAdapter = new CostSaleProductAdapter(
                DetailSaleView.this,
                getContentResolver().query(
                        KasebContract.Products.CONTENT_URI,
                        mProjectionInitialize,
                        null,
                        null,
                        null),
                0,
                "product");

        Cursor mCursor1 = mProductAdapter.getCursor();
        if (mCursor1.moveToFirst())
            for (int i = 0; i < mCursor1.getCount(); i++) {
                mNotChooseProductList.add(mCursor1.getString(mCursor1.getColumnIndex(KasebContract.Products._ID)));
                mCursor1.moveToNext();
            }
        mCursor1.close();

        for (int i = 0; i < mChosenProductList.size(); i++) {
            mNotChooseProductList.remove(mChosenProductList.get(i));
        }
        //endregion Get Products

        //region Get Customer Name & Family
        mProjectionInitialize = new String[]{
                KasebContract.Customers.COLUMN_FIRST_NAME,
                KasebContract.Customers.COLUMN_LAST_NAME};

        mCursorInitialize = getContentResolver().query(
                KasebContract.Customers.buildCustomerUri(customerId),
                mProjectionInitialize,
                null,
                null,
                null);

        if (mCursorInitialize != null)
            if (mCursorInitialize.moveToFirst()) {
                nameCustomer.setText(
                        mCursorInitialize.getString(
                                mCursorInitialize.getColumnIndex(
                                        KasebContract.Customers.COLUMN_FIRST_NAME)));
                familyCustomer.setText(
                        mCursorInitialize.getString(
                                mCursorInitialize.getColumnIndex(
                                        KasebContract.Customers.COLUMN_LAST_NAME)));
            }

        //endregion Get Customer Name & Family

        //region Get Payments
        mProjectionInitialize = new String[]{
                KasebContract.DetailSalePayments._ID,
                KasebContract.DetailSalePayments.COLUMN_AMOUNT,
                KasebContract.DetailSalePayments.COLUMN_DUE_DATE,
                KasebContract.DetailSalePayments.COLUMN_PAYMENT_METHOD_ID};

        mCursorInitialize = getContentResolver().query(
                KasebContract.DetailSalePayments.paymentOfDetailSale(whichDetailSaleId),
                mProjectionInitialize,
                null,
                null,
                null);

        paymentArrayValues = new ArrayList<PaymentListModel>();

        mProjectionInitialize = new String[]{
                KasebContract.PaymentMethods._ID,
                KasebContract.PaymentMethods.COLUMN_PAYMENT_METHOD_POINTER};

        mCursorInitialize.moveToFirst();
        for (int i = 0; i < mCursorInitialize.getCount(); i++) {
            PaymentListModel paymentMethodRow = new PaymentListModel();

            paymentAmountList.add(
                    mCursorInitialize.getLong(
                            mCursorInitialize.getColumnIndex(
                                    KasebContract.DetailSalePayments.COLUMN_AMOUNT)));

            paymentMethodRow.setPaymentAmount(
                    mCursorInitialize.getLong(
                            mCursorInitialize.getColumnIndex(
                                    KasebContract.DetailSalePayments.COLUMN_AMOUNT)));

            paymentDueDateList.add(
                    mCursorInitialize.getString(
                            mCursorInitialize.getColumnIndex(
                                    KasebContract.DetailSalePayments.COLUMN_DUE_DATE)));

            paymentMethodRow.setPaymentDueDate(
                    mCursorInitialize.getString(
                            mCursorInitialize.getColumnIndex(
                                    KasebContract.DetailSalePayments.COLUMN_DUE_DATE)));

            paymentMethodIdTypeList.add(mCursorInitialize.getString(
                    mCursorInitialize.getColumnIndex(
                            KasebContract.DetailSalePayments.COLUMN_PAYMENT_METHOD_ID)));

            Cursor mCursor2 = getContentResolver().query(
                    KasebContract.PaymentMethods.buildPaymentMethodsUri(
                            mCursorInitialize.getLong(
                                    mCursorInitialize.getColumnIndex(
                                            KasebContract.DetailSalePayments.COLUMN_PAYMENT_METHOD_ID))
                    ),
                    mProjectionInitialize,
                    null,
                    null,
                    null);

            if (mCursor2 != null)
                if (mCursor2.moveToFirst()) {
                    paymentMethodTypeList.add(mCursor2.getString(
                            mCursor2.getColumnIndex(
                                    KasebContract.PaymentMethods.COLUMN_PAYMENT_METHOD_POINTER)));

                    paymentMethodRow.setPaymentMethod(
                            mCursor2.getString(
                                    mCursor2.getColumnIndex(
                                            KasebContract.PaymentMethods.COLUMN_PAYMENT_METHOD_POINTER)));
                }

            mCursor2.close();

            paymentArrayValues.add(paymentMethodRow);
            mCursorInitialize.moveToNext();
        }

        PaymentAdapter paymentAdapter = new PaymentAdapter(getBaseContext(), paymentArrayValues);
        paymentMethodRowListView.setAdapter(paymentAdapter);
        //endregion Get Payments

        //region Get Taxes
        mProjectionInitialize = new String[]{
                KasebContract.DetailSaleTaxes._ID,
                KasebContract.DetailSaleTaxes.COLUMN_AMOUNT,
                KasebContract.DetailSaleTaxes.COLUMN_TAX_TYPE_ID};

        mCursorInitialize = getContentResolver().query(
                KasebContract.DetailSaleTaxes.taxOfDetailSale(whichDetailSaleId),
                mProjectionInitialize,
                null,
                null,
                null);

        taxTypesArrayValues = new ArrayList<TaxListModel>();

        mProjectionInitialize = new String[]{
                KasebContract.TaxTypes._ID,
                KasebContract.TaxTypes.COLUMN_TAX_TYPE_POINTER};

        mCursorInitialize.moveToFirst();
        for (int i = 0; i < mCursorInitialize.getCount(); i++) {
            TaxListModel taxDiscountRow = new TaxListModel();
            Long taxAmount;

            taxAmount = mCursorInitialize.getLong(
                    mCursorInitialize.getColumnIndex(
                            KasebContract.DetailSaleTaxes.COLUMN_AMOUNT));

            taxAmountList.add(taxAmount);
            taxDiscountRow.setTaxAmount(taxAmount);

            taxPercentList.add("20");
            taxDiscountRow.setTaxPercent("20");

            taxTypesIdsList.add(mCursorInitialize.getString(
                    mCursorInitialize.getColumnIndex(
                            KasebContract.DetailSaleTaxes.COLUMN_TAX_TYPE_ID)));

            Cursor mCursor3 = getContentResolver().query(
                    KasebContract.TaxTypes.buildTaxTypesUri(
                            mCursorInitialize.getLong(
                                    mCursorInitialize.getColumnIndex(
                                            KasebContract.DetailSaleTaxes.COLUMN_TAX_TYPE_ID))
                    ),
                    mProjectionInitialize,
                    null,
                    null,
                    null);

            if (mCursor3 != null)
                if (mCursor3.moveToFirst()) {
                    taxType = mCursor3.getString(
                            mCursor3.getColumnIndex(
                                    KasebContract.TaxTypes.COLUMN_TAX_TYPE_POINTER));

                    taxTypeList.add(taxType);
                    taxDiscountRow.setTaxType(taxType);
                }

            if (taxType.equals("Discount"))
                sTotalDiscount += taxAmount;
            else
                sTotalTax += taxAmount;

            mCursor3.close();

            taxTypesArrayValues.add(taxDiscountRow);
            mCursorInitialize.moveToNext();
        }

        TaxAdapter taxAdapter = new TaxAdapter(getBaseContext(), taxTypesArrayValues);
        taxTypeRowListView.setAdapter(taxAdapter);
        //endregion Get Taxes

        //region Final Calculate
        for (int i = 0; i < paymentAmountList.size(); i++) {
            sPaidAmount += paymentAmountList.get(i);
        }

        sFinalAmount = sTotalAmount + sTotalTax - sTotalDiscount;
        sBalanceAmount = sFinalAmount - sPaidAmount;

        totalAmountSummary.setText(
                Utility.formatPurchase(mContext, Utility.DecimalSeperation(mContext, sTotalAmount)));
        taxSummary.setText(
                Utility.formatPurchase(mContext, Utility.DecimalSeperation(mContext, sTotalTax)));
        discountSummary.setText(
                Utility.formatPurchase(mContext, Utility.DecimalSeperation(mContext, sTotalDiscount)));
        finalAmountSummary.setText(
                Utility.formatPurchase(mContext, Utility.DecimalSeperation(mContext, sFinalAmount)));
        paidSummary.setText(
                Utility.formatPurchase(mContext, Utility.DecimalSeperation(mContext, sPaidAmount)));
        balanceSummary.setText(
                Utility.formatPurchase(mContext, Utility.DecimalSeperation(mContext, sBalanceAmount)));
        //endregion Final Calculate
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_detail_sale_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case R.id.print_button:
                Toast.makeText(this,
                        getApplicationContext().getResources().getString(R.string.print_your_factor), Toast.LENGTH_LONG).show();
                break;
            case R.id.save:
                //region CheckValidity
                //endregion CheckValidity

                mDb.beginTransaction();

                mSelectionInitialize = new String[]{String.valueOf(whichDetailSaleId)};

                //region Delete Old Rows Of DetailSaleProducts
                mWhereStatementInitialize = KasebContract.DetailSaleProducts.COLUMN_DETAIL_SALE_ID + " = ? ";

                getContentResolver().delete(
                        KasebContract.DetailSaleProducts.CONTENT_URI,
                        mWhereStatementInitialize,
                        mSelectionInitialize
                );
                //endregion Delete Old Rows Of DetailSaleProducts

                //region Delete Old Rows Of DetailSalePayments
                mWhereStatementInitialize = KasebContract.DetailSalePayments.COLUMN_DETAIL_SALE_ID + " = ? ";

                getContentResolver().delete(
                        KasebContract.DetailSalePayments.CONTENT_URI,
                        mWhereStatementInitialize,
                        mSelectionInitialize
                );
                //endregion Delete Old Rows Of DetailSalePayments

                //region Delete Old Rows Of DetailSaleTaxes
                mWhereStatementInitialize = KasebContract.DetailSaleTaxes.COLUMN_DETAIL_SALE_ID + " = ? ";

                getContentResolver().delete(
                        KasebContract.DetailSaleTaxes.CONTENT_URI,
                        mWhereStatementInitialize,
                        mSelectionInitialize
                );
                //endregion Delete Old Rows Of DetailSaleTaxes

                //region Update Sale
                mWhereStatementInitialize = KasebContract.Sales._ID + " = ? ";
                mSelectionInitialize = new String[]{String.valueOf(whichSaleId)};

                saleValues.put(KasebContract.Sales.COLUMN_CUSTOMER_ID, customerId);
                saleValues.put(KasebContract.Sales.COLUMN_IS_DELETED, 0);
                saleValues.put(KasebContract.Sales.COLUMN_SALE_CODE, saleCode.getText().toString());

                getContentResolver().update(
                        KasebContract.Sales.CONTENT_URI,
                        saleValues,
                        mWhereStatementInitialize,
                        mSelectionInitialize
                );
                //endregion Insert Sale

                //region Update DetailSale
                mWhereStatementInitialize = KasebContract.DetailSale._ID + " = ? ";
                mSelectionInitialize = new String[]{String.valueOf(whichDetailSaleId)};

                itemNumber = getProductListView.getAdapter().getCount();

                detailSaleValues.put(KasebContract.DetailSale.COLUMN_DATE, saleDate.getText().toString());
                detailSaleValues.put(KasebContract.DetailSale.COLUMN_IS_BALANCED, 0);
                detailSaleValues.put(KasebContract.DetailSale.COLUMN_ITEMS_NUMBER, itemNumber);
                detailSaleValues.put(KasebContract.DetailSale.COLUMN_SALE_ID, whichSaleId);
                detailSaleValues.put(KasebContract.DetailSale.COLUMN_SUB_TOTAL, sTotalAmount);
                detailSaleValues.put(KasebContract.DetailSale.COLUMN_TOTAL_DISCOUNT, sTotalDiscount);
                detailSaleValues.put(KasebContract.DetailSale.COLUMN_TOTAL_DUE, sFinalAmount);
                detailSaleValues.put(KasebContract.DetailSale.COLUMN_TOTAL_PAID, sPaidAmount);
                detailSaleValues.put(KasebContract.DetailSale.COLUMN_TOTAL_TAX, sTotalTax);

                getContentResolver().update(
                        KasebContract.DetailSale.CONTENT_URI,
                        detailSaleValues,
                        mWhereStatementInitialize,
                        mSelectionInitialize
                );
                //endregion Insert DetailSale

                //region Insert DetailSaleProducts
                int count = mChosenProductList.size();
                itemsValuesArray = new ContentValues[count];

                for (int i = 0; i < count; i++) {
                    itemsValues = new ContentValues();

                    itemsValues.put(KasebContract.DetailSaleProducts.COLUMN_AMOUNT, mAmountOfChosenProduct.get(i));
                    itemsValues.put(KasebContract.DetailSaleProducts.COLUMN_DETAIL_SALE_ID, whichDetailSaleId);
                    itemsValues.put(KasebContract.DetailSaleProducts.COLUMN_PRODUCT_ID, mChosenProductList.get(i));
                    itemsValues.put(KasebContract.DetailSaleProducts.COLUMN_QUANTITY, mNumberOfChosenProduct.get(i));

                    itemsValuesArray[i] = itemsValues;
                }

                this.getContentResolver().bulkInsert(
                        KasebContract.DetailSaleProducts.CONTENT_URI,
                        itemsValuesArray
                );
                //endregion Insert DetailSaleProducts

                //region Insert DetailSalePayments
                count = paymentMethodIdTypeList.size();
                paymentValuesArray = new ContentValues[count];

                for (int i = 0; i < count; i++) {
                    paymentValues = new ContentValues();

                    paymentValues.put(KasebContract.DetailSalePayments.COLUMN_DUE_DATE, paymentDueDateList.get(i));
                    paymentValues.put(KasebContract.DetailSalePayments.COLUMN_DETAIL_SALE_ID, whichDetailSaleId);
                    paymentValues.put(KasebContract.DetailSalePayments.COLUMN_AMOUNT, paymentAmountList.get(i));
                    paymentValues.put(KasebContract.DetailSalePayments.COLUMN_PAYMENT_METHOD_ID, paymentMethodIdTypeList.get(i));

                    paymentValuesArray[i] = paymentValues;
                }

                this.getContentResolver().bulkInsert(
                        KasebContract.DetailSalePayments.CONTENT_URI,
                        paymentValuesArray
                );
                //endregion Insert DetailSalePayments

                //region Insert DetailSaleTaxes
                count = taxTypesIdsList.size();
                taxValuesArray = new ContentValues[count];

                for (int i = 0; i < count; i++) {
                    taxValues = new ContentValues();

                    taxValues.put(KasebContract.DetailSaleTaxes.COLUMN_DETAIL_SALE_ID, whichDetailSaleId);
                    taxValues.put(KasebContract.DetailSaleTaxes.COLUMN_AMOUNT, taxAmountList.get(i));
                    taxValues.put(KasebContract.DetailSaleTaxes.COLUMN_TAX_TYPE_ID, taxTypesIdsList.get(i));

                    taxValuesArray[i] = taxValues;
                }

                this.getContentResolver().bulkInsert(
                        KasebContract.DetailSaleTaxes.CONTENT_URI,
                        taxValuesArray
                );
                //endregion Insert DetailSaleTaxes

                //just a message to show everything are under control
                Toast.makeText(this,
                        getApplicationContext().getResources().getString(R.string.msg_update_succeed), Toast.LENGTH_LONG).show();

                mDb.setTransactionSuccessful();
                mDb.endTransaction();
                finish();

                break;
            case R.id.edit:
                saleCode.setEnabled(true);
                saleDate.setEnabled(true);
                fab.setEnabled(true);

                //region ClickListener Rows Of Product ListView
                modeList = (ListView) findViewById(R.id.listview_sale_items);
                modeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        Cursor cursor = (Cursor) parent.getItemAtPosition(position);

                        if (cursor != null) {
                            String _id = cursor.getString(cursor.getColumnIndex(KasebContract.Products._ID));

                            int index = mChosenProductList.indexOf(_id);

                            mNotChooseProductList.add(_id);

                            mChosenProductList.remove(index);
                            mNumberOfChosenProduct.remove(index);
                            mAmountOfChosenProduct.remove(index);

                            int addListProductNumber = mChosenProductList.size();

                            sTotalAmount = 0l;
                            for (int i = 0; i < addListProductNumber; i++) {
                                sTotalAmount +=
                                        mNumberOfChosenProduct.get(i) *
                                                mAmountOfChosenProduct.get(i);
                            }

                            totalAmountSummary.setText(
                                    Utility.formatPurchase(
                                            mContext,
                                            Utility.DecimalSeperation(mContext, sTotalAmount)));

                            sFinalAmount = sTotalAmount + sTotalTax - sTotalDiscount;

                            finalAmountSummary.setText(
                                    Utility.formatPurchase(
                                            mContext,
                                            Utility.DecimalSeperation(mContext, sFinalAmount)));

                            sBalanceAmount = sFinalAmount - sPaidAmount;

                            balanceSummary.setText(
                                    Utility.formatPurchase(
                                            mContext,
                                            Utility.DecimalSeperation(mContext, sBalanceAmount)));

                            mSelection = new String[(addListProductNumber > 0 ? addListProductNumber : 1)];
                            for (int i = 0; i < addListProductNumber; i++) {
                                mSelection[i] = mChosenProductList.get(i);
                            }

                            if (mSelection[0] == null)
                                mSelection[0] = "-1";

                            mWhereStatement = KasebContract.Products._ID + " IN (" +
                                    Utility.makePlaceholders((addListProductNumber > 0 ? addListProductNumber : 1)) + ")";

                            modeList = (ListView) findViewById(R.id.listview_sale_items);
                            mProductAdapter = new CostSaleProductAdapter(
                                    DetailSaleView.this,
                                    getContentResolver().query(
                                            KasebContract.Products.CONTENT_URI,
                                            mProjection,
                                            mWhereStatement,
                                            mSelection,
                                            null),
                                    0,
                                    "product");
                            modeList.setAdapter(mProductAdapter);
                        }
                    }
                });
                //endregion ClickListener Rows Of Product ListView

                //region LongSelect List Of Products
                modeList.setOnItemLongClickListener(
                        new AdapterView.OnItemLongClickListener() {
                            @Override
                            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                                Cursor cursor = (Cursor) parent.getItemAtPosition(position);
                                if (cursor != null) {
                                    String _id = cursor.getString(cursor.getColumnIndex(KasebContract.Products._ID));

                                    final int index = mChosenProductList.indexOf(_id);

                                    //region Show Dialog To Edit Number Of Product
                                    final Dialog howManyOfThatForEdit = Utility.dialogBuilder(DetailSaleView.this
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

                                            mNumberOfChosenProduct.set(index, num);

                                            int addListProductNumber = mChosenProductList.size();

                                            sTotalAmount = 0l;
                                            for (int i = 0; i < addListProductNumber; i++) {
                                                sTotalAmount +=
                                                        mNumberOfChosenProduct.get(i) *
                                                                mAmountOfChosenProduct.get(i);
                                            }

                                            totalAmountSummary.setText(
                                                    Utility.formatPurchase(
                                                            mContext,
                                                            Utility.DecimalSeperation(mContext, sTotalAmount)));

                                            sFinalAmount = sTotalAmount + sTotalTax - sTotalDiscount;

                                            finalAmountSummary.setText(
                                                    Utility.formatPurchase(
                                                            mContext,
                                                            Utility.DecimalSeperation(mContext, sFinalAmount)));

                                            sBalanceAmount = sFinalAmount - sPaidAmount;

                                            balanceSummary.setText(
                                                    Utility.formatPurchase(
                                                            mContext,
                                                            Utility.DecimalSeperation(mContext, sBalanceAmount)));
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
                        }
                );
                //endregion LongSelect List Of Products

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void fab_detail_sale_insert(View v) {

        PopupMenu popup = new PopupMenu(this, v);
        popup.getMenu().add(0, 0, 0, R.string.fab_edit_product);
        popup.getMenu().add(0, 1, 0, R.string.fab_edit_payment);
        popup.getMenu().add(0, 2, 0, R.string.fab_edit_tax);
        popup.getMenu().add(0, 3, 0, R.string.fab_edit_customer);
        popup.setOnMenuItemClickListener(
                new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            //region case 0 EDIT PRODUCT
                            case 0:
                                builder = new AlertDialog.Builder(DetailSaleView.this);
                                builder.setTitle(R.string.fab_edit_product);

                                //region Set Adapter To Dialog
                                mProjection = new String[]{
                                        KasebContract.Products._ID,
                                        KasebContract.Products.COLUMN_PRODUCT_NAME,
                                        KasebContract.Products.COLUMN_PRODUCT_CODE};

                                mNumberOfNotChooseProduct = mNotChooseProductList.size();
                                mSelection = new String[(mNumberOfNotChooseProduct > 0 ? mNumberOfNotChooseProduct : 1)];
                                for (int i = 0; i < mNotChooseProductList.size(); i++) {
                                    mSelection[i] = mNotChooseProductList.get(i);
                                }

                                if (mSelection[0] == null)
                                    mSelection[0] = "-1";

                                mWhereStatement = KasebContract.Products._ID + " IN (" +
                                        Utility.makePlaceholders((mNumberOfNotChooseProduct > 0 ? mNumberOfNotChooseProduct : 1)) + ")";

                                modeList = new ListView(DetailSaleView.this);
                                mAdapter = new CostSaleProductAdapter(
                                        DetailSaleView.this,
                                        getContentResolver().query(
                                                KasebContract.Products.CONTENT_URI,
                                                mProjection,
                                                mWhereStatement,
                                                mSelection,
                                                null),
                                        0,
                                        "product");
                                modeList.setAdapter(mAdapter);
                                //endregion Set Adapter To Dialog

                                //region ClickListener ListView Dialog
                                modeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        Cursor cursor = (Cursor) parent.getItemAtPosition(position);
                                        if (cursor != null) {
                                            final String _id = cursor.getString(cursor.getColumnIndex(KasebContract.Products._ID));

                                            //region NumberOfChosenProduct
                                            mProjectionProductHistory = new String[]{
                                                    KasebContract.ProductHistory._ID,
                                                    KasebContract.ProductHistory.COLUMN_SALE_PRICE};

                                            Cursor mCursor = getContentResolver().query(
                                                    KasebContract.ProductHistory.aProductHistory(Long.parseLong(_id)),
                                                    mProjectionProductHistory,
                                                    null,
                                                    null,
                                                    null);

                                            if (mCursor != null) {
                                                if (mCursor.moveToLast()) {
                                                    cost = mCursor.getString(mCursor.getColumnIndex(KasebContract.ProductHistory.COLUMN_SALE_PRICE));
                                                }
                                            }

                                            final Dialog howManyOfThat = Utility.dialogBuilder(DetailSaleView.this
                                                    , R.layout.dialog_add_number_of_product_for_sale
                                                    , R.string.how_many);

                                            final EditText howManyEditText = (EditText) howManyOfThat
                                                    .findViewById(R.id.add_number_of_product_for_sale_number);

                                            Button saveButton = (Button) howManyOfThat
                                                    .findViewById(R.id.add_number_of_product_for_sale_save);

                                            saveButton.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    Long num = Long.parseLong(howManyEditText.getText().toString());

                                                    mNumberOfChosenProduct.add(num);
                                                    mAmountOfChosenProduct.add(Long.parseLong(cost));

                                                    sTotalAmount += Long.parseLong(cost) * num;

                                                    if (!mChosenProductList.contains(_id)) {

                                                        //region Add Product To Sale
                                                        productNumber = mChosenProductList.size() + 1;

                                                        mChosenProductList.add(_id);
                                                        mNotChooseProductList.remove(_id);

                                                        mSelection = new String[productNumber];
                                                        for (int i = 0; i < mChosenProductList.size(); i++) {
                                                            mSelection[i] = mChosenProductList.get(i);
                                                        }

                                                        mWhereStatement = KasebContract.Products._ID + " IN (" + Utility.makePlaceholders(productNumber) + ")";

                                                        modeList = (ListView) findViewById(R.id.listview_sale_items);
                                                        mProductAdapter = new CostSaleProductAdapter(
                                                                DetailSaleView.this,
                                                                getContentResolver().query(
                                                                        KasebContract.Products.CONTENT_URI,
                                                                        mProjection,
                                                                        mWhereStatement,
                                                                        mSelection,
                                                                        null),
                                                                0,
                                                                "product");
                                                        modeList.setAdapter(mProductAdapter);
                                                        //endregion Add Product To Sale

                                                        //region ClickLisener List Of Products
                                                        modeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                            @Override
                                                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                                Cursor cursor = (Cursor) parent.getItemAtPosition(position);
                                                                if (cursor != null) {
                                                                    String _id = cursor.getString(cursor.getColumnIndex(KasebContract.Products._ID));

                                                                    int index = mChosenProductList.indexOf(_id);

                                                                    mNotChooseProductList.add(_id);

                                                                    mChosenProductList.remove(index);
                                                                    mNumberOfChosenProduct.remove(index);
                                                                    mAmountOfChosenProduct.remove(index);

                                                                    int addListProductNumber = mChosenProductList.size();

                                                                    sTotalAmount = 0l;
                                                                    for (int i = 0; i < addListProductNumber; i++) {
                                                                        sTotalAmount +=
                                                                                mNumberOfChosenProduct.get(i) *
                                                                                        mAmountOfChosenProduct.get(i);
                                                                    }

                                                                    totalAmountSummary.setText(
                                                                            Utility.formatPurchase(
                                                                                    mContext,
                                                                                    Utility.DecimalSeperation(mContext, sTotalAmount)));

                                                                    sFinalAmount = sTotalAmount + sTotalTax - sTotalDiscount;

                                                                    finalAmountSummary.setText(
                                                                            Utility.formatPurchase(
                                                                                    mContext,
                                                                                    Utility.DecimalSeperation(mContext, sFinalAmount)));

                                                                    sBalanceAmount = sFinalAmount - sPaidAmount;

                                                                    balanceSummary.setText(
                                                                            Utility.formatPurchase(
                                                                                    mContext,
                                                                                    Utility.DecimalSeperation(mContext, sBalanceAmount)));

                                                                    mSelection = new String[(addListProductNumber > 0 ? addListProductNumber : 1)];
                                                                    for (int i = 0; i < addListProductNumber; i++) {
                                                                        mSelection[i] = mChosenProductList.get(i);
                                                                    }

                                                                    if (mSelection[0] == null)
                                                                        mSelection[0] = "-1";

                                                                    mWhereStatement = KasebContract.Products._ID + " IN (" +
                                                                            Utility.makePlaceholders((addListProductNumber > 0 ? addListProductNumber : 1)) + ")";

                                                                    modeList = (ListView) findViewById(R.id.listview_sale_items);
                                                                    mProductAdapter = new CostSaleProductAdapter(
                                                                            DetailSaleView.this,
                                                                            getContentResolver().query(
                                                                                    KasebContract.Products.CONTENT_URI,
                                                                                    mProjection,
                                                                                    mWhereStatement,
                                                                                    mSelection,
                                                                                    null),
                                                                            0,
                                                                            "product");
                                                                    modeList.setAdapter(mProductAdapter);
                                                                }
                                                            }
                                                        });
                                                        //endregion ClickLisener List Of Products

                                                        //region LongSelect List Of Products
                                                        modeList.setOnItemLongClickListener(
                                                                new AdapterView.OnItemLongClickListener() {
                                                                    @Override
                                                                    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                                                                        Cursor cursor = (Cursor) parent.getItemAtPosition(position);
                                                                        if (cursor != null) {
                                                                            String _id = cursor.getString(cursor.getColumnIndex(KasebContract.Products._ID));

                                                                            final int index = mChosenProductList.indexOf(_id);

                                                                            //region Show Dialog To Edit Number Of Product
                                                                            final Dialog howManyOfThatForEdit = Utility.dialogBuilder(DetailSaleView.this
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

                                                                                    mNumberOfChosenProduct.set(index, num);

                                                                                    int addListProductNumber = mChosenProductList.size();

                                                                                    sTotalAmount = 0l;
                                                                                    for (int i = 0; i < addListProductNumber; i++) {
                                                                                        sTotalAmount +=
                                                                                                mNumberOfChosenProduct.get(i) *
                                                                                                        mAmountOfChosenProduct.get(i);
                                                                                    }

                                                                                    totalAmountSummary.setText(
                                                                                            Utility.formatPurchase(
                                                                                                    mContext,
                                                                                                    Utility.DecimalSeperation(mContext, sTotalAmount)));

                                                                                    sFinalAmount = sTotalAmount + sTotalTax - sTotalDiscount;

                                                                                    finalAmountSummary.setText(
                                                                                            Utility.formatPurchase(
                                                                                                    mContext,
                                                                                                    Utility.DecimalSeperation(mContext, sFinalAmount)));

                                                                                    sBalanceAmount = sFinalAmount - sPaidAmount;

                                                                                    balanceSummary.setText(
                                                                                            Utility.formatPurchase(
                                                                                                    mContext,
                                                                                                    Utility.DecimalSeperation(mContext, sBalanceAmount)));
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
                                                                }
                                                        );
                                                        //endregion LongSelect List Of Products

                                                        howManyOfThat.dismiss();
                                                        dialog.dismiss();

                                                        totalAmountSummary.setText(
                                                                Utility.formatPurchase(
                                                                        mContext,
                                                                        Utility.DecimalSeperation(mContext, sTotalAmount)));

                                                        sFinalAmount = sTotalAmount + sTotalTax - sTotalDiscount;

                                                        finalAmountSummary.setText(
                                                                Utility.formatPurchase(
                                                                        mContext,
                                                                        Utility.DecimalSeperation(mContext, sFinalAmount)));

                                                        sBalanceAmount = sFinalAmount - sPaidAmount;

                                                        balanceSummary.setText(
                                                                Utility.formatPurchase(
                                                                        mContext,
                                                                        Utility.DecimalSeperation(mContext, sBalanceAmount)));
                                                    }
                                                }
                                            });

                                            Button cancelButton = (Button) howManyOfThat
                                                    .findViewById(R.id.add_number_of_product_for_sale_cancel);

                                            cancelButton.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    howManyOfThat.dismiss();
                                                }
                                            });

                                            howManyOfThat.show();
                                            //endregion NumberOfChosenProduct
                                        }
                                    }
                                });
                                //endregion ClickListener ListView Dialog

                                builder.setView(modeList);
                                dialog = builder.create();

                                dialog.show();
                                //endregion case 0
                                break;
                            //region case 1 EDIT PAYMENT
                            case 1:
                                dialog = Utility.dialogBuilder(DetailSaleView.this
                                        , R.layout.dialog_add_payment_for_sale
                                        , R.string.fab_edit_payment);

                                paymentAmount = (EditText) dialog.findViewById(R.id.add_payment_for_sale_text1);
                                paymentDueDate = (EditText) dialog.findViewById(R.id.input_buy_date);

                                paymentMethod = (Spinner) dialog.findViewById(R.id.input_payment_method_spinner);
                                mCursor1 = getContentResolver().query(KasebContract.PaymentMethods.CONTENT_URI
                                        , null, null, null, null);

                                cursorAdapter = new TypesSettingAdapter(mContext,
                                        mCursor1,
                                        0,
                                        KasebContract.PaymentMethods.COLUMN_PAYMENT_METHOD_POINTER);

                                paymentMethod.setAdapter(cursorAdapter);

                                paymentMethod.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                                        Cursor mCursor3 = (Cursor) paymentMethod.getSelectedItem();
                                        paymentMethodsId =
                                                mCursor3.getString(
                                                        mCursor3.getColumnIndex(KasebContract.PaymentMethods._ID));
                                        paymentMethodType =
                                                mCursor3.getString(
                                                        mCursor3.getColumnIndex(KasebContract.PaymentMethods.COLUMN_PAYMENT_METHOD_POINTER));
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> arg0) {
                                    }

                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        Cursor mCursor4 = (Cursor) parent.getItemAtPosition(position);
                                        paymentMethodsId =
                                                mCursor4.getString(
                                                        mCursor4.getColumnIndex(KasebContract.PaymentMethods._ID));
                                        paymentMethodType =
                                                mCursor4.getString(
                                                        mCursor4.getColumnIndex(KasebContract.PaymentMethods.COLUMN_PAYMENT_METHOD_POINTER));
                                    }
                                });

                                dialogButton = (Button) dialog.findViewById(R.id.add_payment_for_sale_button1);
                                dialogButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        ArrayList<PaymentListModel> paymentArrayValues = new ArrayList<PaymentListModel>();

                                        paymentAmountList.add(Long.parseLong(paymentAmount.getText().toString()));
                                        paymentDueDateList.add(paymentDueDate.getText().toString());
                                        paymentMethodTypeList.add(paymentMethodType);
                                        paymentMethodIdTypeList.add(paymentMethodsId);

                                        for (int i = 0; i < paymentAmountList.size(); i++) {
                                            PaymentListModel paymentMethodRow = new PaymentListModel();
                                            paymentMethodRow.setPaymentAmount(paymentAmountList.get(i));
                                            paymentMethodRow.setPaymentDueDate(paymentDueDateList.get(i));
                                            paymentMethodRow.setPaymentMethod(paymentMethodTypeList.get(i));

                                            paymentArrayValues.add(paymentMethodRow);
                                        }

                                        PaymentAdapter adapter = new PaymentAdapter(getBaseContext(), paymentArrayValues);
                                        paymentMethodRowListView.setAdapter(adapter);

                                        for (int i = 0; i < paymentAmountList.size(); i++) {
                                            sPaidAmount += paymentAmountList.get(i);
                                        }

                                        paidSummary.setText(
                                                Utility.formatPurchase(
                                                        mContext,
                                                        Utility.DecimalSeperation(mContext, sPaidAmount)));

                                        sBalanceAmount = sFinalAmount - sPaidAmount;

                                        balanceSummary.setText(
                                                Utility.formatPurchase(
                                                        mContext,
                                                        Utility.DecimalSeperation(mContext, sBalanceAmount)));
//                                        int c = paymentMethodRowListView.getCount();
//
//                                        int i = paymentMethodRowListView.getLayoutParams().height;
//                                        paymentMethodRowListView.setLayoutParams(
//                                                new LinearLayout.LayoutParams(
//                                                        LinearLayout.LayoutParams.MATCH_PARENT,
//                                                        c * 85));

                                        dialog.dismiss();
                                    }
                                });

                                dialog.show();
                                //endregion case 1
                                break;
                            //region case 2 EDIT TAX
                            case 2:
                                dialog = Utility.dialogBuilder(DetailSaleView.this
                                        , R.layout.dialog_add_tax_for_sale
                                        , R.string.fab_edit_tax);

                                taxAmount = (EditText) dialog.findViewById(R.id.add_tax_for_sale_text1);
                                taxPercent = (EditText) dialog.findViewById(R.id.add_tax_for_sale_text2);

                                taxTypes = (Spinner) dialog.findViewById(R.id.input_tax_type_spinner);
                                mCursor2 = getContentResolver().query(KasebContract.TaxTypes.CONTENT_URI
                                        , null, null, null, null);

                                cursorAdapter = new TypesSettingAdapter(mContext,
                                        mCursor2,
                                        0,
                                        KasebContract.TaxTypes.COLUMN_TAX_TYPE_POINTER);
                                taxTypes.setAdapter(cursorAdapter);

                                taxTypes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                                        Cursor mCursor5 = (Cursor) taxTypes.getSelectedItem();
                                        taxTypeeId =
                                                mCursor5.getString(
                                                        mCursor5.getColumnIndex(KasebContract.TaxTypes._ID));
                                        taxType =
                                                mCursor5.getString(
                                                        mCursor5.getColumnIndex(KasebContract.TaxTypes.COLUMN_TAX_TYPE_POINTER));
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> arg0) {
                                    }

                                    //                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                                        Cursor mCursor6 = (Cursor) parent.getItemAtPosition(position);
//                                        taxTypeeId =
//                                                mCursor6.getString(
//                                                        mCursor6.getColumnIndex(KasebContract.TaxTypes._ID));
//                                        taxType =
//                                                mCursor6.getString(
//                                                        mCursor6.getColumnIndex(KasebContract.TaxTypes.COLUMN_TAX_TYPE_POINTER));
//                                    }

                                });

                                dialogButton = (Button) dialog.findViewById(R.id.add_tax_for_sale_button1);
                                dialogButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        ArrayList<TaxListModel> taxTypesArrayValues = new ArrayList<TaxListModel>();

                                        taxAmountList.add(Long.parseLong(taxAmount.getText().toString()));
                                        taxPercentList.add(taxPercent.getText().toString());
                                        taxTypeList.add(taxType);
                                        taxTypesIdsList.add(taxTypeeId);

                                        if (taxType.equals("Discount"))
                                            sTotalDiscount += Long.parseLong(taxAmount.getText().toString());
                                        else
                                            sTotalTax += Long.parseLong(taxAmount.getText().toString());

                                        for (int i = 0; i < taxAmountList.size(); i++) {
                                            TaxListModel taxTypeRow = new TaxListModel();
                                            taxTypeRow.setTaxAmount(taxAmountList.get(i));
                                            taxTypeRow.setTaxPercent(taxPercentList.get(i));
                                            taxTypeRow.setTaxType(taxTypeList.get(i));

                                            taxTypesArrayValues.add(taxTypeRow);
                                        }

                                        TaxAdapter adapter = new TaxAdapter(getBaseContext(), taxTypesArrayValues);
                                        taxTypeRowListView.setAdapter(adapter);

                                        taxSummary.setText(
                                                Utility.formatPurchase(
                                                        mContext,
                                                        Utility.DecimalSeperation(mContext, sTotalTax)));
                                        discountSummary.setText(
                                                Utility.formatPurchase(
                                                        mContext,
                                                        Utility.DecimalSeperation(mContext, sTotalDiscount)));

                                        sFinalAmount = sTotalAmount + sTotalTax - sTotalDiscount;

                                        finalAmountSummary.setText(
                                                Utility.formatPurchase(
                                                        mContext,
                                                        Utility.DecimalSeperation(mContext, sFinalAmount)));

                                        sBalanceAmount = sFinalAmount - sPaidAmount;

                                        balanceSummary.setText(
                                                Utility.formatPurchase(
                                                        mContext,
                                                        Utility.DecimalSeperation(mContext, sBalanceAmount)));

                                        dialog.dismiss();
                                    }
                                });

                                dialog.show();
                                //endregion case 2
                                break;
                            //region case 3 EDIT CUSTOMER
                            case 3:
                                mProjection = new String[]{
                                        KasebContract.Customers._ID,
                                        KasebContract.Customers.COLUMN_FIRST_NAME,
                                        KasebContract.Customers.COLUMN_LAST_NAME,
                                        KasebContract.Customers.COLUMN_STATE_ID};

                                modeList = new ListView(DetailSaleView.this);
                                mCAdapter = new CustomerAdapter(
                                        DetailSaleView.this,
                                        getContentResolver().query(
                                                KasebContract.Customers.CONTENT_URI,
                                                mProjection,
                                                null,
                                                null,
                                                null),
                                        0);
                                modeList.setAdapter(mCAdapter);

                                builder = new AlertDialog.Builder(DetailSaleView.this);
                                builder.setTitle(R.string.fab_edit_customer);

                                modeList.setOnItemClickListener(
                                        new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> parent, View view, int position,
                                                                    long id) {
//                                Cursor c = mCAdapter.getCursor();
//                                c.moveToPosition(position);

                                                Cursor cursor = (Cursor) parent.getItemAtPosition(position);
                                                if (cursor != null) {
                                                    nameCustomer.setText(cursor.getString(cursor.getColumnIndex(KasebContract.Customers.COLUMN_FIRST_NAME)));
                                                    familyCustomer.setText(cursor.getString(cursor.getColumnIndex(KasebContract.Customers.COLUMN_LAST_NAME)));
                                                    customerId = Long.parseLong(cursor.getString(cursor.getColumnIndex(KasebContract.Customers._ID)));
                                                    dialog.dismiss();
                                                }
                                                cursor.close();
                                            }
                                        }

                                );

                                builder.setView(modeList);
                                dialog = builder.create();

                                dialog.show();
                                break;
                            //endregion case 3
                            default:
                                break;
                        }
                        return true;
                    }
                }
        );
        popup.show();//showing popup menu
    }
}