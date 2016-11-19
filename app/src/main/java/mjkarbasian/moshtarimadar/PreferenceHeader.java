package mjkarbasian.moshtarimadar;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
//                            Intent contactPickerIntent = new Intent(Intent.ACTION_PICK,
//                                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
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
        String given = null;
        String family= null;
        String display = null;
        String contactMobilePhone= null;
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
                    Cursor cr = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID
                            + " = " + contactId, null, null);
                    if(cr.moveToFirst())
                    contactMobilePhone = cr.getString(cr.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    cr = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null, ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?", new String[]{contactId}, null);;
                    if(cr.moveToFirst())
                     contactMobilePhone = cr.getString(cr.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Email.DATA));
                    cr = getActivity().getContentResolver().query(ContactsContract.Data.CONTENT_URI,null,ContactsContract.Data.CONTACT_ID + " = ?",
                            new String[] {contactId},null);

                    while (cr.moveToNext()) {
                             given = cr.getString(cr.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME));
                             family = cr.getString(cr.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME));
                             display = cr.getString(cr.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME));
                    }
                    cr.close();
                    if (given!=null&&family!=null&&contactMobilePhone!=null){
                    ContentValues contactValue = new ContentValues();
                    contactValue.put(KasebContract.Customers.COLUMN_FIRST_NAME,given);
                    contactValue.put(KasebContract.Customers.COLUMN_LAST_NAME,family);
                    contactValue.put(KasebContract.Customers.COLUMN_PHONE_MOBILE,contactMobilePhone);
                    contactValue.put(KasebContract.Customers.COLUMN_EMAIL,contactEmail);
                    Uri insertUri = getActivity().getContentResolver().insert(KasebContract.Customers.CONTENT_URI,contactValue);
                    }
                    break;
                }

            }
        }
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
        headerTitle.add(getActivity().getResources().getString(R.string.pref_header_import_data));
        headerTitle.add(getActivity().getResources().getString(R.string.pref_header_kaseb));
        headerTitle.add(getActivity().getResources().getString(R.string.pref_header_notifications));
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
        headerIcons.add(R.drawable.information);
        headerIcons.add(R.drawable.information);
        return headerIcons;
    }


}
