package in.gohelper.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import in.gohelper.R;
import in.gohelper.fragment.PastOrderFragment;
import in.gohelper.models.ordermodels.OrderItem;
import in.gohelper.utils.Constants;

public class PastOrdersAdapter extends RecyclerView.Adapter<PastOrdersAdapter.ViewHolder> {
    private List<OrderItem> ordersList;
    private Context mContext;

    public PastOrdersAdapter(List<OrderItem> ordersList, Context mContext) {
        this.ordersList = ordersList;
        this.mContext = mContext;
    }

    @Override
    public PastOrdersAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.past_orders_card_items, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PastOrdersAdapter.ViewHolder viewHolder, final int i) {
        //To add custom font
        if (!Constants.USE_CUSTOM_FONT) {
            String fontPath = Constants.CUSTOM_NAME_SIMPLE;
            Typeface tf = Typeface.createFromAsset(mContext.getAssets(), fontPath);
            viewHolder.tvOrderNo.setTypeface(tf);
            viewHolder.tvOrderAmount.setTypeface(tf);
            viewHolder.tvOrderTime.setTypeface(tf);
            viewHolder.tvServiceLable.setTypeface(tf);
        }

        viewHolder.tvOrderNo.setText(ordersList.get(i).getOrderNo());
        viewHolder.tvOrderAmount.setText(ordersList.get(i).getAmount());
        //viewHolder.tvOrderTime.setText(ordersList.get(i).getOrderItemsStatus());
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
                        PastOrderFragment onGoingOrderFragment = new PastOrderFragment();
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
    }

    @Override
    public int getItemCount() {
        return ordersList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvOrderNo;
        private TextView tvOrderAmount;
        private TextView tvOrderTime;
        private TextView tvServiceLable;
        private TextView tvServiceLableText;
        private ImageView ivOrderDelete;

        public ViewHolder(View view) {
            super(view);

            tvOrderNo = (TextView) view.findViewById(R.id.tv_order_no);
            tvOrderAmount = (TextView) view.findViewById(R.id.tv_order_amount);
            tvOrderTime = (TextView) view.findViewById(R.id.tv_order_time);
            tvServiceLable = (TextView) view.findViewById(R.id.tv_service_lable);
            tvServiceLableText = (TextView) view.findViewById(R.id.tv_service_lable_text);
            ivOrderDelete = (ImageView) view.findViewById(R.id.iv_order_delete);
        }
    }

}