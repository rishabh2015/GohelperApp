package in.gohelper.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import in.gohelper.R;

/**
 * Created by ashishgupta on 13/09/16.
 */
public class MyWalletFragment extends Fragment {


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootview =inflater.inflate(R.layout.fragment_my_wallet,container,false);
        return rootview;
    }
}
