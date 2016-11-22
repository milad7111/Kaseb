package mjkarbasian.moshtarimadar.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import mjkarbasian.moshtarimadar.R;
import mjkarbasian.moshtarimadar.helper.Samples;
import mjkarbasian.moshtarimadar.helper.Utility;

/**
 * Created by family on 8/25/2016.
 */
public class DetailProductPriceList extends BaseAdapter {
    String mProductCode;
    Context mContext;
    ArrayList<ArrayList<String>> mData = new ArrayList<ArrayList<String>>();

    public DetailProductPriceList(Context context, String productCode) {
        super();
        mContext = context;
        mProductCode = productCode;

        //Coloumns are productCode,Date,time,buyPrice,Price for sale,picture
        ArrayList<String> date = new ArrayList<String>();
        ArrayList<String> time = new ArrayList<String>();
        ArrayList<String> bPrice = new ArrayList<String>();
        ArrayList<String> sPrice = new ArrayList<String>();


        int j = 0;
        for (String code : Samples.productPriceList.get(0)) {
            if (code.equals(productCode)) {
                date.add(Samples.productPriceList.get(1).get(j));
                time.add(Samples.productPriceList.get(2).get(j));
                bPrice.add(Samples.productPriceList.get(3).get(j));
                sPrice.add(Samples.productPriceList.get(4).get(j));
            }
            j++;
        }
        mData.add(0, bPrice);
        mData.add(1, sPrice);
        mData.add(2, date);
        mData.add(3, time);

    }

    @Override
    public int getCount() {
        return mData.get(0).size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(0).get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = convertView;
        if (convertView == null) {
            view = inflater.inflate(R.layout.list_item_price_list, null);
        }
        TextView buyPrice = (TextView) view.findViewById(R.id.price_list_bPrice);
        TextView salePrice = (TextView) view.findViewById(R.id.price_list_sPrice);
        TextView date = (TextView) view.findViewById(R.id.price_list_date);
        TextView time = (TextView) view.findViewById(R.id.price_list_time);

        buyPrice.setText(Utility.formatPurchase(mContext, Utility.DecimalSeperation(mContext, Double.parseDouble(mData.get(0).get(position)))));
        salePrice.setText(Utility.formatPurchase(mContext, Utility.DecimalSeperation(mContext, Double.parseDouble(mData.get(1).get(position)))));

        if (!(Utility.getLocale(mContext).equals("IR"))) {
            date.setText(mData.get(2).get(position));
        } else {
            date.setText(Utility.JalaliDatePicker(mData.get(2).get(position)));
        }
        time.setText(mData.get(3).get(position));
        return view;
    }
}
