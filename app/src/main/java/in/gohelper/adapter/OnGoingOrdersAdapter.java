package in.gohelper.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.List;

import in.gohelper.R;
import in.gohelper.activity.OrderListActivity;
import in.gohelper.fragment.OnGoingOrderFragment;
import in.gohelper.models.ordermodels.OrderItem;
import in.gohelper.utils.Constants;

public class OnGoingOrdersAdapter extends RecyclerView.Adapter<OnGoingOrdersAdapter.ViewHolder> {
    private List<OrderItem> ordersList;
    private Context mContext;

    public OnGoingOrdersAdapter(List<OrderItem> ordersList, Context mContext) {
        this.ordersList = ordersList;
        this.mContext = mContext;
    }

    @Override
    public OnGoingOrdersAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.ongoing_orders_card_items, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final OnGoingOrdersAdapter.ViewHolder viewHolder, final int i) {
        //To add custom font
        if (!Constants.USE_CUSTOM_FONT) {
            String fontPath = Constants.CUSTOM_NAME_SIMPLE;
            Typeface tf = Typeface.createFromAsset(mContext.getAssets(), fontPath);
            viewHolder.tvOrderNo.setTypeface(tf);
            viewHolder.tvOrderAmount.setTypeface(tf);
            viewHolder.tvOrderTime.setTypeface(tf);
            viewHolder.tvServiceLable.setTypeface(tf);
        }

        //To make date format to set schedule at
        String date = ordersList.get(i).getScheduledDateTime();
        String dateShowtoUser = null;
        if (date != null && date.length() > 1) {
            dateShowtoUser = date.substring(0, date.length() - 8);
        }

        viewHolder.tvOrderNo.setText(ordersList.get(i).getOrderNo());
        viewHolder.tvOrderAmount.setText(ordersList.get(i).getAmount());
        viewHolder.tvOrderTime.setText(dateShowtoUser);
        viewHolder.tvSlot.setText(ordersList.get(i).getSlotStart());
        viewHolder.tvServiceLable.setText(ordersList.get(i).getService().getLabel());
        viewHolder.ivOrderDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
                // Setting Dialog Title
                alertDialog.setTitle("Confirm Delete...");
                // Setting Dialog Message
                alertDialog.setMessage("Are you sure you want delete this?");
                // Setting Icon to Dialog
                alertDialog.setIcon(R.drawable.ic_delete_black);
                // Setting Positive "Yes" Button
                alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to invoke YES event
                        OnGoingOrderFragment onGoingOrderFragment = new OnGoingOrderFragment();
                        onGoingOrderFragment.cancelOrderFromServer(ordersList.get(i).getId());
                        ordersList.remove(i);
                        notifyDataSetChanged();
                    }
                });

                // Setting Negative "NO" Button
                alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to invoke NO event
                        dialog.cancel();
                    }
                });
                // Showing Alert Message
                alertDialog.show();
            }
        });
        viewHolder.cvOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final OrderItem orderItem = ordersList.get(i);
                String jsonObject = new Gson().toJson(orderItem);
                Intent intent = new Intent(mContext, OrderListActivity.class);
                intent.putExtra("in.gohelper.intent.orderItemObject", jsonObject);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return ordersList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder  {
        private TextView tvOrderNo;
        private TextView tvOrderAmount;
        private TextView tvOrderTime;
        private TextView tvServiceLable;
        private TextView tvServiceLableText;
        private ImageView ivOrderDelete;
        private CardView cvOrder;
        private TextView tvSlot;

        ViewHolder(View view) {
            super(view);

            cvOrder = (CardView) view.findViewById(R.id.cardView_order);
            tvOrderNo = (TextView) view.findViewById(R.id.tv_order_no);
            tvOrderAmount = (TextView) view.findViewById(R.id.tv_order_amount);
            tvOrderTime = (TextView) view.findViewById(R.id.tv_schedule_at);
            tvServiceLable = (TextView) view.findViewById(R.id.tv_service_lable);
            tvServiceLableText = (TextView) view.findViewById(R.id.tv_service_lable_text);
            ivOrderDelete = (ImageView) view.findViewById(R.id.iv_order_delete);
            tvSlot = (TextView) view.findViewById(R.id.tv_slot);

        }
    }

}