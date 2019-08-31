package in.gohelper.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import in.gohelper.R;


public class GetOtpFragment extends Fragment {


    String MobilePattern = "^(\\+91[\\-\\s]?)?[0]?(91)?[789]\\d{9}$";

    private EditText passEditText;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootview =inflater.inflate(R.layout.fragment_get_otp,container,false);

        passEditText = (EditText)  rootview.findViewById(R.id.et_mobile_number);
        Button btn=(Button) rootview.findViewById(R.id.btnGetOTP);
 /*       btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                final String pass = passEditText.getText().toString();
                if (!isValidPassword(pass)) {
                    passEditText.setError("Invalid Phone Number");
                }
                else {
                    ((MobileRegistrationActivity)getActivity()).openSMSVerificationFragement(passEditText.getText().toString());
                }
            }
        });*/

        return rootview;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle("Mobile Signup");
    }

    // validating password with retype password
    private boolean isValidPassword(String pass) {
        if (pass != null && passEditText.getText().toString().matches(MobilePattern)) {
            return true;


        }else {
            Toast.makeText(getActivity(), "Please enter valid 10 digit phone number", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

}

