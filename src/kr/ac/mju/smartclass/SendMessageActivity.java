package kr.ac.mju.smartclass;

import java.net.URL;

import kr.ac.mju.util.ConnectTask;
import kr.ac.mju.util.HttpRequest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SendMessageActivity extends Activity{
	private TextView courseTv;
	private EditText msgEdit;
	private Button sendBtn;
	private String msg;
	private String cno;
	HttpRequest request;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sendmsg_activity);
		
		courseTv = (TextView)findViewById(R.id.cnoTv);
		msgEdit = (EditText)findViewById(R.id.msgEdit);
		sendBtn = (Button)findViewById(R.id.sendBtn);
		
		Intent intent = getIntent();
		courseTv.setText(intent.getStringExtra("course"));
		cno = intent.getStringExtra("course").split(" ")[0];
		sendBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				msg = msgEdit.getText().toString();
				String url = "http://54.201.33.197:3000/services/regmsg";
				try {
					request = new HttpRequest(new URL(url), SendMessageActivity.this);
					request.addParameter("cno", cno);
					request.addParameter("msg", msg);
					ConnectTask task = new ConnectTask(){
						protected void onPostExecute(String result) {
							Toast.makeText(SendMessageActivity.this, "메시지 등록이 완료되었습니다.", Toast.LENGTH_SHORT).show();
							finish();
						};
					};
					task.execute(request);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
