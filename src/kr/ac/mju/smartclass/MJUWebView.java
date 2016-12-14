package kr.ac.mju.smartclass;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.Toast;

public class MJUWebView extends Activity {
	WebView mWebView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mjuweb);
	
		mWebView=(WebView)findViewById(R.id.webview);
		
		mWebView.getSettings().setJavaScriptEnabled(true);
		// 학교 URL load
		mWebView.loadUrl("http://m.mju.ac.kr");
		// 웹뷰 set
		mWebView.setWebViewClient(new WebViewClientClass());
	}
	// 웹뷰 뒤로가기 버튼 클릭시 호출
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		// 웹뷰에서 뒤로가기
		if((keyCode == KeyEvent.KEYCODE_BACK)&&mWebView.canGoBack()){
			mWebView.goBack();
		} 
		// 웹뷰 종료
		if((keyCode == KeyEvent.KEYCODE_BACK)&&!mWebView.canGoBack()){
			mWebView.goBack();
		}
		else if(mWebView.getUrl()==null ||mWebView.getUrl().equals("http://m.mju.ac.kr/mbs/mjumob/index_day.jsp")){
			finish();
		}else if(mWebView.getUrl()==null ||mWebView.getUrl().equals("http://m.mju.ac.kr/mbs/mjumob/index_sunset.jsp")){
			finish();
		}else if(mWebView.getUrl()==null ||mWebView.getUrl().equals("http://m.mju.ac.kr/mbs/mjumob/index_night.jsp")){
			finish();
		}
		return true;
	}
	// 웹뷰 실행
	private class WebViewClientClass extends WebViewClient{

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			// TODO Auto-generated method stub
			view.loadUrl(url);
			return true;
		}
		
	}
	
}
