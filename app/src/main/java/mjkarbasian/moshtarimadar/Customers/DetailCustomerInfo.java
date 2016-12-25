package mjkarbasian.moshtarimadar.Customers;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import mjkarbasian.moshtarimadar.Data.KasebContract;
import mjkarbasian.moshtarimadar.R;

/**
 * Created by family on 7/27/2016.
 */
public class DetailCustomerInfo extends Fragment {
    Integer customerosition;
    String[] mProjectionCursor;
    Uri uriCursor;
    String phoneMobileCustomer;
    String emailCustomer;

    public DetailCustomerInfo() {
        super();
    }

    public DetailCustomerInfo(Uri uri) {
        super();
        uriCursor = uri;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.customer_info, container, false);

        mProjectionCursor = new String[]{
                KasebContract.Customers._ID,
                KasebContract.Customers.COLUMN_PHONE_MOBILE,
                KasebContract.Customers.COLUMN_EMAIL};

        Cursor customerCursor = getContext().getContentResolver().query(
                uriCursor,
                mProjectionCursor,
                null,
                null,
                null
        );

        TextView customerPhoneMobile = (TextView) view.findViewById(R.id.customer_phone_number);
        TextView customerEmail = (TextView) view.findViewById(R.id.customer_email_txt);

        if (customerCursor != null) {
            if (customerCursor.moveToFirst()) {
                phoneMobileCustomer = customerCursor.getString(customerCursor.getColumnIndex(KasebContract.Customers.COLUMN_PHONE_MOBILE));
                emailCustomer = customerCursor.getString(customerCursor.getColumnIndex(KasebContract.Customers.COLUMN_EMAIL));
            }
        }

        customerPhoneMobile.setText(phoneMobileCustomer);
        customerEmail.setText(emailCustomer);

        ImageView callImage = (ImageView) view.findViewById(R.id.customer_call_btn);
        ImageView smsImage = (ImageView) view.findViewById(R.id.customer_sms_btn);
        ImageView emailImage = (ImageView) view.findViewById(R.id.customer_email_btn);
        ImageView shareImage = (ImageView) view.findViewById(R.id.customer_share_btn);

        callImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneMobileCustomer, null));
                getActivity().startActivity(intent);
            }
        });

        smsImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", phoneMobileCustomer, null));
                getActivity().startActivity(intent);
            }
        });

        shareImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, phoneMobileCustomer);
                getActivity().startActivity(Intent.createChooser(intent, "Share with"));
            }
        });

        emailImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/html");
                intent.putExtra(Intent.EXTRA_TEXT, emailCustomer);
                getActivity().startActivity(Intent.createChooser(intent, "Send Email"));
            }
        });

        return view;
    }
}
