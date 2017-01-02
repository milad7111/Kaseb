package mjkarbasian.moshtarimadar.Customers;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;

import mjkarbasian.moshtarimadar.Adapters.TypesSettingAdapter;
import mjkarbasian.moshtarimadar.Data.KasebContract;
import mjkarbasian.moshtarimadar.Helpers.GalleryUtil;
import mjkarbasian.moshtarimadar.Helpers.Utility;
import mjkarbasian.moshtarimadar.R;

/**
 * Created by Unique on 10/21/2016.
 */
public class CustomerInsert extends Fragment {

    private static final int GALLERY_ACTIVITY_CODE = 200;
    private static final int RESULT_CROP = 400;
    private static final int YOUR_SELECT_PICTURE_REQUEST_CODE = 300;

    EditText firstName;
    EditText lastName;
    EditText birthDay;
    Spinner stateType;
    EditText phoneMobile;
    EditText customerDescription;
    EditText email;
    EditText phoneWork;
    EditText phoneHome;
    EditText phoneOther;
    EditText phoneFax;
    EditText addressCountry;
    EditText addressCity;
    EditText addressStreet;
    EditText addressPostalCode;

    View rootView;
    ContentValues customerValues = new ContentValues();
    ImageView mCustomerAvatar;
    Bitmap photo;
    private Uri insertUri;
    private Uri outputFileUri;

    public CustomerInsert() {
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_customer_insert, container, false);

        mCustomerAvatar = (ImageView) rootView.findViewById(R.id.fragment_customer_insert_picture);

        mCustomerAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pic_selector_on_customer_insert(mCustomerAvatar);
            }
        });

        stateType = (Spinner) rootView.findViewById(R.id.input_state_type_spinner);
        firstName = (EditText) rootView.findViewById(R.id.input_first_name);
        lastName = (EditText) rootView.findViewById(R.id.input_last_name);
        birthDay = (EditText) rootView.findViewById(R.id.input_birth_day);
        phoneMobile = (EditText) rootView.findViewById(R.id.input_phone_mobile);
        customerDescription = (EditText) rootView.findViewById(R.id.input_description);
        email = (EditText) rootView.findViewById(R.id.input_email);
        phoneWork = (EditText) rootView.findViewById(R.id.input_phone_work);
        phoneHome = (EditText) rootView.findViewById(R.id.input_phone_home);
        phoneOther = (EditText) rootView.findViewById(R.id.input_phone_other);
        phoneFax = (EditText) rootView.findViewById(R.id.input_phone_fax);
        addressCountry = (EditText) rootView.findViewById(R.id.input_address_country);
        addressCity = (EditText) rootView.findViewById(R.id.input_address_city);
        addressStreet = (EditText) rootView.findViewById(R.id.input_address_street);
        addressPostalCode = (EditText) rootView.findViewById(R.id.input_address_postal_code);

        Cursor cursor = getContext().getContentResolver().query(KasebContract.State.CONTENT_URI
                , null, null, null, KasebContract.State._ID + " DESC");

        int[] toViews = {
                android.R.id.text1
        };
        String[] fromColumns = {
                KasebContract.State.COLUMN_STATE_POINTER
        };

        TypesSettingAdapter cursorAdapter = new TypesSettingAdapter(
                getActivity(), cursor, 0, KasebContract.State.COLUMN_STATE_POINTER);
        stateType.setAdapter(cursorAdapter);
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(
            Menu menu, MenuInflater inflater) {
        menu.removeItem(R.id.sort_button);
        menu.removeItem(R.id.search_button);
        inflater.inflate(R.menu.fragments_for_insert, menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_inputs: {

                if (CheckForValidity(
                        firstName.getText().toString(),
                        lastName.getText().toString(),
                        phoneMobile.getText().toString()
                )) {
                    customerValues.put(KasebContract.Customers.COLUMN_FIRST_NAME, firstName.getText().toString());
                    customerValues.put(KasebContract.Customers.COLUMN_LAST_NAME, lastName.getText().toString());
                    customerValues.put(KasebContract.Customers.COLUMN_BIRTHDAY, birthDay.getText().toString());
                    customerValues.put(KasebContract.Customers.COLUMN_PHONE_MOBILE, phoneMobile.getText().toString());
                    customerValues.put(KasebContract.Customers.COLUMN_PHONE_WORK, phoneWork.getText().toString());
                    customerValues.put(KasebContract.Customers.COLUMN_PHONE_HOME, phoneHome.getText().toString());
                    customerValues.put(KasebContract.Customers.COLUMN_PHONE_FAX, phoneFax.getText().toString());
                    customerValues.put(KasebContract.Customers.COLUMN_PHONE_OTHER, phoneOther.getText().toString());
                    customerValues.put(KasebContract.Customers.COLUMN_DESCRIPTION, customerDescription.getText().toString());
                    customerValues.put(KasebContract.Customers.COLUMN_EMAIL, email.getText().toString());
                    customerValues.put(KasebContract.Customers.COLUMN_ADDRESS_COUNTRY, addressCountry.getText().toString());
                    customerValues.put(KasebContract.Customers.COLUMN_ADDRESS_CITY, addressCity.getText().toString());
                    customerValues.put(KasebContract.Customers.COLUMN_ADDRESS_STREET, addressStreet.getText().toString());
                    customerValues.put(KasebContract.Customers.COLUMN_ADDRESS_POSTAL_CODE, addressPostalCode.getText().toString());

                    customerValues.put(KasebContract.Customers.COLUMN_STATE_ID, stateType.getCount() - stateType.getSelectedItemPosition());
                    insertUri = getActivity().getContentResolver().insert(
                            KasebContract.Customers.CONTENT_URI,
                            customerValues
                    );

                    //region disabling edit
                    firstName.setEnabled(false);
                    lastName.setEnabled(false);
                    birthDay.setEnabled(false);
                    phoneMobile.setEnabled(false);
                    customerDescription.setEnabled(false);
                    email.setEnabled(false);
                    phoneWork.setEnabled(false);
                    phoneHome.setEnabled(false);
                    phoneOther.setEnabled(false);
                    phoneFax.setEnabled(false);
                    addressCountry.setEnabled(false);
                    addressCity.setEnabled(false);
                    addressStreet.setEnabled(false);
                    addressPostalCode.setEnabled(false);
                    stateType.setEnabled(false);

                    //just a message to show everything are under control
                    Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.msg_insert_succeed),
                            Toast.LENGTH_LONG).show();

                    backToLastPage();
                }

                break;
            }

        }
        return super.onOptionsItemSelected(item);
    }

    // this method check the validation and correct entries. its check fill first and then check the validation rules.
    private boolean CheckForValidity(String customerFirstName, String customerLastName, String customerPhoneMobile) {
        if (customerFirstName.equals("") || customerFirstName.equals(null)) {
            Toast.makeText(getActivity(), "Choose apropriate name for CUSTOMER.", Toast.LENGTH_SHORT).show();
            return false;
        } else if (customerLastName.equals("") || customerLastName.equals(null)) {
            Toast.makeText(getActivity(), "Choose apropriate last name for CUSTOMER.", Toast.LENGTH_SHORT).show();
            return false;
        } else if (customerPhoneMobile.equals("") || customerPhoneMobile.equals(null)) {
            Toast.makeText(getActivity(), "Choose apropriate phone mobile for CUSTOMER.", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            Cursor mCursor = getContext().getContentResolver().query(
                    KasebContract.Customers.CONTENT_URI,
                    new String[]{KasebContract.Customers._ID},
                    KasebContract.Customers.COLUMN_PHONE_MOBILE + " = ? ",
                    new String[]{customerPhoneMobile},
                    null);

            if (mCursor != null)
                if (mCursor.moveToFirst())
                    if (mCursor.getCount() > 0) {
                        Toast.makeText(getActivity(), "Choose apropriate (Not Itterative) phone mobile for CUSTOMER.", Toast.LENGTH_SHORT).show();
                        return false;
                    }
            return true;
        }
    }

    private void backToLastPage() {
        Utility.clearForm((ViewGroup) rootView);
        getFragmentManager().popBackStackImmediate();
    }

    public void pic_selector_on_customer_insert(View view) {
        mCustomerAvatar = (ImageView) view;
        Intent gallery_Intent = new Intent(getContext(), GalleryUtil.class);
        startActivityForResult(gallery_Intent, GALLERY_ACTIVITY_CODE);

//        // Determine Uri of camera image to save.
//        final File root = new File(Environment.getExternalStorageDirectory() + File.separator + "MyDir" + File.separator);
//        root.mkdirs();
//        final String fname = "img_" + System.currentTimeMillis() + ".jpg";
//        final File sdImageMainDirectory = new File(root, fname);
//        outputFileUri = Uri.fromFile(sdImageMainDirectory);
//
//        // Camera.
//        final List<Intent> cameraIntents = new ArrayList<Intent>();
//        final Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//        final PackageManager packageManager = getContext().getPackageManager();
//        final List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
//        for (ResolveInfo res : listCam) {
//            final String packageName = res.activityInfo.packageName;
//            final Intent intent = new Intent(captureIntent);
//            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
//            intent.setPackage(packageName);
//            intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
//            cameraIntents.add(intent);
//        }
//
//        // Filesystem.
//        final Intent galleryIntent = new Intent();
//        galleryIntent.setType("image/*");
//        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
//
//        // Chooser of filesystem options.
//        final Intent chooserIntent = Intent.createChooser(galleryIntent, "Select Source");
//
//        // Add the camera options.
//        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, cameraIntents.toArray(new Parcelable[cameraIntents.size()]));
//
//        startActivityForResult(chooserIntent, YOUR_SELECT_PICTURE_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_ACTIVITY_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                String picturePath = data.getStringExtra("picturePath");
                //perform Crop on the Image Selected from Gallery
                performCrop(picturePath);
            }
        }
        if (requestCode == RESULT_CROP) {
            if (resultCode == Activity.RESULT_OK) {
                if (data.getExtras() != null) {
                    photo = data.getExtras().getParcelable("data");
                    mCustomerAvatar.setImageBitmap(photo);

                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    photo.compress(Bitmap.CompressFormat.PNG, 0, byteArrayOutputStream);
                    byte[] imagegBytes = byteArrayOutputStream.toByteArray();

                    customerValues.put(KasebContract.Customers.COLUMN_CUSTOMER_PICTURE, imagegBytes);
                }
            }
        }

