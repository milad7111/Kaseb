package mjkarbasian.moshtarimadar.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

/**
 * Created by family on 11/2/2016.
 */
public class TypesSettingAdapter extends CursorAdapter {
    String columnName;
    private int typePointer;

    public TypesSettingAdapter(Context context, Cursor c, int flags,String column) {
        super(context, c, flags);
        columnName = column;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        TextView typeView = new TextView(context);
        return typeView;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView textView = (TextView)view;
        typePointer = cursor.getInt(cursor.getColumnIndex(columnName));
        cursor.moveToNext();
        textView.setText(context.getResources().getString(typePointer));
    }

}
