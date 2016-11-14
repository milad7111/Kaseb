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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import mjkarbasian.moshtarimadar.Data.KasebContract;
import mjkarbasian.moshtarimadar.adapters.TypesSettingAdapter;
import mjkarbasian.moshtarimadar.helper.Utility;

public class DetailSaleInsert extends AppCompatActivity {
    EditText saleCode;
    EditText saleDate;
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

    TextView customerInfo;

    ContentValues saleValues = new ContentValues();
    ContentValues detailsaleValues = new ContentValues();
    ContentValues[] itemsValuesArray;
    ContentValues itemsValues;
    ContentValues[] paymentValuesArray;
    ContentValues paymentValues;
    ContentValues[] taxValuesArray;
    ContentValues taxValues;

    FloatingActionButton fab;
    AlertDialog.Builder builder;
    ListView modeList;
    String[] stringArray;
    ArrayAdapter<String> modeAdapter;
    Dialog dialog;
    Button dialogButton;
    Spinner paymentMethod;
    Uri insertUri;
    private static Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_sale_insert);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //Fill in Sale summary
        customerId = 1;
        mContext = this;
        customerInfo = (TextView) findViewById(R.id.detail_sales_info_customer);
        TextView totalAmount = (TextView) findViewById(R.id.card_detail_sale_summary_total_amount);
        TextView tax = (TextView) findViewById(R.id.card_detail_sale_summary_tax);
        TextView discount = (TextView) findViewById(R.id.card_detail_sale_summary_discount);
        TextView finalAmount = (TextView) findViewById(R.id.card_detail_sale_summary_final_amount);
        TextView payed = (TextView) findViewById(R.id.card_detail_sale_summary_payed);
        TextView balance = (TextView) findViewById(R.id.card_detail_sale_summary_balance);

        fab = (FloatingActionButton) findViewById(R.id.fab_detail_sale_insert);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
                customerInfo.setEnabled(false);

                EditText saleDate = (EditText) findViewById(R.id.detail_sales_info_sale_date);

                itemNumber = 2;
                subTotal = 10;
                totalDiscount = 5;
                totalDue = 10;
                totalTax = 2;
                totalPaid = 5;

                detailsaleValues.put(KasebContract.DetailSale.COLUMN_DATE, saleDate.getText().toString());
                detailsaleValues.put(KasebContract.DetailSale.COLUMN_IS_BALANCED, 0);
                detailsaleValues.put(KasebContract.DetailSale.COLUMN_ITEMS_NUMBER, itemNumber);
                detailsaleValues.put(KasebContract.DetailSale.COLUMN_SALE_ID, insertUri.getLastPathSegment());
                detailsaleValues.put(KasebContract.DetailSale.COLUMN_SUB_TOTAL, subTotal);
                detailsaleValues.put(KasebContract.DetailSale.COLUMN_TOTAL_DISCOUNT, totalDiscount);
                detailsaleValues.put(KasebContract.DetailSale.COLUMN_TOTAL_DUE, totalDue);
                detailsaleValues.put(KasebContract.DetailSale.COLUMN_TOTAL_PAID, totalPaid);
                detailsaleValues.put(KasebContract.DetailSale.COLUMN_TOTAL_TAX, totalTax);

                insertUri = this.getContentResolver().insert(
                        KasebContract.DetailSale.CONTENT_URI,
                        detailsaleValues
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
        popup.getMenu().add(0, 3, 0, R.string.fab_add_discount);
        popup.getMenu().add(0, 4, 0, R.string.fab_add_customer);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case 0:
                        builder = new AlertDialog.Builder(DetailSaleInsert.this);
                        builder.setTitle(R.string.fab_add_product);

                        modeList = new ListView(DetailSaleInsert.this);
                        stringArray = new String[]{
                                getString(R.string.sample_product_name_7)
                                , getString(R.string.sample_product_name_8)
                                , getString(R.string.sample_product_name_9)
                                , getString(R.string.sample_product_name_10)};
                        modeAdapter = new ArrayAdapter<>(DetailSaleInsert.this,
                                android.R.layout.simple_list_item_1, android.R.id.text1, stringArray);
                        modeList.setAdapter(modeAdapter);

                        modeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position,
                                                    long id) {
                                Toast.makeText(DetailSaleInsert.this, position + "", Toast.LENGTH_SHORT).show();
                            }
                        });

                        builder.setView(modeList);
                        dialog = builder.create();

                        dialog.show();
                        break;
                    case 1:
                        dialog = Utility.dialogBuilder(DetailSaleInsert.this
                                , R.layout.dialog_add_payment_for_sale
                                , R.string.fab_add_payment);

                        paymentMethod = (Spinner) dialog.findViewById(R.id.input_payment_method_spinner);
                        Cursor cursor = getContentResolver().query(KasebContract.PaymentMethods.CONTENT_URI
                                ,null,null,null,null);
                        int[] toViews = {
                                android.R.id.text1
                        };
                        String[] fromColumns = {
                                KasebContract.PaymentMethods.COLUMN_PAYMENT_METHOD_POINTER
                        };

                        TypesSettingAdapter cursorAdapter = new TypesSettingAdapter(mContext,cursor,0,KasebContract.PaymentMethods.COLUMN_PAYMENT_METHOD_POINTER);
                        paymentMethod.setAdapter(cursorAdapter);

                        dialogButton = (Button) dialog.findViewById(R.id.add_payment_for_sale_button1);
                        dialogButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });

                        dialog.show();
                        break;
                    case 2:
                        dialog = Utility.dialogBuilder(DetailSaleInsert.this
                                , R.layout.dialog_add_tax_for_sale
                                , R.string.fab_add_tax);

                        dialogButton = (Button) dialog.findViewById(R.id.add_tax_for_sale_button1);
                        dialogButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });

                        dialog.show();
                        break;
                    case 3:
                        dialog = Utility.dialogBuilder(DetailSaleInsert.this
                                , R.layout.dialog_add_discount_for_sale
                                , R.string.fab_add_discount);

                        dialogButton = (Button) dialog.findViewById(R.id.add_discount_for_sale_button1);
                        dialogButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });

                        dialog.show();
                        break;
                    case 4:
                        builder = new AlertDialog.Builder(DetailSaleInsert.this);
                        builder.setTitle(R.string.fab_add_product);

                        modeList = new ListView(DetailSaleInsert.this);
                        stringArray = new String[]{
                                "John"
                                , "Joseph"
                                , "Christin"
                                , "Susan"};
                        modeAdapter = new ArrayAdapter<String>(DetailSaleInsert.this,
                                android.R.layout.simple_list_item_1, android.R.id.text1, stringArray);
                        modeList.setAdapter(modeAdapter);

                        modeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position,
                                                    long id) {
                                Toast.makeText(DetailSaleInsert.this, position + "", Toast.LENGTH_SHORT).show();
                            }
                        });

                        builder.setView(modeList);
                        dialog = builder.create();

                        dialog.show();
                        break;
                    default:
                }
                return true;
            }
        });
        popup.show();//showing popup menu
    }
}
