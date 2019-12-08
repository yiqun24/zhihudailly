package com.example.zhihudailly;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
public class ArticleContentActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_content);
        setTitle(2);
        WebView webView = findViewById(R.id.webview);
        Intent intent = getIntent();
        String url = intent.getStringExtra("url");
        webView.loadUrl(url);
    }
}
