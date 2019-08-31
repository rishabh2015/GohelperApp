package in.gohelper.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.google.gson.Gson;
import com.viewpagerindicator.UnderlinePageIndicator;

import java.util.ArrayList;
import java.util.List;

import in.gohelper.R;
import in.gohelper.adapter.PagerAdapter;
import in.gohelper.fragment.ServiceQuestionFragment;
import in.gohelper.models.servicemodels.Option;
import in.gohelper.models.servicemodels.Question;
import in.gohelper.models.servicemodels.ServiceData;
import in.gohelper.utils.Constants;

public class ServiceQuestionActivity extends BaseActivity {
    Toolbar toolbar;
    Button btnNext, btnP;
    ViewPager viewPager;
    ServiceData serviceData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.service_question_layout);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        if (intent != null) {
            String serviceJson = intent.getStringExtra("in.gohelper.intent.serviceObject");
            if (serviceJson != null) {
                serviceData = new Gson().fromJson(serviceJson, ServiceData.class);
            }
        }

        String menuTitle = serviceData.getMenuTitle();
        if (TextUtils.isEmpty(menuTitle)) {
            menuTitle = serviceData.getLabel();
        }
        getSupportActionBar().setTitle(menuTitle);

        btnNext = (Button) findViewById(R.id.btn);
        btnP = (Button) findViewById(R.id.btnP);

        //To add custom font
        if (!Constants.USE_CUSTOM_FONT) {
            String fontPath = Constants.CUSTOM_NAME_SIMPLE;
            Typeface tf = Typeface.createFromAsset(ServiceQuestionActivity.this.getAssets(), fontPath);
            btnNext.setTypeface(tf);
            btnP.setTypeface(tf);
        }

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        final PagerAdapter adapterViewPager = new PagerAdapter(getSupportFragmentManager(), serviceData.getQuestions());
        viewPager.setOffscreenPageLimit(10);
        viewPager.setAdapter(adapterViewPager);
        UnderlinePageIndicator mIndicator = (UnderlinePageIndicator) findViewById(R.id.indicator);
        mIndicator.setFades(false);
        mIndicator.setViewPager(viewPager);
        viewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewPager.getCurrentItem() < viewPager.getChildCount() - 1) {
                    viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                    if (viewPager.getCurrentItem() > 0) {
                        btnP.setVisibility(View.VISIBLE);
                    }
                } else {
                    //Questions and Answers stored in List
                    PagerAdapter adapter = (PagerAdapter) viewPager.getAdapter();
                    List<Question> questions = new ArrayList<>();
                    for (int index = 0; index < adapter.getCount(); index++) {
                        ServiceQuestionFragment fragment = (ServiceQuestionFragment) adapter.getItem(index);

                        List<Option> options = fragment.getSelectedOptions();
                        Question question = fragment.getPageQuestion();
                        question.setSelectedOptions(options);
                        questions.add(question);
                    }

                    serviceData.setQuestions(questions);
                    String jsonObject = new Gson().toJson(serviceData);
                    Intent intent = new Intent(getApplicationContext(), ReviewActivity.class);
                    intent.putExtra("in.gohelper.intent.serviceObject", jsonObject);
                    startActivity(intent);
                }
            }
        });

        btnP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
                if (viewPager.getCurrentItem() < 1) {
                    btnP.setVisibility(View.GONE);
                }
            }
        });
    }
}