package in.gohelper.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import in.gohelper.R;
import in.gohelper.models.servicemodels.ServiceData;
import in.gohelper.urls.Urls;
import in.gohelper.utils.Constants;

public class TopServicesAdapter extends BaseAdapter {

    private Context mContext;
    private List<ServiceData> arrayList;

    public TopServicesAdapter(Context context, List<ServiceData> arrayList) {
        mContext = context;
        this.arrayList= arrayList;
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
            gridViewAndroid = new View(mContext);
            gridViewAndroid = inflater.inflate(R.layout.grid_view_layout, null);
            TextView textView = (TextView) gridViewAndroid.findViewById(R.id.android_gridview_text);
            ImageView imageView = (ImageView) gridViewAndroid.findViewById(R.id.android_gridview_image);
            textView.setText(arrayList.get(i).getLabel());
            //To add custom font
            if (!Constants.USE_CUSTOM_FONT) {
                String fontPath = Constants.CUSTOM_NAME_SIMPLE;
                Typeface tf = Typeface.createFromAsset(mContext.getAssets(), fontPath);
                textView.setTypeface(tf);
            }

            //TODO set base Url
            String baseUrl = Urls.IMAGE_URL;
            String Url = arrayList.get(i).getImage();
            String getImageURL = baseUrl+Url;

            Picasso.with(mContext)
                    .load(getImageURL)
                    .placeholder(R.drawable.logo)
                    .into(imageView);
        } else {
            gridViewAndroid = (View) convertView;
        }

        return gridViewAndroid;
    }
}