package mjkarbasian.moshtarimadar.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import mjkarbasian.moshtarimadar.R;
import mjkarbasian.moshtarimadar.helper.TaxListModel;

/**
 * Created by family on 7/12/2016.
 */
public class TaxAdapter extends BaseAdapter implements View.OnClickListener {

    private static LayoutInflater inflater = null;
    TaxListModel tempValues = null;
    private Context mContext;
    private ArrayList data;

    public TaxAdapter(Context context, ArrayList d) {

        mContext = context;
        data = d;

        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        if (data.size() <= 0)
            return 1;
        return data.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        ViewHolder holder;

        if (convertView == null) {
            vi = inflater.inflate(R.layout.list_item_tax_for_sale, null);

            holder = new ViewHolder();
            holder.taxAmount = (TextView) vi.findViewById(R.id.tax_list_for_sale_amount);
            holder.taxPercent = (TextView) vi.findViewById(R.id.tax_list_for_sale_tax_percent);
            holder.taxType = (TextView) vi.findViewById(R.id.tax_list_for_sale_tax_type);

            vi.setTag(holder);
        } else
            holder = (ViewHolder) vi.getTag();

        if (data.size() <= 0) {
            holder.taxAmount.setText("No Data");

        } else {
            tempValues = null;
            tempValues = (TaxListModel) data.get(position);

            holder.taxAmount.setText(tempValues.getTaxAmount().toString());
            holder.taxPercent.setText(tempValues.getTaxPercent());
            holder.taxType.setText(tempValues.getTaxType());

            vi.setOnClickListener(new OnItemClickListener(position));
        }
        return vi;
    }

    @Override
    public void onClick(View v) {
        Toast.makeText(mContext, "What ...?", Toast.LENGTH_SHORT).show();
    }

    public static class ViewHolder {
        public TextView taxAmount;
        public TextView taxPercent;
        public TextView taxType;
    }

    private class OnItemClickListener implements View.OnClickListener {
        private int mPosition;

        OnItemClickListener(int position) {
            mPosition = position;
        }

        @Override
        public void onClick(View arg0) {
        }
    }
}