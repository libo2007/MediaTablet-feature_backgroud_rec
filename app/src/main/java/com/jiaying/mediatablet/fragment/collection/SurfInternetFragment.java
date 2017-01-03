package com.jiaying.mediatablet.fragment.collection;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.jiaying.mediatablet.R;

/*
上网
 */
public class SurfInternetFragment extends Fragment {
    private WebView mWebView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_surf_internet, null);
        mWebView = (WebView) view.findViewById(R.id.webView);
        mWebView.setWebChromeClient(new WebChromeClient());
        mWebView.loadUrl("http://www.youku.com");
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });


        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                //get the newProgress and refresh progress bar
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
//               this is web title
            }
        });
        return view;
    }
}
