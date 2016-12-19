package mjkarbasian.moshtarimadar;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
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

    //region declare Values
    private static Context mContext;
    long customerId;
    long itemNumber;

    ListView taxTypeRowListView;
    ListView getProductListView;

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
    String[] mProjectionProductHistory;
    TextView nameCustomer;
    TextView familyCustomer;
    CostSaleProductAdapter mProductAdapter;
    ListView mListView;
    String[] mSelection;
    String mWhereStatement;
    String mWhereStatementDialog;
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
    //endregion declare Values

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_sale_insert);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //region Initialize Some Views & Values
        mContext = this;

        totalAmountSummary = (TextView) findViewById(R.id.card_detail_sale_summary_total_amount);
        taxSummary = (TextView) findViewById(R.id.card_detail_sale_summary_tax);
        discountSummary = (TextView) findViewById(R.id.card_detail_sale_summary_discount);
        finalAmountSummary = (TextView) findViewById(R.id.card_detail_sale_summary_final_amount);
        paidSummary = (TextView) findViewById(R.id.card_detail_sale_summary_payed);
        balanceSummary = (TextView) findViewById(R.id.card_detail_sale_summary_balance);
        nameCustomer = (TextView) findViewById(R.id.detail_sales_info_customer_name);
        familyCustomer = (TextView) findViewById(R.id.detail_sales_info_customer_family);

        totalAmountSummary.setText(
                Utility.formatPurchase(mContext, Utility.DecimalSeperation(mContext, 0)));
        taxSummary.setText(
                Utility.formatPurchase(mContext, Utility.DecimalSeperation(mContext, 0)));
        discountSummary.setText(
                Utility.formatPurchase(mContext, Utility.DecimalSeperation(mContext, 0)));
        finalAmountSummary.setText(
                Utility.formatPurchase(mContext, Utility.DecimalSeperation(mContext, 0)));
        paidSummary.setText(
                Utility.formatPurchase(mContext, Utility.DecimalSeperation(mContext, 0)));
        balanceSummary.setText(
                Utility.formatPurchase(mContext, Utility.DecimalSeperation(mContext, 0)));

        fab = (FloatingActionButton) findViewById(R.id.fab_detail_sale_insert);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //endregion

        mWhereStatementDialog = KasebContract.Products._ID + " = ? ";

        mProjection = new String[]{
                KasebContract.Products._ID,
                KasebContract.Products.COLUMN_PRODUCT_NAME,
                KasebContract.Products.COLUMN_PRODUCT_CODE};

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
                mNotChooseProductList.add(mCursor.getString(mCursor.getColumnIndex(KasebContract.Products._ID)));
                mCursor.moveToNext();
            }

        mCursor.close();

        paymentMethodRowListView = (ListView) findViewById(R.id.listview_sale_payments);
        taxTypeRowListView = (ListView) findViewById(R.id.listview_sale_tax_discount);
        getProductListView = (ListView) findViewById(R.id.listview_sale_items);
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

                //region SetValues
                EditText saleCode = (EditText) findViewById(R.id.detail_sales_info_sale_code);
                //endregion

                //region Check
                //endregion

                //region Insert Sale
                saleValues.put(KasebContract.Sales.COLUMN_CUSTOMER_ID, customerId);
                saleValues.put(KasebContract.Sales.COLUMN_IS_DELETED, 0);
                saleValues.put(KasebContract.Sales.COLUMN_SALE_CODE, saleCode.getText().toString());

                insertUri = this.getContentResolver().insert(
                        KasebContract.Sales.CONTENT_URI,
                        saleValues
                );

                saleCode.setEnabled(false);
                //endregion

                //region Insert DetailSale
                EditText saleDate = (EditText) findViewById(R.id.detail_sales_info_sale_date);

                itemNumber = getProductListView.getAdapter().getCount();

                detailSaleValues.put(KasebContract.DetailSale.COLUMN_DATE, saleDate.getText().toString());
                detailSaleValues.put(KasebContract.DetailSale.COLUMN_IS_BALANCED, 0);
                detailSaleValues.put(KasebContract.DetailSale.COLUMN_ITEMS_NUMBER, itemNumber);
                detailSaleValues.put(KasebContract.DetailSale.COLUMN_SALE_ID, insertUri.getLastPathSegment());
                detailSaleValues.put(KasebContract.DetailSale.COLUMN_SUB_TOTAL, sTotalAmount);
                detailSaleValues.put(KasebContract.DetailSale.COLUMN_TOTAL_DISCOUNT, sTotalDiscount);
                detailSaleValues.put(KasebContract.DetailSale.COLUMN_TOTAL_DUE, sFinalAmount);
                detailSaleValues.put(KasebContract.DetailSale.COLUMN_TOTAL_PAID, sPaidAmount);
                detailSaleValues.put(KasebContract.DetailSale.COLUMN_TOTAL_TAX, sTotalTax);

                insertUri = this.getContentResolver().insert(
                        KasebContract.DetailSale.CONTENT_URI,
                        detailSaleValues
                );
                //endregion

                //region Insert DetailSaleProducts
                int count = mChosenProductList.size();
                itemsValuesArray = new ContentValues[count];

                for (int i = 0; i < count; i++) {
                    itemsValues = new ContentValues();

                    itemsValues.put(KasebContract.DetailSaleProducts.COLUMN_AMOUNT, mAmountOfChosenProduct.get(i));
                    itemsValues.put(KasebContract.DetailSaleProducts.COLUMN_DETAIL_SALE_ID, insertUri.getLastPathSegment());
                    itemsValues.put(KasebContract.DetailSaleProducts.COLUMN_PRODUCT_ID, mChosenProductList.get(i));
                    itemsValues.put(KasebContract.DetailSaleProducts.COLUMN_QUANTITY, mNumberOfChosenProduct.get(i));

                    itemsValuesArray[i] = itemsValues;
                }

                this.getContentResolver().bulkInsert(
                        KasebContract.DetailSaleProducts.CONTENT_URI,
                        itemsValuesArray
                );
                //endregion

                //region Insert DetailSalePayments
                count = paymentMethodIdTypeList.size();
                paymentValuesArray = new ContentValues[count];

                for (int i = 0; i < count; i++) {
                    paymentValues = new ContentValues();

                    paymentValues.put(KasebContract.DetailSalePayments.COLUMN_DUE_DATE, paymentDueDateList.get(i));
                    paymentValues.put(KasebContract.DetailSalePayments.COLUMN_DETAIL_SALE_ID, insertUri.getLastPathSegment());
                    paymentValues.put(KasebContract.DetailSalePayments.COLUMN_AMOUNT, paymentAmountList.get(i));
                    paymentValues.put(KasebContract.DetailSalePayments.COLUMN_PAYMENT_METHOD_ID, paymentMethodIdTypeList.get(i));

                    paymentValuesArray[i] = paymentValues;
                }

                this.getContentResolver().bulkInsert(
                        KasebContract.DetailSalePayments.CONTENT_URI,
                        paymentValuesArray
                );
                //endregion

                //region Insert DetailSaleTaxes
                count = taxTypesIdsList.size();
                taxValuesArray = new ContentValues[count];

                for (int i = 0; i < count; i++) {
                    taxValues = new ContentValues();

                    taxValues.put(KasebContract.DetailSaleTaxes.COLUMN_DETAIL_SALE_ID, insertUri.getLastPathSegment());
                    taxValues.put(KasebContract.DetailSaleTaxes.COLUMN_AMOUNT, taxAmountList.get(i));
                    taxValues.put(KasebContract.DetailSaleTaxes.COLUMN_TAX_TYPE_ID, taxTypesIdsList.get(i));

                    taxValuesArray[i] = taxValues;
                }

                this.getContentResolver().bulkInsert(
                        KasebContract.DetailSaleTaxes.CONTENT_URI,
                        taxValuesArray
                );
                //endregion

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

                                //region Set Adapter To Dialog
                                mProjection = new String[]{
                                        KasebContract.Products._ID,
                                        KasebContract.Products.COLUMN_PRODUCT_NAME,
                                        KasebContract.Products.COLUMN_PRODUCT_CODE};

                                mNumberOfNotChooseProduct = mNotChooseProductList.size();
                                mSelection = new String[(mNumberOfNotChooseProduct > 0 ? mNumberOfNotChooseProduct : 1)];
                                for (int i = 0; i < mNumberOfNotChooseProduct; i++) {
                                    mSelection[i] = mNotChooseProductList.get(i);
                                }

                                if (mSelection[0] == null)
                                    mSelection[0] = "-1";

                                mWhereStatement = KasebContract.Products._ID + " IN (" +
                                        Utility.makePlaceholders((mNumberOfNotChooseProduct > 0 ? mNumberOfNotChooseProduct : 1)) + ")";

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
                                //endregion Set Adapter To Dialog

                                //region ClickListener ListView Dialog
                                modeList.setOnItemClickListener(
                                        new AdapterView.OnItemClickListener() {
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

                                                    final Dialog howManyOfThat = Utility.dialogBuilder(DetailSaleInsert.this
                                                            , R.layout.dialog_add_number_of_product_for_sale
                                                            , R.string.how_many);

                                                    final EditText howManyEditText = (EditText) howManyOfThat
                                                            .findViewById(R.id.add_number_of_product_for_sale_number);

                                                    Button saveButton = (Button) howManyOfThat
                                                            .findViewById(R.id.add_number_of_product_for_sale_save);

                                                    saveButton.setOnClickListener(
                                                            new View.OnClickListener() {
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
                                                                                            final Dialog howManyOfThatForEdit = Utility.dialogBuilder(DetailSaleInsert.this
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
                                                            }

                                                    );

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
                                mCursor1 =

                                        getContentResolver()

                                                .

                                                        query(KasebContract.PaymentMethods.CONTENT_URI
                                                                , null, null, null, null);

                                cursorAdapter = new

                                        TypesSettingAdapter(mContext,
                                        mCursor1,
                                        0,
                                        KasebContract.PaymentMethods.COLUMN_PAYMENT_METHOD_POINTER);

                                paymentMethod.setAdapter(cursorAdapter);

                                paymentMethod.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()

                                {
                                    @Override
                                    public void onItemSelected(AdapterView<?> arg0, View
                                            arg1, int arg2, long arg3) {
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
//                                        Cursor mCursor4 = (Cursor) parent.getItemAtPosition(position);
//                                        paymentMethodsId =
//                                                mCursor4.getString(
//                                                        mCursor4.getColumnIndex(KasebContract.PaymentMethods._ID));
//                                        paymentMethodType =
//                                                mCursor4.getString(
//                                                        mCursor4.getColumnIndex(KasebContract.PaymentMethods.COLUMN_PAYMENT_METHOD_POINTER));
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

                                        sPaidAmount = 0l;
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

                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    }

                                });

                                dialogButton = (Button) dialog.findViewById(R.id.add_tax_for_sale_button1);
                                dialogButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
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