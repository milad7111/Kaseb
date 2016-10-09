package mjkarbasian.moshtarimadar.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import mjkarbasian.moshtarimadar.R;
import mjkarbasian.moshtarimadar.helper.Samples;
import mjkarbasian.moshtarimadar.helper.Utility;

import static mjkarbasian.moshtarimadar.helper.Samples.productCode;

/**
 * Created by family on 7/21/2016.
 */
public class ProductAdapter extends BaseAdapter {
    Context mContext;

    public ProductAdapter(Context context) {
        super();
        mContext = context;
    }

    @Override
    public int getCount() {
        return productCode.size();
    }

    @Override
    public Object getItem(int position) {
        return productCode.get(position);
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

        nameText.setText(Samples.productName.get(position));
        codeText.setText(Utility.doubleFormatter(Integer.parseInt(Samples.productCode.get(position))));
        if(!(Utility.getLocale(mContext).equals("IR"))){
            dateText.setText(Samples.productDate.get(position));

        }
        else{
            dateText.setText(Utility.JalaliDatePicker(Samples.productDate.get(position)));
        }
        priceText.setText(Utility.formatPurchase(mContext,Utility.DecimalSeperation(mContext,Integer.parseInt(Samples.productPrice.get(position)))));
        return view;
    }
}
