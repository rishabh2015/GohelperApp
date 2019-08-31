package in.gohelper.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import in.gohelper.R;
import in.gohelper.interfaces.DatePickerActionListener;

public class SelectDateHorizontalAdapter extends RecyclerView.Adapter<SelectDateHorizontalAdapter.MyViewHolder> {
    private Context mContext;
    private final List<Date> dates;
    private DatePickerActionListener listener;
    public int selectedPosition;
    public Date selectedDate;

    class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout layout;
        TextView txtViewDays;
        TextView txtViewDates;
        TextView txtViewMonths;

        MyViewHolder(View view) {
            super(view);
            layout = (LinearLayout) view.findViewById(R.id.selectDateLayout);
            txtViewDays = (TextView) view.findViewById(R.id.txtViewDays);
            txtViewDates = (TextView) view.findViewById(R.id.txtViewDates);
            txtViewMonths = (TextView) view.findViewById(R.id.txtViewMonths);
        }
    }


    public SelectDateHorizontalAdapter(Context c, List<Date> dates, DatePickerActionListener listener) {
        this.mContext = c;
        this.dates = dates;
        this.listener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.select_date_horizontal_items, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        final Date date = dates.get(position);
        DateFormat formatDay = new SimpleDateFormat("EEE");
        DateFormat formatDate = new SimpleDateFormat("dd");
        DateFormat formatMonth = new SimpleDateFormat("MMM");

        holder.txtViewDays.setText(formatDay.format(date));
        holder.txtViewDates.setText(formatDate.format(date));
        holder.txtViewMonths.setText(formatMonth.format(date));

        if(position == selectedPosition) {
            holder.layout.setBackgroundColor(mContext.getResources().getColor(R.color.colorPrimary));
            selectedDate = date;
        }
        else {
            holder.layout.setBackgroundColor(Color.TRANSPARENT);
        }

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(position != selectedPosition) {
                    listener.onSelectItemAtPosition(position, selectedPosition);
                    selectedPosition = position;
                    selectedDate = date;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return dates.size();
    }
}
