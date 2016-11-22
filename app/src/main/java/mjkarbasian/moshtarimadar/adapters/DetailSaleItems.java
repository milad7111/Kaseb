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
 * Created by family on 8/18/2016.
 */
public class DetailSaleItems extends BaseAdapter {
    String mSaleCode;
    Context mContext;
    ArrayList<ArrayList<String>> mData = new ArrayList<ArrayList<String>>();

    public DetailSaleItems(Context context, String saleCode) {
        super();
        mContext = context;
        mSaleCode = saleCode;
        //SaleProductList contains: salecode,productName,Price,numbers,total amount
        ArrayList<String> productName = new ArrayList<String>();
        ArrayList<String> price = new ArrayList<String>();
        ArrayList<String> numbers = new ArrayList<String>();
        ArrayList<String> totalAmount = new ArrayList<String>();

        int j = 0;
        for (String code : Samples.saleProductList.get(0)) {
            if (code.equals(saleCode)) {
                productName.add(Samples.saleProductList.get(1).get(j));
                price.add(Samples.saleProductList.get(2).get(j));
                numbers.add(Samples.saleProductList.get(3).get(j));
                totalAmount.add(Samples.saleProductList.get(4).get(j));
            }
            j++;
        }
        mData.add(0, productName);
        mData.add(1, price);
        mData.add(2, numbers);
        mData.add(3, totalAmount);
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
            view = inflater.inflate(R.layout.list_item_detail_sale_items, null);
        }
        TextView productName = (TextView) view.findViewById(R.id.detail_sale_items_list_code);
        TextView numbers = (TextView) view.findViewById(R.id.detail_sale_items_list_numbers);
        TextView amount = (TextView) view.findViewById(R.id.detail_sale_items_list_amount);

        productName.setText(mData.get(0).get(position));
        numbers.setText(mData.get(2).get(position));
        amount.setText(Utility.formatPurchase(mContext, Utility.DecimalSeperation(mContext, Double.parseDouble(mData.get(3).get(position)))));

        return view;
    }
}
