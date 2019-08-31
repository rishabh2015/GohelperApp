package in.gohelper.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import in.gohelper.R;
import in.gohelper.adapter.ManageAddressAdapter;
import in.gohelper.interfaces.AddressListActionListener;
import in.gohelper.models.CustomerAddressmodels.CustomerAddressData;
import in.gohelper.models.CustomerAddressmodels.CustomerAddressModel;
import in.gohelper.models.CustomerAddressmodels.CustomerAddressUpdateModel;
import in.gohelper.rest.ApiClient;
import in.gohelper.rest.ApiInterface;
import in.gohelper.utils.Constants;
import in.gohelper.utils.PreferencesManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManageAddressActivity extends BaseActivity {
    Toolbar toolbar;
    RecyclerView recyclerView;
    List<CustomerAddressData> arrayList;
    TextView emptyView;
    Button addAddrssButton;
    private boolean fromReviewPage = false;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        if (intent != null) {
            fromReviewPage = intent.getBooleanExtra("in.gohelper.source.review", false);
        }

        setContentView(R.layout.manage_address_activity);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if(fromReviewPage) {
            getSupportActionBar().setTitle("Select Address");
        }
        else {
            getSupportActionBar().setTitle("Manage Address");
        }


        emptyView = (TextView) findViewById(R.id.empty_address);

        /*if (arrayList.size() == 0) {
            rlNoAddress.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            rlNoAddress.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }*/

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        addAddrssButton = (Button) findViewById(R.id.addAddress);

        //To add custom font
        if (!Constants.USE_CUSTOM_FONT) {
            String fontPath = Constants.CUSTOM_NAME_SIMPLE;
            Typeface tf = Typeface.createFromAsset(getApplicationContext().getAssets(), fontPath);
            emptyView.setTypeface(tf);
            addAddrssButton.setTypeface(tf);
        }

        addAddrssButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManageAddressActivity.this, AddAddressActivity.class);
                startActivity(intent);
            }
        });

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        arrayList = new ArrayList<>();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getCustomerAddressFromServer();
    }

    public void getCustomerAddressFromServer() {
        showProgress();
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<CustomerAddressModel> call = apiInterface.getCustomerAddress(PreferencesManager.getPrefString(Constants.AUTH_TOKEN_TYPE) + " " + PreferencesManager.getPrefString(Constants.AUTH_TOKEN));
        call.enqueue(new Callback<CustomerAddressModel>() {

            @Override
            public void onResponse(Call<CustomerAddressModel> call, Response<CustomerAddressModel> response) {
                dismissProgress();
                if (response != null) {
                    if (response.body() != null) {
                        if (response.body().getData() != null) {
                            arrayList = response.body().getData();
                            ManageAddressAdapter adapter = new ManageAddressAdapter(arrayList, ManageAddressActivity.this);
                            if (fromReviewPage) {
                                adapter.setAddressListActionListener(new AddressListActionListener() {
                                    @Override
                                    public void onSelectAddress(CustomerAddressData addressData) {
                                        String addressJson = new Gson().toJson(addressData);
                                        Intent intent = new Intent(ManageAddressActivity.this, ReviewActivity.class);
                                        intent.putExtra("in.gohelper.source.addresslist", true);
                                        intent.putExtra("in.gohelper.intent.addressObject", addressJson);
                                        startActivity(intent);
                                    }
                                });
                            }
                            if (adapter.getItemCount() != 0) {
                                recyclerView.setAdapter(adapter);
                                recyclerView.setAdapter(adapter);
                                emptyView.setVisibility(View.GONE);
                            } else {
                                Toast.makeText(ManageAddressActivity.this, "No Address Available", Toast.LENGTH_SHORT).show();
                                emptyView.setVisibility(View.VISIBLE);
                                addAddrssButton.setVisibility(View.GONE);
                                emptyView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        startActivity(new Intent(ManageAddressActivity.this, AddAddressActivity.class));
                                    }
                                });
                            }
                        } else {
                        }
                    } else {
                    }
                } else {
                }
            }

            @Override
            public void onFailure(Call<CustomerAddressModel> call, Throwable t) {
                dismissProgress();
            }
        });
    }

    public void deleteCustomerAddressFromServer(int customerAddressId) {
        //  showProgress();
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<CustomerAddressUpdateModel> call = apiInterface.deleteCustomerAddress(PreferencesManager.getPrefString(Constants.AUTH_TOKEN_TYPE) + " " + PreferencesManager.getPrefString(Constants.AUTH_TOKEN), customerAddressId);
        call.enqueue(new Callback<CustomerAddressUpdateModel>() {
            @Override
            public void onResponse(Call<CustomerAddressUpdateModel> call, Response<CustomerAddressUpdateModel> response) {
                dismissProgress();
            }

            @Override
            public void onFailure(Call<CustomerAddressUpdateModel> call, Throwable t) {
                dismissProgress();
            }
        });
    }

}
