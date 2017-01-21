package mjkarbasian.moshtarimadar.Others;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import mjkarbasian.moshtarimadar.AboutUs.AboutUs;
import mjkarbasian.moshtarimadar.Adapters.ChooseTourAdapter;
import mjkarbasian.moshtarimadar.Costs.Costs;
import mjkarbasian.moshtarimadar.Customers.Customers;
import mjkarbasian.moshtarimadar.Dashboard;
import mjkarbasian.moshtarimadar.Debaters.Debaters;
import mjkarbasian.moshtarimadar.Helpers.Utility;
import mjkarbasian.moshtarimadar.Products.Products;
import mjkarbasian.moshtarimadar.R;
import mjkarbasian.moshtarimadar.Sales.Sales;
import mjkarbasian.moshtarimadar.Setting.MySetting;
import mjkarbasian.moshtarimadar.Setting.TourFragment;

public class DrawerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //region declare values
    DrawerLayout mDrawer;
    ListView mListViewTour;
    AlertDialog.Builder builder;
    AlertDialog dialogView;
    ArrayList tourTitle = new ArrayList<>();
    ArrayList<Integer> tourIcons = new ArrayList<>();

    android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
    Bundle tourBundle = new Bundle();
    int howManyWhichTourSelect = 0;
    private ActionBarDrawerToggle mDrawerToggle;
    //endregion declare values

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.setDrawerListener(toggle);
        mDrawerToggle = toggle;
        toggle.syncState();

        //region add title and icon to tour lists
        tourTitle.add(getBaseContext().getResources().getString(R.string.insert_customer));
        tourTitle.add(getBaseContext().getResources().getString(R.string.insert_product));
        tourTitle.add(getBaseContext().getResources().getString(R.string.insert_sale_and_print));
        tourTitle.add(getBaseContext().getResources().getString(R.string.insert_cost));
        tourTitle.add(getBaseContext().getResources().getString(R.string.change_setting));
        tourTitle.add(getBaseContext().getResources().getString(R.string.view_sale_and_print));
        tourTitle.add(getBaseContext().getResources().getString(R.string.contact_debtors));
        tourTitle.add(getBaseContext().getResources().getString(R.string.view_reports));

        tourIcons.add(R.drawable.ic_menu_account_multiple);
        tourIcons.add(R.drawable.ic_factory);
        tourIcons.add(R.drawable.sales);
        tourIcons.add(R.drawable.cost_icon);
        tourIcons.add(R.drawable.ic_menu_manage);
        tourIcons.add(R.drawable.sales);
        tourIcons.add(R.drawable.ic_menu_account_alert);
        tourIcons.add(R.drawable.report_icon);
        //endregion add title and icon to tour lists

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //region handle sharepreference
        View header = navigationView.getHeaderView(0);
        TextView kasebProfileTextView = (TextView) header.findViewById(R.id.kaseb_name_text);
        String kasebPREFERENCES = "kasebProfile";
        SharedPreferences kasebSharedPreferences = getSharedPreferences(kasebPREFERENCES, MODE_PRIVATE);

        if (kasebSharedPreferences.getString("firstName", null) != null)
            kasebProfileTextView.setText(
                    String.format("%s  %s", kasebSharedPreferences.getString("firstName", null), kasebSharedPreferences.getString("lastName", null)));
        //endregion handle sharepreference
    }

    @Override
    public void onBackPressed() {
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (mDrawer.isDrawerOpen(GravityCompat.START))
            mDrawer.closeDrawer(GravityCompat.START);
        else
            super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (mDrawerToggle.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent intent;
        if (id == R.id.nav_dashboard) {
            // Handle the camera action
            intent = new Intent(this, Dashboard.class);
            finish();
            startActivity(intent);
            Utility.setActivityTransition(this);
        }

        if (id == R.id.nav_customers) {
            // Handle the camera action
            intent = new Intent(this, Customers.class);
            finish();
            startActivity(intent);
            Utility.setActivityTransition(this);
        } else if (id == R.id.nav_debtors) {
            intent = new Intent(this, Debaters.class);
            finish();
            startActivity(intent);
            Utility.setActivityTransition(this);
        } else if (id == R.id.nav_sales) {
            intent = new Intent(this, Sales.class);
            finish();
            startActivity(intent);
            Utility.setActivityTransition(this);
        } else if (id == R.id.nav_costs) {
            intent = new Intent(this, Costs.class);
            finish();
            startActivity(intent);
            Utility.setActivityTransition(this);
        } else if (id == R.id.nav_products) {
            intent = new Intent(this, Products.class);
            finish();
            startActivity(intent);
            Utility.setActivityTransition(this);
        } else if (id == R.id.nav_setting) {
            intent = new Intent(this, MySetting.class);
            finish();
            startActivity(intent);
            Utility.setActivityTransition(this);
        } else if (id == R.id.nav_about_us) {
            intent = new Intent(this, AboutUs.class);
            finish();
            startActivity(intent);
            Utility.setActivityTransition(this);
        } else if (id == R.id.nav_take_tour) {

            //region create tour dialog
            ChooseTourAdapter mChooseTourAdapter = new ChooseTourAdapter(getBaseContext(), tourIcons, tourTitle);

            builder = new AlertDialog.Builder(DrawerActivity.this)
                    .setView(getLayoutInflater().inflate(R.layout.dialog_choose_tour, null))
                    .setNegativeButton(R.string.cancel_button, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            dialogView.dismiss();
                        }
                    }).setTitle(R.string.title_take_tour);

            dialogView = builder.create();
            dialogView.show();
            //endregion create tour dialog

            //region declare views in dialog
            mListViewTour = (ListView) dialogView.findViewById(R.id.list_view_tour_items);
            mListViewTour.setAdapter(mChooseTourAdapter);
            Utility.setHeightOfListView(mListViewTour);

            mListViewTour.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                    tourBundle.putString("whichTour", String.valueOf(position));
                    final TourFragment mTourFragment = new TourFragment();
                    mTourFragment.setArguments(tourBundle);
                    String mMessageOfSnackBar = "";

                    switch (position) {
                        case 0: { //insert_customer
                            mMessageOfSnackBar = getString(R.string.explain_tour_insert_customer);
                            break;
                        }
                        case 1: { //insert_product
                            mMessageOfSnackBar = getString(R.string.explain_tour_insert_product);
                            break;
                        }
                        case 2: { //insert_sale_and_print
                            mMessageOfSnackBar = getString(R.string.explain_tour_create_invoice);
                            break;
                        }
                        case 3: { //insert_cost
                            mMessageOfSnackBar = getString(R.string.explain_tour_insert_costs);
                            break;
                        }
                        case 4: { //change_setting
                            mMessageOfSnackBar = getString(R.string.explain_tour_change_setting);
                            break;
                        }
                        case 5: { //view_sale_and_print
                            mMessageOfSnackBar = getString(R.string.explain_tour_view_invoices_print);
                            break;
                        }
                        case 6: { //contact_debtors
                            mMessageOfSnackBar = getString(R.string.explain_tour_contact_debtors);
                            break;
                        }
                        case 7: { //view_reports
                            mMessageOfSnackBar = getString(R.string.explain_tour_dashboard);
                            break;
                        }
                    }

                    Snackbar snackbar = Snackbar
                            .make(mListViewTour, mMessageOfSnackBar,
                                    Snackbar.LENGTH_INDEFINITE).setAction(R.string.start_tour, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (position != 0)
                                        Toast.makeText(getBaseContext(), "Not Set Yet!", Toast.LENGTH_SHORT).show();
                                    else {
                                        fragmentManager.beginTransaction().replace(R.id.container, mTourFragment).commit();
                                        dialogView.dismiss();
                                    }
                                }
                            }).setActionTextColor(getResources().getColor(R.color.colorAccent));

                    View snackbarView = snackbar.getView();
                    TextView mTextView = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
                    mTextView.setTextColor(getResources().getColor(R.color.colorPrimaryLight));
                    mTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                    Typeface mTypeface = Utility.createFontForTexts(getBaseContext());
                    mTextView.setTypeface(mTypeface);
                    mTextView.setMaxLines(5);

                    snackbar.show();
                }
            });
            //endregion declare views in dialog

            Utility.setActivityTransition(this);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
}
