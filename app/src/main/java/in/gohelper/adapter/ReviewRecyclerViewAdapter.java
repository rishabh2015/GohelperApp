package in.gohelper.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import in.gohelper.R;
import in.gohelper.models.servicemodels.Option;
import in.gohelper.models.servicemodels.Question;
import in.gohelper.utils.Constants;

public class ReviewRecyclerViewAdapter extends RecyclerView.Adapter<ReviewRecyclerViewAdapter.MyViewHolder> {

    private List<Question> arrayList;
    private Context mContext;


    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textQuestions, textOptions;

        MyViewHolder(View view) {
            super(view);
            textQuestions = (TextView) itemView.findViewById(R.id.textViewQuestion);
            textOptions = (TextView) itemView.findViewById(R.id.textViewOptions);
        }
    }


    public ReviewRecyclerViewAdapter(Context context, List<Question> arrayList) {
        mContext = context;
        this.arrayList = arrayList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_list_items, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
//To add custom font
        if (!Constants.USE_CUSTOM_FONT) {
            String fontPath = Constants.CUSTOM_NAME_SIMPLE;
            Typeface tf = Typeface.createFromAsset(mContext.getAssets(), fontPath);
            holder.textQuestions.setTypeface(tf);
            holder.textOptions.setTypeface(tf);
        }

        if (arrayList != null) {
            Question question = arrayList.get(position);
            if (question != null) {
                holder.textQuestions.setText(question.getQuestionText());
                List<Option> selectedOptions = question.getSelectedOptions();
                List<String> optionsText = new ArrayList<>();
                for (Option option : selectedOptions) {
                    optionsText.add(option.getLabel());
                }
                String selectedLabel = TextUtils.join(",", optionsText);
                holder.textOptions.setText(selectedLabel);
            }
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}