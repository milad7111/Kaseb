package mjkarbasian.moshtarimadar;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
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
import mjkarbasian.moshtarimadar.adapters.CostSaleProductAdapter;
import mjkarbasian.moshtarimadar.adapters.CustomerAdapter;
import mjkarbasian.moshtarimadar.adapters.PaymentAdapter;
import mjkarbasian.moshtarimadar.adapters.TaxAdapter;
import mjkarbasian.moshtarimadar.adapters.TypesSettingAdapter;
import mjkarbasian.moshtarimadar.helper.PaymentListModel;
import mjkarbasian.moshtarimadar.helper.TaxListModel;
import mjkarbasian.moshtarimadar.helper.Utility;

public class DetailSaleInsert extends AppCompatActivity {
    private static Context mContext;
    long customerId;
    long[] productIds = new long[2];
    long itemNumber;
    long subTotal;
    long totalDiscount;
    long totalDue;
    long totalTax;
    long totalPaid;
    long quantity;
    String amount;
    long paymentMethodId[];
    long amountPayment[];
    String dueDate[];
    long taxTypeId[];
    long amountTax[];
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
    Uri insertUri;
    String[] mProjection;
    TextView nameCustomer;
    TextView familyCustomer;
    CostSaleProductAdapter mProductAdapter;
    ListView mListView;
    String[] mSelection;
    String mWhereStatement;
    String mWhereStatementDialog;
    int productNumber = 0;
    int productNumberDialog = 0;
    List<String> mNewList = new ArrayList<String>();
    List<String> mNewListDialog = new ArrayList<String>();
    EditText paymentAmount;
    EditText paymentDueDate;
    EditText taxAmount;
    EditText taxPercent;
    String paymentMethodsId;
    String taxTypeeId;
    String paymentMethodType;
    String taxType;
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

    Cursor mCursor1;
    Cursor mCursor2;

    TextView totalAmountSummary;
    TextView taxSummary;
    TextView discountSummary;
    TextView finalAmountSummary;
    TextView paidSummary;
    TextView balanceSummary;

    TypesSettingAdapter cursorAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_sale_insert);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Fill in Sale summary
        customerId = 1;
        mContext = this;
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

        mWhereStatementDialog = KasebContract.Products._ID + " = ? ";

        mProjection = new String[]{
                KasebContract.Products._ID,
                KasebContract.Products.COLUMN_PRODUCT_NAME,
                KasebContract.Products.COLUMN_PRODUCT_CODE};

//        mSelection = new String[]{"-1"};
//        mWhereStatement = KasebContract.Products._ID + " = ? ";

//        modeList = (ListView) findViewById(R.id.listview_sale_items);
        mProductAdapter = new CostSaleProductAdapter(
                DetailSaleInsert.this,
                getContentResolver().query(
                        KasebContract.Products.CONTENT_URI,
                        mProjection,
                        mWhereStatement,
                        mSelection,
                        null),
                0,
                "product");

        Cursor mCursor = mProductAdapter.getCursor();
        if (mCursor.moveToFirst())
            for (int i = 0; i < mCursor.getCount(); i++) {
                mNewListDialog.add(mCursor.getString(mCursor.getColumnIndex(KasebContract.Products._ID)));
                mCursor.moveToNext();
            }

        mCursor.close();
