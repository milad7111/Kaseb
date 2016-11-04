package mjkarbasian.moshtarimadar;

import android.app.Dialog;
import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import mjkarbasian.moshtarimadar.Data.KasebContract;
import mjkarbasian.moshtarimadar.helper.Utility;

public class DetailProducts extends AppCompatActivity {

    EditText buyPrice;
    EditText quantity;
    EditText salePrice;
    EditText buyDate;
    ContentValues productHistoryValues = new ContentValues();
    private Uri insertUri;
    String productCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_products);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle bundle = getIntent().getExtras();
        productCode = bundle.getString("productCode");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_detail_product);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void fab_detail_product(View view) {
        final Dialog dialog = Utility.dialogBuilder(DetailProducts.this
                , R.layout.dialog_add_product_history
                , R.string.title_add_product_history);

        buyPrice = (EditText) dialog.findViewById(R.id.add_product_history_buy_price);
        quantity = (EditText) dialog.findViewById(R.id.add_product_history_quantity);
        salePrice = (EditText) dialog.findViewById(R.id.add_product_history_sale_price);
        buyDate = (EditText) dialog.findViewById(R.id.add_product_history_buy_date);

        Button dialogButton = (Button) dialog.findViewById(R.id.add_product_history_button1);

        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productHistoryValues.put(KasebContract.ProductHistory.COLUMN_COST, buyPrice.getText().toString());
                productHistoryValues.put(KasebContract.ProductHistory.COLUMN_QUANTITY, quantity.getText().toString());
                productHistoryValues.put(KasebContract.ProductHistory.COLUMN_SALE_PRICE, salePrice.getText().toString());
                productHistoryValues.put(KasebContract.ProductHistory.COLUMN_DATE, buyDate.getText().toString());
                productHistoryValues.put(KasebContract.ProductHistory.COLUMN_PRODUCT_ID, productCode);

                insertUri = getContentResolver().insert(
                        KasebContract.ProductHistory.CONTENT_URI,
                        productHistoryValues
                );
                Toast.makeText(DetailProducts.this, getResources().getString(R.string.msg_insert_succeed), Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_detail_products, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case R.id.action_detail_product_edit:
                Toast.makeText(this, getApplicationContext().getResources().getString(R.string.edit_detail_product_action_description), Toast.LENGTH_LONG).show();
                break;
            case R.id.action_detail_product_share: {
                Toast.makeText(this, getApplicationContext().getResources().getString(R.string.share_action_description), Toast.LENGTH_LONG).show();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
