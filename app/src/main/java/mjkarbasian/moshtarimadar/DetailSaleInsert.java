package mjkarbasian.moshtarimadar;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import mjkarbasian.moshtarimadar.Data.KasebContract;
import mjkarbasian.moshtarimadar.adapters.DetailSaleItems;
import mjkarbasian.moshtarimadar.adapters.DetailSalePayment;
import mjkarbasian.moshtarimadar.adapters.DetailSaleTax;
import mjkarbasian.moshtarimadar.helper.Samples;
import mjkarbasian.moshtarimadar.helper.Utility;

public class DetailSaleInsert extends AppCompatActivity {
    ListView itemsList;
    ListView paymentList;
    ListView offTaxList;
    FloatingActionButton fab;
    DetailSaleItems itemsAdapter;
    DetailSalePayment paymentAdapter;
    DetailSaleTax taxAdapter;
    AlertDialog.Builder builder;
    ListView modeList;
    String[] stringArray;
    ArrayAdapter<String> modeAdapter;
    Dialog dialog;
    Button dialogButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_sale);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        String saleCode = intent.getStringExtra("saleCode");

        itemsList = (ListView) findViewById(R.id.listview_sale_items);
        paymentList = (ListView) findViewById(R.id.listview_sale_payments);
        offTaxList = (ListView) findViewById(R.id.listview_sale_tax_discount);

        itemsAdapter = new DetailSaleItems(this, saleCode);
        itemsList.setAdapter(itemsAdapter);

        paymentAdapter = new DetailSalePayment(this, saleCode);
        paymentList.setAdapter(paymentAdapter);

        taxAdapter = new DetailSaleTax(this, saleCode);
        offTaxList.setAdapter(taxAdapter);

        //dynamically change cards height but it must modify
        CardView itemCard = (CardView) findViewById(R.id.card_detail_sale_items_list);
        ViewGroup.LayoutParams layoutParams = itemCard.getLayoutParams();
        layoutParams.height = Utility.dipConverter(itemsList.getCount() * 48 + 30, this);//this is in pixels Must item height recognize dynamically
        itemCard.setLayoutParams(layoutParams);

//        //dynamically change cards height but it must modify
        CardView paymentCard = (CardView) findViewById(R.id.card_detail_sale_payments);
        ViewGroup.LayoutParams layoutParamsPaymet = paymentCard.getLayoutParams();
        layoutParamsPaymet.height = Utility.dipConverter(paymentList.getCount() * 48 + 30, this);//this is in pixels Must item height recognize dynamically
        paymentCard.setLayoutParams(layoutParamsPaymet);

        //dynamically change cards height but it must modify
        CardView taxCard = (CardView) findViewById(R.id.card_detail_sale_tax_discounts);
        ViewGroup.LayoutParams layoutParamsTax = paymentCard.getLayoutParams();
        layoutParamsPaymet.height = Utility.dipConverter(offTaxList.getCount() * 32 + 30, this);//this is in pixels Must item height recognize dynamically
        taxCard.setLayoutParams(layoutParamsTax);

//        //Fill in Sale summary
        TextView cutomerName = (TextView) findViewById(R.id.card_detail_sale_customer_code);
        TextView totalAmount = (TextView) findViewById(R.id.card_detail_sale_summary_total_amount);
        TextView tax = (TextView) findViewById(R.id.card_detail_sale_summary_tax);
        TextView discount = (TextView) findViewById(R.id.card_detail_sale_summary_discount);
        TextView finalAmount = (TextView) findViewById(R.id.card_detail_sale_summary_final_amount);
        TextView payed = (TextView) findViewById(R.id.card_detail_sale_summary_payed);
        TextView balance = (TextView) findViewById(R.id.card_detail_sale_summary_balance);

        int index = Samples.sales.get(1).indexOf(saleCode);
        cutomerName.setText(Samples.sales.get(2).get(index));
        totalAmount.setText(Utility.formatPurchase(this, Utility.DecimalSeperation(this, Double.parseDouble(Samples.saleSummary.get(1).get(index)))));
        tax.setText(Utility.formatPurchase(this, Utility.DecimalSeperation(this, Double.parseDouble(Samples.saleSummary.get(2).get(index)))));
        discount.setText(Utility.formatPurchase(this, Utility.DecimalSeperation(this, Double.parseDouble(Samples.saleSummary.get(3).get(index)))));
        finalAmount.setText(Utility.formatPurchase(this, Utility.DecimalSeperation(this, Double.parseDouble(Samples.saleSummary.get(4).get(index)))));
        payed.setText(Utility.formatPurchase(this, Utility.DecimalSeperation(this, Double.parseDouble(Samples.saleSummary.get(5).get(index)))));
        balance.setText(Utility.formatPurchase(this, Utility.DecimalSeperation(this, Double.parseDouble(Samples.saleSummary.get(6).get(index)))));

        fab = (FloatingActionButton) findViewById(R.id.fab_detail_sale);
//        fab.setVisibility(View.INVISIBLE);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_detail_sale, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case R.id.print_button:
                Toast.makeText(this, getApplicationContext().getResources().getString(R.string.print_detail_sale_action_description), Toast.LENGTH_LONG).show();
                break;
            case R.id.edit: {
                Toast.makeText(this, getApplicationContext().getResources().getString(R.string.edit_detail_sale_action_description), Toast.LENGTH_LONG).show();
                fab.setVisibility(View.VISIBLE);
                break;
            }
            case R.id.save:
                Toast.makeText(this, getApplicationContext().getResources().getString(R.string.save_detail_sale_action_description), Toast.LENGTH_LONG).show();

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void fab_detail_sale(View v) {

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
                        stringArray = new String[] { getResources().getString(R.string.sample_product_name),
                                getResources().getString(R.string.sample_product_name_2)
                        ,getResources().getString(R.string.sample_product_name_3)
                        ,getResources().getString(R.string.sample_product_name_4)};
                        modeAdapter = new ArrayAdapter<String>(DetailSaleInsert.this,
                                android.R.layout.simple_list_item_1, android.R.id.text1, stringArray);
                        modeList.setAdapter(modeAdapter);

                        builder.setView(modeList);
                        dialog = builder.create();

                        dialog.show();
                        break;
                    case 1:
                        dialog = new Dialog(DetailSaleInsert.this);
                        dialog.setContentView(R.layout.dialog_add_payment_for_sale);
                        dialog.setTitle(R.string.fab_add_payment);

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
                        dialog = new Dialog(DetailSaleInsert.this);
                        dialog.setContentView(R.layout.dialog_add_tax_for_sale);
                        dialog.setTitle(R.string.fab_add_tax);

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
                        dialog = new Dialog(DetailSaleInsert.this);
                        dialog.setContentView(R.layout.dialog_add_discount_for_sale);
                        dialog.setTitle(R.string.fab_add_discount);

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
                        dialog = new Dialog(DetailSaleInsert.this);
                        dialog.setContentView(R.layout.dialog_add_customer_for_sale);
                        dialog.setTitle(R.string.fab_add_customer);

                        dialogButton = (Button) dialog.findViewById(R.id.add_customer_for_sale_button1);
                        dialogButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });

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
