package in.gohelper.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import in.gohelper.R;

/**
 * Created by ashishgupta on 13/09/16.
 */
public class ReferEarnFragment extends Fragment {
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootview =inflater.inflate(R.layout.fragment_refer_earn,container,false);
        return rootview;
    }
}
