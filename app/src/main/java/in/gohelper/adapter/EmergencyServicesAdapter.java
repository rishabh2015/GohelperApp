package in.gohelper.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import in.gohelper.R;
import in.gohelper.models.emergencyservicemodels.EmergencyServiceData;
import in.gohelper.urls.Urls;
import in.gohelper.utils.Constants;

public class EmergencyServicesAdapter extends BaseAdapter {

    private Activity mContext;
    private List<EmergencyServiceData> arrayList;

    public EmergencyServicesAdapter(Activity context, List<EmergencyServiceData> arrayList) {
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
            gridViewAndroid = inflater.inflate(R.layout.grid_view_item_down_home_page, null);
            TextView textView = (TextView) gridViewAndroid.findViewById(R.id.android_gridview_text);
            ImageView imageView = (ImageView) gridViewAndroid.findViewById(R.id.android_gridview_image);
            textView.setText(arrayList.get(i).getTitle());

            if (!Constants.USE_CUSTOM_FONT) {
                String fontPath = Constants.CUSTOM_NAME_SIMPLE;
                Typeface tf = Typeface.createFromAsset(mContext.getAssets(), fontPath);
                textView.setTypeface(tf);
            }

            //To make Image URL to pass to server
            String base_URL = Urls.IMAGE_URL;
            String ES_URL = arrayList.get(i).getImage();
            String getES_URL = base_URL+ES_URL;
            if (!TextUtils.isEmpty(getES_URL)) {
                //Load the image
                Picasso.with(mContext)
                        .load(getES_URL).placeholder(R.drawable.logo)
                        .into(imageView);
            }
        } else {
            gridViewAndroid = (View) convertView;
        }
        return gridViewAndroid;
    }
}