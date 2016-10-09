package mjkarbasian.moshtarimadar.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;

import mjkarbasian.moshtarimadar.R;
import mjkarbasian.moshtarimadar.helper.Samples;
import mjkarbasian.moshtarimadar.helper.Utility;


/**
 * Created by family on 6/23/2016.
 */
public class CustomerAdapter extends BaseAdapter {

    private Context mContext;

    public CustomerAdapter(Context c) {
        mContext = c;
    }
    @Override
    public int getCount() {
        return Samples.customerName.length;
    }

    @Override
    public Object getItem(int position) {
        return Samples.customerName[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = convertView;
        if(convertView==null)
        {
            view = inflater.inflate(R.layout.list_item_customers,parent,false);
        }
        TextView customerNameText = (TextView)view.findViewById(R.id.item_list_customer_name);
        customerNameText.setText(mContext.getString(Samples.customerName[position]));

        ImageView customerAvater = (ImageView) view.findViewById(R.id.item_list_customer_avatar);
        if(Samples.customerAvatar.size()==0)
        {
            customerAvater.setImageResource(R.drawable.kaseb_pic);
        }
        else
        {
            if(!(Samples.customerAvatar.size()<=position)){
            customerAvater.setImageURI(Samples.customerAvatar.get(position));}
            else {
                customerAvater.setImageResource(R.drawable.kaseb_pic);
            }
        }

        TextView customerPurchaseAmountText =(TextView)view.findViewById(R.id.item_list_purchase_amount);
        DecimalFormat format = new DecimalFormat();
        format.setDecimalSeparatorAlwaysShown(true);
        String purchaseAmount =(String)format.format(Samples.customerPurchaseAmount[position]);
        purchaseAmount = Utility.formatPurchase(mContext, purchaseAmount);
        customerPurchaseAmountText.setText(purchaseAmount);
        ImageView customerState =(ImageView)view.findViewById(R.id.item_list_customer_state);
        Utility.setCustomerState(customerState, Samples.customerMembership[position]);
        return view;
    }


    //Dummy data references;



}
