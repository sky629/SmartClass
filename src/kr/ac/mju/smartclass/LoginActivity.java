package kr.ac.mju.smartclass;

import java.net.URL;

import kr.ac.mju.util.ConnectTask;
import kr.ac.mju.util.HttpRequest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gcm.GCMRegistrar;

public class LoginActivity extends Activity{
	
	private TextView snoTv;
	private EditText pwdEdit;
	private Button btn;
	private String sno;
	private ProgressDialog dialog;
	private String regId = "";
	private Boolean isManager;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_activity);
		Intent intent = getIntent();
		
		snoTv = (TextView)findViewById(R.id.sno);
		btn = (Button)findViewById(R.id.loginBtn);
		sno = intent.getStringExtra("sno");
		pwdEdit = (EditText)findViewById(R.id.pwd);
		isManager = intent.getBooleanExtra("manager",false);
		if(isManager){
			snoTv.setText("관리자");
		}else{
			snoTv.setText(sno);
		}
		if(!isManager){	//관리자가 아닐경우 Registration ID값을 얻어서 서버로 전송
			/**Registration ID값 얻기
			 * 기기에서 GCM 푸시 서비스에 필요한
			 * Registration ID값을 얻는다
			 */
			GCMRegistrar.checkDevice(this);		//GCM단말기 확인
			GCMRegistrar.checkManifest(this);	//앱 권한 확인
			regId = GCMRegistrar.getRegistrationId(this);
			if ("".equals(regId)) {
				GCMRegistrar.register(this, "775333259345");
				regId = GCMRegistrar.getRegistrationId(this);
				System.out.println("regid : "+regId);
			} else {
				System.out.println("regid : "+regId);
			}
		}
		
		btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String pwd = pwdEdit.getText().toString();
				pwdEdit.setText("");
				String url = "http://54.201.33.197:3000/services/login";
				
				HttpRequest request;
				try {
					dialog = ProgressDialog.show(LoginActivity.this, "", "비밀번호 확인...",true);
					request = new HttpRequest(new URL(url),LoginActivity.this);
					request.addParameter("sno", sno);
					request.addParameter("pwd", pwd);
					request.addParameter("rid",regId);
					ConnectTask task = new ConnectTask(){
						protected void onPostExecute(String result) {
							if(result.equals("false")){				
								//로그인 실패
								dialog.dismiss();
								new AlertDialog.Builder(LoginActivity.this)
									.setTitle("비밀번호 확인")
									.setMessage("비밀번호가 틀렸습니다. 다시 확인해 주세요.")
									.setNeutralButton("확인", null)
									.show();
							}else{									
								//로그인 성공
								dialog.dismiss();
								if(!isManager){
									//일반 학생 로그인
									Toast.makeText(LoginActivity.this, "로그인 성공", Toast.LENGTH_SHORT).show();
									Intent intent = new Intent(LoginActivity.this,Tab.class);
									intent.putExtra("sno", sno);
									startActivity(intent);
									finish();
								}else{
									//관리자 로그인
									Toast.makeText(LoginActivity.this, "관리자 로그인", Toast.LENGTH_SHORT).show();
									Intent intent = new Intent(LoginActivity.this,ManagerTabActivity.class);
									intent.putExtra("ano",sno);
									startActivity(intent);
									finish();
								}
							}
						};
					};
					task.execute(request);
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
		});
	}
}
