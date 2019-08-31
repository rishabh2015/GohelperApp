package in.gohelper.adapter;

import android.app.Activity;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import in.gohelper.R;
import in.gohelper.models.staticpagesmodels.StaticPageData;
import in.gohelper.urls.Urls;
import in.gohelper.utils.Constants;

public class HelpAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private List<StaticPageData> staticPageData;

    public HelpAdapter(Activity context,  List<StaticPageData> staticPageData) {
        super(context, R.layout.help_recycler_items);
        this.context = context;
        this.staticPageData = staticPageData;
    }

    @NonNull
    @Override
    public View getView(int position, View view, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View rowView = inflater.inflate(R.layout.help_recycler_items, null, true);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.txt_help);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.iv);


        //To add custom font
        if (!Constants.USE_CUSTOM_FONT) {
            String fontPath = Constants.CUSTOM_NAME_SIMPLE;
            Typeface tf = Typeface.createFromAsset(context.getAssets(), fontPath);
            txtTitle.setTypeface(tf);
        }

        StaticPageData child = staticPageData.get(position);
        txtTitle.setText(child.getTitle());

        //To make Image URl
        String baseUrl = Urls.IMAGE_URL;
        String imageUrl = child.getIcon();
        String getImageURL = baseUrl + imageUrl;

        if (!TextUtils.isEmpty(getImageURL)) {
            //To load image from server
            Picasso.with(context).load(getImageURL).placeholder(R.drawable.logo).into(imageView);
        }

        //  String imageUrl = child.getImage();
        if(!TextUtils.isEmpty(getImageURL)) {
        }
        return rowView;
    }

    @Override
    public int getCount() {
        return staticPageData.size();
    }
}