package in.gohelper.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;

import com.crashlytics.android.Crashlytics;

import in.gohelper.R;
import in.gohelper.utils.Constants;
import in.gohelper.utils.PreferencesManager;
import io.fabric.sdk.android.Fabric;

public class SplashActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_splash);

  /*      AppCompatTextView tvGohelper = (AppCompatTextView) findViewById(R.id.tv_gohelper);

        //To add custom font
        if (!Constants.USE_CUSTOM_FONT) {
            String fontPath = Constants.CUSTOM_NAME_SIMPLE;
            Typeface tf = Typeface.createFromAsset(getApplicationContext().getAssets(), fontPath);
            tvGohelper.setTypeface(tf);
        }*/

        new PreferencesManager(this);
        int SPLASH_TIME_OUT = 3000;
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                if (PreferencesManager.getPrefBool(Constants.MOBILE_VERIFIED) || PreferencesManager.getPrefBool(Constants.SKIP)) {
                    String name = PreferencesManager.getPrefString(Constants.CUSTOMER_NAME);
                    if (TextUtils.isEmpty(name)) {
                        Intent i = new Intent(SplashActivity.this, MyAccountActivity.class);
                        startActivity(i);
                    } else {
                        Intent i = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(i);
                    }
                } else {
                    Intent i = new Intent(SplashActivity.this, MobileRegistrationActivity.class);
                    startActivity(i);
                }
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
