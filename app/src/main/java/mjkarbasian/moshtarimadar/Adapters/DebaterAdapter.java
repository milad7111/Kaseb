package mjkarbasian.moshtarimadar.Adapters;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import mjkarbasian.moshtarimadar.Data.KasebContract;
import mjkarbasian.moshtarimadar.Helpers.Utility;
import mjkarbasian.moshtarimadar.R;

/**
 * Created by family on 12/19/2016.
 */
public class DebaterAdapter extends CursorAdapter {

    private LayoutInflater cursorInflater;
    private android.content.Context mContext;


    public DebaterAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        cursorInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mContext = context;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return cursorInflater.inflate(R.layout.list_item_debaters, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        //region get debator name
        String[] customersProjection = new String[]{
                KasebContract.Customers.COLUMN_FIRST_NAME,
                KasebContract.Customers.COLUMN_LAST_NAME
        };
        Uri customerUri = KasebContract.Customers.buildCustomerUri(cursor.
                getLong(cursor.getColumnIndex(KasebContract.Sales.TABLE_NAME + "." + KasebContract.Sales.COLUMN_CUSTOMER_ID)));
        Cursor adapterCursor = mContext.getContentResolver().query(customerUri, null, null, null, null);
        String firstName = null;
        String lastName = null;
        if (adapterCursor.moveToFirst()) {
            firstName = adapterCursor.getString(adapterCursor.getColumnIndex(customersProjection[0]));
            lastName = adapterCursor.getString(adapterCursor.getColumnIndex(customersProjection[1]));
        }
        TextView debatorNameText = (TextView) view.findViewById(R.id.item_card_debator_name);
        debatorNameText.setText(firstName + " " + lastName);
        //endregion

        ImageView customerAvater = (ImageView) view.findViewById(R.id.item_card_customer_avater);

        //region set debator balance amount
        TextView debatorBalanceText = (TextView) view.findViewById(R.id.item_card_balance_amount);
        int dueSumIndex = 2;
        int paidSumIndex = 3;
        Float totalBalance = Float.parseFloat(cursor.getString(dueSumIndex)) - Float.parseFloat(cursor.getString(paidSumIndex));
        debatorBalanceText.setText(Utility.formatPurchase(mContext, Float.toString(totalBalance)));
        //endregion

        //Card actions click handler
        ImageView phoneImage = (ImageView) view.findViewById(R.id.debtor_call);
        phoneImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", "01", null));
                mContext.startActivity(intent);
            }
        });

        ImageView messageImage = (ImageView) view.findViewById(R.id.debtor_message);
        messageImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", "01", null));
                mContext.startActivity(intent);
            }
        });

        ImageView shareImage = (ImageView) view.findViewById(R.id.debtor_share);
        final String finalBalanceAmount;
        finalBalanceAmount = String.format("%s%s", mContext.getResources().getString(R.string.debtor_share_text_to_send), "Hello");
        shareImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, finalBalanceAmount);
                mContext.startActivity(Intent.createChooser(intent, "Share with"));
            }
        });

    }


}
