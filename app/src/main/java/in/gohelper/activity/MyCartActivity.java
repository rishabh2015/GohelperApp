package in.gohelper.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import in.gohelper.R;
import in.gohelper.adapter.CartAdapter;
import in.gohelper.interfaces.CartActionListener;
import in.gohelper.models.cartmodels.CartData;
import in.gohelper.models.cartmodels.CartItem;
import in.gohelper.models.cartmodels.CartModel;
import in.gohelper.models.cartmodels.SuccessCartModel;
import in.gohelper.models.servicemodels.Option;
import in.gohelper.models.servicemodels.Question;
import in.gohelper.rest.ApiClient;
import in.gohelper.rest.ApiInterface;
import in.gohelper.utils.Constants;
import in.gohelper.utils.PreferencesManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyCartActivity extends BaseActivity implements CartActionListener {
    private List<CartData> cartData;
    private TextView emptyView;
    private double totalAmount = 0.0;
    private RecyclerView recyclerView;
    private RelativeLayout rl_NoCartItem;
    private CartAdapter adapter;
    private Button btnMakePayment;
    private RelativeLayout rlPayableAmount, rlEmptyView;
    private ImageView ivShoppingCart;
    private TextView tvTotalAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mycart_layout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btnMakePayment = (Button) findViewById(R.id.button_make_payment);
        emptyView = (TextView) findViewById(R.id.empty_view);
        tvTotalAmount = (TextView) findViewById(R.id.tv_big_amount);
        rlPayableAmount = (RelativeLayout) findViewById(R.id.rl_payable_amount);
        rlEmptyView = (RelativeLayout) findViewById(R.id.rl_empty_view);
        ivShoppingCart = (ImageView) findViewById(R.id.shopping_cart);

        //To add custom font
        if (!Constants.USE_CUSTOM_FONT) {
            String fontPath = Constants.CUSTOM_NAME_SIMPLE;
            Typeface tf = Typeface.createFromAsset(MyCartActivity.this.getAssets(), fontPath);
            btnMakePayment.setTypeface(tf);
            emptyView.setTypeface(tf);
        }

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_cart);

        getCartFromServer();

        btnMakePayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyCartActivity.this, PaymentActivity.class);
                intent.putExtra("TotalAmount", totalAmount);
                startActivity(intent);
            }
        });

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        cartData = new ArrayList<>();
    }

    public void getCartFromServer() {
        showProgress();
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<CartModel> call = apiInterface.getCart(PreferencesManager.getPrefString(Constants.AUTH_TOKEN_TYPE) + " " + PreferencesManager.getPrefString(Constants.AUTH_TOKEN));
        call.enqueue(new Callback<CartModel>() {

            @Override
            public void onResponse(Call<CartModel> call, Response<CartModel> response) {
                dismissProgress();
                if (response != null) {
                    if (response.body() != null) {
                        if (response.body().getData() != null) {
                            cartData = response.body().getData();

                            for (CartData cartDataItem : cartData) {
                                CartItem cartitem = cartDataItem.getCartItem();

                                HashMap<String, String> optionMap = new HashMap<String, String>();

                                String optionsString = cartitem.getServiceOptions();
                                String[] optionStringComps = optionsString.split("\\|");
                                for (byte index = 0; index < optionStringComps.length; index++) {
                                    String component = optionStringComps[index];
                                    String[] comps = component.split(":");
                                    if (comps.length == 2) {
                                        String questionId = comps[0];
                                        String optionId = comps[1];

                                        optionMap.put(questionId, optionId);
                                    }
                                }
                                List<Question> questions = new ArrayList<>();
                                for (int index = 0; index < cartitem.getService().getQuestions().size(); index++) {
                                    Question question = cartitem.getService().getQuestions().get(index);
                                    List<Option> options = new ArrayList<Option>();
                                    String optionIdString = optionMap.get(question.getId().toString());
                                    for (Option option : question.getOptions()) {
                                        if (optionIdString.equals(option.getId().toString())) {
                                            options.add(option);
                                        }
                                    }
                                    question.setSelectedOptions(options);
                                    questions.add(question);
                                }
                                cartitem.getService().setQuestions(questions);
                                totalAmount = totalAmount + Double.parseDouble(cartitem.getItemAmount());
                            }

                            adapter = new CartAdapter(cartData, MyCartActivity.this);
                            // recyclerView.setAdapter(adapter);
                            if (adapter.getItemCount() != 0) {
                                recyclerView.setAdapter(adapter);
                                rlPayableAmount.setVisibility(View.VISIBLE);
                                tvTotalAmount.setText(Double.toString(totalAmount));
                                emptyView.setVisibility(View.GONE);
                                rlEmptyView.setVisibility(View.VISIBLE);
                                ivShoppingCart.setVisibility(View.GONE);

                            } else {
                                emptyView.setVisibility(View.VISIBLE);
                                btnMakePayment.setVisibility(View.GONE);
                                rlPayableAmount.setVisibility(View.GONE);
                                ivShoppingCart.setVisibility(View.VISIBLE);
                                rlEmptyView.setVisibility(View.VISIBLE);
                                emptyView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        startActivity(new Intent(MyCartActivity.this, MainActivity.class));
                                    }
                                });
                            }
                        } else {
                            Log.e("data null in getCart", response.body().toString());
                        }
                    } else {
                        Log.e("body null in getCart", response.toString());
                    }
                } else {
                    Log.e("response null getCart", null);
                }
            }

            @Override
            public void onFailure(Call<CartModel> call, Throwable t) {
                dismissProgress();
            }
        });
    }

    public void deleteCartFromServer(int carItemId) {
        showProgress();
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<SuccessCartModel> call = apiInterface.deleteCart(PreferencesManager.getPrefString(Constants.AUTH_TOKEN_TYPE) + " " + PreferencesManager.getPrefString(Constants.AUTH_TOKEN), carItemId);
        call.enqueue(new Callback<SuccessCartModel>() {
            @Override
            public void onResponse(Call<SuccessCartModel> call, Response<SuccessCartModel> response) {
                dismissProgress();
            }

            @Override
            public void onFailure(Call<SuccessCartModel> call, Throwable t) {
                dismissProgress();
            }
        });
    }

    @Override
    public void onDeleteCartItem(CartData cartItem) {
        deleteCartFromServer(cartItem.getCartItem().getId());
    }

    @Override
    public void onEditCartItem(CartData cartItem) {
        String jsonObject = new Gson().toJson(cartItem);
        Intent intent = new Intent(getApplicationContext(), ReviewActivity.class);
        intent.putExtra("in.gohelper.intent.cartItemObject", jsonObject);
        startActivity(intent);
    }

    @Override
    public void onSelectCartItem(CartData cartItem) {
        String jsonObject = new Gson().toJson(cartItem);
        Intent intent = new Intent(getApplicationContext(), ReviewActivity.class);
        intent.putExtra("in.gohelper.intent.cartItemObject", jsonObject);
        startActivity(intent);
    }
}