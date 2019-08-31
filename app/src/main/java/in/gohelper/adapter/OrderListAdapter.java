package in.gohelper.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import in.gohelper.R;
import in.gohelper.models.ordermodels.OrderData;
import in.gohelper.models.servicemodels.Option;
import in.gohelper.models.servicemodels.Question;
import in.gohelper.models.servicemodels.ServiceData;
import in.gohelper.utils.Constants;

public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.MyViewHolder> {

    private ServiceData service;
    private Context mContext;


    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textQuestions, textOptions;

        MyViewHolder(View view) {
            super(view);
            textQuestions = (TextView) itemView.findViewById(R.id.textViewQuestion);
            textOptions = (TextView) itemView.findViewById(R.id.textViewOptions);
        }
    }


    public OrderListAdapter(Context context, ServiceData service) {
        mContext = context;
        this.service = service;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_list_items, parent, false);

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
        Question question = service.getQuestions().get(position);
        if(question != null) {
            holder.textQuestions.setText(question.getQuestionText());
            Option option = question.getSelectedOptions().get(0);
            if(option != null) {
                holder.textOptions.setText(option.getLabel());
            }
        }
    }

    @Override
    public int getItemCount() {
        return service.getQuestions().size();
    }
}