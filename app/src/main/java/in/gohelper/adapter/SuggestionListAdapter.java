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
import in.gohelper.models.servicemodels.ServiceData;
import in.gohelper.urls.Urls;
import in.gohelper.utils.Constants;


public class SuggestionListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private List<ServiceData> arrayList;

    public SuggestionListAdapter(Activity context, List<ServiceData> arrayList) {
        super(context, R.layout.auto_complete_text_list_item);
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public View getView(int position, View view, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.auto_complete_text_list_item, null, true);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.suggestion);
        ImageView imgSuggestions = (ImageView) rowView.findViewById(R.id.imgSuggestions);

        //To add custom font
        if (!Constants.USE_CUSTOM_FONT) {
            String fontPath = Constants.CUSTOM_NAME_SIMPLE;
            Typeface tf = Typeface.createFromAsset(context.getAssets(), fontPath);
            txtTitle.setTypeface(tf);
        }

        ServiceData child = arrayList.get(position);
        txtTitle.setText(child.getLabel());

        //Make ImageURL to get image from server
        String baseUrl = Urls.IMAGE_URL;
        String imageUrl = child.getImage();
        String getSuggestionsImageURL = baseUrl + imageUrl;

        if (!TextUtils.isEmpty(getSuggestionsImageURL)) {
            Picasso.with(context).load(getSuggestionsImageURL).placeholder(R.drawable.logo).into(imgSuggestions);
        }
        return rowView;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }
}
