package in.gohelper.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.List;
import in.gohelper.R;
import in.gohelper.adapter.PaymentOptionsAdapter;
import in.gohelper.models.SuccessModel;
import in.gohelper.models.paymentmodels.Payment;
import in.gohelper.models.paymentmodels.PaymentOptionsModel;
import in.gohelper.rest.ApiClient;
import in.gohelper.rest.ApiInterface;
import in.gohelper.utils.Constants;
import in.gohelper.utils.PreferencesManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentActivity extends BaseActivity {
    ListView paymentListView;
    String itemValue;
    List<Payment> payment;
    String paymentMode;
    double totalAmount;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        payment = new ArrayList<>();

        Intent intent = this.getIntent();
        totalAmount = intent.getExtras().getDouble("TotalAmount");

        getPaymentOPtionsFromServer();
        paymentListView = (ListView) findViewById(R.id.list_view_payment);
        paymentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                paymentMode = payment.get(position).getPayment().getPaymentLabel();
                if (paymentMode.equals("COD")) {
                    createCODPaymentToServer();
                    Intent intent = new Intent(PaymentActivity.this, ThankYouPageActivity.class);
                    startActivity(intent);
                } else {

                }

             /*   if(itemValue.equals("COD")){
                    createCODPaymentToServer();
                }
                else
                {
                    //TODO payTm API Hit
                    Toast.makeText(PaymentActivity.this, itemValue, Toast.LENGTH_SHORT).show();
                }*/
            }
        });
    }

    public void getPaymentOPtionsFromServer() {
        showProgress();
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<PaymentOptionsModel> call = apiInterface.getPaymentOptions(PreferencesManager.getPrefString(Constants.AUTH_TOKEN_TYPE) + " " + PreferencesManager.getPrefString(Constants.AUTH_TOKEN));
        call.enqueue(new Callback<PaymentOptionsModel>() {

            @Override
            public void onResponse(Call<PaymentOptionsModel> call, Response<PaymentOptionsModel> response) {
                dismissProgress();
                if (response != null) {
                    if (response.body() != null) {
                        if (response.body().getData() != null) {
                            payment = response.body().getData();
                            PaymentOptionsAdapter paymentAdapter = new PaymentOptionsAdapter(getApplicationContext(), payment);
                            paymentListView.setAdapter(paymentAdapter);
                        }else {}
                    }else {}
                }else{}
            }

            @Override
            public void onFailure(Call<PaymentOptionsModel> call, Throwable t) {
                dismissProgress();
            }
        });
    }

    public void createCODPaymentToServer() {
        showProgress();
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<SuccessModel> call = apiInterface.createCODPayment(PreferencesManager.getPrefString(Constants.AUTH_TOKEN_TYPE) + " " + PreferencesManager.getPrefString(Constants.AUTH_TOKEN), paymentMode, totalAmount);
        call.enqueue(new Callback<SuccessModel>() {

            @Override
            public void onResponse(Call<SuccessModel> call, Response<SuccessModel> response) {
                dismissProgress();
                Log.e("Success", String.valueOf(response));
            }

            @Override
            public void onFailure(Call<SuccessModel> call, Throwable t) {
                dismissProgress();
            }
        });
    }
}