package mjkarbasian.moshtarimadar.Products;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import mjkarbasian.moshtarimadar.Others.CostSaleProductList;
import mjkarbasian.moshtarimadar.Others.DrawerActivity;
import mjkarbasian.moshtarimadar.R;
import tourguide.tourguide.TourGuide;

public class Products extends DrawerActivity {

    //region declare values
    CostSaleProductList costsSaleProductFragment = new CostSaleProductList();
    Bundle productsBundle = new Bundle();
    Fragment productInsert = new ProductInsert();
    TourGuide mTourGuideProducts;

    android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
    private String mQuery;
    //endregion declare values

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setDefaultKeyMode(DEFAULT_KEYS_SEARCH_LOCAL);

        productsBundle.putString("witchActivity", "product");
        costsSaleProductFragment.setArguments(productsBundle);

        try {
            if (getIntent().getStringExtra("whichTour").equals("1")) {
                productsBundle.putString("whichTour", getIntent().getStringExtra("whichTour"));
            }
        } catch (Exception e) {
        }

        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction()))
            handleIntent(intent);
        else
            fragmentManager.beginTransaction().replace(R.id.container, costsSaleProductFragment, "CostSaleProductList").commit();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            doMySearch(query);
        }
    }

    private void doMySearch(String query) {
        CostSaleProductList queryFragment = (CostSaleProductList) fragmentManager.findFragmentByTag("CostSaleProductList");
        queryFragment.getSearchQuery(query);
    }

    public void fab_cost_sale_product(View v) {

        try {
            if (productsBundle.getString("whichTour").equals("1")) {
                mTourGuideProducts.cleanUp();
                productInsert.setArguments(productsBundle);
            }
        } catch (Exception e) {
        }

        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_in_up, R.anim.slide_out_down, R.anim.slide_in_down, R.anim.slide_out_up);
        fragmentTransaction.replace(R.id.container, productInsert);
        fragmentTransaction.addToBackStack(null);
        int callBackStack = fragmentTransaction.commit();
    }

    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_products, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(this.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search_button).getActionView();
        searchView.setQueryHint(getResources().getString(R.string.search_title_products));
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {
                doMySearch(newText);
                // this is your adapter that will be filtered
                return true;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                mQuery = query;
                //Here u can get the value "query" which is entered in the search box.
                return (query != null) ? true : false;
            }
        };
        searchView.setOnQueryTextListener(queryTextListener);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        CostSaleProductList sortFragment = (CostSaleProductList) fragmentManager.findFragmentByTag("CostSaleProductList");
        switch (item.getItemId()) {
            case R.id.menu_sort_code:
                sortFragment.getSortOrder(R.id.menu_sort_code);
                break;
            case R.id.menu_sort_name:
                sortFragment.getSortOrder(R.id.menu_sort_name);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

    public void setValueForTourGuide(TourGuide mTourGuide) {
        mTourGuideProducts = mTourGuide;
    }
}
