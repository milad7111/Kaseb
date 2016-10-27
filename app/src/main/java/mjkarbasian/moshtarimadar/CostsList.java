package mjkarbasian.moshtarimadar;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import java.text.ParseException;
import mjkarbasian.moshtarimadar.adapters.CostAdapter;
import mjkarbasian.moshtarimadar.helper.Utility;
import static mjkarbasian.moshtarimadar.helper.Samples.costs;
import static mjkarbasian.moshtarimadar.helper.Samples.costsCode;
import static mjkarbasian.moshtarimadar.helper.Samples.costsDue;
import static mjkarbasian.moshtarimadar.helper.Samples.setCost;
import static mjkarbasian.moshtarimadar.helper.Samples.setCostAmount;
import static mjkarbasian.moshtarimadar.helper.Samples.setCostCode;
import static mjkarbasian.moshtarimadar.helper.Samples.setCostDue;
import static mjkarbasian.moshtarimadar.helper.Samples.setCostNames;

/**
 * A placeholder fragment containing a simple view.
 */
public class CostsList extends Fragment {
    CostAdapter mCostAdapter;
    ListView mListView;

    public CostsList() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mCostAdapter = new CostAdapter(getActivity());
        Boolean todayCheck;
        int todayCounter = 0;
        Boolean weekCheck;
        int weekCounter = 0;
        Boolean monthCheck;
        int monthCounter = 0;
        int olderCounter = 0;

        if (costsCode.size() == 0) {
            setCostCode();
            setCostDue();
            setCostNames(getActivity());
            setCostAmount();
            setCost(getActivity());
        }

        for (String date : costsDue) {
            try {
                todayCheck = Utility.isToday(date);
                if (todayCheck) todayCounter++;
                weekCheck = Utility.isThisWeek(date);
                if (weekCheck && !todayCheck) weekCounter++;
                monthCheck = Utility.isThisMonth(date);
                if (monthCheck && !weekCheck && !todayCheck) monthCounter++;
                if (!monthCheck && !weekCheck && !todayCheck) olderCounter++;
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
        if (todayCounter > 0) {
            mCostAdapter.addSectionHeaderItem(getString(R.string.header_today));
            for (int i = 0; i < costs.get(0).size(); i++) {
                try {
                    if (Utility.isToday(costs.get(0).get(i)))
                        mCostAdapter.addItem(costs.get(1).get(i));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        if (weekCounter > 0) {
            mCostAdapter.addSectionHeaderItem(getString(R.string.header_this_week));
            for (int i = 0; i < costs.get(0).size(); i++) {
                try {
                    if (!Utility.isToday(costs.get(0).get(i)) && Utility.isThisWeek(costs.get(0).get(i)))
                        mCostAdapter.addItem(costs.get(1).get(i));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        if (monthCounter > 0) {
            mCostAdapter.addSectionHeaderItem(getString(R.string.header_this_month));
            for (int i = 0; i < costs.get(0).size(); i++) {
                try {
                    if (!Utility.isToday(costs.get(0).get(i)) && !Utility.isThisWeek(costs.get(0).get(i)) && Utility.isThisMonth(costs.get(0).get(i)))
                        mCostAdapter.addItem(costs.get(1).get(i));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        if (olderCounter > 0) {
            mCostAdapter.addSectionHeaderItem(getString(R.string.header_older));
            for (int i = 0; i < costs.get(0).size(); i++) {
                try {
                    if (!Utility.isToday(costs.get(0).get(i)) && !Utility.isThisWeek(costs.get(0).get(i)) && !Utility.isThisMonth(costs.get(0).get(i)))
                        mCostAdapter.addItem(costs.get(1).get(i));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }

        View rootView = inflater.inflate(R.layout.fragment_costs, container, false);
        mListView = (ListView) rootView.findViewById(R.id.list_view_costs);
        mListView.setAdapter(mCostAdapter);
        return rootView;
    }


}
