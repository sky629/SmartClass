package kr.ac.mju.smartclass;

import java.net.URL;

import kr.ac.mju.util.ConnectTask;
import kr.ac.mju.util.HttpRequest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class JoinActivity extends Activity{
	
	private EditText stdNo;
	private EditText phoneNo;
	private Button btn;
	private EditText pwd;
	private EditText pwdConfirm;
	ProgressDialog dialog;
	
	protected void onCreate(android.os.Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.join_activity);
		
		Intent intent = getIntent();
		
		stdNo = (EditText)findViewById(R.id.stdno);
		stdNo.setText(intent.getStringExtra("sno"));
		phoneNo = (EditText)findViewById(R.id.phone_no);
		TelephonyManager mgr = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
		phoneNo.setText(mgr.getLine1Number());
		pwd = (EditText)findViewById(R.id.pwd);
		pwdConfirm = (EditText)findViewById(R.id.pwd_confirm);
		btn = (Button)findViewById(R.id.resBtn);
		btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(pwd.getText().length() < 6){
					new AlertDialog.Builder(JoinActivity.this)
						.setTitle("비밀번호 확인")
						.setMessage("비밀번호는 6자리 이상 20자리 미만입니다.")
						.setNeutralButton("확인",null)
						.show();
				}else if(!pwd.getText().toString().equals(pwdConfirm.getText().toString())){
					new AlertDialog.Builder(JoinActivity.this)
						.setTitle("비밀번호 확인")
						.setMessage("두 비밀번호가 다릅니다. 다시 확인해 주세요.")
						.setNeutralButton("확인", null)
						.show();
				}else{
					dialog = ProgressDialog.show(JoinActivity.this, "", "가입 요청 중입니다.",true);
					HttpRequest request;
					String url = "http://54.201.33.197:3000/services/join";
					try {
						request = new HttpRequest(new URL(url),JoinActivity.this);
						request.addParameter("phone", phoneNo.getText().toString());
						request.addParameter("pwd", pwd.getText().toString());
						request.addParameter("sno", stdNo.getText().toString());
						
						ConnectTask task = new ConnectTask(){
							protected void onPostExecute(String result) {
								dialog.dismiss();
								Toast.makeText(JoinActivity.this, "등록이 완료 되었습니다.", Toast.LENGTH_SHORT).show();
								finish();
							};
						};
						task.execute(request);
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
				}
			}
		});
	};
}
