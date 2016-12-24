package mjkarbasian.moshtarimadar;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import mjkarbasian.moshtarimadar.Data.KasebContract;
import mjkarbasian.moshtarimadar.Data.KasebDbHelper;
import mjkarbasian.moshtarimadar.Data.KasebProvider;
import mjkarbasian.moshtarimadar.adapters.CostSaleProductAdapter;
import mjkarbasian.moshtarimadar.adapters.CustomerAdapter;
import mjkarbasian.moshtarimadar.adapters.TypesSettingAdapter;
import mjkarbasian.moshtarimadar.helper.Utility;

public class DetailSaleView extends AppCompatActivity {

    //region declare Values
    Context mContext;
    KasebDbHelper mOpenHelper;
    SQLiteDatabase mDb;
    FragmentManager frm;
    Bundle bundleCardViewFragments;
    AlertDialog.Builder builder;
    Dialog dialog;
    LinearLayout addCustomerLayout;
    int mNumberOfChooseProduct = 0;

    CardViewProducts mCardViewProducts;
    CardViewPayments mCardViewPayments;
    CardViewTaxes mCardViewTaxes;

    ArrayList<Map<String, String>> mChosenProductListMap;
    ArrayList<Map<String, String>> mPaymentListMap;
    ArrayList<Map<String, String>> mTaxListMap;

    ContentValues[] itemsValuesArray;
    ContentValues[] paymentValuesArray;
    ContentValues[] taxValuesArray;

    ContentValues saleValues = new ContentValues();
    ContentValues detailSaleValues = new ContentValues();
    ContentValues itemsValues;
    ContentValues paymentValues;
    ContentValues taxValues;

    String[] mProjection;
    String[] mProjectionProductHistory;
    String[] mSelection;
    String[] mProjectionInitialize;
    String[] mSelectionInitialize;

    String mWhereStatement;
    String mWhereStatementInitialize;

    Long sTotalAmount = 0l;
    Long sTotalTax = 0l;
    Long sTotalDiscount = 0l;
    Long sFinalAmount = 0l;
    Long sPaidAmount = 0l;
    Long sBalanceAmount = 0l;
    Long customerId;
    Long cost = 0l;
    Long whichSaleId;
    Long whichDetailSaleId;

    Cursor mCursor1;
    Cursor mCursor2;
    Cursor mCursorInitialize;

    FloatingActionButton fabProducts;
    FloatingActionButton fabPayments;
    FloatingActionButton fabTaxes;

    Button dialogButton;

    Spinner paymentMethod;
    Spinner taxTypes;

    TextView totalAmountSummary;
    TextView taxSummary;
    TextView discountSummary;
    TextView finalAmountSummary;
    TextView paidSummary;
    TextView balanceSummary;
    TextView nameCustomer;
    TextView familyCustomer;

    EditText paymentAmount;
    EditText paymentDueDate;
    EditText taxAmount;
    EditText taxPercent;
    EditText saleCode;
    EditText saleDate;

    ListView mListView;
    ListView modeList;
    ListView mProducyListView;
    ListView mPaymentListView;
    ListView mTaxListView;

    TypesSettingAdapter cursorAdapter = null;
    CostSaleProductAdapter mAdapter = null;
    CustomerAdapter mCAdapter = null;
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

        bundleCardViewFragments = new Bundle();
        bundleCardViewFragments.putString("activity", "view");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        frm = getSupportFragmentManager();

        mChosenProductListMap = new ArrayList<Map<String, String>>();
        mPaymentListMap = new ArrayList<Map<String, String>>();
        mTaxListMap = new ArrayList<Map<String, String>>();

        saleCode = (EditText) findViewById(R.id.detail_sales_info_sale_code);
        saleDate = (EditText) findViewById(R.id.detail_sales_info_sale_date);
        addCustomerLayout = (LinearLayout) findViewById(R.id.content_detail_sale_insert_linear_layout_add_customer);
        //endregion Initialize Some Views & Values

        //region Set Fragments
        mCardViewProducts = new CardViewProducts();
        mCardViewPayments = new CardViewPayments();
        mCardViewTaxes = new CardViewTaxes();

