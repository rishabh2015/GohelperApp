package in.gohelper.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import in.gohelper.R;
import in.gohelper.models.otpmodels.OtpModel;
import in.gohelper.models.otpverifymodels.OtpVerifyData;
import in.gohelper.models.otpverifymodels.OtpVerifyModel;
import in.gohelper.rest.ApiClient;
import in.gohelper.rest.ApiInterface;
import in.gohelper.utils.Constants;
import in.gohelper.utils.PreferencesManager;
import in.gohelper.utils.SmsReceiver;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OtpVerifyActivity extends BaseActivity {
    AppCompatEditText etVerifyOtp;
    String OTP;
    AppCompatTextView tvTimer, tvResend;
    SmsReceiver receiver;
    String TYPE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp);

        AppCompatTextView editPhoneNumber = (AppCompatTextView) findViewById(R.id.edit_phone_number);
        AppCompatButton buttonVerify = (AppCompatButton) findViewById(R.id.buttonVerify);
        etVerifyOtp = (AppCompatEditText) findViewById(R.id.et_verify);
        tvTimer = (AppCompatTextView) findViewById(R.id.tv_timer);
        tvResend = (AppCompatTextView) findViewById(R.id.tvResend);

        //add custom font
        if (!Constants.USE_CUSTOM_FONT) {
            String fontPath = Constants.CUSTOM_NAME_SIMPLE;
            Typeface tf = Typeface.createFromAsset(OtpVerifyActivity.this.getAssets(), fontPath);
            editPhoneNumber.setTypeface(tf);
            buttonVerify.setTypeface(tf);
        }

        if (getIntent() != null) {
            TYPE = getIntent().getStringExtra("TYPE");
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        receiver = new SmsReceiver();
        receiver.setMainActivityHandler(this);
        new PreferencesManager(this);

        countDownTimer();

        editPhoneNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgress();
                Intent intent = new Intent(OtpVerifyActivity.this, MobileRegistrationActivity.class);
                startActivity(intent);
            }
        });
    }

    public void receiveSms(String otp) {
        OTP = otp;
        Toast.makeText(this, "" + otp, Toast.LENGTH_SHORT).show();
        etVerifyOtp.setText(otp);
    }

    public void onClickOtpVerify(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        if (etVerifyOtp.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please enter the verification number", Toast.LENGTH_SHORT).show();
        }
       /* else if (!etVerifyOtp.getText().toString().equals(OTP)){
            setToast("You have entered wrong OTP");
        }*/
        else {
            postOtpVerifyServer();
        }
    }

    public void postOtpVerifyServer() {
        showProgress();
        String otp = etVerifyOtp.getText().toString();
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<OtpVerifyModel> call = apiInterface.verifyOtp(PreferencesManager.getPrefString(Constants.GUEST_ID), PreferencesManager.getPrefString(Constants.MOBILE), otp);
        call.enqueue(new Callback<OtpVerifyModel>() {
            @Override
            public void onResponse(Call<OtpVerifyModel> call, Response<OtpVerifyModel> response) {
                dismissProgress();

                OtpVerifyModel model = response.body();
                if (model != null) {
                    OtpVerifyData data = model.getData();
                    if (data != null) {
                        boolean success = data.getSuccess();
                        if (success == true) {
                            String token = data.getAuthToken();
                            String authTokenType = data.getAuthTokenType();
                            Integer customerId = data.getCustomerId();
                            String customerName = data.getCustomerName();

                            //Save to preferences
                            PreferencesManager.setPrefString(Constants.AUTH_TOKEN, token);
                            PreferencesManager.setPrefString(Constants.AUTH_TOKEN_TYPE, authTokenType);
                            PreferencesManager.setPrefInt(Constants.CUSTOMER_ID, customerId);
                            PreferencesManager.setPrefBool(Constants.MOBILE_VERIFIED, true);

                            if (TextUtils.isEmpty(customerName)) {
                                startActivity(new Intent(OtpVerifyActivity.this, MyAccountActivity.class));
                            } else if (runningTaskCount() == 1) {
                                PreferencesManager.setPrefString(Constants.CUSTOMER_NAME, customerName);
                                startActivity(new Intent(OtpVerifyActivity.this, MainActivity.class));
                            }
                           /* else if(TYPE !=null){
                                startActivity(new Intent(OtpVerifyActivity.this,ReviewActivity.class));

                            }*/
                            finish();
                        } else {
                            Toast.makeText(OtpVerifyActivity.this, "Wrong OTP Please try again", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        //TODO:Error
                    }
                } else {
                    //TODO:Error
                }
            }

            @Override
            public void onFailure(Call<OtpVerifyModel> call, Throwable t) {
                dismissProgress();
               // Toast.makeText(OtpVerifyActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onClickResend(View view) {
        tvResend.setVisibility(View.GONE);
        getOtpFromServer();
    }

    public void getOtpFromServer() {
        showProgress();
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<OtpModel> call = apiInterface.getOtp(PreferencesManager.getPrefString(Constants.GUEST_ID), PreferencesManager.getPrefString(Constants.MOBILE));
        call.enqueue(new Callback<OtpModel>() {
            @Override
            public void onResponse(Call<OtpModel> call, Response<OtpModel> response) {
                dismissProgress();
                countDownTimer();
            }

            @Override
            public void onFailure(Call<OtpModel> call, Throwable t) {
                dismissProgress();
            }
        });
    }

    public void countDownTimer() {
        tvTimer.setVisibility(View.VISIBLE);
        new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if ((millisUntilFinished / 1000) < 10)
                    tvTimer.setText("00" + " : 0" + String.valueOf(millisUntilFinished / 1000));

                else
                    tvTimer.setText("00" + " : " + String.valueOf(millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                tvTimer.setText("00:00");
                tvTimer.setVisibility(View.GONE);
                tvResend.setVisibility(View.VISIBLE);
            }
        }.start();
    }
}
