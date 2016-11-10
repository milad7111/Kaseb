package mjkarbasian.moshtarimadar.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

/**
 * Created by Unique on 10/11/2016.
 */
public class CostSaleProductAdapter extends CursorAdapter {
    String columnName;
    private String name;
    private String date;
    private String amount;
    private String code;

    public CostSaleProductAdapter(Context context, Cursor c, int flags, String column) {
        super(context, c, flags);
        columnName = column;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        TextView nameView = new TextView(context);
//        TextView dateView = new TextView(context);
//        TextView amountView = new TextView(context);
//        TextView codeView = new TextView(context);
        return nameView;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView textView = (TextView) view;
        name = cursor.getString(cursor.getColumnIndex(columnName));
        textView.setText(name);
    }
}