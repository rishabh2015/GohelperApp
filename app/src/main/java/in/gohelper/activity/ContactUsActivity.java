package in.gohelper.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import in.gohelper.R;
import in.gohelper.models.SuccessModel;
import in.gohelper.models.staticpagesmodels.StaticPageData;
import in.gohelper.rest.ApiClient;
import in.gohelper.rest.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContactUsActivity extends BaseActivity implements AdapterView.OnItemSelectedListener {

    EditText etName, etPhone, etEmailId, etQuery;
    private StaticPageData staticPageData;
    TextInputLayout nameLayout, phoneLayout, emailIdLayout, queryLayout;
    String MobilePattern = "^(\\+91[\\-\\s]?)?[0]?(91)?[789]\\d{9}$";
    Button submitBtn;
    String item;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_us_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        etName = (EditText) findViewById(R.id.et_name);
        etPhone = (EditText) findViewById(R.id.et_phone);
        etEmailId = (EditText) findViewById(R.id.et_email_id);
        etQuery = (EditText) findViewById(R.id.et_query);
        nameLayout = (TextInputLayout) findViewById(R.id.nameLayout);
        phoneLayout = (TextInputLayout) findViewById(R.id.phoneLayout);
        emailIdLayout = (TextInputLayout) findViewById(R.id.emailIDLayout);
        queryLayout = (TextInputLayout) findViewById(R.id.queryLayout);
        submitBtn = (AppCompatButton) findViewById(R.id.saveBtn);

        etName.addTextChangedListener(new ContactUsActivity.MyTextWatcher(etName));
        etPhone.addTextChangedListener(new ContactUsActivity.MyTextWatcher(etPhone));
        etEmailId.addTextChangedListener(new ContactUsActivity.MyTextWatcher(etEmailId));


        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (submitForm()) {
                    postContactUs();
                }
            }
        });

        //get static page id from help fragment
        Intent intent = getIntent();
        if (intent != null) {
            String serviceJson = intent.getStringExtra("in.gohelper.intent.contactObject");
            if (serviceJson != null) {
                staticPageData = new Gson().fromJson(serviceJson, StaticPageData.class);
            }
        }


        ImageView ivCall = (ImageView) findViewById(R.id.iv_call);
        ivCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel://9205205203"));
                startActivity(callIntent);
            }
        });

        // Spinner element
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        // Spinner click listener
        spinner.setOnItemSelectedListener(this);
        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("Select enquiry type");
        categories.add("No Services");
        categories.add("Carreers");
        categories.add("Request for booking");
        categories.add("Help with an existing order");
        categories.add("Travel");
        categories.add("Complaints");
        categories.add("Others");
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
    }

    private Boolean submitForm() {
        if (!validateName()) {
            return false;
        }
        if (!validateMobile()) {
            return false;
        }
        if (!validateEmail()) {
            return false;
        }
        return true;
    }

    private String getName() {
        return etName.getText().toString().trim();
    }

    private String getPhoneNumber() {
        return etPhone.getText().toString().trim();
    }

    private String getEmailId() {
        return etEmailId.getText().toString().trim();
    }

    private String getQuery() {
        return etQuery.getText().toString().trim();
    }

    private boolean validateName() {
        if (getName().trim().isEmpty()) {
            nameLayout.setError(getString(R.string.err_msg_name));
            requestFocus(etName);
            return false;
        } else if (!getName().matches("^[a-zA-Z]+[\\-'\\s]?[a-zA-Z ]+$")) {
            nameLayout.setError("Enter Only Alphabetical Character");
            requestFocus(etName);
            return false;
        } else {
            nameLayout.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateMobile() {
        if (getPhoneNumber().trim().isEmpty()) {
            phoneLayout.setError(getString(R.string.err_msg_mobile));
            requestFocus(etPhone);
            return false;
        } else if (!getPhoneNumber().matches(MobilePattern)) {
            phoneLayout.setError(getString(R.string.err_invalid_mobile));
            requestFocus(etPhone);
            return false;

        } else {
            phoneLayout.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateEmail() {
        String email = getEmailId();
        if (email.isEmpty() || !isValidEmail(email)) {
            emailIdLayout.setError(getString(R.string.err_msg_email));
            requestFocus(etEmailId);
            return false;
        } else {
            emailIdLayout.setErrorEnabled(false);
        }
        return true;
    }

    private boolean isValidEmail(String email) {
        boolean check;
        Pattern p;
        Matcher m;
        String EMAIL_STRING = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        p = Pattern.compile(EMAIL_STRING);
        m = p.matcher(email);
        check = m.matches();

        if (!check) {
            emailIdLayout.setError("Not Valid Email");
        }
        return check;
    }

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
                case R.id.et_name:
                    validateName();
                    break;
                case R.id.et_phone:
                    validateMobile();
                    break;
                case R.id.et_email_id:
                    validateEmail();
                    break;
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        item = parent.getItemAtPosition(position).toString();

        // Showing selected spinner item
        //Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }

    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

    public void postContactUs() {
        showProgress();
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<SuccessModel> call = apiInterface.getContactUs(staticPageData.getId(), getName(), getPhoneNumber(), getEmailId(), item, getQuery());
        call.enqueue(new Callback<SuccessModel>() {

            @Override
            public void onResponse(Call<SuccessModel> call, Response<SuccessModel> response) {
                dismissProgress();
                Intent intent = new Intent(getApplicationContext(), ContactUsActivity.class);
                startActivity(intent);
            }

            @Override
            public void onFailure(Call<SuccessModel> call, Throwable t) {
                dismissProgress();
            }
        });
    }

}
