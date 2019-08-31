package in.gohelper.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import in.gohelper.R;
import in.gohelper.models.guestmodels.GuestIdModel;
import in.gohelper.models.otpmodels.OtpModel;
import in.gohelper.rest.ApiClient;
import in.gohelper.rest.ApiInterface;
import in.gohelper.utils.Constants;
import in.gohelper.utils.PreferencesManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MobileRegistrationActivity extends BaseActivity {

    AppCompatTextView textView;
    AppCompatEditText etMobileNumber;
    String deviceId, mobileNumber;
    String TYPE;
    String MobilePattern = "^(\\+91[\\-\\s]?)?[0]?(91)?[789]\\d{9}$";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_registration);

        AppCompatTextView textViewEnterNumber = (AppCompatTextView) findViewById(R.id.textViewGetOTP);
        textView = (AppCompatTextView) findViewById(R.id.skip);
        AppCompatButton btnGetOTP = (AppCompatButton) findViewById(R.id.btnGetOTP);
        etMobileNumber = (AppCompatEditText) findViewById(R.id.et_mobile_number);

        //add custom font
        if (!Constants.USE_CUSTOM_FONT) {
            String fontPath = Constants.CUSTOM_NAME_SIMPLE;
            Typeface tf = Typeface.createFromAsset(MobileRegistrationActivity.this.getAssets(), fontPath);
            textViewEnterNumber.setTypeface(tf);
            textView.setTypeface(tf);
            btnGetOTP.setTypeface(tf);
        }

        new PreferencesManager(this);

        if (getIntent() != null) {
            TYPE = getIntent().getStringExtra("TYPE");
        }
        deviceId = getId();
        Log.e("Device Id", deviceId);
        sendDeviceIdToServer();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PreferencesManager.setPrefBool(Constants.SKIP, true);
                Intent i = new Intent(MobileRegistrationActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    private boolean isValidMobileNumber(String pass) {
        if (pass != null && etMobileNumber.getText().toString().matches(MobilePattern)) {
            return true;

        } else {
            Toast.makeText(getApplicationContext(), "Please enter the correct phone number", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public void onClickGetOtp(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        mobileNumber = etMobileNumber.getText().toString();
        if (mobileNumber.isEmpty()) {
            etMobileNumber.setError("Phone number can not be empty");
        } else if (!isValidMobileNumber(mobileNumber)) {
            etMobileNumber.setError("Invalid Phone Number");
        } else {
            getOtpFromServer();
        }
    }

    public String getId() {
        return android.provider.Settings.System.getString(super.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
    }

    public void getOtpFromServer() {
        showProgress();
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<OtpModel> call = apiInterface.getOtp(PreferencesManager.getPrefString(Constants.GUEST_ID), mobileNumber);
        call.enqueue(new Callback<OtpModel>() {
            @Override
            public void onResponse(Call<OtpModel> call, Response<OtpModel> response) {
                dismissProgress();
                PreferencesManager.setPrefString(Constants.MOBILE, mobileNumber);
                startActivity(new Intent(MobileRegistrationActivity.this, OtpVerifyActivity.class));

               /* if (TYPE!=null){
                    startActivity(new Intent(MobileRegistrationActivity.this, OtpVerifyActivity.class));
                }
                else {
                    startActivity(new Intent(MobileRegistrationActivity.this, OtpVerifyActivity.class).putExtra("TYPE",TYPE));
                    finish();
                }*/
                finish();
            }

            @Override
            public void onFailure(Call<OtpModel> call, Throwable t) {
                dismissProgress();
            }
        });
    }

    public void sendDeviceIdToServer() {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<GuestIdModel> call = apiInterface.getGuestId(deviceId);
        call.enqueue(new Callback<GuestIdModel>() {
            @Override
            public void onResponse(Call<GuestIdModel> call, Response<GuestIdModel> response) {
                if (response != null) {
                    if (response.body() != null) {
                        if (response.body().getData() != null) {
                            String guestId = response.body().getData().getGuestId();
                            if (guestId != null) {
                                PreferencesManager.setPrefString(Constants.GUEST_ID, guestId);
                            }
                        } else {
                        }
                    } else {
                    }
                } else {
                }
            }

            @Override
            public void onFailure(Call<GuestIdModel> call, Throwable t) {
                // Log error here since request failed
                Log.e("", t.toString());
            }
        });
    }
}

