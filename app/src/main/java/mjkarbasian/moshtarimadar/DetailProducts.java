package mjkarbasian.moshtarimadar;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class DetailProducts extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_products);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_detail_product);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void fab_detail_product(View view) {
        Toast.makeText(this, getApplicationContext().getResources().getString(R.string.detail_product_action_description), Toast.LENGTH_LONG).show();

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
