package in.gohelper.fragment;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import in.gohelper.R;
import in.gohelper.activity.MainActivity;
import in.gohelper.adapter.PastOrdersAdapter;
import in.gohelper.models.SuccessModel;
import in.gohelper.models.ordermodels.OrderData;
import in.gohelper.models.ordermodels.OrderItem;
import in.gohelper.models.ordermodels.OrdersModel;
import in.gohelper.rest.ApiClient;
import in.gohelper.rest.ApiInterface;
import in.gohelper.utils.Constants;
import in.gohelper.utils.PreferencesManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PastOrderFragment extends Fragment {
    View view;
    private TextView emptyView;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private boolean loading = true;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    private LinearLayoutManager mLayoutManager;
    private List<OrderItem> orderItems;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.past_order_fragment, container, false);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        emptyView = (TextView) view.findViewById(R.id.empty_view);

        //To add custom font
        if (!Constants.USE_CUSTOM_FONT) {
            String fontPath = Constants.CUSTOM_NAME_SIMPLE;
            Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), fontPath);
            emptyView.setTypeface(tf);
        }

        getOrder();
        initViews();
        return view;
        //Returning the layout file after inflating
        //Change R.layout.ongoing_order_fragment in you classes
    }

    private void initViews() {
        recyclerView = (RecyclerView) view.findViewById(R.id.card_recycler_view);
        recyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        orderItems = new ArrayList<>();


 /*       recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(dy > 0) //check for scroll down
                {
                    visibleItemCount = mLayoutManager.getChildCount();
                    totalItemCount = mLayoutManager.getItemCount();
                    pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();
                    if (loading)
                    {
                        if ( (visibleItemCount + pastVisiblesItems) >= totalItemCount)
                        {
                            loading = false;
                            Log.v("...", "Last Item Wow !");
                            //Do pagination.. i.e. fetch new data
                        }
                    }
                }

            }
        });*/

/*        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            GestureDetector gestureDetector = new GestureDetector(getActivity(), new GestureDetector.SimpleOnGestureListener() {

                @Override public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

            });
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

                View child = rv.findChildViewUnder(e.getX(), e.getY());
                if(child != null && gestureDetector.onTouchEvent(e)) {
                    int position = rv.getChildAdapterPosition(child);
                    Toast.makeText(getActivity(), orderData.get(position).toString(), Toast.LENGTH_SHORT).show();
                }

                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });*/
    }

    public void getOrder() {
        progressBar.setVisibility(View.VISIBLE);
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<OrdersModel> call = apiInterface.getOrders(PreferencesManager.getPrefString(Constants.AUTH_TOKEN_TYPE) + " " + PreferencesManager.getPrefString(Constants.AUTH_TOKEN));
        call.enqueue(new Callback<OrdersModel>() {
            @Override
            public void onResponse(Call<OrdersModel> call, final Response<OrdersModel> response) {
                progressBar.setVisibility(View.GONE);
                if (response != null) {
                    List<OrderData> orderData = response.body().getData();
                    for (OrderData order: orderData) {
                        orderItems.addAll(order.getOrderItems());
                    }
                    RecyclerView.Adapter adapter = new PastOrdersAdapter(orderItems, getActivity());
                    // recyclerView.setAdapter(adapter);
                    if (adapter.getItemCount() != 0) {
                        recyclerView.setAdapter(adapter);
                        emptyView.setVisibility(View.GONE);
                    } else {
                        Toast.makeText(getActivity(), "No Order book ", Toast.LENGTH_SHORT).show();
                        emptyView.setVisibility(View.VISIBLE);
                        emptyView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startActivity(new Intent(getActivity(), MainActivity.class));
                            }
                        });
                    }
                }
            }

            @Override
            public void onFailure(Call<OrdersModel> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    public void cancelOrderFromServer(int orderItemId) {
//        progressBar.setVisibility(View.VISIBLE);
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<SuccessModel> call = apiInterface.cancelOrder(PreferencesManager.getPrefString(Constants.AUTH_TOKEN_TYPE) + " " + PreferencesManager.getPrefString(Constants.AUTH_TOKEN), orderItemId);
        call.enqueue(new Callback<SuccessModel>() {
            @Override
            public void onResponse(Call<SuccessModel> call, Response<SuccessModel> response) {
//                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<SuccessModel> call, Throwable t) {
                //  progressBar.setVisibility(View.GONE);
            }
        });
    }
}


