package net.cuiwei.xiangle;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;

public class WebViewActivity extends BaseActivity {

    @Override
    protected TransitionMode getOverridePendingTransitionMode() {
        return TransitionMode.LEFT;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent=getIntent();
        String title=intent.getStringExtra("title");
        setTitle(title);
        String url=intent.getStringExtra("url");
        WebView webView=findViewById(R.id.webview);
        if (!url.equals("")){
            webView.loadUrl(url);
        }
    }

    @Override
    protected boolean hasBackIcon() {
        return true;
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_web_view);
    }

    @Override
    protected void initializeData(Bundle saveInstance) {

    }
}