//        modeList.setAdapter(mProductAdapter);

        paymentMethodRowListView = (ListView) findViewById(R.id.listview_sale_payments);

        ArrayList<PaymentListModel> paymentArrayValues1 = new ArrayList<PaymentListModel>();

        paymentAmountList.add(12000l);
        paymentDueDateList.add("1395/1/12");
        paymentMethodTypeList.add("Cash");
        paymentMethodIdTypeList.add("1");

        for (int i = 0; i < paymentAmountList.size(); i++) {
            PaymentListModel paymentMethodRow = new PaymentListModel();
            paymentMethodRow.setPaymentAmount(paymentAmountList.get(i));
            paymentMethodRow.setPaymentDueDate(paymentDueDateList.get(i));
            paymentMethodRow.setPaymentMethod(paymentMethodTypeList.get(i));

            paymentArrayValues1.add(paymentMethodRow);
        }

        PaymentAdapter adapter = new PaymentAdapter(getBaseContext(), paymentArrayValues1);
        paymentMethodRowListView.setAdapter(adapter);
        paymentMethodRowListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(DetailSaleInsert.this, "etet", Toast.LENGTH_SHORT).show();
            }
        });

        try
        {
            Toast.makeText(DetailSaleInsert.this, getIntent().getExtras().get("saleId").toString(), Toast.LENGTH_SHORT).show();
        }catch (Exception e)
        {

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_detail_sale_insert, menu);
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
                EditText saleCode = (EditText) findViewById(R.id.detail_sales_info_sale_code);

                saleValues.put(KasebContract.Sales.COLUMN_CUSTOMER_ID, customerId);
                saleValues.put(KasebContract.Sales.COLUMN_IS_DELETED, 0);
                saleValues.put(KasebContract.Sales.COLUMN_SALE_CODE, saleCode.getText().toString());

                insertUri = this.getContentResolver().insert(
                        KasebContract.Sales.CONTENT_URI,
                        saleValues
                );

                //region disabling edit
                saleCode.setEnabled(false);

                EditText saleDate = (EditText) findViewById(R.id.detail_sales_info_sale_date);

                ListView getProductListView = (ListView) findViewById(R.id.listview_sale_items);
                itemNumber = getProductListView.getAdapter().getCount();
                subTotal = 10;
                totalDiscount = 5;
                totalDue = 10;
                totalTax = 2;
                totalPaid = 5;

                detailSaleValues.put(KasebContract.DetailSale.COLUMN_DATE, saleDate.getText().toString());
                detailSaleValues.put(KasebContract.DetailSale.COLUMN_IS_BALANCED, 0);
                detailSaleValues.put(KasebContract.DetailSale.COLUMN_ITEMS_NUMBER, itemNumber);
                detailSaleValues.put(KasebContract.DetailSale.COLUMN_SALE_ID, insertUri.getLastPathSegment());
                detailSaleValues.put(KasebContract.DetailSale.COLUMN_SUB_TOTAL, subTotal);
                detailSaleValues.put(KasebContract.DetailSale.COLUMN_TOTAL_DISCOUNT, totalDiscount);
                detailSaleValues.put(KasebContract.DetailSale.COLUMN_TOTAL_DUE, totalDue);
                detailSaleValues.put(KasebContract.DetailSale.COLUMN_TOTAL_PAID, totalPaid);
                detailSaleValues.put(KasebContract.DetailSale.COLUMN_TOTAL_TAX, totalTax);

                insertUri = this.getContentResolver().insert(
                        KasebContract.DetailSale.CONTENT_URI,
                        detailSaleValues
                );

                productIds[0] = 1;
                productIds[1] = 2;
                quantity = 5;
                amount = "5";

                itemsValuesArray = new ContentValues[2];

                itemsValues = new ContentValues();
                itemsValues.put(KasebContract.DetailSaleProducts.COLUMN_AMOUNT, amount);
                itemsValues.put(KasebContract.DetailSaleProducts.COLUMN_DETAIL_SALE_ID, insertUri.getLastPathSegment());
                itemsValues.put(KasebContract.DetailSaleProducts.COLUMN_PRODUCT_ID, productIds[0]);
                itemsValues.put(KasebContract.DetailSaleProducts.COLUMN_QUANTITY, quantity);

                itemsValuesArray[0] = itemsValues;

                itemsValues = new ContentValues();
                itemsValues.put(KasebContract.DetailSaleProducts.COLUMN_AMOUNT, amount);
                itemsValues.put(KasebContract.DetailSaleProducts.COLUMN_DETAIL_SALE_ID, insertUri.getLastPathSegment());
                itemsValues.put(KasebContract.DetailSaleProducts.COLUMN_PRODUCT_ID, productIds[1]);
                itemsValues.put(KasebContract.DetailSaleProducts.COLUMN_QUANTITY, quantity);

                itemsValuesArray[1] = itemsValues;

                this.getContentResolver().bulkInsert(
                        KasebContract.DetailSaleProducts.CONTENT_URI,
                        itemsValuesArray
                );

                paymentMethodId = new long[2];
                amountPayment = new long[2];
                dueDate = new String[2];

                paymentMethodId[0] = 1;
                paymentMethodId[1] = 2;
                amountPayment[0] = 120000;
                amountPayment[1] = 52000;
                dueDate[0] = "95-10-2";
                dueDate[1] = "95-11-3";

                paymentValuesArray = new ContentValues[2];

                paymentValues = new ContentValues();
                paymentValues.put(KasebContract.DetailSalePayments.COLUMN_DUE_DATE, dueDate[0]);
                paymentValues.put(KasebContract.DetailSalePayments.COLUMN_DETAIL_SALE_ID, insertUri.getLastPathSegment());
                paymentValues.put(KasebContract.DetailSalePayments.COLUMN_AMOUNT, amountPayment[0]);
                paymentValues.put(KasebContract.DetailSalePayments.COLUMN_PAYMENT_METHOD_ID, paymentMethodId[0]);

                paymentValuesArray[0] = paymentValues;

                paymentValues = new ContentValues();
                paymentValues.put(KasebContract.DetailSalePayments.COLUMN_DUE_DATE, dueDate[1]);
                paymentValues.put(KasebContract.DetailSalePayments.COLUMN_DETAIL_SALE_ID, insertUri.getLastPathSegment());
                paymentValues.put(KasebContract.DetailSalePayments.COLUMN_AMOUNT, amountPayment[1]);
                paymentValues.put(KasebContract.DetailSalePayments.COLUMN_PAYMENT_METHOD_ID, paymentMethodId[1]);

                paymentValuesArray[1] = paymentValues;

                this.getContentResolver().bulkInsert(
                        KasebContract.DetailSalePayments.CONTENT_URI,
                        paymentValuesArray
                );

                taxTypeId = new long[2];
                amountTax = new long[2];

                taxTypeId[0] = 1;
                taxTypeId[1] = 2;
                amountTax[0] = 120000;
                amountTax[1] = 52000;

                taxValuesArray = new ContentValues[2];

                taxValues = new ContentValues();
                taxValues.put(KasebContract.DetailSaleTaxes.COLUMN_DETAIL_SALE_ID, insertUri.getLastPathSegment());
                taxValues.put(KasebContract.DetailSaleTaxes.COLUMN_AMOUNT, amountTax[0]);
                taxValues.put(KasebContract.DetailSaleTaxes.COLUMN_TAX_TYPE_ID, taxTypeId[0]);

                taxValuesArray[0] = taxValues;

                taxValues = new ContentValues();
                taxValues.put(KasebContract.DetailSaleTaxes.COLUMN_DETAIL_SALE_ID, insertUri.getLastPathSegment());
                taxValues.put(KasebContract.DetailSaleTaxes.COLUMN_AMOUNT, amountTax[1]);
                taxValues.put(KasebContract.DetailSaleTaxes.COLUMN_TAX_TYPE_ID, taxTypeId[1]);

                taxValuesArray[1] = taxValues;

                this.getContentResolver().bulkInsert(
                        KasebContract.DetailSaleTaxes.CONTENT_URI,
                        taxValuesArray
                );

                //just a message to show everything are under control
                Toast.makeText(this,
                        getApplicationContext().getResources().getString(R.string.msg_insert_succeed), Toast.LENGTH_LONG).show();

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void fab_detail_sale_insert(View v) {

        PopupMenu popup = new PopupMenu(this, v);
        popup.getMenu().add(0, 0, 0, R.string.fab_add_product);
        popup.getMenu().add(0, 1, 0, R.string.fab_add_payment);
        popup.getMenu().add(0, 2, 0, R.string.fab_add_tax);
        popup.getMenu().add(0, 3, 0, R.string.fab_add_customer);
        popup.setOnMenuItemClickListener(
                new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            //region case 0 ADD PRODUCT
                            case 0:
                                builder = new AlertDialog.Builder(DetailSaleInsert.this);
                                builder.setTitle(R.string.fab_add_product);

                                mProjection = new String[]{
                                        KasebContract.Products._ID,
                                        KasebContract.Products.COLUMN_PRODUCT_NAME,
                                        KasebContract.Products.COLUMN_PRODUCT_CODE};

                                productNumberDialog = mNewListDialog.size();
                                mSelection = new String[(productNumberDialog > 0 ? productNumberDialog : 1)];
                                for (int i = 0; i < mNewListDialog.size(); i++) {
                                    mSelection[i] = mNewListDialog.get(i);
                                }

                                if (mSelection[0] == null)
                                    mSelection[0] = "-1";

                                mWhereStatement = KasebContract.Products._ID + " IN (" +
                                        Utility.makePlaceholders((productNumberDialog > 0 ? productNumberDialog : 1)) + ")";

                                modeList = new ListView(DetailSaleInsert.this);
                                mAdapter = new CostSaleProductAdapter(
                                        DetailSaleInsert.this,
                                        getContentResolver().query(
                                                KasebContract.Products.CONTENT_URI,
                                                mProjection,
                                                mWhereStatement,
                                                mSelection,
                                                null),
                                        0,
                                        "product");
                                modeList.setAdapter(mAdapter);

                                modeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        Cursor cursor = (Cursor) parent.getItemAtPosition(position);
                                        if (cursor != null) {
                                            String _id = cursor.getString(cursor.getColumnIndex(KasebContract.Products._ID));

                                            if (!mNewList.contains(_id)) {
                                                productNumber = mNewList.size() + 1;

                                                mNewList.add(_id);
                                                mNewListDialog.remove(_id);

                                                mSelection = new String[productNumber];
                                                for (int i = 0; i < mNewList.size(); i++) {
                                                    mSelection[i] = mNewList.get(i);
                                                }

                                                mWhereStatement = KasebContract.Products._ID + " IN (" + Utility.makePlaceholders(productNumber) + ")";

                                                modeList = (ListView) findViewById(R.id.listview_sale_items);
                                                mProductAdapter = new CostSaleProductAdapter(
                                                        DetailSaleInsert.this,
                                                        getContentResolver().query(
                                                                KasebContract.Products.CONTENT_URI,
                                                                mProjection,
                                                                mWhereStatement,
                                                                mSelection,
                                                                null),
                                                        0,
                                                        "product");
                                                modeList.setAdapter(mProductAdapter);

                                                modeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                    @Override
                                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                        Cursor cursor = (Cursor) parent.getItemAtPosition(position);
                                                        if (cursor != null) {
                                                            String _id = cursor.getString(cursor.getColumnIndex(KasebContract.Products._ID));

                                                            mNewListDialog.add(_id);
                                                            mNewList.remove(_id);

                                                            int addListProductNumber = mNewList.size();

                                                            mSelection = new String[(addListProductNumber > 0 ? addListProductNumber : 1)];
                                                            for (int i = 0; i < addListProductNumber; i++) {
                                                                mSelection[i] = mNewList.get(i);
                                                            }

                                                            if (mSelection[0] == null)
                                                                mSelection[0] = "-1";

                                                            mWhereStatement = KasebContract.Products._ID + " IN (" +
                                                                    Utility.makePlaceholders((addListProductNumber > 0 ? addListProductNumber : 1)) + ")";

                                                            modeList = (ListView) findViewById(R.id.listview_sale_items);
                                                            mProductAdapter = new CostSaleProductAdapter(
                                                                    DetailSaleInsert.this,
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

                                                dialog.dismiss();
                                            }
                                        }
                                        cursor.close();
                                    }
                                });

                                builder.setView(modeList);
                                dialog = builder.create();

//                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                                dialog.show();
                                //endregion case 0
                                break;
                            //region case 1 ADD PAYMENT
                            case 1:
                                dialog = Utility.dialogBuilder(DetailSaleInsert.this
                                        , R.layout.dialog_add_payment_for_sale
                                        , R.string.fab_add_payment);

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

                                        dialog.dismiss();
                                    }
                                });

                                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                    @Override
                                    public void onDismiss(DialogInterface dialog) {
                                        paymentMethodRowListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                Toast.makeText(DetailSaleInsert.this, "hello", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                });
                                dialog.show();
                                //endregion case 1
                                break;
                            //region case 2 ADD TAX
                            case 2:
                                dialog = Utility.dialogBuilder(DetailSaleInsert.this
                                        , R.layout.dialog_add_tax_for_sale
                                        , R.string.fab_add_tax);

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
                                        ListView taxTypeRowListView = (ListView) findViewById(R.id.listview_sale_tax_discount);

                                        taxTypeRowListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                Toast.makeText(getBaseContext(), "10", Toast.LENGTH_SHORT).show();
                                            }
                                        });

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

                                        taxSummary.setText(sTotalTax.toString());
                                        discountSummary.setText(sTotalDiscount.toString());

                                        dialog.dismiss();
                                    }
                                });

                                dialog.show();
                                //endregion case 2
                                break;
                            //region case 3 ADD CUSTOMER
                            case 3:
                                mProjection = new String[]{
                                        KasebContract.Customers._ID,
                                        KasebContract.Customers.COLUMN_FIRST_NAME,
                                        KasebContract.Customers.COLUMN_LAST_NAME,
                                        KasebContract.Customers.COLUMN_STATE_ID};

                                modeList = new ListView(DetailSaleInsert.this);
                                mCAdapter = new CustomerAdapter(
                                        DetailSaleInsert.this,
                                        getContentResolver().query(
                                                KasebContract.Customers.CONTENT_URI,
                                                mProjection,
                                                null,
                                                null,
                                                null),
                                        0);
                                modeList.setAdapter(mCAdapter);

                                builder = new AlertDialog.Builder(DetailSaleInsert.this);
                                builder.setTitle(R.string.fab_add_customer);

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