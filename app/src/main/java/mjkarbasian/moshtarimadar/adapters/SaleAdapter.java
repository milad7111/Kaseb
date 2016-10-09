package mjkarbasian.moshtarimadar.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.TreeSet;

import mjkarbasian.moshtarimadar.helper.Samples;
import mjkarbasian.moshtarimadar.R;
import mjkarbasian.moshtarimadar.helper.Utility;

/**
 * Created by family on 7/12/2016.
 */
public class SaleAdapter extends BaseAdapter {
    Context mContext;
    private static final int TYPE_ITEM = 0;
    private static final int TYPE_SEPARATOR = 1;

    private ArrayList<String> mData = new ArrayList<String>();
    private TreeSet<Integer> sectionHeader = new TreeSet<Integer>();
    LayoutInflater mInflater;


    public SaleAdapter(Context context) {
        mContext = context;
        mInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    public void addItem(final String item) {
        mData.add(item);
        notifyDataSetChanged();

    }

    @Override
    public int getItemViewType(int position) {
        return sectionHeader.contains(position) ? TYPE_SEPARATOR : TYPE_ITEM;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    public void addSectionHeaderItem(final String item) {
        mData.add(item);
        sectionHeader.add(mData.size() - 1);
        notifyDataSetChanged();}


    @Override
    public int getCount() {
     return mData.size();

    }

    @Override
    public Object getItem(int position) {
        return  mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return getItemViewType(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int rowType = getItemViewType(position);
        View view = convertView;
            switch (rowType) {
                case TYPE_ITEM:
                    view = mInflater.inflate(R.layout.list_item_sales, null);
                    TextView customerName = (TextView)view.findViewById(R.id.item_list_sale_customer_name);
                    TextView saleCode =(TextView)view.findViewById(R.id.item_list_sale_code);
                    TextView saleAmount = (TextView)view.findViewById(R.id.item_list_sale_purchase_amount);
                    TextView saleDue = (TextView)view.findViewById(R.id.item_list_sale_due_date);
                    customerName.setText(Samples.sales.get(2).get(Samples.sales.get(1).indexOf(mData.get(position))));
                    saleCode.setText(Utility.doubleFormatter(Integer.parseInt(mData.get(position))));
                    saleAmount.setText(Utility.formatPurchase(mContext, Utility.DecimalSeperation(mContext, Integer.parseInt(Samples.sales.get(4).get(Samples.sales.get(1).indexOf(mData.get(position)))))));
                    if(!(Utility.getLocale(mContext).equals("IR"))){
                        saleDue.setText(Samples.sales.get(0).get(Samples.sales.get(1).indexOf(mData.get(position))));

                    }
                    else{
                     saleDue.setText(Utility.JalaliDatePicker(Samples.sales.get(0).get(Samples.sales.get(1).indexOf(mData.get(position)))));
                    }
                    break;
                case TYPE_SEPARATOR:
                    view = mInflater.inflate(R.layout.list_item_date_header, null);
                    TextView headerText = (TextView)view.findViewById(R.id.header_list_sale);
                    headerText.setText(mData.get(position));
                    break;
                default:

            }

        return view;
    }
}
