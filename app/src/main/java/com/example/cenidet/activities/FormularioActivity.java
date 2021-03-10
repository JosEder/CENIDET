package com.example.cenidet.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.cenidet.*;

public class FormularioActivity extends AppCompatActivity {

    private WebView myWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario);

        myWebView = (WebView) findViewById(R.id.FormWebView);
        myWebView.getSettings().setJavaScriptEnabled(true);
        myWebView.setWebViewClient(new WebViewClient());
        myWebView.loadUrl("https://docs.google.com/forms/d/e/1FAIpQLSd7D27uzSuNhD1aqSUijfztDR0cQsl-aT1LIsW0QmPlpT0yXQ/viewform");
    }
}