package mjkarbasian.moshtarimadar.adapters;

import android.content.Context;
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

    public DetailCustomerAdapter(FragmentManager fm, int NumOfTabs,Context context,Integer position) {
        super(fm);
        mContext = context;
        this.mNumOfTabs = NumOfTabs;
        customerPosition = position;

    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                DetailCustomerInfo tab1 = new DetailCustomerInfo(customerPosition);
                return tab1;
            case 1:
                DetailCustomerDash tab2 = new DetailCustomerDash(customerPosition);
                return tab2;
            case 2:
                DetailCustomerBill tab3 = new DetailCustomerBill(customerPosition);
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