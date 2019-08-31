package in.gohelper.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;

import in.gohelper.R;
import in.gohelper.utils.Constants;

public class ThankYouPageActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.thank_you_layout);

        AppCompatTextView tvContinueShopping = (AppCompatTextView) findViewById(R.id.tv_continue_shoping);

        //To add custom font
        if (!Constants.USE_CUSTOM_FONT) {
            String fontPath = Constants.CUSTOM_FONT_BOLD;
            Typeface tf = Typeface.createFromAsset(getApplicationContext().getAssets(), fontPath);
            tvContinueShopping.setTypeface(tf);
        }

        tvContinueShopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ThankYouPageActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }
}
