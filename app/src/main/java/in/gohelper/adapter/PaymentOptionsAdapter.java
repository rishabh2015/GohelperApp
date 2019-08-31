package in.gohelper.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import in.gohelper.R;
import in.gohelper.models.paymentmodels.Payment;
import in.gohelper.utils.Constants;

public class PaymentOptionsAdapter extends BaseAdapter {

    private Context mContext;
    private List<Payment> arrayList;

    public PaymentOptionsAdapter(Context context, List<Payment> arrayList) {
        mContext = context;
        this.arrayList = arrayList;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        View gridViewAndroid;
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            gridViewAndroid = inflater.inflate(R.layout.payment_option_list_item, null);
            TextView textOptions = (TextView) gridViewAndroid.findViewById(R.id.tv_payment_option_label);
            if (arrayList != null) {
                Payment payment = arrayList.get(i);
                textOptions.setText(payment.getPayment().getPaymentLabel());

                //To add custom font
                if (!Constants.USE_CUSTOM_FONT) {
                    String fontPath = Constants.CUSTOM_NAME_SIMPLE;
                    Typeface tf = Typeface.createFromAsset(mContext.getAssets(), fontPath);
                    textOptions.setTypeface(tf);
                }
            }
        } else {
            gridViewAndroid = (View) convertView;
        }
        return gridViewAndroid;
    }
}