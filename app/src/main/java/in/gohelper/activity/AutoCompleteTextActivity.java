package in.gohelper.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;

import com.google.gson.Gson;

import java.util.List;

import in.gohelper.R;
import in.gohelper.adapter.SuggestionListAdapter;
import in.gohelper.models.servicemodels.ServiceData;
import in.gohelper.models.servicemodels.ServiceModel;
import in.gohelper.rest.ApiClient;
import in.gohelper.rest.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AutoCompleteTextActivity extends BaseActivity {
    private ListView listView;
    private List<ServiceData> suggestions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.auto_complete_text_activity);

        //set the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listView = (ListView) findViewById(R.id.listViewSuggestions);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                ServiceData service = suggestions.get(position);
                String jsonObject = new Gson().toJson(service);
                if (suggestions.get(position).getQuestions().size() > 0) {
                    Intent intent = new Intent(getApplicationContext(), ServiceQuestionActivity.class);
                    intent.putExtra("in.gohelper.intent.serviceObject", jsonObject);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getApplicationContext(), ServiceActivity.class);
                    intent.putExtra("in.gohelper.intent.serviceObject", jsonObject);
                    startActivity(intent);
                }
            }
        });

        AutoCompleteTextView autoCompleteText = (AutoCompleteTextView) toolbar.findViewById(R.id.autoCompleteTextView);
        autoCompleteText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    getAutoCompleteTextFromServer(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    public void getAutoCompleteTextFromServer(String query) {
        showProgress();
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ServiceModel> call = apiInterface.searchService(query);
        call.enqueue(new Callback<ServiceModel>() {
            @Override
            public void onResponse(Call<ServiceModel> call, Response<ServiceModel> response) {
                dismissProgress();
                if (response != null) {
                    if (response.body() != null) {
                        if (response.body().getData() != null) {
                            suggestions = response.body().getData();
                            loadListView();
                        } else {
                        }
                    } else {
                    }
                } else {
                }
            }

            @Override
            public void onFailure(Call<ServiceModel> call, Throwable t) {
                dismissProgress();
            }
        });
    }

    public void loadListView() {
        SuggestionListAdapter adapter = new SuggestionListAdapter(this, suggestions);
        listView.setAdapter(adapter);
    }
}
