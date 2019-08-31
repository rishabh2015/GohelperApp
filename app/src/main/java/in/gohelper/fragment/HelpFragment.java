package in.gohelper.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import in.gohelper.R;
import in.gohelper.activity.AboutUsActivity;
import in.gohelper.activity.ContactUsActivity;
import in.gohelper.activity.FAQActivity;
import in.gohelper.activity.PrivacyPolicyActivity;
import in.gohelper.adapter.HelpAdapter;
import in.gohelper.models.staticpagesmodels.StaticPageData;
import in.gohelper.models.staticpagesmodels.StaticPagesModel;
import in.gohelper.rest.ApiClient;
import in.gohelper.rest.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//Our class extending fragment
public class HelpFragment extends Fragment {
    View view;
    ListView listView;
    List<StaticPageData> staticPages;
    ProgressBar progressBar;
    StaticPageData page;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_help, container, false);

        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        staticPages = new ArrayList<>();
        getStaticPages();

        listView = (ListView) view.findViewById(R.id.list_view_help);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String staticPage = staticPages.get(position).getType();
                switch (staticPage) {
                    case "contact_us": {
                        page = staticPages.get(position);
                        String jsonObject = new Gson().toJson(page);
                        Intent intent = new Intent(getActivity(), ContactUsActivity.class);
                        intent.putExtra("in.gohelper.intent.contactObject", jsonObject);
                        startActivity(intent);
                        break;
                    }
                    case "about_us": {
                        //page = staticPages.get(position);
                        //String jsonObject = new Gson().toJson(page);
                        //Intent intent = new Intent(getActivity(), AboutUsActivity.class);
                        //intent.putExtra("in.gohelper.intent.contactObject", jsonObject);
                        //startActivity(intent);
                        break;
                    }
                    case "FAQ": {
                        //page = staticPages.get(position);
                        //String jsonObject = new Gson().toJson(page);
                        //Intent intent = new Intent(getActivity(), FAQActivity.class);
                        //intent.putExtra("in.gohelper.intent.contactObject", jsonObject);
                        //startActivity(intent);
                        break;
                    }
                    case "privacy_policy": {
                        //page = staticPages.get(position);
                        //String jsonObject = new Gson().toJson(page);
                        //Intent intent = new Intent(getActivity(), PrivacyPolicyActivity.class);
                        //intent.putExtra("in.gohelper.intent.contactObject", jsonObject);
                        //startActivity(intent);
                        break;
                    }
                    default:

                        break;
                }
            }
        });
        return view;
    }

    public void getStaticPages() {
        progressBar.setVisibility(View.VISIBLE);
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<StaticPagesModel> call = apiInterface.getStaticPages();
        call.enqueue(new Callback<StaticPagesModel>() {

            @Override
            public void onResponse(Call<StaticPagesModel> call, Response<StaticPagesModel> response) {
                progressBar.setVisibility(View.GONE);
                if (response != null) {
                    if (response.body() != null) {
                        staticPages = response.body().getData();
                        HelpAdapter adapter = new HelpAdapter(getActivity(), staticPages);
                        listView.setAdapter(adapter);
                    }
                }
            }

            @Override
            public void onFailure(Call<StaticPagesModel> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
            }
        });
    }
}


