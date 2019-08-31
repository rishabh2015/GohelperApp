package in.gohelper.fragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import in.gohelper.R;
import in.gohelper.adapter.ServiceQuestionAdapter;
import in.gohelper.models.servicemodels.Option;
import in.gohelper.models.servicemodels.Question;
import in.gohelper.utils.Constants;

public class ServiceQuestionFragment extends Fragment {
    private TextView textView;
    private ListView listView;
    private ServiceQuestionAdapter listViewAdapter;

    public Question getPageQuestion() {
        return pageQuestion;
    }

    public void setPageQuestion(Question pageQuestion) {
        this.pageQuestion = pageQuestion;
    }

    private Question pageQuestion;

    public List<Option> getSelectedOptions() {
        return selectedOptions;
    }

    private List<Option> selectedOptions = new ArrayList<>();

    public ServiceQuestionFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_service_question, container, false);
        textView = (TextView) view.findViewById(R.id.textViewQuestion);
        textView.setText(pageQuestion.getQuestionText());
        listView = (ListView)view.findViewById(R.id.listOptions);
        listViewAdapter = new ServiceQuestionAdapter(getActivity(), pageQuestion.getOptions(), selectedOptions);
        listView.setAdapter(listViewAdapter);

        //To add custom font
        if (!Constants.USE_CUSTOM_FONT) {
            String fontPath = Constants.CUSTOM_NAME_SIMPLE;
            Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), fontPath);
            textView.setTypeface(tf);

        }

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
         final HashMap<String, List<Option>> h = new HashMap<>();


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                List<Option> options = pageQuestion.getOptions();
                Option clickedOption = options.get(position);

                if(pageQuestion.getQType().equals("checkbox")) {
                    if (selectedOptions.contains(clickedOption)) {
                        selectedOptions.remove(clickedOption);
                    } else {
                        selectedOptions.add(clickedOption);
                    }
                }
                else {
                    selectedOptions.clear();
                    selectedOptions.add(clickedOption);
                }

                listViewAdapter.setSelectedOptions(selectedOptions);
                listViewAdapter.notifyDataSetChanged();
            }
        });
    }

    public static ServiceQuestionFragment newInstance(Question question) {
        ServiceQuestionFragment fragmentFirst = new ServiceQuestionFragment();
        fragmentFirst.setPageQuestion(question);
        return fragmentFirst;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
