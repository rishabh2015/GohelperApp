package in.gohelper.adapter;

import android.app.Activity;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import in.gohelper.R;
import in.gohelper.models.servicemodels.Option;
import in.gohelper.utils.Constants;

public class ServiceQuestionAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private List<Option> options;

    public List<Option> getSelectedOptions() {
        return selectedOptions;
    }

    public void setSelectedOptions(List<Option> selectedOptions) {
        this.selectedOptions = selectedOptions;
    }

    private List<Option> selectedOptions;

    public ServiceQuestionAdapter(Activity context, List<Option> options, List<Option> optionsSelected) {
        super(context, R.layout.service_question_list_items);
        this.context = context;
        this.options = options;
        this.selectedOptions = optionsSelected;
    }

    @NonNull
    @Override
    public View getView(int position, View view, @NonNull ViewGroup parent) {
        Option currentOption = options.get(position);

        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.service_question_list_items, null, true);
        TextView textView = (TextView) rowView.findViewById(R.id.optionText);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.question_image);

        //To add custom font
        if (!Constants.USE_CUSTOM_FONT) {
            String fontPath = Constants.CUSTOM_NAME_SIMPLE;
            Typeface tf = Typeface.createFromAsset(context.getAssets(), fontPath);
            textView.setTypeface(tf);
        }

        //radioButton.setText(currentOption.getLabel());
        if(selectedOptions != null && selectedOptions.size() > 0) {
            boolean isSelected = selectedOptions.contains(currentOption);
            textView.setText(currentOption.getLabel());
            imageView.setImageResource(isSelected ? R.drawable.ic_radio_button_checked : R.drawable.ic_radio_button_unchecked);
        }
        else {
            textView.setText(currentOption.getLabel());
            imageView.setImageResource(R.drawable.ic_radio_button_unchecked);
        }

        return rowView;
    }

    @Override
    public int getCount() {
        return options.size();
    }
}