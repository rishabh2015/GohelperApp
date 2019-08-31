package in.gohelper.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import in.gohelper.R;
import in.gohelper.adapter.OnGoingOrdersAdapter;
import in.gohelper.fragment.OnGoingOrderFragment;
import in.gohelper.fragment.PastOrderFragment;
import in.gohelper.models.ordermodels.OrderData;
import in.gohelper.models.ordermodels.OrderItem;
import in.gohelper.models.ordermodels.OrdersModel;
import in.gohelper.models.servicemodels.Option;
import in.gohelper.models.servicemodels.Question;
import in.gohelper.rest.ApiClient;
import in.gohelper.rest.ApiInterface;
import in.gohelper.utils.Constants;
import in.gohelper.utils.PreferencesManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//Implementing the interface OnTabSelectedListener to our MainActivity
//This interface would help in swiping views
public class OrdersActivity extends BaseActivity implements TabLayout.OnTabSelectedListener {

    //This is our tablayout
    private TabLayout tabLayout;
    private List<OrderItem> orderItems;


    ArrayList prgmName;

    //This is our viewPager
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        //Adding toolbar to the activity
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getOrder();

        //Initializing the tablayout
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);

        //Adding the tabs using addTab() method
        tabLayout.addTab(tabLayout.newTab().setText("OnGoing Orders"));
        tabLayout.addTab(tabLayout.newTab().setText("Past Orders"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        //Initializing viewPager
        viewPager = (ViewPager) findViewById(R.id.pager);

        //Creating our pager adapter
        OrderPagerAdapter adapter = new OrderPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        //Adding adapter to pager
        viewPager.setOffscreenPageLimit(0);
        viewPager.setAdapter(adapter);
        //Adding onTabSelectedListener to swipe views
        tabLayout.setOnTabSelectedListener(this);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

     private class OrderPagerAdapter extends FragmentStatePagerAdapter {

        //integer to count number of tabs
        private int tabCount;

        //Constructor to the class
         OrderPagerAdapter(FragmentManager fm, int tabCount) {
            super(fm);
            //Initializing tab count
            this.tabCount= tabCount;
        }

        //Overriding method getItem
        @Override
        public Fragment getItem(int position) {
            //Returning the current tabs
            switch (position) {
                case 0:
                    OnGoingOrderFragment tab1 = new OnGoingOrderFragment();
                   /* Bundle bundle = new Bundle();
                    bundle.putString("edttext", "data From Activity");
                    tab1.setArguments(bundle);*/
                    return tab1;
                case 1:
                    PastOrderFragment tab2 = new PastOrderFragment();
                    return tab2;
                default:
                    return null;
            }
        }

        //Overriden method getCount to get the number of tabs
        @Override
        public int getCount() {
            return tabCount;
        }
    }


   /* public void getOrder() {
        showProgress();
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<OrdersModel> call = apiInterface.getOrders(PreferencesManager.getPrefString(Constants.AUTH_TOKEN_TYPE) + " " + PreferencesManager.getPrefString(Constants.AUTH_TOKEN));
        call.enqueue(new Callback<OrdersModel>() {
            @Override
            public void onResponse(Call<OrdersModel> call, final Response<OrdersModel> response) {
                dismissProgress();
                if (response != null) {

                    List<OrderData> orderData = response.body().getData();
                    for (OrderData order : orderData) {
                        for(OrderItem orderItem : order.getOrderItems()) {
                            int orderStatusId = Integer.parseInt(orderItem.getOrderStatusId());
                            orderItem.setOrderTime(order.getOrderTime());

                            HashMap<String, String> optionMap = new HashMap<String, String>();

                            String optionsString = orderItem.getServiceOptions();
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
                            for (int index = 0; index < orderItem.getService().getQuestions().size(); index++) {
                                Question question = orderItem.getService().getQuestions().get(index);
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
                            orderItem.getService().setQuestions(questions);



                            if(orderStatusId < 3) {
                                orderItems.add(orderItem);
                            }
                        }
                    }

           *//*         RecyclerView.Adapter adapter = new OnGoingOrdersAdapter(orderItems, getApplicationContext());
                    // recyclerView.setAdapter(adapter);
                    if (adapter.getItemCount() != 0) {
                        //recyclerView.setAdapter(adapter);
                        //emptyView.setVisibility(View.GONE);

                    } else {
                        Toast.makeText(getApplicationContext(), "No Order book ", Toast.LENGTH_SHORT).show();
                       // emptyView.setVisibility(View.VISIBLE);
//                        emptyView.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
//                            }
//                        });
                    }*//*
                }
            }

            @Override
            public void onFailure(Call<OrdersModel> call, Throwable t) {
                dismissProgress();
            }
        });
    }*/
}