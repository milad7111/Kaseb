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
 * Created by family on 7/30/2016.
 */
public class CustomerBillAdapter extends BaseAdapter {
    Context mContext;
    Integer customerPosition;
    ArrayList<String> mData = new ArrayList<String>();


    public CustomerBillAdapter(Context context,int position)
    {
        super();
        mContext = context;
        customerPosition = position;
        String customerName = context.getResources().getString(Samples.customerName[position]);

        if(mData.size()==0 && !(Samples.sales.size()==0))
        {
            for(int i=0 ; i< Samples.sales.get(2).size();i++){
                if(Samples.sales.get(2).get(i).equals(customerName)){
                    mData.add(Samples.sales.get(1).get(i));
                }
            }
        }
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = convertView;
        if(convertView==null)
        {
            view = inflater.inflate(R.layout.list_item_products,null);
        }
        TextView nameText = (TextView) view.findViewById(R.id.item_list_product_name);
        TextView codeText = (TextView) view.findViewById(R.id.item_list_product_code);
        TextView dateText = (TextView) view.findViewById(R.id.item_list_product_date);
        TextView priceText = (TextView) view.findViewById(R.id.item_list_product_price);

        nameText.setText(Samples.sales.get(2).get(Samples.sales.get(1).indexOf(mData.get(position))));
        codeText.setText(Utility.doubleFormatter(Double.parseDouble(mData.get(position))));

        if(!(Utility.getLocale(mContext).equals("IR"))){
            dateText.setText(Samples.sales.get(0).get(Samples.sales.get(1).indexOf(mData.get(position))));

        }
        else{
            dateText.setText(Utility.JalaliDatePicker(Samples.sales.get(0).get(Samples.sales.get(1).indexOf(mData.get(position)))));
        }

        priceText.setText(Utility.formatPurchase(mContext, Utility.DecimalSeperation(mContext, Integer.parseInt(Samples.sales.get(3).get(Samples.sales.get(1).indexOf(mData.get(position)))))));


        return view;
    }
}
