package mjkarbasian.moshtarimadar.AboutUs;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import mjkarbasian.moshtarimadar.Helpers.Utility;
import mjkarbasian.moshtarimadar.R;

/**
 * Created by Morteza on 10/19/2016.
 */
public class AboutUsFragment extends Fragment {
    private static final String LOG_TAG = AboutUsFragment.class.getSimpleName();

    EditText yourText;

    public AboutUsFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about_us, container, false);
        yourText = (EditText) view.findViewById(R.id.about_us_your_text);
        final TextInputLayout mTextInputLayout = (TextInputLayout) view.findViewById(R.id.text_input_layout_about_us_your_text);

        yourText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int aft) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                // this will show characters remaining
                mTextInputLayout.setError(200 - s.toString().length() + "/200");
                Utility.changeColorOfHelperText(getActivity(), mTextInputLayout, R.color.colorPrimaryLight);
            }
        });
        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_about_us, menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_about_us_send: {

                if (yourText.getText().toString().equals("") || yourText.getText().toString().equals(null))
                    Toast.makeText(getContext(), "Please write your Opinion, Next try send it!", Toast.LENGTH_SHORT).show();
                else {
                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("message/rfc822");
                    i.putExtra(Intent.EXTRA_EMAIL, new String[]{"androidteam@chmail.ir"});
                    i.putExtra(Intent.EXTRA_SUBJECT, "Opinion from customer");
                    i.putExtra(Intent.EXTRA_TEXT, yourText.getText().toString());
                    try {
                        startActivity(Intent.createChooser(i, "Send mail..."));
                    } catch (android.content.ActivityNotFoundException ex) {
                        Toast.makeText(getContext(), "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
