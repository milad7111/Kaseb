package mjkarbasian.moshtarimadar;

import android.app.Activity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import mjkarbasian.moshtarimadar.Data.KasebContract;
import mjkarbasian.moshtarimadar.adapters.HeaderAdaper;

/**
 * Created by family on 11/3/2016.
 */
public class PreferenceHeader extends Fragment {
    private static final int RESULT_PICK_CONTACT = 1;
    private static final String LOG_TAG = PreferenceHeader.class.getSimpleName();
    ListView mListView;
    HeaderAdaper headerAdaper;
    ArrayList<Integer> headerIcons = new ArrayList<>();
    ArrayList<String> headerTitle = new ArrayList<>();
    ArrayList<String> headerSummary = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        headerIcons = initHeaderIcon();
        headerTitle = initHeaderTitle();
        headerSummary = initHeaderSummary();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_setting_types, container, false);
        mListView = (ListView) rootView.findViewById(R.id.list_view_setting_types);
        headerAdaper = new HeaderAdaper(getActivity(), headerIcons, headerTitle, headerSummary);
        mListView.setAdapter(headerAdaper);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //create an instance of typeSetting Fragment and its passing value bundle
                Bundle columnName = new Bundle();
                TypeSettingFragment typeFragment = new TypeSettingFragment();
                //filtering typeSetting from Headers By an if
                if (position < 4) {
                    switch (position) {
                        case 0: {
                            // this is cost types setting so we call typeSettingFragment by passing it Cost type column Name
                            columnName.putString("columnName", KasebContract.CostTypes.COLUMN_COST_TYPE_POINTER);
                            break;
                        }
                        case 1: {
                            columnName.putString("columnName", KasebContract.TaxTypes.COLUMN_TAX_TYPE_POINTER);
                            break;
                        }
                        case 2: {
                            columnName.putString("columnName", KasebContract.PaymentMethods.COLUMN_PAYMENT_METHOD_POINTER);
                            break;
                        }
                        case 3: {
                            columnName.putString("columnName", KasebContract.State.COLUMN_STATE_POINTER);
                            break;
                        }

                        default:
                    }
                    typeFragment.setArguments(columnName);
                    android.support.v4.app.FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.container, typeFragment);
                    fragmentTransaction.addToBackStack(null);
                    int callBackStack = fragmentTransaction.commit();
                } else {
                    switch (position) {
                        case 5: {
                            Intent contactPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
                            contactPickerIntent.setType(ContactsContract.Contacts.CONTENT_ITEM_TYPE);
                            startActivityForResult(contactPickerIntent, RESULT_PICK_CONTACT);
                        }
                    }
                }
            }
        });
        return rootView;
    }

    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);
        String[] displayName = new String[2];
        String phoneMobile= null;
        String phoneHome = null;
        String contactEmail = null;
        String contactId = null;
        switch (reqCode) {
            case (RESULT_PICK_CONTACT): {
                if (resultCode == Activity.RESULT_OK) {
                    Uri contactData = data.getData();
                    Cursor contactCursor = getActivity().getContentResolver().query(contactData, null, null, null, null);
                    if (contactCursor.moveToFirst()) {
                        contactId = contactCursor.getString(contactCursor.getColumnIndex(ContactsContract.Contacts._ID));
                    }
                    displayName = getNames(contactId);
                    if(displayName[0]==null||displayName[1]==null){
                        dataError(getActivity().getResources().getString(R.string.dialog_input_import_contact_first_last_name));
                        break;
                    }
                    if(contactCursor.getString(contactCursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)).equals("1"))
                    phoneMobile = getMobileNumber(contactId);
                    else {
                        dataError(getActivity().getResources().getString(R.string.dialog_input_import_contact_phone_mobile));
                        break;
                    }
                    contactEmail = getEmail(contactId);

                    if (displayName[0]!=null&&displayName[1]!=null&&phoneMobile!=null){
                    ContentValues contactValue = new ContentValues();
                    contactValue.put(KasebContract.Customers.COLUMN_FIRST_NAME,displayName[0]);
                    contactValue.put(KasebContract.Customers.COLUMN_LAST_NAME,displayName[1]);
                    contactValue.put(KasebContract.Customers.COLUMN_PHONE_MOBILE,phoneMobile);
                    contactValue.put(KasebContract.Customers.COLUMN_EMAIL,contactEmail);
                    Uri insertUri = getActivity().getContentResolver().insert(KasebContract.Customers.CONTENT_URI,contactValue);
                    Log.v(LOG_TAG, "Contact successfully is imported in: " + insertUri.toString());
                    }
                    else {
                    dataError(getActivity().getResources().getString(R.string.dialog_input_import_contact_general));
                    }
                    break;
                }

            }
        }
    }

    private void dataError(String name) {
        new AlertDialog.Builder(getActivity())
                .setTitle(getActivity().getResources().getString(R.string.dialog_title_import_customer_fail))
                .setMessage(getActivity().getResources().getString(R.string.dialog_message_import_customer_fail_par1) + name +
                        getActivity().getResources().getString(R.string.dialog_message_import_customer_fail_par2))
                .setPositiveButton(getActivity().getResources().getString(R.string.dialog_positive_button), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
                })
                .show();

    }

    private String getEmail(String contactId) {
        Cursor cr = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                null, ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?", new String[]{contactId}, null);;
        String contactEmail = null;
        if(cr.moveToFirst())
            contactEmail = cr.getString(cr.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Email.DATA));
        return contactEmail;
    }

    private String getMobileNumber(String contactId) {
        Cursor cr = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID
                + " = " + contactId, null, null);
        String contactMobilePhone = null;
        if(cr.moveToFirst())
            contactMobilePhone = cr.getString(cr.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER));
        return contactMobilePhone;
    }

    private String[] getNames(String contactId) {
        String firstName = null;
        String family = null;
        String whereName = ContactsContract.Data.MIMETYPE + " = ? AND " + ContactsContract.CommonDataKinds.StructuredName.CONTACT_ID + " = ?";
        String[] whereNameParams = new String[] { ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE, contactId };
        Cursor cursor = getActivity().getContentResolver().query(ContactsContract.Data.CONTENT_URI
                , null, whereName, whereNameParams,
                ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME);
        while (cursor.moveToNext()) {
            firstName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME));
            family = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME));
        }

        return new String[]{firstName, family};
    }

    private ArrayList<String> initHeaderSummary() {
        return null;
    }

    private ArrayList<String> initHeaderTitle() {
        ArrayList headerTitle = new ArrayList<>();
        headerTitle.add(getActivity().getResources().getString(R.string.pref_header_cost));
        headerTitle.add(getActivity().getResources().getString(R.string.pref_header_tax));
        headerTitle.add(getActivity().getResources().getString(R.string.pref_header_payment));
        headerTitle.add(getActivity().getResources().getString(R.string.pref_header_customer));
        headerTitle.add(getActivity().getResources().getString(R.string.pref_header_backup));
        headerTitle.add(getActivity().getResources().getString(R.string.pref_header_import_contacts));
//        headerTitle.add(getActivity().getResources().getString(R.string.pref_header_kaseb));
//        headerTitle.add(getActivity().getResources().getString(R.string.pref_header_notifications));
        return headerTitle;
    }

    private ArrayList<Integer> initHeaderIcon() {
        ArrayList<Integer> headerIcons = new ArrayList<>();
        headerIcons.add(R.drawable.information);
        headerIcons.add(R.drawable.information);
        headerIcons.add(R.drawable.information);
        headerIcons.add(R.drawable.information);
        headerIcons.add(R.drawable.information);
        headerIcons.add(R.drawable.information);
//        headerIcons.add(R.drawable.information);
//        headerIcons.add(R.drawable.information);
        return headerIcons;
    }


}
