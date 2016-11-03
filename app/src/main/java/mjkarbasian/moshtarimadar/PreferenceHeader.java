package mjkarbasian.moshtarimadar;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import mjkarbasian.moshtarimadar.adapters.HeaderAdaper;

/**
 * Created by family on 11/3/2016.
 */
public class PreferenceHeader extends Fragment {
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
        return rootView;
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
