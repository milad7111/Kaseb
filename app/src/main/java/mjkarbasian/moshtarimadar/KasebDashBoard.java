package mjkarbasian.moshtarimadar;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import mjkarbasian.moshtarimadar.Data.KasebContract;

/**
 * Created by family on 12/30/2016.
 */
public class KasebDashBoard extends android.support.v4.app.Fragment {


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_kaseb_dashboard, container, false);
        //region set customers dashboard
        Cursor cursor = getContext().getContentResolver().query(KasebContract.Customers.CONTENT_URI, null, null, null, null);
        TextView totalCustomers = (TextView) rootView.findViewById(R.id.kaseb_dashboard_customer_qty);
        if (cursor != null) {
            totalCustomers.setText(String.valueOf(cursor.getCount()));
        }
        TextView goldCustomers = (TextView) rootView.findViewById(R.id.kaseb_dashboard_customer_qty_gold);
        cursor = getContext().getContentResolver().query(KasebContract.Customers.CONTENT_URI, null,
                KasebContract.Customers.COLUMN_STATE_ID + " = ?", new String[]{"1"}, null);
        if (cursor != null) {
            goldCustomers.setText(String.valueOf(cursor.getCount()));
        }

        TextView silverCustomers = (TextView) rootView.findViewById(R.id.kaseb_dashboard_customer_qty_silver);
        cursor = getContext().getContentResolver().query(KasebContract.Customers.CONTENT_URI, null,
                KasebContract.Customers.COLUMN_STATE_ID + " = ?", new String[]{"2"}, null);
        if (cursor != null) {
            silverCustomers.setText(String.valueOf(cursor.getCount()));
        }
        TextView bronzeCustomers = (TextView) rootView.findViewById(R.id.kaseb_dashboard_customer_qty_bronze);
        cursor = getContext().getContentResolver().query(KasebContract.Customers.CONTENT_URI, null,
                KasebContract.Customers.COLUMN_STATE_ID + " = ?", new String[]{"3"}, null);
        if (cursor != null) {
            bronzeCustomers.setText(String.valueOf(cursor.getCount()));
        }
        TextView inStartCustomers = (TextView) rootView.findViewById(R.id.kaseb_dashboard_customer_qty_instart);
        cursor = getContext().getContentResolver().query(KasebContract.Customers.CONTENT_URI, null,
                KasebContract.Customers.COLUMN_STATE_ID + " = ?", new String[]{"4"}, null);
        if (cursor != null) {
            inStartCustomers.setText(String.valueOf(cursor.getCount()));
        }
        //endregion

        //region set Total Sales
        String[] projection = new String[]{"sum(" + KasebContract.DetailSale.TABLE_NAME + "." + KasebContract.DetailSale.COLUMN_TOTAL_DUE + ") as total"};
        String selection = KasebContract.Sales.TABLE_NAME + "." + KasebContract.Sales.COLUMN_IS_DELETED + " =? ";
        String[] selectArg = new String[]{"0"};
        cursor = getContext().getContentResolver().query(KasebContract.Sales.saleDetailSaleJoin(), projection, selection, selectArg, null);
        TextView totalSales = (TextView) rootView.findViewById(R.id.kaseb_dashboard_sale_total);
        assert cursor != null;
        if (cursor.moveToFirst())
            totalSales.setText(cursor.getString(0)==null? "0":cursor.getString(0));
        //endregion
        //region set Total sales of gold members
        TextView totalSalesGold = (TextView) rootView.findViewById(R.id.kaseb_dashboard_Sale_total_gold);
        TextView totalSalesSilver = (TextView) rootView.findViewById(R.id.kaseb_dashboard_Sale_total_silver);
        TextView totalSalesBronze = (TextView) rootView.findViewById(R.id.kaseb_dashboard_Sale_total_bronze);
        TextView totalSalesInStart = (TextView) rootView.findViewById(R.id.kaseb_dashboard_Sale_total_instart);
        selection = KasebContract.Sales.TABLE_NAME + "." + KasebContract.Sales.COLUMN_IS_DELETED + " =? " + " AND "+
                KasebContract.Sales.COLUMN_CUSTOMER_ID + " =?";
        //iterate for 4 main customer state. i represent state id of gold,silver,bronze and in start
        Long[] totalDue = new Long[4];
        for (int i = 0 ; i<4 ;i++){
            totalDue[i] = 0l;
            Cursor customerCursGold = getContext().getContentResolver().query(KasebContract.Customers.CONTENT_URI, null,
                    KasebContract.Customers.COLUMN_STATE_ID + " = ?", new String[]{String.valueOf(i)}, null);
            assert customerCursGold != null;
            while (customerCursGold.moveToNext()){
           selectArg =new String[] {"0",customerCursGold.getString(customerCursGold.getColumnIndex(KasebContract.Customers._ID))};
           cursor = getContext().getContentResolver().query(KasebContract.Sales.saleDetailSaleJoin(),projection,selection,selectArg,null);
            if(cursor.moveToFirst())
            totalDue[i] += cursor.getLong(0);
       }
            }
        totalSalesGold.setText(String.valueOf(totalDue[0]));
        totalSalesSilver.setText(String.valueOf(totalDue[1]));
        totalSalesBronze.setText(String.valueOf(totalDue[2]));
        totalSalesInStart.setText(String.valueOf(totalDue[3]));
        //endregion


        TextView totalRecievable = (TextView) rootView.findViewById(R.id.kaseb_dashboard_others_total_recievables);
        TextView totalRecievableGold = (TextView) rootView.findViewById(R.id.kaseb_dashboard_others_total_recievables_gold);
        TextView totalRecievableSilver = (TextView) rootView.findViewById(R.id.kaseb_dashboard_others_total_recievables_silver);
        TextView totalRecievableBronze = (TextView) rootView.findViewById(R.id.kaseb_dashboard_others_total_recievables_bronze);
        TextView totalRecievableInStart = (TextView) rootView.findViewById(R.id.kaseb_dashboard_others_total_recievables_instart);
        cursor.close();
        return rootView;
    }
}
