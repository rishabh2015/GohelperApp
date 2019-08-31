package in.gohelper.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import in.gohelper.R;
import in.gohelper.adapter.ReviewRecyclerViewAdapter;
import in.gohelper.adapter.SelectDateHorizontalAdapter;
import in.gohelper.interfaces.DatePickerActionListener;
import in.gohelper.models.CustomerAddressmodels.CustomerAddressData;
import in.gohelper.models.SuccessModel;
import in.gohelper.models.cartmodels.CartData;
import in.gohelper.models.cartmodels.CartItem;
import in.gohelper.models.servicemodels.Option;
import in.gohelper.models.servicemodels.Question;
import in.gohelper.models.servicemodels.ServiceData;
import in.gohelper.rest.ApiClient;
import in.gohelper.rest.ApiInterface;
import in.gohelper.utils.Constants;
import in.gohelper.utils.PreferencesManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReviewActivity extends BaseActivity implements View.OnClickListener, DatePickerActionListener {

    private AppCompatButton slot1, slot2, slot3, slot4;
    private double basePrice;
    private RecyclerView horizontal_recycler_view;

    private TextView tvLogin, tvAddress;

    //Bundle data
    private ServiceData service;
    private CartItem cartItem;
    private CustomerAddressData addressData;
    private ServiceData serviceData;
    //Local data
    private AppCompatButton slotSelected;
    private String selectedOptionsStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.review_layout);
        processExtraData();

        new PreferencesManager(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        AppCompatTextView tvSelectDate = (AppCompatTextView) findViewById(R.id.text_date);
        AppCompatTextView tvSelectTime = (AppCompatTextView) findViewById(R.id.text_time);
        AppCompatTextView tvPrice = (AppCompatTextView) findViewById(R.id.tv_prize);
        AppCompatTextView totalPrize = (AppCompatTextView) findViewById(R.id.tv_total_prize);

        tvLogin = (TextView) findViewById(R.id.tv_Login);
        tvAddress = (TextView) findViewById(R.id.tv_address);

        Button addCart = (Button) findViewById(R.id.btn_add_cart);

        slot1 = (AppCompatButton) findViewById(R.id.buttonSlot1);
        slot2 = (AppCompatButton) findViewById(R.id.buttonSlot2);
        slot3 = (AppCompatButton) findViewById(R.id.buttonSlot3);
        slot4 = (AppCompatButton) findViewById(R.id.buttonSlot4);

        slotSelected = slot1;
        if (cartItem != null) {
            String slotStart = cartItem.getSlotStart();
            int hours = Integer.parseInt(slotStart.split(":")[0]);
            if (hours < 8) {
                hours += 12;
            }

            if (hours >= 16) {
                slotSelected = slot4;
            } else if (hours >= 14) {
                slotSelected = slot3;
            } else if (hours >= 10) {
                slotSelected = slot2;
            } else {
                slotSelected = slot1;
            }
        }
        slotSelected.setBackgroundResource(R.drawable.schedule_selected_bg);
        slotSelected.setTextColor(getResources().getColor(R.color.colorBlue));

        //add custom font
        if (!Constants.USE_CUSTOM_FONT) {
            String fontPath = Constants.CUSTOM_NAME_SIMPLE;
            Typeface tf = Typeface.createFromAsset(getApplicationContext().getAssets(), fontPath);

            tvSelectDate.setTypeface(tf);
            tvSelectTime.setTypeface(tf);
            tvPrice.setTypeface(tf);
            tvLogin.setTypeface(tf);
            tvAddress.setTypeface(tf);
            addCart.setTypeface(tf);
            totalPrize.setTypeface(tf);
        }
        slot1.setOnClickListener(this);
        slot2.setOnClickListener(this);
        slot3.setOnClickListener(this);
        slot4.setOnClickListener(this);

        reloadAddressSection();
        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MobileRegistrationActivity.class);
                intent.putExtra("TYPE", Constants.REVIEW_ACTIVITY);
                startActivity(intent);
            }
        });

        tvAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ManageAddressActivity.class);
                intent.putExtra("in.gohelper.source.review", true);
                startActivity(intent);
            }
        });

        Date scheduleDate = null;
        DateFormat dateFormatterCart = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        if (cartItem != null) {
            String scheduleDateString = cartItem.getScheduledDateTime();
            try {
                scheduleDate = dateFormatterCart.parse(scheduleDateString);
            } catch (Exception e) {
            }
        }

        DateFormat dateFormatter = new SimpleDateFormat("dd MMM yyyy");
        dateFormatter.setLenient(false);

        Calendar c = Calendar.getInstance();
        c.setTime(new Date());

        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        final int totalMins = hour * 60 + minute + 1;

        if (totalMins <= 16 * 60) {
            c.add(Calendar.DATE, -1);
        }

        //Reset time comps
        c.set(year, month, day, 0, 0, 0);
        c.setTimeInMillis(c.getTime().getTime() / 1000 * 1000);

        byte selectedPosition = 0;
        byte numberOfdays = 8;
        ArrayList<String> datesStrings = new ArrayList<String>();
        ArrayList<Date> dates = new ArrayList<Date>();
        for (byte index = 0; index < numberOfdays; index++) {
            c.add(Calendar.DATE, 1);
            Date date = c.getTime();
            dates.add(date);
            String formattedDate = dateFormatter.format(date);
            datesStrings.add(formattedDate);

            if (scheduleDate != null && scheduleDate.equals(date)) {
                selectedPosition = index;
            }
        }

        horizontal_recycler_view = (RecyclerView) findViewById(R.id.horizontal_recycler_view);
        SelectDateHorizontalAdapter horizontalAdapter = new SelectDateHorizontalAdapter(ReviewActivity.this, dates, this);
        LinearLayoutManager horizontalLayoutManagaer = new LinearLayoutManager(ReviewActivity.this, LinearLayoutManager.HORIZONTAL, false);
        horizontal_recycler_view.setLayoutManager(horizontalLayoutManagaer);
        horizontal_recycler_view.setAdapter(horizontalAdapter);
        horizontalAdapter.selectedPosition = selectedPosition;

        updateSlot(totalMins < 16 * 60, totalMins);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        ReviewRecyclerViewAdapter mAdapter = new ReviewRecyclerViewAdapter(getApplicationContext(), service.getQuestions());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        //To get Selected options id which is pass to cart
        List<String> selectedOptionsList = new ArrayList<>();
        for (Question question : service.getQuestions()) {
            Integer qId = question.getId();
            List<Integer> optionsText = new ArrayList<>();
            for (Option option : question.getSelectedOptions()) {
                optionsText.add(option.getId());
            }
            String selectedOptions = TextUtils.join(",", optionsText);

            String question_option_str = qId + ":" + selectedOptions;
            selectedOptionsList.add(question_option_str);
        }

        selectedOptionsStr = TextUtils.join("|", selectedOptionsList);

        //to get basePrice + price change and show
        basePrice = Double.parseDouble(service.getBasePrice());

        for (int i = 0; i < service.getQuestions().size(); i++) {
            List<Option> options = service.getQuestions().get(i).getSelectedOptions();
            if (options.size() > 0) {
                basePrice = basePrice + Double.parseDouble(options.get(0).getPriceChange());
            }
        }
        totalPrize.setText(String.valueOf(basePrice));

        if (cartItem != null) {
            addCart.setText("Update");
        } else {
            addCart.setText("Add To Cart");
        }
        addCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cartUpdateOnServer();
            }
        });
    }

    private void reloadAddressSection() {
        if (!PreferencesManager.getPrefBool(Constants.MOBILE_VERIFIED) && !PreferencesManager.getPrefBool(Constants.SELECT_ADDRESS)) {
            tvLogin.setVisibility(View.VISIBLE);
            tvAddress.setVisibility(View.GONE);

        } else if (PreferencesManager.getPrefBool(Constants.MOBILE_VERIFIED) && addressData != null) {
            tvLogin.setVisibility(View.GONE);
            tvAddress.setVisibility(View.VISIBLE);
            tvLogin.setError(null);
            tvAddress.setError(null);
            tvAddress.setText(addressData.getAddress() + " , " + addressData.getLocality());


        } else {
            tvLogin.setVisibility(View.GONE);
            tvAddress.setVisibility(View.VISIBLE);
            tvAddress.setText("Select Address");
        }
    }

    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);//must store the new intent unless getIntent() will return the old one
        processExtraData();
    }

    private void processExtraData() {
        //To get service question object from ServiceQuestionActivity
        Intent intent = getIntent();
        if (intent != null) {
            boolean fromAddress = intent.getBooleanExtra("in.gohelper.source.addresslist", false);
            if (fromAddress == false) {
                String serviceJson = intent.getStringExtra("in.gohelper.intent.serviceObject");
                String cartJson = intent.getStringExtra("in.gohelper.intent.cartItemObject");
                if (serviceJson != null) {
                    service = new Gson().fromJson(serviceJson, ServiceData.class);
                } else if (cartJson != null) {
                    CartData cartdata = new Gson().fromJson(cartJson, CartData.class);
                    cartItem = cartdata.getCartItem();
                    addressData = cartItem.getAddress();
                    service = cartItem.getService();
                } else {
                    finish();
                }
            } else {
                String addressJson = intent.getStringExtra("in.gohelper.intent.addressObject");
                if (addressJson != null) {
                    addressData = new Gson().fromJson(addressJson, CustomerAddressData.class);
                    reloadAddressSection();
                }
            }
        }
    }

    private void updateSlot(Boolean disable, int totalMins) {
        if (disable) {

            //Disable Slot for today id possible
            if (totalMins > 6 * 60) {
                slot1.setEnabled(false);
            }

            if (totalMins > 10 * 60) {
                slot2.setEnabled(false);
            }

            if (totalMins > 6 * 60) {
                slot3.setEnabled(false);
            }
        } else {
            slot1.setEnabled(true);
            slot2.setEnabled(true);
            slot3.setEnabled(true);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        slotSelected.setBackgroundResource(R.drawable.schedule_bg);
        slotSelected.setTextColor(getResources().getColor(R.color.colorPrimary));
        switch (id) {
            case R.id.buttonSlot1:
                slotSelected = slot1;
                break;
            case R.id.buttonSlot2:
                slotSelected = slot2;
                break;
            case R.id.buttonSlot3:
                slotSelected = slot3;
                break;
            case R.id.buttonSlot4:
                slotSelected = slot4;
                break;
            default:
                break;
        }
        slotSelected.setBackgroundResource(R.drawable.schedule_selected_bg);
        slotSelected.setTextColor(getResources().getColor(R.color.colorBlue));
    }

    public boolean cartUpdateOnServer() {
        showProgress();
        boolean success = true;
        try {
            String dateFormat = "dd MMM yyyy";
            Date today = new Date();
            Date date = ((SelectDateHorizontalAdapter) horizontal_recycler_view.getAdapter()).selectedDate;
            SimpleDateFormat fmtOut = new SimpleDateFormat("yyyy-MM-dd'T'00:00:00+05:30");
            String dateOutput = fmtOut.format(date);
            String slotText = slotSelected.getText().toString();

            if (addressData == null || addressData.getId() == null) {
                Toast.makeText(getApplicationContext(), "Address Required", Toast.LENGTH_SHORT).show();
                tvLogin.setError("please select address");
                tvAddress.setError("please select address");
                return false;
            }

            if (TextUtils.isEmpty(slotText) || date == null) {
                return false;
            }

            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            if (cartItem != null) {

                Call<SuccessModel> call = apiInterface.updateCart(PreferencesManager.getPrefString(Constants.AUTH_TOKEN_TYPE) + " " + PreferencesManager.getPrefString(Constants.AUTH_TOKEN), cartItem.getId(), service.getId(), addressData.getId(), dateOutput, selectedOptionsStr, slotText);
                call.enqueue(new Callback<SuccessModel>() {

                    @Override
                    public void onResponse(Call<SuccessModel> call, Response<SuccessModel> response) {
                        dismissProgress();
                        finish();
                    }

                    @Override
                    public void onFailure(Call<SuccessModel> call, Throwable t) {
                        dismissProgress();
                    }
                });
            } else {
                Call<SuccessModel> call = apiInterface.createCart(PreferencesManager.getPrefString(Constants.AUTH_TOKEN_TYPE) + " " + PreferencesManager.getPrefString(Constants.AUTH_TOKEN), service.getId(), addressData.getId(), dateOutput, selectedOptionsStr, slotText, String.valueOf(basePrice));
                call.enqueue(new Callback<SuccessModel>() {

                    @Override
                    public void onResponse(Call<SuccessModel> call, Response<SuccessModel> response) {
                        dismissProgress();
                        Intent intent = new Intent(ReviewActivity.this, MyCartActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onFailure(Call<SuccessModel> call, Throwable t) {
                        dismissProgress();
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            success = false;
        }

        return success;
    }

/*    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }*/

/*    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_cart) {
            Intent intent = new Intent(this, MyCartActivity.class);
            this.startActivity(intent);
            return true;
        }

        //noinspection SimplifiableIfStatement
       *//* if (id == R.id.action_settings) {
            return true;
        }*//*
        return super.onOptionsItemSelected(item);
    }*/

    @Override
    public void onSelectItemAtPosition(int position, int previousPos) {
        horizontal_recycler_view.getAdapter().notifyItemChanged(previousPos);
        horizontal_recycler_view.getAdapter().notifyItemChanged(position);
    }
}