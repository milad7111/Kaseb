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
 * Created by family on 8/20/2016.
 */
public class DetailSaleTax extends BaseAdapter {
    Context mContext;
    String mSaleCode;
    ArrayList<ArrayList<String>> mData = new ArrayList<ArrayList<String>>();


    public DetailSaleTax(Context context,String saleCode) {
        super();
        //Coloumns are : salecode,Off/Tax,amount
        mContext = context;
        mSaleCode = saleCode;
        ArrayList<String> offTaxModel = new ArrayList<String>();
        ArrayList<String> offTaxAmount = new ArrayList<String>();

        int j= 0;
        for(String code: Samples.saleOffTaxList.get(0)){
            if(code.equals(saleCode)){
                offTaxModel.add(Samples.saleOffTaxList.get(1).get(j));
                offTaxAmount.add(Samples.saleOffTaxList.get(2).get(j));
            }
            j++;
        }
        mData.add(0,offTaxModel);
        mData.add(1,offTaxAmount);
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
        TextView offTaxModel =(TextView) view.findViewById(R.id.detail_sale_items_list_code);
        TextView offTaxAmount =(TextView) view.findViewById(R.id.detail_sale_items_list_amount);
        offTaxModel.setText(mData.get(0).get(position));
//        if(mData.get(0).get(position).equals(mContext.getResources().getString(Samples.offTaxSticks[1])))
//        offTaxModel.setTextColor(mContext.getResources().getColor(R.color.colorAccent));
        offTaxAmount.setText(Utility.formatPurchase(mContext, Utility.DecimalSeperation(mContext, Double.parseDouble(mData.get(1).get(position)))));

        return view;    }
}
