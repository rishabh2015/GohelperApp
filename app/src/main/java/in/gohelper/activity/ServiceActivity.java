package in.gohelper.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import java.util.ArrayList;
import java.util.List;

import in.gohelper.R;
import in.gohelper.adapter.ServiceDetailAdapter;
import in.gohelper.adapter.ServiceDetailListAdapter;
import in.gohelper.models.MarketingBoxModels.MarketingBoxModel;
import in.gohelper.models.servicemodels.Metum;
import in.gohelper.models.servicemodels.ServiceData;
import in.gohelper.models.servicemodels.ServiceDetailModel;
import in.gohelper.rest.ApiClient;
import in.gohelper.rest.ApiInterface;
import in.gohelper.urls.Urls;
import in.gohelper.utils.Constants;
import in.gohelper.utils.PreferencesManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServiceActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    private ServiceData serviceData;
    private CarouselView carouselView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.service_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        AppCompatTextView tvWhyGohelper = (AppCompatTextView) findViewById(R.id.whyGohelper);

        //To add custom font
        if (!Constants.USE_CUSTOM_FONT) {
            String fontPath = Constants.CUSTOM_NAME_SIMPLE;
            Typeface tf = Typeface.createFromAsset(ServiceActivity.this.getAssets(), fontPath);
            tvWhyGohelper.setTypeface(tf);
        }

        Intent intent = getIntent();
        if (intent != null) {
            String serviceJson = intent.getStringExtra("in.gohelper.intent.serviceObject");
            if (serviceJson != null) {
                serviceData = new Gson().fromJson(serviceJson, ServiceData.class);
            }
        }

        //Set MenuTitle
        String menuTitle = serviceData.getMenuTitle();
        if (TextUtils.isEmpty(menuTitle)) {
            menuTitle = serviceData.getLabel();
        }
        getSupportActionBar().setTitle(menuTitle);

        //Setup carousel view for each service detail page
        carouselView = (CarouselView) findViewById(R.id.carouselViewServiceDetail);
        String mbIdentifier = null;
        for (int i = 0; i < serviceData.getMeta().size(); i++) {
            Metum meta = serviceData.getMeta().get(i);
            if (meta.getMetaType().equals("mbs")) {
                String mb = meta.getDescription();
                if (!TextUtils.isEmpty(mb)) {
                    mbIdentifier = mb;
                    // Only to get only one mb identifier
                    break;
                }
            } else {
            }
        }
        if (!TextUtils.isEmpty(mbIdentifier)) {
            //Download Marketing boxes
            getServiceDetailMB(mbIdentifier);
        } else {
            //TODO: Hide Carusel view
        }

        //Setup grid view for whyus
        GridView gridView = (GridView) findViewById(R.id.grid_view);
        List<Metum> whyusList = new ArrayList<Metum>();
        for (int i = 0; i < serviceData.getMeta().size(); i++) {
            Metum meta = serviceData.getMeta().get(i);
            if (meta.getMetaType().equals("why_us")) {
                whyusList.add(meta);
            } else {
            }
        }

        ServiceDetailAdapter adapterView = new ServiceDetailAdapter(ServiceActivity.this, whyusList);
        gridView.setAdapter(adapterView);
        ListView list = (ListView) findViewById(R.id.list);
        list.setOnItemClickListener(this);
        ServiceDetailListAdapter adapter = new ServiceDetailListAdapter(ServiceActivity.this, serviceData.getChilds());
        list.setAdapter(adapter);
        //setListViewHeightBasedOnChildren(list);
    }

    public void getServiceDetailMB(String identifier) {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<MarketingBoxModel> call = apiInterface.marketingBox(identifier);
        call.enqueue(new Callback<MarketingBoxModel>() {
            @Override
            public void onResponse(Call<MarketingBoxModel> call, final Response<MarketingBoxModel> response) {
                Log.e("Size", "" + response.body().getData().size());
                ImageListener listener = new ImageListener() {
                    @Override
                    public void setImageForPosition(int position, ImageView imageView) {

                        //To make Image URL to pass to server
                        String baseUrl = Urls.IMAGE_URL;
                        String imageUrl = response.body().getData().get(position).getImage();
                        String getServiceDetailMBImageURL = baseUrl + imageUrl;
                        if (!TextUtils.isEmpty(getServiceDetailMBImageURL)) {
                            //Load the image
                            Picasso.with(getApplicationContext())
                                    .load(getServiceDetailMBImageURL).placeholder(R.drawable.placeholder)
                                    .into(imageView);
                        }
                    }
                };
                carouselView.setImageListener(listener);
                carouselView.setPageCount(response.body().getData().size());
            }

            @Override
            public void onFailure(Call<MarketingBoxModel> call, Throwable t) {
            }
        });
    }

    public void getServiceDetailFromServer(int serviceId) {
        showProgress();
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ServiceDetailModel> call = apiInterface.getServiceDetail(PreferencesManager.getPrefString(Constants.AUTH_TOKEN_TYPE) + " " + PreferencesManager.getPrefString(Constants.AUTH_TOKEN), serviceId);
        call.enqueue(new Callback<ServiceDetailModel>() {
            @Override
            public void onResponse(Call<ServiceDetailModel> call, final Response<ServiceDetailModel> response) {
                dismissProgress();
                if (response != null) {
                    if (response.body() != null) {
                        if (response.body().getData() != null) {
                            ServiceData service = response.body().getData();
                            String jsonObject = new Gson().toJson(service);
                            if (service.getQuestions().size() == 0) {
                                Intent intent = new Intent(getApplicationContext(), ServiceActivity.class);
                                intent.putExtra("in.gohelper.intent.serviceObject", jsonObject);
                                startActivity(intent);
                            } else {
                                if (service.getQuestions().get(0).getQType().equals("radio")) {
                                    Intent intent = new Intent(ServiceActivity.this, ServiceQuestionActivity.class);
                                    intent.putExtra("in.gohelper.intent.serviceObject", jsonObject);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(ServiceActivity.this, "Question Checkbox Page", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }else {}
                    }else {}
                }else {}
            }

            @Override
            public void onFailure(Call<ServiceDetailModel> call, Throwable t) {
                dismissProgress();
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        getServiceDetailFromServer(serviceData.getChilds().get(position).getId());
    }
}
