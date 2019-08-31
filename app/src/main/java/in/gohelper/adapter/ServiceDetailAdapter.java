package in.gohelper.adapter;

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
import in.gohelper.models.servicemodels.Metum;
import in.gohelper.urls.Urls;
import in.gohelper.utils.Constants;

public class ServiceDetailAdapter extends BaseAdapter {

    private Context mContext;
    private List<Metum> arrayList;


    public ServiceDetailAdapter(Context context, List<Metum> arrayList) {
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
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        convertView = inflater.inflate(R.layout.grid_view_servicedetail, parent, false);
        TextView textView = (TextView) convertView.findViewById(R.id.android_gridview_text);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.android_gridview_image);
        Metum meta = arrayList.get(i);
        textView.setText(meta.getIconTitle());

        //To add custom font
        if (!Constants.USE_CUSTOM_FONT) {
            String fontPath = Constants.CUSTOM_NAME_SIMPLE;
            Typeface tf = Typeface.createFromAsset(mContext.getAssets(), fontPath);
            textView.setTypeface(tf);
        }

        String baseUrl = Urls.IMAGE_URL;
        String Url = meta.getIcon();
        String getImageURL = baseUrl + Url;

        if (!TextUtils.isEmpty(getImageURL)) {
            Picasso.with(mContext).load(getImageURL).placeholder(R.drawable.logo).into(imageView);
        }
        return convertView;
    }
}