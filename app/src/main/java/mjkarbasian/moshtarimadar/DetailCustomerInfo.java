package mjkarbasian.moshtarimadar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import mjkarbasian.moshtarimadar.helper.Samples;
import mjkarbasian.moshtarimadar.helper.Utility;

import static mjkarbasian.moshtarimadar.helper.Samples.customerPhoneNumber;

/**
 * Created by family on 7/27/2016.
 */
public class DetailCustomerInfo extends Fragment {
    Integer customerPosition;
    public DetailCustomerInfo(Integer position) {
        super();
        customerPosition = position;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.customer_info, container, false);


        TextView customerPhone = (TextView)view.findViewById(R.id.customer_phone_number);
        TextView customerEmail = (TextView)view.findViewById(R.id.customer_email_txt);

        customerPhone.setText(Utility.doubleFormatter(Double.parseDouble(Samples.customerPhoneNumber[customerPosition])));
        customerEmail.setText(Samples.customerEmail[customerPosition]);

        ImageView callImage = (ImageView)view.findViewById(R.id.customer_call_btn);
        ImageView smsImage = (ImageView)view.findViewById(R.id.customer_sms_btn);
        ImageView emailImage = (ImageView)view.findViewById(R.id.customer_email_btn);
        ImageView shareImage = (ImageView)view.findViewById(R.id.customer_share_btn);

        callImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", customerPhoneNumber[customerPosition], null));
                getActivity().startActivity(intent);
            }
        });

        smsImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW,Uri.fromParts("sms", customerPhoneNumber[customerPosition], null));
                getActivity().startActivity(intent);
            }
        });

        shareImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, customerPhoneNumber[customerPosition]);
                getActivity().startActivity(Intent.createChooser(intent, "Share with"));
            }
        });

        emailImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/html");
                intent.putExtra(Intent.EXTRA_TEXT, customerPhoneNumber[customerPosition]);
                getActivity().startActivity(Intent.createChooser(intent, "Send Email"));
            }
        });


        return view;
}
}
