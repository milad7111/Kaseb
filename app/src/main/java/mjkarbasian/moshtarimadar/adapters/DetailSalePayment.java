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
 * Created by family on 8/19/2016.
 */
public class DetailSalePayment extends BaseAdapter {

    Context mContext;
    String mSaleCode;
    ArrayList<ArrayList<String>> mData = new ArrayList<ArrayList<String>>();


    public DetailSalePayment(Context context,String saleCode) {
        super();

        mContext = context;
        mSaleCode = saleCode;
        //Coloumns are : salecode,totalAmount,paymentDate,paymentModel,paymentAmount,paymentBalance
        ArrayList<String> paymentDate = new ArrayList<String>();
        ArrayList<String> paymentModel = new ArrayList<String>();
        ArrayList<String> paymentAmount = new ArrayList<String>();

        int j= 0;
        for(String code: Samples.salePaymentList.get(0)){
            if(code.equals(saleCode)){
                paymentDate.add(Samples.salePaymentList.get(2).get(j));
                paymentModel.add(Samples.salePaymentList.get(3).get(j));
                paymentAmount.add(Samples.salePaymentList.get(4).get(j));
            }
            j++;
        }
        mData.add(0,paymentDate);
        mData.add(1,paymentModel);
        mData.add(2,paymentAmount);
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
        LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = convertView;
        if(convertView==null)
        {
            view = inflater.inflate(R.layout.list_item_detail_sale_items,null);
        }
        TextView paymentDate =(TextView) view.findViewById(R.id.detail_sale_items_list_code);
        TextView paymentModel =(TextView) view.findViewById(R.id.detail_sale_items_list_numbers);
        TextView paymentAmount =(TextView) view.findViewById(R.id.detail_sale_items_list_amount);

        if(!(Utility.getLocale(mContext).equals("IR"))) {
            paymentDate.setText(mData.get(0).get(position));
        }else{
            paymentDate.setText(Utility.JalaliDatePicker(mData.get(0).get(position)));
        }
        paymentModel.setText(mData.get(1).get(position));
        paymentAmount.setText(Utility.formatPurchase(mContext, Utility.DecimalSeperation(mContext, Double.parseDouble(mData.get(2).get(position)))));

        return view;
    }
}
