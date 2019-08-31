package in.gohelper.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.google.gson.Gson;

import in.gohelper.R;
import in.gohelper.adapter.OrderListAdapter;
import in.gohelper.models.ordermodels.OrderItem;

public class OrderListActivity extends BaseActivity {

    private OrderItem orderItems;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_list_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        if (intent != null) {
            String orderJson = intent.getStringExtra("in.gohelper.intent.orderItemObject");
            if (orderJson != null) {
                orderItems = new Gson().fromJson(orderJson, OrderItem.class);
            }
        }

        TextView tvOrderNo = (TextView) findViewById(R.id.tv_order_no);
        TextView tvOrderDate = (TextView) findViewById(R.id.tv_order_date);
        TextView tvOrderAmount = (TextView) findViewById(R.id.tv_order_amount);
        //TextView tvTotalDiscount = (TextView) findViewById(R.id.tv_total_discount);
        TextView tvGrandTotal = (TextView) findViewById(R.id.tv_grand_total);
        TextView tvAddress = (TextView) findViewById(R.id.tv_address);
        TextView tvServiceTitle = (TextView) findViewById(R.id.tv_service_title);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        AppCompatImageView ivOrderDelete = (AppCompatImageView) findViewById(R.id.iv_order_delete);

        tvOrderNo.setText(orderItems.getOrderNo());
        tvOrderDate.setText(orderItems.getOrderTime());
        tvOrderAmount.setText(orderItems.getAmount());
        tvGrandTotal.setText(orderItems.getAmount());
        tvAddress.setText(orderItems.getAddress().getAddress() +" , " + orderItems.getAddress().getLocality());
        tvServiceTitle.setText(orderItems.getService().getLabel());

        OrderListAdapter mAdapter = new OrderListAdapter(getApplicationContext(), orderItems.getService());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
    }

}
