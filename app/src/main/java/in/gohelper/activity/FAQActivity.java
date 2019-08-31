package in.gohelper.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.google.gson.Gson;

import in.gohelper.R;
import in.gohelper.models.staticpagesmodels.StaticPageData;

public class FAQActivity extends BaseActivity {

    ProgressBar progressBar;
    WebView webview;
    private StaticPageData staticPageData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.faq_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //get static page id from help fragment
        Intent intent = getIntent();
        if (intent != null) {
            String serviceJson = intent.getStringExtra("in.gohelper.intent.contactObject");
            if (serviceJson != null) {
                staticPageData = new Gson().fromJson(serviceJson, StaticPageData.class);
            }
        }
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setMax(100);
        webview = (WebView) findViewById(R.id.webView);
        webview.setWebViewClient(new WebViewClientDemo());
        webview.setWebChromeClient(new WebChrome());
        webview.getSettings().setJavaScriptEnabled(true);
        webview.loadUrl(staticPageData.getUrl());
    }

    private class WebViewClientDemo extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            progressBar.setVisibility(View.GONE);
            progressBar.setProgress(100);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(0);
        }
    }

    private class WebChrome extends WebChromeClient {
        public void onProgressChanged(WebView view, int progress) {
            progressBar.setProgress(progress);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webview.canGoBack()) {
            webview.goBack();
            return true;
        } else {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
