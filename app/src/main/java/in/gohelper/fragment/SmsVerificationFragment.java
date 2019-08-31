package in.gohelper.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import in.gohelper.R;

public class SmsVerificationFragment extends Fragment {
    ProgressBar progress;
    Button btn;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootview =inflater.inflate(R.layout.fragement_sms_verification,container,false);

        TextView tv=(TextView)rootview.findViewById(R.id.text_view);
        tv.setText(getArguments().getString("PhoneNumber"));
        progress=(ProgressBar)rootview.findViewById(R.id.progressBar);
        btn=(Button)rootview.findViewById(R.id.btnGetOTP);
        return rootview;
    }




}
