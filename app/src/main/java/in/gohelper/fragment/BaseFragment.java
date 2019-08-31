package in.gohelper.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import in.gohelper.R;

public class BaseFragment extends Fragment {
TextView baseText;
    String text;

    public BaseFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_base, container, false);
        baseText = (TextView) view.findViewById(R.id.basetext);
        return view;
    }
}
