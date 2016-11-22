package mjkarbasian.moshtarimadar.adapters;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.text.DecimalFormat;

import mjkarbasian.moshtarimadar.R;
import mjkarbasian.moshtarimadar.helper.Samples;

import static mjkarbasian.moshtarimadar.helper.Samples.customerDebateBalance;
import static mjkarbasian.moshtarimadar.helper.Samples.customerName;
import static mjkarbasian.moshtarimadar.helper.Samples.debatorName;
import static mjkarbasian.moshtarimadar.helper.Samples.debtorBalance;
import static mjkarbasian.moshtarimadar.helper.Samples.debtorsCode;
import static mjkarbasian.moshtarimadar.helper.Samples.debtorsCodeNums;
import static mjkarbasian.moshtarimadar.helper.Samples.debtorsDue;
import static mjkarbasian.moshtarimadar.helper.Samples.debtorsMobileNumber;
import static mjkarbasian.moshtarimadar.helper.Utility.DecimalSeperation;
import static mjkarbasian.moshtarimadar.helper.Utility.JalaliDatePicker;
import static mjkarbasian.moshtarimadar.helper.Utility.dipConverter;
import static mjkarbasian.moshtarimadar.helper.Utility.doubleFormatter;
import static mjkarbasian.moshtarimadar.helper.Utility.formatPurchase;
import static mjkarbasian.moshtarimadar.helper.Utility.getLocale;

/**
 * Created by family on 6/27/2016.
 */
public class DebatorAdapter extends BaseAdapter {
    Context mContext;

    public DebatorAdapter(Context context) {
        mContext = context;
    }

    @Override
    public int getCount() {
        return debatorName.length;
    }

    @Override
    public Object getItem(int position) {
        return debatorName[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = convertView;

        if (convertView == null) {
            view = inflater.inflate(R.layout.list_item_debators, parent, false);
        }


        TextView debatorNameText = (TextView) view.findViewById(R.id.item_card_debator_name);
        debatorNameText.setText(mContext.getString(customerName[position]));

        ImageView customerAvater = (ImageView) view.findViewById(R.id.item_card_customer_avater);
        if (Samples.customerAvatar.size() == 0) {
            customerAvater.setImageResource(R.drawable.kaseb_pic);
        } else {
            if (!(Samples.customerAvatar.size() <= position)) {
                customerAvater.setImageURI(Samples.customerAvatar.get(position));
            } else {
                customerAvater.setImageResource(R.drawable.kaseb_pic);
            }
        }

        TextView debatorBalanceText = (TextView) view.findViewById(R.id.item_card_balance_amount);
        DecimalFormat format = new DecimalFormat();
        format.setDecimalSeparatorAlwaysShown(true);
        String balanceAmount = (String) format.format(customerDebateBalance[position]);
        balanceAmount = formatPurchase(mContext, balanceAmount);
        debatorBalanceText.setText(balanceAmount);

        TableLayout balanceTable = (TableLayout) view.findViewById(R.id.table_balance_debators);

        for (int i = 0; i < debtorsCodeNums[position]; i++) {
            //there is an if for a bug that shows row twice
            if (balanceTable.getChildCount() <= debtorsCodeNums[position]) {
                final TableRow tableRow = new TableRow(mContext);

                //Table layout attrbute
                TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(0, 4, 0, 0);
                tableRow.setLayoutParams(layoutParams);
                tableRow.setClickable(true);
                tableRow.setOnClickListener(new View.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onClick(View v) {

                        tableRow.setBackground(mContext.getDrawable(R.drawable.list_ripple));

                    }
                });
                //Define row elements
                TextView codeText = new TextView(mContext);
                TextView dueText = new TextView(mContext);
                TextView balanceText = new TextView(mContext);
                codeText.setText(doubleFormatter(debtorsCode[position][i]));
                if (!getLocale(mContext).equals("IR")) {
                    dueText.setText(debtorsDue[position][i]);
                } else {
                    dueText.setText(JalaliDatePicker(debtorsDue[position][i]));
                }
                balanceText.setText(DecimalSeperation(mContext, debtorBalance[position][i]));

                //Layout attributes of table row element
                codeText.setGravity(Gravity.CENTER);
                dueText.setGravity(Gravity.CENTER);
                balanceText.setGravity(Gravity.CENTER);

                if (Build.VERSION.SDK_INT < 23) {
                    codeText.setTextAppearance(mContext, R.style.debator_table_cell_text);
                    dueText.setTextAppearance(mContext, R.style.debator_table_cell_text);
                    balanceText.setTextAppearance(mContext, R.style.debator_table_cell_text);

                } else {
                    codeText.setTextAppearance(R.style.debator_table_cell_text);
                    dueText.setTextAppearance(R.style.debator_table_cell_text);
                    balanceText.setTextAppearance(R.style.debator_table_cell_text);

                }

                int height = dipConverter(40, mContext); // margin in pixels
                TableRow.LayoutParams layoutParams1 = new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, height, 1.0f);
                TableRow.LayoutParams layoutParams2 = new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, height, 1.0f);
                TableRow.LayoutParams layoutParams3 = new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, height, 2.0f);
                codeText.setLayoutParams(layoutParams1);
                dueText.setLayoutParams(layoutParams2);
                balanceText.setLayoutParams(layoutParams3);

                //Adding views to parents
                tableRow.addView(codeText);
                tableRow.addView(dueText);
                tableRow.addView(balanceText);
                balanceTable.addView(tableRow);
            }
        }

        //Card actions click handler
        ImageView phoneImage = (ImageView) view.findViewById(R.id.debtor_call);
        phoneImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", debtorsMobileNumber[position], null));
                mContext.startActivity(intent);
            }
        });

        ImageView messageImage = (ImageView) view.findViewById(R.id.debtor_message);
        messageImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", debtorsMobileNumber[position], null));
                mContext.startActivity(intent);
            }
        });

        ImageView shareImage = (ImageView) view.findViewById(R.id.debtor_share);
        final String finalBalanceAmount;
        finalBalanceAmount = String.format("%s%s", mContext.getResources().getString(R.string.debtor_share_text_to_send), balanceAmount);
        shareImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, finalBalanceAmount);
                mContext.startActivity(Intent.createChooser(intent, "Share with"));
            }
        });

        return view;
    }


}