//        String picturePath;
//
//        if (requestCode == YOUR_SELECT_PICTURE_REQUEST_CODE)
//            if (resultCode == Activity.RESULT_OK) {
//                final boolean isCamera;
//                if (data == null) {
//                    isCamera = true;
//                } else {
//                    final String action = data.getAction();
//                    if (action == null) {
//                        isCamera = false;
//                    } else {
//                        isCamera = action.equals(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//                    }
//                }
//
//                if (isCamera)
//                    picturePath = outputFileUri.getPath();
//                else
//                    picturePath = data == null ? null : data.toUri(0);
//
//                //perform Crop on the Image Selected from Gallery
//                performCrop(picturePath);
//            }
//
//        if (requestCode == RESULT_CROP)
//            if (resultCode == Activity.RESULT_OK) {
//                if (data.getExtras() != null) {
//                    photo = data.getExtras().getParcelable("data");
//                    mCustomerAvatar.setImageBitmap(photo);
//
//                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//                    photo.compress(Bitmap.CompressFormat.PNG, 0, byteArrayOutputStream);
//                    byte[] imagegBytes = byteArrayOutputStream.toByteArray();
//
//                    customerValues.put(KasebContract.Customers.COLUMN_CUSTOMER_PICTURE, imagegBytes);
//                }
//            }
    }

    private void performCrop(String picUri) {
        try {
            //Start Crop Activity

            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            // indicate image type and Uri
            File f = new File(picUri);
            Uri contentUri = Uri.fromFile(f);

            cropIntent.setDataAndType(contentUri, "image/*");
            // set crop properties
            cropIntent.putExtra("crop", "true");
            // indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            // indicate output X and Y
            cropIntent.putExtra("outputX", 280);
            cropIntent.putExtra("outputY", 280);

            // retrieve data on return
            cropIntent.putExtra("return-data", true);
            // start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, RESULT_CROP);
        }
        // respond to users whose devices do not support the crop action
        catch (ActivityNotFoundException anfe) {
            // display an error message
            String errorMessage = "your device doesn't support the crop action!";
            Toast toast = Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}