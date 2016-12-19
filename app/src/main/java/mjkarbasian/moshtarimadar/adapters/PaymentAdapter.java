package mjkarbasian.moshtarimadar.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import mjkarbasian.moshtarimadar.R;
import mjkarbasian.moshtarimadar.helper.PaymentListModel;

/**
 * Created by family on 7/12/2016.
 */
public class PaymentAdapter extends BaseAdapter implements View.OnClickListener {

    private static LayoutInflater inflater = null;
    PaymentListModel tempValues = null;
    private Context mContext;
    private ArrayList data;

    public PaymentAdapter(Context context, ArrayList d) {
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
            vi = inflater.inflate(R.layout.list_item_payment_for_sale, null);

            holder = new ViewHolder();
            holder.paymentAmount = (TextView) vi.findViewById(R.id.payment_list_for_sale_amount);
            holder.paymentDueDate = (TextView) vi.findViewById(R.id.payment_list_for_sale_due_date);
            holder.paymentMethod = (TextView) vi.findViewById(R.id.payment_list_for_sale_payment_method);

            vi.setTag(holder);
        } else
            holder = (ViewHolder) vi.getTag();

        if (data.size() <= 0) {
            holder.paymentAmount.setText("No Data");

        } else {
            tempValues = null;
            tempValues = (PaymentListModel) data.get(position);

            holder.paymentAmount.setText(tempValues.getPaymentAmount().toString());
            holder.paymentDueDate.setText(tempValues.getPaymentDueDate());
            holder.paymentMethod.setText(tempValues.getPaymentMethod());

            vi.setOnClickListener(new OnItemClickListener(position));
        }

        return vi;
    }

    @Override
    public void onClick(View v) {
    }

    public static class ViewHolder {
        public TextView paymentAmount;
        public TextView paymentDueDate;
        public TextView paymentMethod;
    }

    private class OnItemClickListener implements View.OnClickListener {
        private int mPosition;

        OnItemClickListener(int position) {
            mPosition = position;
        }

        @Override
        public void onClick(View arg0) {
//            Toast.makeText(mContext, mPosition + "", Toast.LENGTH_SHORT).show();
        }
    }
}