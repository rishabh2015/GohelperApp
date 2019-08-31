package in.gohelper.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import in.gohelper.R;


public class MobieVerificationActivity extends Activity {
    ProgressBar mProgressBar;
    Toolbar toolbar;
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragement_sms_verification);
        TextView tv = (TextView) findViewById(R.id.text_view);
        tv.setText(getIntent().getExtras().getString("PhoneNumber"));
        toolbar = (Toolbar) findViewById(R.id.toolbar);


    }
}
