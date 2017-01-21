package mjkarbasian.moshtarimadar.Setting;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

import mjkarbasian.moshtarimadar.Helpers.Utility;
import mjkarbasian.moshtarimadar.R;

/**
 * Created by Morteza on 10/19/2016.
 */
public class TourFragment extends Fragment {

    //region declare values
    String whichTour;
    int mIndexOfImages = 0;
    int mQuantityOfImages = 0;
    ImageView mImageView;
    ArrayList<Integer> tourImages = new ArrayList<>();
    //endregion declare values

    public TourFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_take_tour, container, false);
        whichTour = getArguments().getString("whichTour");

        //region add images to tour
        switch (whichTour) {
            case "0": { //insert_customer
                tourImages.add(R.drawable.insert_customer_1);
                tourImages.add(R.drawable.insert_customer_2);
                tourImages.add(R.drawable.insert_customer_3);
                tourImages.add(R.drawable.insert_customer_4);
                tourImages.add(R.drawable.insert_customer_5);
                tourImages.add(R.drawable.insert_customer_6);
                tourImages.add(R.drawable.insert_customer_7);
                tourImages.add(R.drawable.insert_customer_8);

                mQuantityOfImages = 8;

                mIndexOfImages = 0;
                mImageView = (ImageView) view.findViewById(R.id.image_view_fragment_take_tour);
                mImageView.setImageDrawable(getResources().getDrawable(tourImages.get(0)));

                mImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        mIndexOfImages++;
                        mIndexOfImages = mIndexOfImages % mQuantityOfImages;

                        Utility.ImageViewAnimatedChange(getActivity(), mImageView, BitmapFactory.decodeResource(getResources(),
                                tourImages.get(mIndexOfImages)));
                    }
                });

                break;
            }
            case "1": { //insert_product

                mQuantityOfImages = 0;
                break;
            }
            case "2": { //insert_sale_and_print

                mQuantityOfImages = 0;
                break;
            }
            case "3": { //insert_cost

                mQuantityOfImages = 0;
                break;
            }
            case "4": { //change_setting

                mQuantityOfImages = 0;
                break;
            }
            case "5": { //view_sale_and_print

                mQuantityOfImages = 0;
                break;
            }
            case "6": { //contact_debtors

                mQuantityOfImages = 0;
                break;
            }
            case "7": { //view_reports

                mQuantityOfImages = 0;
                break;
            }
        }
        //endregion add images to tour
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_tour_guide, menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.tour_menu_cancel_button: {

                getFragmentManager().popBackStackImmediate();

                break;
            }
            case R.id.tour_menu_back_button: {

                if (mIndexOfImages != 0) {

                    mIndexOfImages--;
                    mIndexOfImages = mIndexOfImages % mQuantityOfImages;

                    Utility.ImageViewAnimatedChange(getActivity(), mImageView, BitmapFactory.decodeResource(getResources(),
                            tourImages.get(mIndexOfImages % mQuantityOfImages)));
                }

                break;
            }
            case R.id.tour_menu_forward_button: {

                if (mIndexOfImages != mQuantityOfImages - 1) {

                    mIndexOfImages++;
                    mIndexOfImages = mIndexOfImages % mQuantityOfImages;

                    Utility.ImageViewAnimatedChange(getActivity(), mImageView, BitmapFactory.decodeResource(getResources(),
                            tourImages.get(mIndexOfImages)));
                }

                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
