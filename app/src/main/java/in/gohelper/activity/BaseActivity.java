package in.gohelper.activity;

import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.List;

import in.gohelper.R;

public class BaseActivity extends AppCompatActivity {
    private ProgressDialog dialog;
    protected Toolbar toolbar;
    protected FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Bundle bundle = new Bundle();
        bundle.putString("pageName", this.getClass().getSimpleName());
        mFirebaseAnalytics.logEvent("PageView", bundle);
    }

    public void showProgress() {
        if (dialog == null) {
            dialog = new ProgressDialog(this, R.style.TransparentProgressDialog);
            WindowManager.LayoutParams wlmp = getWindow().getAttributes();
            wlmp.gravity = Gravity.CENTER_HORIZONTAL;
            dialog.getWindow().setAttributes(wlmp);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.setTitle(null);
            dialog.setCancelable(false);
            dialog.setOnCancelListener(null);
        }
        dialog.show();
    }

    public void dismissProgress() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

   /* public boolean isOnline(Context context) {
        ConnectivityManager conMgr = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

        if(netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable()){
            Toast.makeText(context, "No Internet connection!", Toast.LENGTH_LONG).show();
            finish();
            return false;
        }
        return true;
    }*/

    public void setToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

    }


    public int runningTaskCount() {
        ActivityManager am = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        return tasks.size();
    }

    @Override
    public void onBackPressed() {

       if(this.getClass().getSimpleName().equalsIgnoreCase(MainActivity.class.getSimpleName())
               || this.getClass().getSimpleName().equalsIgnoreCase(MobileRegistrationActivity.class.getSimpleName())
               || this.getClass().getSimpleName().equalsIgnoreCase(MobieVerificationActivity.class.getSimpleName())) {
           new AlertDialog.Builder(this)
                   .setTitle("Really Exit?")
                   .setMessage("Are you sure you want to exit?")
                   .setNegativeButton(android.R.string.no, null)
                   .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                       public void onClick(DialogInterface arg0, int arg1) {
                            BaseActivity.super.onBackPressed();
                       }
                   }).create().show();
       }
        else {
           super.onBackPressed();
       }
    }
}
