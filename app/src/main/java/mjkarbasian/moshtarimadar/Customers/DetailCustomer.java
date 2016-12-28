package mjkarbasian.moshtarimadar.Customers;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
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
import android.widget.EditText;

import mjkarbasian.moshtarimadar.Adapters.DetailCustomerAdapter;
import mjkarbasian.moshtarimadar.Data.KasebContract;
import mjkarbasian.moshtarimadar.Helpers.Utility;
import mjkarbasian.moshtarimadar.R;


public class DetailCustomer extends AppCompatActivity {
    Toolbar mToolbar;

    String[] mProjection;
    String nameCustomer;
    String familyCustomer;
    int mCustomerId;
    int mStateId;

    EditText customerFirstName;
    EditText customerLastName;
    EditText customerBirthDay;
    EditText customerPhoneMobile;
    EditText customerDescription;
    EditText customerEmail;
    EditText customerPhoneWork;
    EditText customerPhoneHome;
    EditText customerPhoneOther;
    EditText customerPhoneFax;
    EditText customerAddressCountry;
    EditText customerAddressCity;
    EditText customerAddressStreet;
    EditText customerAddressPostalCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_customer);
        mToolbar = (Toolbar) findViewById(R.id.customer_detail_toolbar);
        setSupportActionBar(mToolbar);

        if (!(Utility.getLocale(this).equals("IR"))) {
            mToolbar.setNavigationIcon(R.drawable.arrow_left);
        } else {
            mToolbar.setNavigationIcon(R.drawable.arrow_right);
        }

        Intent intent = getIntent();
        Uri uri = intent.getData();
        mProjection = new String[]{
                KasebContract.Customers._ID,
                KasebContract.Customers.COLUMN_FIRST_NAME,
                KasebContract.Customers.COLUMN_LAST_NAME,
                KasebContract.Customers.COLUMN_STATE_ID};

        Cursor customerCursor = getBaseContext().getContentResolver().query(
                uri,
                mProjection,
                null,
                null,
                null
        );

        if (customerCursor != null) {
            if (customerCursor.moveToFirst()) {
                mCustomerId = customerCursor.getInt(customerCursor.getColumnIndex(KasebContract.Customers._ID));
                nameCustomer = customerCursor.getString(customerCursor.getColumnIndex(KasebContract.Customers.COLUMN_FIRST_NAME));
                familyCustomer = customerCursor.getString(customerCursor.getColumnIndex(KasebContract.Customers.COLUMN_LAST_NAME));
                mStateId = customerCursor.getInt(customerCursor.getColumnIndex(KasebContract.Customers.COLUMN_STATE_ID));

                ((CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_layout))
                        .setTitle(nameCustomer + "  " + familyCustomer);
            }
        }

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText(getResources().getString(R.string.customer_tab_info)));
        tabLayout.addTab(tabLayout.newTab().setText(getResources().getString(R.string.customer_tab_dash)));
        tabLayout.addTab(tabLayout.newTab().setText(getResources().getString(R.string.customer_tab_bill)));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final DetailCustomerAdapter adapter = new DetailCustomerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount(), this, uri);
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
        customerCursor.close();
    }

    @Override
    protected void onStop() {
        super.onStop();
        this.finish();
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
        // make update for membership selection
        Uri uri = KasebContract.Customers.CONTENT_URI;
        ContentValues contentValues = new ContentValues();
        String key = KasebContract.Customers.COLUMN_STATE_ID;
        int value = 0;
        String selection = KasebContract.State.COLUMN_STATE_COLOR + " = ? ";
        String[] selectArg;
        Cursor cursor;
        String updateSelect = KasebContract.Customers._ID + " = ?";
        String[] updSelArg = new String[]{String.valueOf(mCustomerId)};
        int updatedRow;

        //region Switch Case for state type
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                break;
            case R.id.edit:

                customerFirstName = (EditText) findViewById(R.id.customer_first_name);
                customerLastName = (EditText) findViewById(R.id.customer_last_name);
                customerBirthDay = (EditText) findViewById(R.id.customer_birth_day);
                customerPhoneMobile = (EditText) findViewById(R.id.customer_phone_mobile);
                customerDescription = (EditText) findViewById(R.id.customer_description);
                customerEmail = (EditText) findViewById(R.id.customer_email_txt);
                customerPhoneWork = (EditText) findViewById(R.id.customer_phone_work);
                customerPhoneHome = (EditText) findViewById(R.id.customer_phone_home);
                customerPhoneOther = (EditText) findViewById(R.id.customer_phone_other);
                customerPhoneFax = (EditText) findViewById(R.id.customer_phone_fax);
                customerAddressCountry = (EditText) findViewById(R.id.customer_address_country);
                customerAddressCity = (EditText) findViewById(R.id.customer_address_city);
                customerAddressStreet = (EditText) findViewById(R.id.customer_address_street);
                customerAddressPostalCode = (EditText) findViewById(R.id.customer_address_postal_code);

                customerFirstName.setEnabled(true);
                customerLastName.setEnabled(true);
                customerBirthDay.setEnabled(true);
                customerPhoneMobile.setEnabled(true);
                customerDescription.setEnabled(true);
                customerEmail.setEnabled(true);
                customerPhoneWork.setEnabled(true);
                customerPhoneHome.setEnabled(true);
                customerPhoneOther.setEnabled(true);
                customerPhoneFax.setEnabled(true);
                customerAddressCountry.setEnabled(true);
                customerAddressCity.setEnabled(true);
                customerAddressStreet.setEnabled(true);
                customerAddressPostalCode.setEnabled(true);

                break;
            case R.id.gold_member:
                selectArg = new String[]{String.valueOf(Color.rgb(255, 215, 0))};
                cursor = getContentResolver().query(KasebContract.State.CONTENT_URI,
                        new String[]{KasebContract.State._ID, KasebContract.State.COLUMN_STATE_COLOR}, selection,
                        selectArg, null);
                if (cursor.moveToFirst())
                    value = cursor.getInt(cursor.getColumnIndex(KasebContract.State._ID));
                contentValues.put(key, value);
                updatedRow = getContentResolver().update(uri, contentValues, updateSelect, updSelArg);
                break;
            case R.id.silver_member:
                selectArg = new String[]{String.valueOf(Color.rgb(192, 192, 192))};
                cursor = getContentResolver().query(KasebContract.State.CONTENT_URI,
                        new String[]{KasebContract.State._ID, KasebContract.State.COLUMN_STATE_COLOR}, selection,
                        selectArg, null);
                if (cursor.moveToFirst())
                    value = cursor.getInt(cursor.getColumnIndex(KasebContract.State._ID));
                contentValues.put(key, value);
                updatedRow = getContentResolver().update(uri, contentValues, updateSelect, updSelArg);
                break;
            case R.id.bronze_member:
                selectArg = new String[]{String.valueOf(Color.rgb(218, 165, 32))};
                cursor = getContentResolver().query(KasebContract.State.CONTENT_URI,
                        new String[]{KasebContract.State._ID, KasebContract.State.COLUMN_STATE_COLOR}, selection,
                        selectArg, null);
                if (cursor.moveToFirst())
                    value = cursor.getInt(cursor.getColumnIndex(KasebContract.State._ID));
                contentValues.put(key, value);
                updatedRow = getContentResolver().update(uri, contentValues, updateSelect, updSelArg);
                break;
            case R.id.non_member:
                selectArg = new String[]{String.valueOf(Color.rgb(176, 224, 230))};
                cursor = getContentResolver().query(KasebContract.State.CONTENT_URI,
                        new String[]{KasebContract.State._ID, KasebContract.State.COLUMN_STATE_COLOR}, selection,
                        selectArg, null);
                if (cursor.moveToFirst())
                    value = cursor.getInt(cursor.getColumnIndex(KasebContract.State._ID));
                contentValues.put(key, value);
                updatedRow = this.getContentResolver().update(uri, contentValues, updateSelect, updSelArg);
                break;
            default:
                return true;
        }
        //endregion Switch Case for state type

        return super.onOptionsItemSelected(item);
    }
}