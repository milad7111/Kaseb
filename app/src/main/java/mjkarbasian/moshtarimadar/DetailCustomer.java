package mjkarbasian.moshtarimadar;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import java.text.ParseException;

import mjkarbasian.moshtarimadar.adapters.DetailCustomerAdapter;
import mjkarbasian.moshtarimadar.helper.Samples;
import mjkarbasian.moshtarimadar.helper.Utility;

import static mjkarbasian.moshtarimadar.helper.Samples.salesCode;
import static mjkarbasian.moshtarimadar.helper.Samples.setSale;
import static mjkarbasian.moshtarimadar.helper.Samples.setSaleDueDate;
import static mjkarbasian.moshtarimadar.helper.Samples.setSaleFinalAmount;
import static mjkarbasian.moshtarimadar.helper.Samples.setSaleOffTaxList;
import static mjkarbasian.moshtarimadar.helper.Samples.setSalePaymentList;
import static mjkarbasian.moshtarimadar.helper.Samples.setSaleProductList;
import static mjkarbasian.moshtarimadar.helper.Samples.setSaleSummary;
import static mjkarbasian.moshtarimadar.helper.Samples.setSalesAmount;
import static mjkarbasian.moshtarimadar.helper.Samples.setSalesCode;
import static mjkarbasian.moshtarimadar.helper.Samples.setSalesCustomer;

public class DetailCustomer extends AppCompatActivity {
    Toolbar mToolbar;
    Integer customerPosiotion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_customer);
        mToolbar = (Toolbar) findViewById(R.id.customer_detail_toolbar);
        setSupportActionBar(mToolbar);
        if(!(Utility.getLocale(this).equals("IR"))){
        mToolbar.setNavigationIcon(R.drawable.arrow_left);
        }else{
            mToolbar.setNavigationIcon(R.drawable.arrow_right);
        }

        Intent intent = getIntent();
        int position = intent.getIntExtra("position", 0);
        customerPosiotion = position;

        ((CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_layout)).setTitle(this.getResources().getString(Samples.customerName[position]));
        ImageView customerAvatar = (ImageView)findViewById(R.id.image_toolbar);

        if(Samples.customerAvatar.size()==0)
        {
            customerAvatar.setImageResource(R.drawable.account);
        }
        else
        {
            if(!(Samples.customerAvatar.size()<=position)){
                customerAvatar.setImageURI(Samples.customerAvatar.get(position));}
            else {
                customerAvatar.setImageResource(R.drawable.account);
            }
        }

        if(salesCode.size()==0)
        {

            setSalesCode();
            setSaleDueDate();
            setSalesCustomer(this);
            setSalesAmount();
            setSaleProductList();
            try {
                setSalePaymentList(this);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            setSaleOffTaxList(this);
            setSaleFinalAmount(this);
            setSale();
            setSaleSummary();
        }


        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText(getResources().getString(R.string.customer_tab_info)));
        tabLayout.addTab(tabLayout.newTab().setText(getResources().getString(R.string.customer_tab_dash)));
        tabLayout.addTab(tabLayout.newTab().setText(getResources().getString(R.string.customer_tab_bill)));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final DetailCustomerAdapter adapter = new DetailCustomerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount(),this,position);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_detail_customer, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                break;
            case R.id.edit:
                Toast.makeText(this, getApplicationContext().getResources().getString(R.string.edit_action_description), Toast.LENGTH_LONG).show();
                break;
            case R.id.gold_member:
                Samples.customerMembership[customerPosiotion] = 1 ;

                break;
            case R.id.silver_member:
                Samples.customerMembership[customerPosiotion] = 2 ;
                break;
            case R.id.bronze_member:
                Samples.customerMembership[customerPosiotion] = 3 ;
                break;
            case R.id.non_member:
                Samples.customerMembership[customerPosiotion] = 4 ;
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
