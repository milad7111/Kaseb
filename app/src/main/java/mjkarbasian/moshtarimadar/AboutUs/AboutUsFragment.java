package mjkarbasian.moshtarimadar.AboutUs;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import mjkarbasian.moshtarimadar.R;

/**
 * Created by Morteza on 10/19/2016.
 */
public class AboutUsFragment extends Fragment {
    private static final String LOG_TAG = AboutUsFragment.class.getSimpleName();

    EditText yourText;

    Button btnStartProgress;
    ProgressDialog progressBar;
    private int progressBarStatus = 0;
    private Handler progressBarHandler = new Handler();
    private long fileSize = 0;

    public AboutUsFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void addListenerOnButton(View view) {

        btnStartProgress = (Button) view.findViewById(R.id.btnStartProgress);
        btnStartProgress.setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // prepare for a progress bar dialog
                        progressBar = new ProgressDialog(v.getContext());
                        progressBar.setCancelable(true);
                        progressBar.setMessage("File downloading ...");
                        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        progressBar.setProgress(0);
                        progressBar.setMax(100);
                        progressBar.show();

                        //reset progress bar status
                        progressBarStatus = 0;

                        //reset filesize
                        fileSize = 0;

                        new Thread(new Runnable() {
                            public void run() {
                                while (progressBarStatus < 100) {

                                    // process some tasks
                                    progressBarStatus = doSomeTasks();

                                    // your computer is too fast, sleep 1 second
                                    try {
                                        Thread.sleep(1000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }

                                    // Update the progress bar
                                    progressBarHandler.post(new Runnable() {
                                        public void run() {
                                            progressBar.setProgress(progressBarStatus);
                                        }
                                    });
                                }

                                // ok, file is downloaded,
                                if (progressBarStatus >= 100) {

                                    // sleep 2 seconds, so that you can see the 100%
                                    try {
                                        Thread.sleep(2000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }

                                    // close the progress bar dialog
                                    progressBar.dismiss();
                                }
                            }
                        }).start();
                    }
                });
    }

    // file download simulator... a really simple
    public int doSomeTasks() {

        while (fileSize <= 1000000) {

            fileSize++;

            if (fileSize == 100000) {
                return 10;
            } else if (fileSize == 200000) {
                return 20;
            } else if (fileSize == 300000) {
                return 30;
            }
            // ...add your own

        }

        return 100;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about_us, container, false);
        addListenerOnButton(view);
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

                yourText = (EditText) getView().findViewById(R.id.about_us_your_text);
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
