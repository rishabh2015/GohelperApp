package in.gohelper.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import in.gohelper.R;
import in.gohelper.models.SuccessModel;
import in.gohelper.models.customerprofilemodels.CustomerProfileModel;
import in.gohelper.rest.ApiClient;
import in.gohelper.rest.ApiInterface;
import in.gohelper.utils.Constants;
import in.gohelper.utils.PreferencesManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyAccountActivity extends BaseActivity {

    Toolbar toolbar;
    private static final String TAG = MainActivity.class.getSimpleName();
    CallbackManager callbackManager;
    AccessTokenTracker accessTokenTracker;
    AppCompatButton saveBtn;
    TextView tvManageAddress;
    AppCompatButton loginButton;
    ImageView iv_profile_pic;
    EditText etFirstName, etLastName, etEmailId, etProfessinalField;
    TextInputLayout firstNameLayout, lastNameLayout, emailIdLayout, professionFieldLayout;
    String accessToken;
    private ProfileTracker profileTracker;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private String strBase64;
    private ImageView imgProfilePicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.my_account_activity);
        customerProfileFromServer();

        //Set the toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//      tv_profile_name = (TextView) findViewById(R.id.tv_profile_name);
        iv_profile_pic = (ImageView) findViewById(R.id.iv_profile_pic);
        loginButton = (AppCompatButton) findViewById(R.id.login_button);
        saveBtn = (AppCompatButton) findViewById(R.id.saveBtn);

        etFirstName = (EditText) findViewById(R.id.first_name);
        etLastName = (EditText) findViewById(R.id.last_name);
        etEmailId = (EditText) findViewById(R.id.emaill_id);
        etProfessinalField = (EditText) findViewById(R.id.professional_field);

        firstNameLayout = (TextInputLayout) findViewById(R.id.firstNameLayout);
        lastNameLayout = (TextInputLayout) findViewById(R.id.lastNameLayout);
        emailIdLayout = (TextInputLayout) findViewById(R.id.emailIDLayout);
        professionFieldLayout = (TextInputLayout) findViewById(R.id.professionalFieldLayout);

        etFirstName.addTextChangedListener(new MyTextWatcher(etFirstName));
        etLastName.addTextChangedListener(new MyTextWatcher(etLastName));
        etEmailId.addTextChangedListener(new MyTextWatcher(etEmailId));
        etProfessinalField.addTextChangedListener(new MyTextWatcher(etProfessinalField));
        // tvManageAddress = (TextView) findViewById(R.id.manage_address);


        //To add custom font
        if (!Constants.USE_CUSTOM_FONT) {
            String fontPath = Constants.CUSTOM_NAME_SIMPLE;
            Typeface tf = Typeface.createFromAsset(getApplicationContext().getAssets(), fontPath);
            etFirstName.setTypeface(tf);
            etLastName.setTypeface(tf);
            etEmailId.setTypeface(tf);
            etProfessinalField.setTypeface(tf);
            lastNameLayout.setTypeface(tf);
            emailIdLayout.setTypeface(tf);
            professionFieldLayout.setTypeface(tf);
            //tvManageAddress.setTypeface(tf);
            saveBtn.setTypeface(tf);
        }

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (submitForm()) {
                    updateCustomerProfileFromServer();
                    //updateProfilePic();
                }
            }
        });

        /*tvManageAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyAccountActivity.this, ManageAddressActivity.class);
                startActivity(intent);
            }
        });*/

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logInWithReadPermissions(MyAccountActivity.this, Collections.singletonList("public_profile, email, user_birthday, user_friends"));
                callbackManager = CallbackManager.Factory.create();
                LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        accessToken = loginResult.getAccessToken().getToken();
                        GraphRequest request = GraphRequest.newMeRequest(
                                loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(JSONObject object, GraphResponse response) {
                                        try {
                                            Integer str_id = object.getInt("id");
                                            String str_firstname = object.getString("first_name");
                                            String str_lastname = object.getString("last_name");
                                            String str_emailId = object.getString("email");
                                            String str_profession = object.getString("profession");
                                            etFirstName.setText(str_firstname);
                                            etLastName.setText(str_lastname);
                                            etEmailId.setText(str_emailId);
                                            etProfessinalField.setText(str_profession);
                                            String imageurl = "https://graph.facebook.com/" + str_id + "/picture?type=large";
                                            Picasso.with(getApplicationContext()).load(imageurl).into(iv_profile_pic);

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });

                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id,name,link,email,first_name,last_name,gender,profile_picture");
                        request.setParameters(parameters);
                        request.executeAsync();
                        accessTokenTracker = new AccessTokenTracker() {
                            @Override
                            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                                if (currentAccessToken == null) {
                                    etFirstName.setText("");
                                }
                            }
                        };
                    }

                    @Override
                    public void onCancel() {
                        Log.d("TAG_CANCEL", "On cancel");
                    }

                    @Override
                    public void onError(FacebookException error) {
                        Log.d("TAG_ERROR", error.toString());
                    }
                });
            }
        });

    }

    private String getFirstName() {
        return etFirstName.getText().toString().trim();
    }

    private String getLastName() {
        return etLastName.getText().toString().trim();
    }

    private String getEmail() {
        return etEmailId.getText().toString().trim();
    }

    private String getProfession() {
        return etProfessinalField.getText().toString().trim();
    }

    //Validate the form
    private Boolean submitForm() {
        return validateFirstName() && validateLastName() && validateEmail() && validateProfession();
    }

    private boolean validateFirstName() {
        if (getFirstName().trim().isEmpty()) {
            firstNameLayout.setError(getString(R.string.err_msg_first_name));
            requestFocus(etFirstName);
            return false;
        } else if (!getFirstName().matches("^[a-zA-Z]+[\\-'\\s]?[a-zA-Z ]+$")) {
            firstNameLayout.setError("Enter Only Alphabetical Character");
            requestFocus(etFirstName);
            return false;
        } else if (getFirstName().length() < 3) {
            firstNameLayout.setError("at least 3 characters");
            requestFocus(etFirstName);
            return false;
        } else {
            firstNameLayout.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateLastName() {
        if (getLastName().trim().isEmpty()) {
            lastNameLayout.setError(getString(R.string.err_msg_last_name));
            requestFocus(etLastName);
            return false;
        } else if (!getLastName().matches("^[a-zA-Z]+[\\-'\\s]?[a-zA-Z ]+$")) {
            lastNameLayout.setError("Enter Only Alphabetical Character");
            requestFocus(etLastName);
            return false;
        } else {
            lastNameLayout.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateEmail() {
        String email = getEmail();
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
        String EMAIL_STRING = "^[_A-Za-z0-9-+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        p = Pattern.compile(EMAIL_STRING);
        m = p.matcher(email);
        check = m.matches();

        if (!check) {
            emailIdLayout.setError("Not Valid Email");
        }
        return check;
    }
/*    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }*/

    private boolean validateProfession() {
        if (getProfession().isEmpty()) {
            professionFieldLayout.setError(getString(R.string.err_msg_profession));
            requestFocus(etProfessinalField);
            return false;
        } else {
            professionFieldLayout.setErrorEnabled(false);
        }
        return true;
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
                case R.id.first_name:
                    validateFirstName();
                    break;

                case R.id.last_name:
                    validateLastName();
                    break;
                case R.id.emaill_id:
                    validateEmail();
                    break;
                case R.id.professional_field:
                    validateProfession();
                    break;
            }
        }
    }

    public void onClickProfilePicture(View view) {
        final CharSequence[] items = {"Take Photo", "Choose from Library", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(MyAccountActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CAMERA);
                } else if (items[item].equals("Choose from Library")) {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(
                            Intent.createChooser(intent, "Select File"),
                            SELECT_FILE);
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap bitmap = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        if (bitmap != null) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        }
        File file = new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis() + ".jpg");
        FileOutputStream fo;
        try {
            file.createNewFile();
            fo = new FileOutputStream(file);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        iv_profile_pic.setImageBitmap(bitmap);
        //Convert Bitmap to Base64
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        if (bitmap != null) {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        }
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        strBase64 = Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        Uri selectedImageUri = data.getData();
        String[] projection = {MediaStore.MediaColumns.DATA};
        Cursor cursor = managedQuery(selectedImageUri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        cursor.moveToFirst();
        String selectedImagePath = cursor.getString(column_index);
        Bitmap bitmap;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(selectedImagePath, options);

        final int REQUIRED_SIZE = 200;
        int scale = 1;
        while (options.outWidth / scale / 2 >= REQUIRED_SIZE && options.outHeight / scale / 2 >= REQUIRED_SIZE)
            scale *= 2;
        options.inSampleSize = scale;
        options.inJustDecodeBounds = false;
        bitmap = BitmapFactory.decodeFile(selectedImagePath, options);
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        iv_profile_pic.setImageBitmap(bitmap);

        //Convert Bitmap to Base64
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        strBase64 = Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //callbackManager.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    public void customerProfileFromServer() {
        showProgress();
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<CustomerProfileModel> call = apiInterface.customerProfile(PreferencesManager.getPrefString(Constants.AUTH_TOKEN_TYPE) + " " + PreferencesManager.getPrefString(Constants.AUTH_TOKEN), PreferencesManager.getPrefInt(Constants.CUSTOMER_ID));
        call.enqueue(new Callback<CustomerProfileModel>() {

            @Override
            public void onResponse(Call<CustomerProfileModel> call, Response<CustomerProfileModel> response) {
                dismissProgress();

                if (response != null) {

                    if (response.body() != null) {

                        if (response.body().getData() != null) {

                            etFirstName.setText(response.body().getData().getFirstName());
                            etLastName.setText(response.body().getData().getLastName());
                            etEmailId.setText(response.body().getData().getEmail());
                            etProfessinalField.setText(response.body().getData().getProfession());

                            String fName = response.body().getData().getFirstName();
                            String lName = response.body().getData().getLastName();
                            String email = response.body().getData().getEmail();
                            String profession = response.body().getData().getProfession();

                            PreferencesManager.setPrefString(Constants.FIRST_NAME, fName);
                            PreferencesManager.setPrefString(Constants.LAST_NAME, lName);
                            PreferencesManager.setPrefString(Constants.EMAIL_ID, email);
                            PreferencesManager.setPrefString(Constants.EMAIL_ID, profession);
                            if (!TextUtils.isEmpty(fName) && !TextUtils.isEmpty(lName)) {
                                PreferencesManager.setPrefString(Constants.CUSTOMER_NAME, fName + " " + lName);
                            }
                        } else {
                        }
                    } else {
                    }
                } else {
                }
             /*   profilePic =response.body().getData().getProfession();
                 companyName=response.body().getData().getProfession();
                type =response.body().getData().getType();*/
                //   startActivity(new Intent(MyAccountActivity.this, MainActivity.class));
            }

            @Override
            public void onFailure(Call<CustomerProfileModel> call, Throwable t) {
                dismissProgress();
            }
        });
    }

    public void updateCustomerProfileFromServer() {
        showProgress();
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<CustomerProfileModel> call = apiInterface.updateProfile(PreferencesManager.getPrefString(Constants.AUTH_TOKEN_TYPE) + " " + PreferencesManager.getPrefString(Constants.AUTH_TOKEN), PreferencesManager.getPrefInt(Constants.CUSTOMER_ID), getFirstName(), getLastName(), getEmail(), getProfession(), accessToken, "1223", "fb");
        call.enqueue(new Callback<CustomerProfileModel>() {
            @Override
            public void onResponse(Call<CustomerProfileModel> call, Response<CustomerProfileModel> response) {
                dismissProgress();
                PreferencesManager.setPrefString(Constants.CUSTOMER_NAME, getFirstName() + " " + getFirstName());
                startActivity(new Intent(MyAccountActivity.this, MainActivity.class));
                finish();
            }

            @Override
            public void onFailure(Call<CustomerProfileModel> call, Throwable t) {
                dismissProgress();
            }
        });
    }
}


