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
        //region set Total Sales and recievables
        String[] projection = new String[]{"sum(" + KasebContract.DetailSale.TABLE_NAME + "." + KasebContract.DetailSale.COLUMN_TOTAL_DUE + ") as total"};
        String[] recievProj = new String[]{"sum(" + KasebContract.DetailSale.TABLE_NAME + "." + KasebContract.DetailSale.COLUMN_TOTAL_PAID + ") as total"};
        String selection = KasebContract.Sales.TABLE_NAME + "." + KasebContract.Sales.COLUMN_IS_DELETED + " =? ";
        String[] selectArg = new String[]{"0"};
        TextView totalSales = (TextView) rootView.findViewById(R.id.kaseb_dashboard_sale_total);
        TextView totalRecievable = (TextView) rootView.findViewById(R.id.kaseb_dashboard_others_total_recievables);

        Cursor recievCurs = null;
        cursor = getContext().getContentResolver().query(KasebContract.Sales.saleDetailSaleJoin(), projection, selection, selectArg, null);
        recievCurs = getContext().getContentResolver().query(KasebContract.Sales.saleDetailSaleJoin(), recievProj, selection, selectArg, null);

        assert cursor != null;
        if (cursor.moveToFirst())
            totalSales.setText(cursor.getString(0) == null ? "0" : cursor.getString(0));
        assert recievCurs != null;
        if(recievCurs.moveToFirst())
            totalRecievable.setText(recievCurs.getString(0)==null?cursor.getString(0):String.valueOf(cursor.getLong(0) - recievCurs.getLong(0)));

        //endregion
        //region defining views
        TextView totalSalesGold = (TextView) rootView.findViewById(R.id.kaseb_dashboard_Sale_total_gold);
        TextView totalSalesSilver = (TextView) rootView.findViewById(R.id.kaseb_dashboard_Sale_total_silver);
        TextView totalSalesBronze = (TextView) rootView.findViewById(R.id.kaseb_dashboard_Sale_total_bronze);
        TextView totalSalesInStart = (TextView) rootView.findViewById(R.id.kaseb_dashboard_Sale_total_instart);
        TextView totalRecievableGold = (TextView) rootView.findViewById(R.id.kaseb_dashboard_others_total_recievables_gold);
        TextView totalRecievableSilver = (TextView) rootView.findViewById(R.id.kaseb_dashboard_others_total_recievables_silver);
        TextView totalRecievableBronze = (TextView) rootView.findViewById(R.id.kaseb_dashboard_others_total_recievables_bronze);
        TextView totalRecievableInStart = (TextView) rootView.findViewById(R.id.kaseb_dashboard_others_total_recievables_instart);
        //endregion defining views
        //region defining variables
        selection = KasebContract.Sales.TABLE_NAME + "." + KasebContract.Sales.COLUMN_IS_DELETED + " =? " + " AND " +
                KasebContract.Sales.COLUMN_CUSTOMER_ID + " =?";
        //iterate for 4 main customer state. i represent state id of gold,silver,bronze and in start
        Long[] totalDue = new Long[5];
        Long[] totalPaid = new Long[5];
        Cursor customerCurs = null;
        //endregion defining variables
        //region set memberships due and recievables
        for (int i = 0; i < 5; i++) {
            totalDue[i] = 0l;
            totalPaid[i] = 0l;
            customerCurs = getContext().getContentResolver().query(KasebContract.Customers.CONTENT_URI, null,
                    KasebContract.Customers.COLUMN_STATE_ID + " = ?", new String[]{String.valueOf(i)}, null);
            assert customerCurs != null;
            while (customerCurs.moveToNext()) {
                selectArg = new String[]{"0", customerCurs.getString(customerCurs.getColumnIndex(KasebContract.Customers._ID))};
                cursor = getContext().getContentResolver().query(KasebContract.Sales.saleDetailSaleJoin(), projection, selection, selectArg, null);
                recievCurs = getContext().getContentResolver().query(KasebContract.Sales.saleDetailSaleJoin(), recievProj, selection, selectArg, null);
                if (cursor.moveToFirst())
                    totalDue[i] += cursor.getLong(0);
                if (recievCurs.moveToFirst())
                    totalPaid[i] += recievCurs.getLong(0);
            }
        }
        //region setViews of membership
        totalSalesGold.setText(totalDue[0] ==null?"0":String.valueOf(totalDue[1]));
        totalSalesSilver.setText(totalDue[1] == null ? "0" : String.valueOf(totalDue[2]));
        totalSalesBronze.setText(totalDue[2] ==null?"0":String.valueOf(totalDue[3]));
        totalSalesInStart.setText(totalDue[3] ==null?"0":String.valueOf(totalDue[4]));
        totalRecievableGold.setText(totalDue[0]==null?"0":String.valueOf(totalDue[1] - totalPaid[1]));
        totalRecievableSilver.setText(totalDue[1] ==null?"0":String.valueOf(totalDue[2] - totalPaid[2]));
        totalRecievableBronze.setText(totalDue[2] ==null?"0":String.valueOf(totalDue[3] - totalPaid[3]));
        totalRecievableInStart.setText(totalDue[3] ==null?"0":String.valueOf(totalDue[4] - totalPaid[4]));
        //endregion

        recievCurs.close();
        customerCurs.close();
        cursor.close();
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }


}
