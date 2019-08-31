package in.gohelper.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.List;

import in.gohelper.R;
import in.gohelper.activity.AddAddressActivity;
import in.gohelper.activity.ManageAddressActivity;
import in.gohelper.interfaces.AddressListActionListener;
import in.gohelper.models.CustomerAddressmodels.CustomerAddressData;
import in.gohelper.utils.Constants;
import in.gohelper.utils.PreferencesManager;

public class ManageAddressAdapter extends RecyclerView.Adapter<ManageAddressAdapter.ViewHolder> {
    private List<CustomerAddressData> listItems;
    private Context mContext;

    public void setAddressListActionListener(AddressListActionListener addressListActionListener) {
        this.addressListActionListener = addressListActionListener;
    }

    private AddressListActionListener addressListActionListener;

    public ManageAddressAdapter(List<CustomerAddressData> listItems, Context mContext) {
        this.listItems = listItems;
        this.mContext = mContext;
        new PreferencesManager(mContext);
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.manage_address_card_items, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        //To add custom font
        if (!Constants.USE_CUSTOM_FONT) {
            String fontPath = Constants.CUSTOM_NAME_SIMPLE;
            Typeface tf = Typeface.createFromAsset(mContext.getAssets(), fontPath);
            holder.txtTitle.setTypeface(tf);
            holder.txtDescription.setTypeface(tf);
        }

        final CustomerAddressData customerAddressData = listItems.get(position);
        holder.txtTitle.setText("Address " + (position + 1));
        holder.txtDescription.setText(customerAddressData.getFullName() + " :  " + customerAddressData.getPhoneNumber());
        holder.tvGetAddress.setText(customerAddressData.getAddress() +" , " + customerAddressData.getLocality());
                PreferencesManager.setPrefString(Constants.CUSTOMER_ADDRESS, listItems.get(0).getAddress());
        holder.txtOptionDigits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Display Option Menu
                PopupMenu popupMenu = new PopupMenu(mContext, holder.txtOptionDigits);
                popupMenu.inflate(R.menu.card_main);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        String jsonObject = new Gson().toJson(customerAddressData);
                        switch (item.getItemId()) {
                            case R.id.edit:
                                Intent intent = new Intent(mContext, AddAddressActivity.class);
                                intent.putExtra("in.gohelper.intent.addressObject", jsonObject);
                                mContext.startActivity(intent);
                                break;
                            case R.id.delete:
                                AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
                                // Setting Dialog Title
                                alertDialog.setTitle("Confirm Delete...");
                                // Setting Dialog Message
                                alertDialog.setMessage("Are you sure you want delete this?");
                                // Setting Icon to Dialog
                                alertDialog.setIcon(R.drawable.ic_delete_black);
                                // Setting Positive "Yes" Button
                                alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {public void onClick(DialogInterface dialog, int which) {
                                                // Write your code here to invoke YES event
                                            ManageAddressActivity activity = new ManageAddressActivity();
                                            activity.deleteCustomerAddressFromServer(listItems.get(position).getId());
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

                                break;
                            default:
                                break;
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });

        //send address onClick review activity select address
        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(addressListActionListener != null) {
                    addressListActionListener.onSelectAddress(customerAddressData);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txtTitle;
        public TextView txtDescription;
        public TextView tvGetAddress;
        public ImageView txtOptionDigits;
        CardView cv;


        public ViewHolder(View itemView) {
            super(itemView);
            txtTitle = (TextView) itemView.findViewById(R.id.txtTitle);
            txtDescription = (TextView) itemView.findViewById(R.id.txtDescription);
            txtOptionDigits = (ImageView) itemView.findViewById(R.id.txtOptionDigits);
            cv = (CardView) itemView.findViewById(R.id.cardView);
            tvGetAddress = (TextView) itemView.findViewById(R.id.tv_getAddress);

        }
    }
}