        mCardViewProducts.setArguments(bundleCardViewFragments);
        mCardViewPayments.setArguments(bundleCardViewFragments);
        mCardViewTaxes.setArguments(bundleCardViewFragments);

        frm.beginTransaction().replace(R.id.my_container_1, mCardViewProducts, "Frag_CardViewProducts_tag").commit();
        frm.beginTransaction().replace(R.id.my_container_2, mCardViewPayments, "Frag_CardViewPayments_tag").commit();
        frm.beginTransaction().replace(R.id.my_container_3, mCardViewTaxes, "Frag_CardViewTaxes_tag").commit();
        //endregion Set Fragments

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
    }

    @Override
    protected void onStart() {
        super.onStart();

        //region Initialize Some Views
        fabProducts = (FloatingActionButton) findViewById(R.id.fab_fragment_card_view_products);
        fabPayments = (FloatingActionButton) findViewById(R.id.fab_fragment_card_view_payments);
        fabTaxes = (FloatingActionButton) findViewById(R.id.fab_fragment_card_view_taxes);

        mProducyListView = (ListView) findViewById(R.id.list_view_fragment_card_view_products);
        mPaymentListView = (ListView) findViewById(R.id.list_view_fragment_card_view_payments);
        mTaxListView = (ListView) findViewById(R.id.list_view_fragment_card_view_taxes);
        //endregion Initialize Some Views

        //region Disable Views
        saleCode.setEnabled(false);
        saleDate.setEnabled(false);

        addCustomerLayout.setEnabled(false);

        mProducyListView.setEnabled(false);
        mPaymentListView.setEnabled(false);
        mTaxListView.setEnabled(false);

        fabProducts.hide();
        fabPayments.hide();
        fabTaxes.hide();
        //endregion Disable Views

        //region Get Products
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
                    Map<String, String> mProductsRowMap = new HashMap<>();

                    String _id = mCursorInitialize.getString(
                            mCursorInitialize.getColumnIndex(
                                    KasebContract.DetailSaleProducts.COLUMN_PRODUCT_ID));

                    Cursor mCursor = getContentResolver().query(
                            KasebContract.Products.buildProductsUri(Long.valueOf(_id)),
                            new String[]{
                                    KasebContract.Products._ID,
                                    KasebContract.Products.COLUMN_PRODUCT_NAME},
                            null,
                            null,
                            null);

                    Long amount = mCursorInitialize.getLong(
                            mCursorInitialize.getColumnIndex(
                                    KasebContract.DetailSaleProducts.COLUMN_AMOUNT));

                    Long num = mCursorInitialize.getLong(
                            mCursorInitialize.getColumnIndex(
                                    KasebContract.DetailSaleProducts.COLUMN_QUANTITY));

                    mProductsRowMap.put("id", _id);

                    if (mCursor != null)
                        if (mCursor.moveToFirst())
                            mProductsRowMap.put("name",
                                    mCursor.getString(
                                            mCursor.getColumnIndex(
                                                    KasebContract.Products.COLUMN_PRODUCT_NAME)));

                    mProductsRowMap.put("quantity", String.valueOf(num));
                    mProductsRowMap.put("price", String.valueOf(amount / num));

                    mChosenProductListMap.add(mProductsRowMap);
                    mCursorInitialize.moveToNext();
                }

                mCardViewProducts.getChosenProductAdapter(mChosenProductListMap);
            }
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

        mProjectionInitialize = new String[]{
                KasebContract.PaymentMethods._ID,
                KasebContract.PaymentMethods.COLUMN_PAYMENT_METHOD_POINTER};

        if (mCursorInitialize != null)
            if (mCursorInitialize.moveToFirst()) {
                for (int i = 0; i < mCursorInitialize.getCount(); i++) {
                    String _id = mCursorInitialize.getString(
                            mCursorInitialize.getColumnIndex(
                                    KasebContract.DetailSalePayments.COLUMN_PAYMENT_METHOD_ID));

                    Map<String, String> mPaymentsRowMap = new HashMap<>();

                    Cursor mCursor2 = getContentResolver().query(
                            KasebContract.PaymentMethods.buildPaymentMethodsUri(Long.valueOf(_id)),
                            mProjectionInitialize,
                            null,
                            null,
                            null);

                    if (mCursor2 != null)
                        if (mCursor2.moveToFirst())
                            mPaymentsRowMap.put("type",
                                    mCursor2.getString(
                                            mCursor2.getColumnIndex(
                                                    KasebContract.PaymentMethods.COLUMN_PAYMENT_METHOD_POINTER)));

                    mPaymentsRowMap.put("id", _id);

                    mPaymentsRowMap.put("amount", String.valueOf(
                            mCursorInitialize.getLong(
                                    mCursorInitialize.getColumnIndex(
                                            KasebContract.DetailSalePayments.COLUMN_AMOUNT))));

                    mPaymentsRowMap.put("duedate", String.valueOf(
                            mCursorInitialize.getString(
                                    mCursorInitialize.getColumnIndex(
                                            KasebContract.DetailSalePayments.COLUMN_DUE_DATE))));

                    mPaymentListMap.add(mPaymentsRowMap);
                    mCursorInitialize.moveToNext();
                }

                mCardViewPayments.getPaymentAdapter(mPaymentListMap);
            }

        //endregion Get Payments

        //region Get Taxes
        mProjectionInitialize = new String[]{
                KasebContract.DetailSaleTaxes._ID,
                KasebContract.DetailSaleTaxes.COLUMN_DETAIL_SALE_ID,
                KasebContract.DetailSaleTaxes.COLUMN_AMOUNT,
                KasebContract.DetailSaleTaxes.COLUMN_TAX_TYPE_ID};

        mCursorInitialize = getContentResolver().query(
                KasebContract.DetailSaleTaxes.taxOfDetailSale(whichDetailSaleId),
                mProjectionInitialize,
                null,
                null,
                null);

        mProjectionInitialize = new String[]{
                KasebContract.TaxTypes._ID,
                KasebContract.TaxTypes.COLUMN_TAX_TYPE_POINTER};

        if (mCursorInitialize != null)
            if (mCursorInitialize.moveToFirst()) {
                for (int i = 0; i < mCursorInitialize.getCount(); i++) {
                    Map<String, String> mTaxesRowMap = new HashMap<>();

                    String _id = mCursorInitialize.getString(
                            mCursorInitialize.getColumnIndex(
                                    KasebContract.DetailSaleTaxes.COLUMN_TAX_TYPE_ID));

                    Long taxAmount = mCursorInitialize.getLong(
                            mCursorInitialize.getColumnIndex(
                                    KasebContract.DetailSaleTaxes.COLUMN_AMOUNT));

                    mTaxesRowMap.put("id", _id);
                    mTaxesRowMap.put("amount", String.valueOf(taxAmount));

                    Cursor mCursor2 = getContentResolver().query(
                            KasebContract.TaxTypes.buildTaxTypesUri(Long.valueOf(_id)),
                            mProjectionInitialize,
                            null,
                            null,
                            null);

                    if (mCursor2 != null)
                        if (mCursor2.moveToFirst())
                            mTaxesRowMap.put("type",
                                    mCursor2.getString(
                                            mCursor2.getColumnIndex(
                                                    KasebContract.TaxTypes.COLUMN_TAX_TYPE_POINTER)));

                    Cursor mCursor3 = getContentResolver().query(
                            KasebContract.DetailSale.buildDetailSaleUri(
                                    mCursorInitialize.getLong(
                                            mCursorInitialize.getColumnIndex(
                                                    KasebContract.DetailSaleTaxes.COLUMN_DETAIL_SALE_ID))),
                            new String[]{
                                    KasebContract.DetailSale.COLUMN_SUB_TOTAL},
                            null,
                            null,
                            null);

                    if (mCursor3 != null)
                        if (mCursor3.moveToFirst()) {
                            Long totalAmount = mCursor3.getLong(
                                    mCursor3.getColumnIndex(
                                            KasebContract.DetailSale.COLUMN_SUB_TOTAL));

                            mTaxesRowMap.put("percent", String.format("%.2f", (taxAmount * 100 / (double) totalAmount)));
                        }
                    mTaxListMap.add(mTaxesRowMap);
                    mCursorInitialize.moveToNext();
                }

                mCardViewTaxes.getTaxAdapter(mTaxListMap);
            }
        //endregion Get Taxes
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

                detailSaleValues.put(KasebContract.DetailSale.COLUMN_DATE, saleDate.getText().toString());
                detailSaleValues.put(KasebContract.DetailSale.COLUMN_IS_BALANCED, 0);
                detailSaleValues.put(KasebContract.DetailSale.COLUMN_ITEMS_NUMBER, mChosenProductListMap.size());
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
                int count = mChosenProductListMap.size();
                itemsValuesArray = new ContentValues[count];

                for (int i = 0; i < count; i++) {
                    itemsValues = new ContentValues();

                    itemsValues.put(KasebContract.DetailSaleProducts.COLUMN_AMOUNT,
                            Long.valueOf(mChosenProductListMap.get(i).get("price").toString()) *
                                    Long.valueOf(mChosenProductListMap.get(i).get("quantity").toString()));

                    itemsValues.put(KasebContract.DetailSaleProducts.COLUMN_DETAIL_SALE_ID, whichDetailSaleId);
                    itemsValues.put(KasebContract.DetailSaleProducts.COLUMN_PRODUCT_ID, mChosenProductListMap.get(i).get("id").toString());
                    itemsValues.put(KasebContract.DetailSaleProducts.COLUMN_QUANTITY, mChosenProductListMap.get(i).get("quantity").toString());

                    itemsValuesArray[i] = itemsValues;
                }

                this.getContentResolver().bulkInsert(
                        KasebContract.DetailSaleProducts.CONTENT_URI,
                        itemsValuesArray
                );
                //endregion Insert DetailSaleProducts

                //region Insert DetailSalePayments
                count = mPaymentListMap.size();
                paymentValuesArray = new ContentValues[count];

                for (int i = 0; i < count; i++) {
                    paymentValues = new ContentValues();

                    paymentValues.put(KasebContract.DetailSalePayments.COLUMN_DUE_DATE, mPaymentListMap.get(i).get("duedate").toString());
                    paymentValues.put(KasebContract.DetailSalePayments.COLUMN_DETAIL_SALE_ID, whichDetailSaleId);
                    paymentValues.put(KasebContract.DetailSalePayments.COLUMN_AMOUNT, Long.valueOf(mPaymentListMap.get(i).get("amount").toString()));
                    paymentValues.put(KasebContract.DetailSalePayments.COLUMN_PAYMENT_METHOD_ID, mPaymentListMap.get(i).get("id").toString());

                    paymentValuesArray[i] = paymentValues;
                }

                this.getContentResolver().bulkInsert(
                        KasebContract.DetailSalePayments.CONTENT_URI,
                        paymentValuesArray
                );
                //endregion Insert DetailSalePayments

                //region Insert DetailSaleTaxes
                count = mTaxListMap.size();
                taxValuesArray = new ContentValues[count];

                for (int i = 0; i < count; i++) {
                    taxValues = new ContentValues();

                    taxValues.put(KasebContract.DetailSaleTaxes.COLUMN_DETAIL_SALE_ID, whichDetailSaleId);
                    taxValues.put(KasebContract.DetailSaleTaxes.COLUMN_AMOUNT, Long.valueOf(mTaxListMap.get(i).get("amount").toString()));
                    taxValues.put(KasebContract.DetailSaleTaxes.COLUMN_TAX_TYPE_ID, mTaxListMap.get(i).get("id").toString());

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

                mProducyListView.setEnabled(true);
                mPaymentListView.setEnabled(true);
                mTaxListView.setEnabled(true);

                fabProducts.show();
                fabPayments.show();
                fabTaxes.show();

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void fab_detail_sale_add_customer(View v) {
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
        builder.setTitle(R.string.fab_add_customer);

        modeList.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position,
                                            long id) {

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
    }

    public void fab_detail_sale_add_product(View v) {
        builder = new AlertDialog.Builder(DetailSaleView.this);
        builder.setTitle(R.string.fab_add_product);

        //region Set Adapter To Dialog
        mProjection = new String[]{
                KasebContract.Products._ID,
                KasebContract.Products.COLUMN_PRODUCT_NAME,
                KasebContract.Products.COLUMN_PRODUCT_CODE};

        mNumberOfChooseProduct = mChosenProductListMap.size();
        mSelection = new String[(mNumberOfChooseProduct > 0 ? mNumberOfChooseProduct : 1)];
        for (int i = 0; i < mNumberOfChooseProduct; i++) {
            mSelection[i] = mChosenProductListMap.get(i).get("id");
        }

        if (mSelection[0] == null)
            mSelection[0] = "-1";

        mWhereStatement = KasebContract.Products._ID + " NOT IN (" +
                Utility.makePlaceholders((mNumberOfChooseProduct > 0 ? mNumberOfChooseProduct : 1)) + ")";

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
        modeList.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Cursor cursor = (Cursor) parent.getItemAtPosition(position);
                        if (cursor != null) {
                            final String _id = cursor.getString(
                                    cursor.getColumnIndex(KasebContract.Products._ID));

                            final String _name = cursor.getString(
                                    cursor.getColumnIndex(KasebContract.Products.COLUMN_PRODUCT_NAME));

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
                                    cost = mCursor.getLong(mCursor.getColumnIndex(KasebContract.ProductHistory.COLUMN_SALE_PRICE));
                                }
                            }

                            final Dialog howManyOfThat = Utility.dialogBuilder(DetailSaleView.this
                                    , R.layout.dialog_add_number_of_product_for_sale
                                    , R.string.how_many);

                            final EditText howManyEditText = (EditText) howManyOfThat
                                    .findViewById(R.id.add_number_of_product_for_sale_number);

                            //region Save Button
                            Button saveButton = (Button) howManyOfThat
                                    .findViewById(R.id.add_number_of_product_for_sale_save);

                            saveButton.setOnClickListener(
                                    new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Map<String, String> mProductsRowMap = new HashMap<>();

                                            String num = howManyEditText.getText().toString();

                                            mProductsRowMap.put("id", _id);
                                            mProductsRowMap.put("name", _name);
                                            mProductsRowMap.put("quantity", String.valueOf(num));
                                            mProductsRowMap.put("price", String.valueOf(cost));

                                            sTotalAmount += cost * Long.valueOf(num);

                                            int mIndex = Utility.
                                                    indexOfRowsInMap(mChosenProductListMap, "id", _id);

                                            if (mIndex == -1) {
                                                //region Add Product To Sale
                                                mChosenProductListMap.add(mProductsRowMap);
                                                mCardViewProducts.getChosenProductAdapter(mChosenProductListMap);

                                                howManyOfThat.dismiss();
                                                dialog.dismiss();
                                            }
                                        }
                                    }

                            );
                            //endregion Save Button

                            //region Cancel Button
                            Button cancelButton = (Button) howManyOfThat
                                    .findViewById(R.id.add_number_of_product_for_sale_cancel);

                            cancelButton.setOnClickListener(
                                    new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            howManyOfThat.dismiss();
                                        }
                                    }

                            );
                            //endregion Cancel Button

                            howManyOfThat.show();
                            //endregion NumberOfChosenProduct
                        }
                    }
                }

        );
        //endregion ClickListener ListView Dialog

        builder.setView(modeList);
        dialog = builder.create();

        dialog.show();
    }

    public void fab_detail_sale_add_payment(View v) {
        dialog = Utility.dialogBuilder(DetailSaleView.this
                , R.layout.dialog_add_payment_for_sale
                , R.string.fab_add_payment);

        final Map<String, String> paymentMapRow = new HashMap<>();

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

        paymentMethod.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View
                            arg1, int arg2, long arg3) {
                        Cursor mCursor3 = (Cursor) paymentMethod.getSelectedItem();

                        paymentMapRow.put("id", mCursor3.getString(
                                mCursor3.getColumnIndex(KasebContract.PaymentMethods._ID)).toString());

                        paymentMapRow.put("type", mCursor3.getString(
                                mCursor3.getColumnIndex(KasebContract.PaymentMethods.COLUMN_PAYMENT_METHOD_POINTER)).toString());
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                    }

                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    }
                });

        dialogButton = (Button) dialog.findViewById(R.id.add_payment_for_sale_button1);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paymentMapRow.put("amount", paymentAmount.getText().toString());
                paymentMapRow.put("duedate", paymentDueDate.getText().toString());

                mPaymentListMap.add(paymentMapRow);
                mCardViewPayments.getPaymentAdapter(mPaymentListMap);
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
    }

    public void fab_detail_sale_add_tax(View v) {
        dialog = Utility.dialogBuilder(DetailSaleView.this
                , R.layout.dialog_add_tax_for_sale
                , R.string.fab_add_tax);

        final Map<String, String> taxMapRow = new HashMap<>();

        taxAmount = (EditText) dialog.findViewById(R.id.add_tax_for_sale_text1);
        taxPercent = (EditText) dialog.findViewById(R.id.add_tax_for_sale_text2);

        taxPercent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    Float percent = Float.valueOf(taxPercent.getText().toString());
                    if (percent > 100)
                        taxPercent.setText("100");
                } catch (Exception e) {
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    Float percent = Float.valueOf(taxPercent.getText().toString());
                    taxAmount.setText(String.format("%.0f", Float.valueOf(percent * sTotalAmount / 100)));
                } catch (Exception e) {
                }
            }
        });

        taxTypes = (Spinner) dialog.findViewById(R.id.input_tax_type_spinner);
        mCursor2 = getContentResolver()

                .

                        query(KasebContract.TaxTypes.CONTENT_URI
                                , null, null, null, null);

        cursorAdapter = new TypesSettingAdapter(mContext,
                mCursor2,
                0,
                KasebContract.TaxTypes.COLUMN_TAX_TYPE_POINTER);
        taxTypes.setAdapter(cursorAdapter);

        taxTypes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()

        {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                Cursor mCursor5 = (Cursor) taxTypes.getSelectedItem();

                taxMapRow.put("id", mCursor5.getString(
                        mCursor5.getColumnIndex(KasebContract.TaxTypes._ID)).toString());

                taxMapRow.put("type", mCursor5.getString(
                        mCursor5.getColumnIndex(KasebContract.TaxTypes.COLUMN_TAX_TYPE_POINTER)).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }

        });

        dialogButton = (Button) dialog.findViewById(R.id.add_tax_for_sale_button1);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                taxMapRow.put("amount", taxAmount.getText().toString());
                taxMapRow.put("percent", taxPercent.getText().toString());

                mTaxListMap.add(taxMapRow);
                mCardViewTaxes.getTaxAdapter(mTaxListMap);

                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void setValuesOfFactor() {
        sTotalAmount = 0l;
        for (int i = 0; i < mChosenProductListMap.size(); i++) {
            sTotalAmount +=
                    Long.valueOf(mChosenProductListMap.get(i).get("quantity").toString()) *
                            Long.valueOf(mChosenProductListMap.get(i).get("price").toString());
        }

        totalAmountSummary.setText(
                Utility.formatPurchase(
                        mContext,
                        Utility.DecimalSeperation(mContext, sTotalAmount)));

        sTotalDiscount = 0l;
        sTotalTax = 0l;
        for (int i = 0; i < mTaxListMap.size(); i++) {
            String type = mTaxListMap.get(i).get("type").toString();

            if (type.equals("Discount"))
                sTotalDiscount += Long.valueOf(mTaxListMap.get(i).get("amount").toString());
            else
                sTotalTax += Long.valueOf(mTaxListMap.get(i).get("amount").toString());
        }

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

        sPaidAmount = 0l;
        for (int i = 0; i < mPaymentListMap.size(); i++) {
            sPaidAmount += Long.valueOf(mPaymentListMap.get(i).get("amount").toString());
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
    }
}