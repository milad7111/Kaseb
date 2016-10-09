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
public class CostAdapter extends BaseAdapter {
    Context mContext;
    private static final int TYPE_ITEM = 0;
    private static final int TYPE_SEPARATOR = 1;

    private ArrayList<String> mDataCosts = new ArrayList<String>();
    private TreeSet<Integer> sectionHeader = new TreeSet<Integer>();
    LayoutInflater mInflater;


    public CostAdapter(Context context) {
        mContext = context;
        mInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    public void addItem(final String item) {
        mDataCosts.add(item);
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
        mDataCosts.add(item);
        sectionHeader.add(mDataCosts.size() - 1);
        notifyDataSetChanged();}


    @Override
    public int getCount() {
        return mDataCosts.size();

    }

    @Override
    public Object getItem(int position) {
        return  mDataCosts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int rowType = getItemViewType(position);
        View view = convertView;
        switch (rowType) {
            case TYPE_ITEM:
                view = mInflater.inflate(R.layout.list_item_costs, null);
                TextView costName = (TextView)view.findViewById(R.id.item_list_cost_name);
                TextView costCode =(TextView)view.findViewById(R.id.item_list_cost_code);
                TextView costAmount = (TextView)view.findViewById(R.id.item_list_cost_amount);
                TextView costDue = (TextView)view.findViewById(R.id.item_list_cost_date);
                costName.setText(Samples.costs.get(2).get(Samples.costs.get(1).indexOf(mDataCosts.get(position))));
                costCode.setText(Utility.doubleFormatter(Integer.parseInt(mDataCosts.get(position))));
                costAmount.setText(Utility.formatPurchase(mContext, Utility.DecimalSeperation(mContext, Integer.parseInt(Samples.costs.get(3).get(Samples.costs.get(1).indexOf(mDataCosts.get(position)))))));
                if(!(Utility.getLocale(mContext).equals("IR"))){
                    costDue.setText(Samples.costs.get(0).get(Samples.costs.get(1).indexOf(mDataCosts.get(position))));

                }
                else{
                    costDue.setText(Utility.JalaliDatePicker(Samples.costs.get(0).get(Samples.costs.get(1).indexOf(mDataCosts.get(position)))));
                }
                break;
            case TYPE_SEPARATOR:
                view = mInflater.inflate(R.layout.list_item_date_header, null);
                TextView headerText = (TextView)view.findViewById(R.id.header_list_sale);
                headerText.setText(mDataCosts.get(position));
                break;
            default:

        }

        return view;
    }
}
