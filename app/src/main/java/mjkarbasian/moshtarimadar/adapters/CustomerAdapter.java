package mjkarbasian.moshtarimadar.adapters;

import android.content.Context;
import android.database.Cursor;
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

        String selection = KasebContract.State._ID + " = ?";
        String[] selecArg = new String[]{String.valueOf(cursor.getInt(cursor.getColumnIndex(KasebContract.Customers.COLUMN_STATE_ID)))};
        Cursor colorCursor = context.getContentResolver().query(KasebContract.State.CONTENT_URI,
                new String[]{KasebContract.State._ID ,KasebContract.State.COLUMN_STATE_COLOR},selection,selecArg,null);
        if(colorCursor.moveToFirst())
        imageViewState.setColorFilter(colorCursor.getInt(colorCursor.getColumnIndex(KasebContract.State.COLUMN_STATE_COLOR)));

        stateId = cursor.getString(cursor.getColumnIndex(KasebContract.Customers.COLUMN_STATE_ID));
        name = cursor.getString(cursor.getColumnIndex(KasebContract.Customers.COLUMN_FIRST_NAME)) + "   " +
                cursor.getString(cursor.getColumnIndex(KasebContract.Customers.COLUMN_LAST_NAME));
        amount = "120000";
        textViewName.setText(name);
        textViewAmount.setText(amount);
    }
}
