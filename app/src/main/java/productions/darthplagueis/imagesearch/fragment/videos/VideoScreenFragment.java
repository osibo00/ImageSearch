package productions.darthplagueis.imagesearch.fragment.videos;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import productions.darthplagueis.imagesearch.R;
import productions.darthplagueis.imagesearch.util.Constants;
import productions.darthplagueis.imagesearch.util.VideoEnabledWebChromeClient;
import productions.darthplagueis.imagesearch.util.VideoEnabledWebView;

public class VideoScreenFragment extends Fragment {

    private String videoURL;

    public VideoScreenFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_video_screen, container, false);

        setArguments();
        setWebView(rootView);

        return rootView;
    }

    private void setArguments() {
        Bundle args = getArguments();
        if (args != null) {
            videoURL = args.getString(Constants.VIDEO_LINK);
        }
    }

    private void setWebView(View rootView) {
        VideoEnabledWebView webView = rootView.findViewById(R.id.webView);
        View nonVideoLayout = rootView.findViewById(R.id.nonVideoLayout);
        ViewGroup videoLayout = rootView.findViewById(R.id.videoLayout);

        View loadingView = getLayoutInflater().inflate(R.layout.view_loading_video, null);
        VideoEnabledWebChromeClient webChromeClient = new VideoEnabledWebChromeClient(nonVideoLayout, videoLayout, loadingView, webView) {
            @Override
            public void onProgressChanged(WebView view, int progress) {
            }
        };
        webChromeClient.setOnToggledFullscreen(new VideoEnabledWebChromeClient.ToggledFullscreenCallback() {
            @Override
            public void toggledFullscreen(boolean fullscreen) {
                if (fullscreen) {
                    WindowManager.LayoutParams attrs = getActivity().getWindow().getAttributes();
                    attrs.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
                    attrs.flags |= WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
                    getActivity().getWindow().setAttributes(attrs);
                    getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
                } else {
                    WindowManager.LayoutParams attrs = getActivity().getWindow().getAttributes();
                    attrs.flags &= ~WindowManager.LayoutParams.FLAG_FULLSCREEN;
                    attrs.flags &= ~WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
                    getActivity().getWindow().setAttributes(attrs);
                    getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                }
            }
        });
        webView.setWebChromeClient(webChromeClient);
        webView.setWebViewClient(new InsideWebViewClient());
        webView.loadUrl(videoURL);
    }

    /*
    Force links to be opened inside WebView and not in Default Browser
     */
    private class InsideWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}
