package in.gohelper.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import in.gohelper.R;
import in.gohelper.activity.MyCartActivity;
import in.gohelper.models.cartmodels.CartData;
import in.gohelper.models.servicemodels.Option;
import in.gohelper.models.servicemodels.Question;
import in.gohelper.utils.Constants;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    private List<CartData> listItems;
    private Context mContext;

    public CartAdapter(List<CartData> listItems, Context mContext) {
        this.listItems = listItems;
        this.mContext = mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_card_view_items, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final CartData cartData = listItems.get(position);
        //
        String date = cartData.getCartItem().getScheduledDateTime();
        String dateShowtoUser = null;
        if (date != null && date.length() > 1) {
            dateShowtoUser = date.substring(0, date.length() - 8);
        }
        holder.tvAmount.setText(cartData.getCartItem().getItemAmount());
        holder.menuTitle.setText(cartData.getCartItem().getService().getLabel());

        Question question = cartData.getCartItem().getService().getQuestions().get(0);
        if(question != null) {
            holder.question1.setText(question.getQuestionText());
            Option option = question.getSelectedOptions().get(0);
            if(option != null) {
                holder.option1.setText(option.getLabel());
            }
        }
        holder.addresses.setText(cartData.getCartItem().getAddress().getFullName() + ":" + cartData.getCartItem().getAddress().getAddress() + ","
                + cartData.getCartItem().getAddress().getLocality());
        holder.tvDateTime.setText(dateShowtoUser);
        holder.tvSlot.setText(cartData.getCartItem().getSlotStart());

        if ((cartData.getCartItem().getService().getQuestions().size() - 1) == 0) {
            holder.tvViewMore.setVisibility(View.GONE);
        } else {
            holder.tvViewMore.setVisibility(View.VISIBLE);
            holder.tvViewMore.setText("+" + (cartData.getCartItem().getService().getQuestions().size() - 1) + " view more");
        }

        holder.cartDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mContext instanceof MyCartActivity) {
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
                            ((MyCartActivity) mContext).onDeleteCartItem(cartData);
                            listItems.remove(position);
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
            }
        });

        holder.cartEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MyCartActivity) mContext).onEditCartItem(cartData);
            }
        });

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MyCartActivity) mContext).onSelectCartItem(cartData);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView menuTitle, question1, option1, addresses;
        private CardView cardView;
        private AppCompatImageView cartDelete;
        private AppCompatImageView cartEdit;
        private TextView tvScheduleAtText;
        private TextView tvAddressText;
        private TextView tvDateTime;
        private TextView tvAmount;
        private TextView tvSlot;
        private TextView tvViewMore;

        ViewHolder(View itemView) {
            super(itemView);

            menuTitle = (TextView) itemView.findViewById(R.id.tv_menu_title);
            question1 = (TextView) itemView.findViewById(R.id.tv_question1);
            option1 = (TextView) itemView.findViewById(R.id.tv_option1);
            tvAmount = (TextView) itemView.findViewById(R.id.tv_amount);
            addresses = (TextView) itemView.findViewById(R.id.tv_address_textes);
            cardView = (CardView) itemView.findViewById(R.id.cv_cart);
            cartDelete = (AppCompatImageView) itemView.findViewById(R.id.iv_cart_delete);
            cartEdit = (AppCompatImageView) itemView.findViewById(R.id.iv_cart_edit);
            tvScheduleAtText = (TextView) itemView.findViewById(R.id.tv_scheduleAt_text);
            tvAddressText = (TextView) itemView.findViewById(R.id.tv_address_text);
            tvDateTime = (TextView) itemView.findViewById(R.id.tv_date_time);
            tvSlot = (TextView) itemView.findViewById(R.id.tv_slot);
            tvViewMore = (TextView) itemView.findViewById(R.id.tv_view_more);

            //To add custom font
            if (!Constants.USE_CUSTOM_FONT) {
                String fontPath = Constants.CUSTOM_NAME_SIMPLE;
                Typeface tf = Typeface.createFromAsset(mContext.getAssets(), fontPath);
                menuTitle.setTypeface(tf);

            }
        }
    }


}