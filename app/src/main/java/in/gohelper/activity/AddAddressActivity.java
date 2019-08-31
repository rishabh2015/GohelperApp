package in.gohelper.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;

import in.gohelper.R;
import in.gohelper.models.CustomerAddressmodels.CustomerAddressData;
import in.gohelper.models.CustomerAddressmodels.CustomerAddressUpdateModel;
import in.gohelper.rest.ApiClient;
import in.gohelper.rest.ApiInterface;
import in.gohelper.utils.Constants;
import in.gohelper.utils.PreferencesManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddAddressActivity extends BaseActivity {
    private EditText fName, phone, locality, street, landmark;
    private String FullName, Locality, address, Landmark, phoneNumber;
    private TextInputLayout fNameLayout, phoneLayout, localityLayout, streetLayout, landmarkLayout;
    private Toolbar toolbar;
    private CustomerAddressData customerAddressObject = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //To get address object from ManageAddressActivity
        Intent intent = getIntent();
        if (intent != null) {
            String addressJson = intent.getStringExtra("in.gohelper.intent.addressObject");
            if (addressJson != null) {
                customerAddressObject = new Gson().fromJson(addressJson, CustomerAddressData.class);
            }
        }

        setContentView(R.layout.add_address_layout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fName = (EditText) findViewById(R.id.fName);
        phone = (EditText) findViewById(R.id.phone);
        locality = (EditText) findViewById(R.id.locality);
        street = (EditText) findViewById(R.id.street);
        landmark = (EditText) findViewById(R.id.landmark);
        fNameLayout = (TextInputLayout) findViewById(R.id.fNameLayout);
        phoneLayout = (TextInputLayout) findViewById(R.id.phoneLayout);
        localityLayout = (TextInputLayout) findViewById(R.id.localityLayout);
        streetLayout = (TextInputLayout) findViewById(R.id.streetLayout);
        landmarkLayout = (TextInputLayout) findViewById(R.id.landmarkLayout);

        if (customerAddressObject == null) {
            toolbar.setTitle("Add Address");
        } else {
            toolbar.setTitle("Edit Address");

            fName.setText(customerAddressObject.getFullName());
            phone.setText(customerAddressObject.getPhoneNumber());
            locality.setText(customerAddressObject.getLocality());
            street.setText(customerAddressObject.getAddress());
            landmark.setText(customerAddressObject.getLandmark());
        }

        Button btn = (Button) findViewById(R.id.saveBtn);
        fName.addTextChangedListener(new MyTextWatcher(fName));
        phone.addTextChangedListener(new MyTextWatcher(phone));
        locality.addTextChangedListener(new MyTextWatcher(locality));
        street.addTextChangedListener(new MyTextWatcher(street));
        landmark.addTextChangedListener(new MyTextWatcher(landmark));

        //To add custom font
        if (!Constants.USE_CUSTOM_FONT) {
            String fontPath = Constants.CUSTOM_NAME_SIMPLE;
            Typeface tf = Typeface.createFromAsset(getApplicationContext().getAssets(), fontPath);
            btn.setTypeface(tf);
            fNameLayout.setTypeface(tf);
            phoneLayout.setTypeface(tf);
            localityLayout.setTypeface(tf);
            streetLayout.setTypeface(tf);
            landmarkLayout.setTypeface(tf);
            fName.setTypeface(tf);
            phone.setTypeface(tf);
            locality.setTypeface(tf);
            street.setTypeface(tf);
            landmark.setTypeface(tf);
            btn.setTypeface(tf);
        }

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FullName = fName.getText().toString();
                phoneNumber = phone.getText().toString();
                Locality = locality.getText().toString();
                address = street.getText().toString();
                Landmark = landmark.getText().toString();
                Locality = locality.getText().toString();
                if (submitForm()) {
                    int addressId = 0;
                    if (customerAddressObject != null) {
                        addressId = customerAddressObject.getId();
                    }
                    customerAddressUpdateOnServer(addressId);
                }
            }
        });
    }

    private Boolean submitForm() {
        if (!validateName()) {
            return false;
        }
        if (!validateMobile()) {
            return false;
        }
        if (!validateLocality()) {
            return false;
        }
        if (!validateAddress()) {
            return false;
        }
       /* if (!validateLandmark()) {
            return false;
        }*/
        return true;
    }

    private String getFullName() {
        return fName.getText().toString().trim();
    }

    private String getPhoneNumber() {
        return phone.getText().toString().trim();
    }

    private String getLocality() {
        return locality.getText().toString().trim();
    }

    private String getAddress() {
        return street.getText().toString().trim();
    }

    private String getLandmark() {
        return landmark.getText().toString().trim();
    }

    private boolean validateName() {
        if (getFullName().trim().isEmpty()) {
            fNameLayout.setError(getString(R.string.err_msg_name));
            requestFocus(fName);
            return false;
        } else if (!getFullName().matches("^[a-zA-Z]+[\\-'\\s]?[a-zA-Z ]+$")) {
            fNameLayout.setError("Enter Only Alphabetical Character");
            requestFocus(fName);
            return false;
        } else {
            fNameLayout.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateMobile() {
        if (getPhoneNumber().trim().isEmpty()) {
            phoneLayout.setError(getString(R.string.err_msg_mobile));
            requestFocus(phone);
            return false;
        } else if (!getPhoneNumber().matches("^(\\+91[\\-\\s]?)?[0]?(91)?[789]\\d{9}$")) {
            phoneLayout.setError(getString(R.string.err_invalid_mobile));
            requestFocus(phone);
            return false;

        } else {
            phoneLayout.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateLocality() {
        if (getLocality().trim().isEmpty()) {
            localityLayout.setError(getString(R.string.err_msg_locality));
            requestFocus(locality);
            return false;
        } else {
            localityLayout.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateAddress() {
        if (getAddress().trim().isEmpty()) {
            streetLayout.setError(getString(R.string.err_msg_address));
            requestFocus(street);
            return false;
        } else {
            streetLayout.setErrorEnabled(false);
        }
        return true;
    }

  /*  private boolean validateLandmark() {
        if (getLandmark().trim().isEmpty()) {
            landmarkLayout.setError(getString(R.string.err_msg_landmark));
            requestFocus(landmark);
            return false;
        } else {
            landmarkLayout.setErrorEnabled(false);
        }
        return true;
    }*/

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private class MyTextWatcher implements TextWatcher {
        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.fName:
                    validateName();
                    break;
                case R.id.phone:
                    validateMobile();
                    break;
                case R.id.locality:
                    validateLocality();
                    break;
                case R.id.street:
                    validateMobile();
                    break;
                case R.id.landmark:
                    validateAddress();
                    break;
            }
        }
    }

    public void customerAddressUpdateOnServer(int addressId) {
        showProgress();
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        if (addressId > 0) {
            Call<CustomerAddressUpdateModel> call = apiInterface.updateCustomerAddress(PreferencesManager.getPrefString(Constants.AUTH_TOKEN_TYPE) + " " + PreferencesManager.getPrefString(Constants.AUTH_TOKEN), addressId, FullName, phoneNumber, Locality, address, Landmark, "201001");
            call.enqueue(new Callback<CustomerAddressUpdateModel>() {

                @Override
                public void onResponse(Call<CustomerAddressUpdateModel> call, Response<CustomerAddressUpdateModel> response) {
                    dismissProgress();
                    finish();
                }

                @Override
                public void onFailure(Call<CustomerAddressUpdateModel> call, Throwable t) {
                    dismissProgress();
                }
            });
        } else {
            Call<CustomerAddressUpdateModel> call = apiInterface.customerAddress(PreferencesManager.getPrefString(Constants.AUTH_TOKEN_TYPE) + " " + PreferencesManager.getPrefString(Constants.AUTH_TOKEN), FullName, phoneNumber, Locality, address, Landmark, "201001");
            call.enqueue(new Callback<CustomerAddressUpdateModel>() {

                @Override
                public void onResponse(Call<CustomerAddressUpdateModel> call, Response<CustomerAddressUpdateModel> response) {
                    dismissProgress();
                    finish();
                }

                @Override
                public void onFailure(Call<CustomerAddressUpdateModel> call, Throwable t) {
                    dismissProgress();
                }
            });
        }
    }


}




