package kr.ac.mju.smartclass;

import java.net.MalformedURLException;
import java.net.URL;

import kr.ac.mju.util.ConnectTask;
import kr.ac.mju.util.HttpRequest;
import kr.ac.mju.util.Utility;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class MessageContent extends Activity {
	TextView cname;
	TextView date;
	TextView content;
	HttpRequest request;
	String msgNo;
	ProgressDialog dialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.msgcontent_activity);
		cname = (TextView)findViewById(R.id.cnameTv);
		date = (TextView)findViewById(R.id.dateTv);
		content = (TextView)findViewById(R.id.msgContent);
		
		Intent intent = getIntent();
		msgNo = intent.getStringExtra("msgNumber");
		String url = "http://54.201.33.197:3000/services/messagecontent";
		try {
			dialog = ProgressDialog.show(MessageContent.this, "", "잠시만 기다려 주세요...",true);
			request = new HttpRequest(new URL(url),MessageContent.this);
			request.addParameter("message_id", msgNo);
			ConnectTask task = new ConnectTask(){
				// 강좌 번호 추가
				protected void onPostExecute(String result) {
					Utility util = new Utility(result);
					if(util.getRowString(0, "cno").length()==1){
						cname.setText("000"+util.getRowString(0, "cno") + "    " + util.getRowString(0, "cname"));
					}else if(util.getRowString(0, "cno").length()==2){
						cname.setText("00"+util.getRowString(0, "cno") + "    " + util.getRowString(0, "cname"));
					}else if(util.getRowString(0, "cno").length()==3){
						cname.setText("0"+util.getRowString(0, "cno") + "    " + util.getRowString(0, "cname"));
					}else{
						cname.setText(util.getRowString(0, "cno") + "    " + util.getRowString(0, "cname"));
					}
					date.setText(util.getRowString(0, "send_date"));
					content.setText(util.getRowString(0, "message"));
					dialog.dismiss();
				}
			};
			task.execute(request);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
}
