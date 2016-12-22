package mjkarbasian.moshtarimadar.adapters;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import mjkarbasian.moshtarimadar.Data.KasebContract;
import mjkarbasian.moshtarimadar.R;


/**
 * Created by family on 6/23/2016.
 */
public class CustomerAdapter extends CursorAdapter {

    private LayoutInflater cursorInflater;
    String name;
    String stateId;
    String amount;

    public CustomerAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        cursorInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return cursorInflater.inflate(R.layout.list_item_customers, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView textViewName = (TextView) view.findViewById(R.id.item_list_customer_name);
        TextView textViewAmount = (TextView) view.findViewById(R.id.item_list_purchase_amount);
        ImageView imageViewState = (ImageView) view.findViewById(R.id.item_list_customer_state);

        stateId = cursor.getString(cursor.getColumnIndex(KasebContract.Customers.COLUMN_STATE_ID));
        name = cursor.getString(cursor.getColumnIndex(KasebContract.Customers.COLUMN_FIRST_NAME)) + "   " +
                cursor.getString(cursor.getColumnIndex(KasebContract.Customers.COLUMN_LAST_NAME));
        amount = "120000";

        switch (stateId) {
            case "1": {
                imageViewState.setColorFilter(Color.rgb(255, 223, 0));
                break;
            }
            case "2": {
                imageViewState.setColorFilter(Color.rgb(192, 192, 192));
                break;
            }
            case "3": {
                imageViewState.setColorFilter(Color.rgb(205, 127, 50));
                break;
            }
            default:
                break;
        }
        textViewName.setText(name);
        textViewAmount.setText(amount);
    }
}
