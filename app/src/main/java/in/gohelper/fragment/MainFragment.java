package in.gohelper.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;
import com.synnapps.carouselview.ViewListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import in.gohelper.R;
import in.gohelper.activity.AddAddressActivity;
import in.gohelper.activity.AutoCompleteTextActivity;
import in.gohelper.activity.ServiceActivity;
import in.gohelper.activity.ServiceQuestionActivity;
import in.gohelper.adapter.EmergencyServicesAdapter;
import in.gohelper.adapter.TopServicesAdapter;
import in.gohelper.models.MarketingBoxModels.MarketingBoxModel;
import in.gohelper.models.SuccessModel;
import in.gohelper.models.emergencyservicemodels.EmergencyServiceModel;
import in.gohelper.models.servicemodels.ServiceData;
import in.gohelper.models.servicemodels.ServiceModel;
import in.gohelper.models.testimonialsmodels.TestimonialsModel;
import in.gohelper.rest.ApiClient;
import in.gohelper.rest.ApiInterface;
import in.gohelper.urls.Urls;
import in.gohelper.utils.Constants;
import in.gohelper.utils.MyGridView;
import in.gohelper.utils.PreferencesManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static com.facebook.FacebookSdk.getApplicationContext;

public class MainFragment extends Fragment implements AdapterView.OnItemClickListener {
    private CarouselView carouselView, carouselViewTextinomial;
    private GridView gridview;
    private MyGridView myGridView;
    private List<ServiceData> topServices;
    private TextView tvAddress;
    private AppCompatButton searchView;
    private final static int MY_PERMISSION_FINE_LOCATION = 101;
    private final static int PLACE_PICKER_REQUEST = 1;
    private String upadteAddress;
    private List<Address> addresses;
    private  String postalCode;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_main, container, false);

        requestPermission();
        new PreferencesManager(getActivity());


        carouselView = (CarouselView) rootview.findViewById(R.id.carouselView);
        gridview = (GridView) rootview.findViewById(R.id.grid_view_down);
        myGridView = (MyGridView) rootview.findViewById(R.id.grid_view1);
        carouselViewTextinomial = (CarouselView) rootview.findViewById(R.id.carouselView1);
        tvAddress = (TextView) rootview.findViewById(R.id.tv_address);
        searchView = (AppCompatButton) rootview.findViewById(R.id.search_view);
        AppCompatTextView tvTopServiceTitle = (AppCompatTextView) rootview.findViewById(R.id.tv_topServiceTitle);
        AppCompatTextView tvEmergencyService = (AppCompatTextView) rootview.findViewById(R.id.textview_emergency_service);
        AppCompatTextView tvHappyCustomers = (AppCompatTextView) rootview.findViewById(R.id.happyCustomers);
        AppCompatTextView tvShareLove = (AppCompatTextView) rootview.findViewById(R.id.shareLove);
        AppCompatTextView tvShare = (AppCompatTextView) rootview.findViewById(R.id.tv_share);

        //add custom font
        if (!Constants.USE_CUSTOM_FONT) {
            String fontPath = Constants.CUSTOM_NAME_SIMPLE;
            Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), fontPath);
            tvTopServiceTitle.setTypeface(tf);
            tvHappyCustomers.setTypeface(tf);
            tvEmergencyService.setTypeface(tf);
            tvAddress.setTypeface(tf);
            tvShareLove.setTypeface(tf);
            tvShare.setTypeface(tf);
            searchView.setTypeface(tf);
        }
        topServices = new ArrayList<>();
        getMarketingBox();
        getTopService();
        getTestinomial();
        getEmergencyServices();

        //guest not verified
        if (!PreferencesManager.getPrefBool(Constants.MOBILE_VERIFIED)) {
            tvAddress.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AutocompleteFilter typeFilter = new AutocompleteFilter.Builder().
                            setTypeFilter(Place.TYPE_COUNTRY).setCountry("IN").build();
                    try {
                        Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                                .setFilter(typeFilter)
                                .build(getActivity());
                        startActivityForResult(intent, PLACE_PICKER_REQUEST);
                    } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
                        e.printStackTrace();
                    }
                }
            });
        } else {
            tvAddress.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tvAddress.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AutocompleteFilter typeFilter = new AutocompleteFilter.Builder().
                                    setTypeFilter(Place.TYPE_COUNTRY).setCountry("IN").build();
                            try {
                                Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                                        .setFilter(typeFilter)
                                        .build(getActivity());
                                startActivityForResult(intent, PLACE_PICKER_REQUEST);
                            } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            });
        }

        if (searchView != null) {
            searchView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), AutoCompleteTextActivity.class);
                    startActivity(intent);
                }
            });
        }

        final NestedScrollView scrollView = ((NestedScrollView) rootview.findViewById(R.id.scrollView));
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(ScrollView.FOCUS_UP);
            }
        });

        int[] attrs = new int[]{R.attr.selectableItemBackground};
        TypedArray typedArray = getActivity().obtainStyledAttributes(attrs);
        int backgroundResource = typedArray.getResourceId(0, 0);
        myGridView.setBackgroundResource(backgroundResource);
        myGridView.setOnItemClickListener(this);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                switch (position) {
                    case 0:
                        Intent callIntent = new Intent(Intent.ACTION_DIAL);
                        callIntent.setData(Uri.parse("tel://181"));
                        startActivity(callIntent);
                        break;
                    case 1:
                        Intent callIntent1 = new Intent(Intent.ACTION_DIAL);
                        callIntent1.setData(Uri.parse("tel://100"));
                        startActivity(callIntent1);
                        break;
                    case 2:
                        Intent callIntent2 = new Intent(Intent.ACTION_DIAL);
                        callIntent2.setData(Uri.parse("tel://102"));
                        startActivity(callIntent2);
                        break;
                    case 3:
                        Intent callIntent3 = new Intent(Intent.ACTION_DIAL);
                        callIntent3.setData(Uri.parse("tel://101"));
                        startActivity(callIntent3);
                        break;
                }
            }
        });

        AppCompatImageView imageView = (AppCompatImageView) rootview.findViewById(R.id.shareApp);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                String shareBody = "Please download the hyperLocal app here";
                String shareSub = "Your gohelper service link is: https://www.hyperLocal.in";
                intent.putExtra(Intent.EXTRA_SUBJECT, shareBody);
                intent.putExtra(Intent.EXTRA_TEXT, shareSub);
                startActivity(Intent.createChooser(intent, "Share Via"));
            }
        });
        return rootview;
    }

    private void requestPermission() {
        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSION_FINE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSION_FINE_LOCATION:
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(getActivity(), data);

                if (place.getAddress() != null) {
                    upadteAddress = place.getAddress().toString();
                    PreferencesManager.setPrefString(Constants.CUSTOMER_LOCATION, upadteAddress);
                }

                Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
                double latitude = place.getLatLng().latitude;
                double longitude = place.getLatLng().longitude;

                try {
                    addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                } catch (IOException e) {
                    e.printStackTrace();
                }

                String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                String city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();
                postalCode = addresses.get(0).getPostalCode();


                String knownName = addresses.get(0).getFeatureName();
                postCustomerUpdateLocation();
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ServiceData service = topServices.get(position);
        String jsonObject = new Gson().toJson(service);
        if (topServices.get(position).getQuestions().size() > 0) {
            Intent intent = new Intent(getActivity(), ServiceQuestionActivity.class);
            intent.putExtra("in.gohelper.intent.serviceObject", jsonObject);
            startActivity(intent);
        } else {
            Intent intent = new Intent(getActivity(), ServiceActivity.class);
            intent.putExtra("in.gohelper.intent.serviceObject", jsonObject);
            startActivity(intent);
        }
    }

    public void getMarketingBox() {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<MarketingBoxModel> call = apiInterface.marketingBox("HP_MB");
        call.enqueue(new Callback<MarketingBoxModel>() {
            @Override
            public void onResponse(Call<MarketingBoxModel> call, final Response<MarketingBoxModel> response) {
                ImageListener listener = new ImageListener() {
                    @Override
                    public void setImageForPosition(int position, ImageView imageView) {

                        //To make Image URL to pass to server
                        String baseUrl = Urls.IMAGE_URL;
                        String MB_Url = response.body().getData().get(position).getImage();
                        String getMBImageURL = baseUrl + MB_Url;
                        if (!TextUtils.isEmpty(getMBImageURL)) {
                            //Load the image
                            Picasso.with(getActivity())
                                    .load(getMBImageURL).placeholder(R.drawable.placeholder)
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

    public void getEmergencyServices() {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<EmergencyServiceModel> call = apiInterface.emergencyServices();
        call.enqueue(new Callback<EmergencyServiceModel>() {
            @Override
            public void onResponse(Call<EmergencyServiceModel> call, final Response<EmergencyServiceModel> response) {
                if (response != null) {
                    if (response.body() != null) {
                        if (response.body().getData() != null) {
                            EmergencyServicesAdapter adapterView = new EmergencyServicesAdapter(getActivity(), response.body().getData());
                            gridview.setAdapter(adapterView);
                        } else {
                        }
                    } else {
                    }
                } else {
                }
            }

            @Override
            public void onFailure(Call<EmergencyServiceModel> call, Throwable t) {
            }
        });
    }

    public void getTestinomial() {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<TestimonialsModel> call = apiInterface.testimonial();
        call.enqueue(new Callback<TestimonialsModel>() {
            @Override
            public void onResponse(final Call<TestimonialsModel> call, final Response<TestimonialsModel> response) {
                ViewListener viewListener = new ViewListener() {
                    @Override
                    public View setViewForPosition(int position) {
                        View customView = getActivity().getLayoutInflater().inflate(R.layout.carousel_item_layout, null);
                        ImageView imageView = (ImageView) customView.findViewById(R.id.review_profile_pic);
                        TextView textView = (TextView) customView.findViewById(R.id.reviewTv);
                        TextView textViewReviwer = (TextView) customView.findViewById(R.id.reviewer_name);

                        //To add custom font
                        if (!Constants.USE_CUSTOM_FONT) {
                            String fontPath = Constants.CUSTOM_NAME_SIMPLE;
                            Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), fontPath);
                            textView.setTypeface(tf);
                            textViewReviwer.setTypeface(tf);
                        }

                        //To make Image URL to pass to server
                        String baseUrl = Urls.IMAGE_URL;
                        String testinomial_Url = response.body().getData().get(position).getReviewer().getImage();
                        String getTestinomialImageURL = baseUrl + testinomial_Url;
                        if (!TextUtils.isEmpty(getTestinomialImageURL)) {
                            //Load the image
                            Picasso.with(getActivity())
                                    .load(getTestinomialImageURL)
                                    .placeholder(R.drawable.logo).resize(90, 90).centerCrop()
                                    .into(imageView);
                        }

                        textView.setText("" + response.body().getData().get(position).getReview());
                        textViewReviwer.setText("" + response.body().getData().get(position).getReviewer().getName());
                        return customView;
                    }
                };

                carouselViewTextinomial.setViewListener(viewListener);
                carouselViewTextinomial.setPageCount(response.body().getData().size());
            }

            @Override
            public void onFailure(Call<TestimonialsModel> call, Throwable t) {
            }
        });
    }

    public void getTopService() {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ServiceModel> call = apiInterface.getTopService();
        call.enqueue(new Callback<ServiceModel>() {
            @Override
            public void onResponse(Call<ServiceModel> call, final Response<ServiceModel> response) {
                if (response != null) {
                    if (response.body() != null) {
                        if (response.body().getData() != null) {
                            topServices = response.body().getData();
                            TopServicesAdapter adapterViewAndroid = new TopServicesAdapter(getActivity(), response.body().getData());
                            myGridView.setAdapter(adapterViewAndroid);
                        } else {
                        }
                    } else {
                    }
                } else {
                }
                /*
                if (response.body().getData().get(0).getQuestions().size() == 0) {
                    topServices = response.body().getData();
                    TopServicesAdapter adapterViewAndroid = new TopServicesAdapter(getActivity(), response.body().getData());
                    myGridView.setAdapter(adapterViewAndroid);

                } else {
                    if (response.body().getData().get(0).getQuestions().get(0).getQType().equals("radio")) {
                        Toast.makeText(getActivity(), "Question Radio Page", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getActivity(), ServiceQuestionActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getActivity(), "Question Checkbox Page", Toast.LENGTH_SHORT).show();
                    }
                }*/

            }

            @Override
            public void onFailure(Call<ServiceModel> call, Throwable t) {
            }
        });
    }

    public void postCustomerUpdateLocation() {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<SuccessModel> call = apiInterface.updateCustomerLocation(PreferencesManager.getPrefString(Constants.GUEST_ID), PreferencesManager.getPrefInt(Constants.CUSTOMER_ID), upadteAddress, postalCode);
        call.enqueue(new Callback<SuccessModel>() {

            @Override
            public void onResponse(Call<SuccessModel> call, Response<SuccessModel> response) {
                tvAddress.setText(PreferencesManager.getPrefString(Constants.CUSTOMER_LOCATION));
                Toast.makeText(getActivity(), "Location updated", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<SuccessModel> call, Throwable t) {
            }
        });
    }


}
