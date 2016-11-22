//package mjkarbasian.moshtarimadar.adapters;

//import android.content.Context;
//import android.database.Cursor;
//import android.graphics.Color;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.app.FragmentStatePagerAdapter;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.CursorAdapter;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import mjkarbasian.moshtarimadar.Data.KasebContract;
//import mjkarbasian.moshtarimadar.DetailCustomerBill;
//import mjkarbasian.moshtarimadar.DetailCustomerDash;
//import mjkarbasian.moshtarimadar.DetailCustomerInfo;
//import mjkarbasian.moshtarimadar.R;

//public class DetailCustomerAdapter extends CursorAdapter {
//
//    private LayoutInflater cursorInflater;
//    String name;
//    String stateId;
//    String statePointer;
//    String amount;
//    Cursor c;
//
//    public DetailCustomerAdapter(Context context, Cursor c, int flags) {
//        super(context, c, flags);
//        cursorInflater = (LayoutInflater) context.getSystemService(
//                Context.LAYOUT_INFLATER_SERVICE);
//    }

//    @Override
//    public View newView(Context context, Cursor cursor, ViewGroup parent) {
//        return cursorInflater.inflate(R.layout.list_item_cost_sale_product, parent, false);
//    }

//    @Override
//    public void bindView(View view, Context context, Cursor cursor) {
//        TextView textViewName = (TextView) view.findViewById(R.id.item_list_name);
//        TextView textViewCode = (TextView) view.findViewById(R.id.item_list_code);
//        TextView textViewDate = (TextView) view.findViewById(R.id.item_list_date);
//        TextView textViewAmount = (TextView) view.findViewById(R.id.item_list_amount);
//
//        //region read customer FirstName & LastName
////        stateId = cursor.getString(cursor.getColumnIndex(KasebContract.Customers.COLUMN_STATE_ID));
////
////        c = context.getContentResolver().query(
////                KasebContract.State.buildStateUri(Long.parseLong(stateId)),
////                new String[]{KasebContract.State.COLUMN_STATE_POINTER},
////                null,
////                null,
////                null);
////
////        if (c != null) {
////            if (c.moveToFirst()) {
////                statePointer = c.getString(c.getColumnIndex(KasebContract.State.COLUMN_STATE_POINTER));
////            }
////        }
//        //endregion
//
////        name = cursor.getString(cursor.getColumnIndex(KasebContract.Customers.COLUMN_FIRST_NAME)) + "   " +
////                cursor.getString(cursor.getColumnIndex(KasebContract.Customers.COLUMN_LAST_NAME));
////
////        amount = "120000";
////
////        switch (stateId) {
////            case "1": {
////                imageViewState.setColorFilter(Color.rgb(255, 223, 0));
////                break;
////            }
////            case "2": {
////                imageViewState.setColorFilter(Color.rgb(192,192,192));
////                break;
////            }
////            case "3": {
////                imageViewState.setColorFilter(Color.rgb(205, 127, 50));
////                break;
////            }
////            default:
////                break;
////        }
//
////        imageViewState.setImageResource(R.drawable.star);
////        imageViewState.setColorFilter(Color.argb(10, 10, 10, 10));
////        imageViewState.setColorFilter(ContextCompat.getColor(context, R.color.cardview_light_background));
//
////        textViewName.setText(name);
////        textViewAmount.setText(amount);
//    }
//}

package mjkarbasian.moshtarimadar.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import mjkarbasian.moshtarimadar.DetailCustomerBill;
import mjkarbasian.moshtarimadar.DetailCustomerDash;
import mjkarbasian.moshtarimadar.DetailCustomerInfo;

public class DetailCustomerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    Context mContext;
    int customerPosition;
    Uri uriCursor;

    public DetailCustomerAdapter(FragmentManager fm, int NumOfTabs, Context context, Integer position, Uri uri) {
        super(fm);
        mContext = context;
        this.mNumOfTabs = NumOfTabs;
        customerPosition = position;
        uriCursor = uri;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                DetailCustomerInfo tab1 = new DetailCustomerInfo(uriCursor);
                return tab1;
            case 1:
                DetailCustomerDash tab2 = new DetailCustomerDash(uriCursor);
                return tab2;
            case 2:
                DetailCustomerBill tab3 = new DetailCustomerBill(Long.parseLong(uriCursor.getLastPathSegment()));
                return tab3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}