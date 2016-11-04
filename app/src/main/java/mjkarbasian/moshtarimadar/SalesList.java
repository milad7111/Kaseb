package mjkarbasian.moshtarimadar;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.text.ParseException;

import mjkarbasian.moshtarimadar.adapters.SaleAdapter;
import mjkarbasian.moshtarimadar.helper.Utility;

import static mjkarbasian.moshtarimadar.helper.Samples.sales;
import static mjkarbasian.moshtarimadar.helper.Samples.salesDue;


/**
 * A placeholder fragment containing a simple view.
 */
public class SalesList extends Fragment {
    SaleAdapter mSalesAdapter;
    ListView mListView;

    public SalesList() {
    }

    public void onCreate(Bundle savedInstanceState) {

        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab_products);
        FloatingActionButton fab2 = (FloatingActionButton) getActivity().findViewById(R.id.fab_detail_sale);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mSalesAdapter = new SaleAdapter(getActivity());
        Boolean todayCheck;
        int todayCounter = 0;
        Boolean weekCheck;
        int weekCounter = 0;
        Boolean monthCheck;
        int monthCounter = 0;
        int olderCounter = 0;

        for (String date : salesDue)
        {
            try {
              todayCheck = Utility.isToday(date);
                if(todayCheck) todayCounter++;
              weekCheck = Utility.isThisWeek(date);
                if(weekCheck && !todayCheck) weekCounter++;
              monthCheck = Utility.isThisMonth(date);
                if(monthCheck && !weekCheck && !todayCheck) monthCounter++;
                if(!monthCheck&& !weekCheck && !todayCheck) olderCounter++;
            } catch (ParseException e) {e.printStackTrace();}

        }
        if(todayCounter>0){ mSalesAdapter.addSectionHeaderItem(getString(R.string.header_today));
        for(int i=0;i< sales.get(0).size();i++)
        {
            try {
                if(Utility.isToday(sales.get(0).get(i)))mSalesAdapter.addItem(sales.get(1).get(i));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }}
        if(weekCounter>0) {
            mSalesAdapter.addSectionHeaderItem(getString(R.string.header_this_week));
            for(int i=0;i< sales.get(0).size();i++)
            {
                try {
                    if(!Utility.isToday(sales.get(0).get(i))&&Utility.isThisWeek(sales.get(0).get(i)))mSalesAdapter.addItem(sales.get(1).get(i));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }}
        if(monthCounter>0){ mSalesAdapter.addSectionHeaderItem(getString(R.string.header_this_month));
            for(int i=0;i< sales.get(0).size();i++)
            {
                try {
                    if(!Utility.isToday(sales.get(0).get(i))&&!Utility.isThisWeek(sales.get(0).get(i))&&Utility.isThisMonth(sales.get(0).get(i)))mSalesAdapter.addItem(sales.get(1).get(i));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }}
        if(olderCounter>0)
        {
        mSalesAdapter.addSectionHeaderItem(getString(R.string.header_older));
            for(int i=0;i< sales.get(0).size();i++)
            {
                try {
                    if(!Utility.isToday(sales.get(0).get(i))&&!Utility.isThisWeek(sales.get(0).get(i))&&!Utility.isThisMonth(sales.get(0).get(i)))mSalesAdapter.addItem(sales.get(1).get(i));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
            View rootView = inflater.inflate(R.layout.fragment_sales, container, false);
            mListView = (ListView) rootView.findViewById(R.id.list_view_sales);
            mListView.setAdapter(mSalesAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = null;
                if (id == 0) {

                    if (position < sales.get(1).size()) {
                        intent = new Intent(getActivity(), DetailSale.class).putExtra("saleCode", sales.get(1).get(position));
                    } else {
                        intent = new Intent(getActivity(), DetailSale.class).putExtra("saleCode", sales.get(1).get(sales.get(1).size()-1));

                    }
                    startActivity(intent);
                }
            }
        });
            return rootView;
        }
    }
