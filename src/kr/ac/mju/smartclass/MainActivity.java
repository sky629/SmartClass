package kr.ac.mju.smartclass;

import java.net.URL;

import kr.ac.mju.util.ConnectTask;
import kr.ac.mju.util.HttpRequest;
import kr.ac.mju.util.Utility;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {

	EditText stdNo;
	Button btn;
	ProgressDialog dialog;
	SQLiteDatabase mDb;
	Cursor c;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		stdNo = (EditText)findViewById(R.id.inputStdNo);
		btn = (Button)findViewById(R.id.btn);

		//자동 로그인 검사
		mDb = openOrCreateDatabase("smartclass.db", MODE_PRIVATE, null);
		try{
			c = mDb.rawQuery("select * from member;", null);	//테이블이 있으면
			c.moveToFirst();									//자동 로그인
			String s = c.getString(0);
			Intent intent = new Intent(MainActivity.this,Tab.class);
			intent.putExtra("sno", s);
			System.out.println("Auto Login...");
			startActivity(intent);
			c.close();
		}catch(Exception e){
			System.out.println("Not Auto Login...");			//없으면 예외로 잡고 진행
		}
		
		btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(stdNo.length()!=8){
					new AlertDialog.Builder(MainActivity.this)
					.setTitle("학번을 확인하세요.")
					.setMessage("학번은 8자리 입니다.")
					.setNeutralButton("확인", null)
					.show();
				}else{
					dialog = ProgressDialog.show(MainActivity.this,"","인증 중입니다.",true);
					String sno = stdNo.getText().toString();
					String url = "http://54.201.33.197:3000/services/checksno";
					HttpRequest request;

					try {
						request = new HttpRequest(new URL(url),MainActivity.this);
						request.addParameter("sno", sno);
						ConnectTask task = new ConnectTask(){
							protected void onPostExecute(String result){
								if(result == null){
									dialog.dismiss();
									Toast.makeText(MainActivity.this, "서버 점검중...", Toast.LENGTH_LONG).show();
									new AlertDialog.Builder(MainActivity.this)
										.setTitle("서버 점검중")
										.setMessage("서버에 문제가 있습니다.\n잠시만 기다려주세요.")
										.setNeutralButton("확인", null)
										.show();
								}else{
									if(result.equals("false")){
										dialog.dismiss();
										new AlertDialog.Builder(MainActivity.this)
										.setTitle("학번을 확인하세요.")
										.setMessage("등록된 학번이 아닙니다.")
										.setNeutralButton("확인", null)
										.show();
									}else{
										Utility util = new Utility(result);
										if((util.getRowString(0, "pwd")).equals("null")){
											dialog.dismiss();
											AlertDialog.Builder ab = new AlertDialog.Builder(MainActivity.this);
											ab.setMessage("등록 후 계속할 수 있습니다.").setCancelable(false).setPositiveButton("등록", 
													new DialogInterface.OnClickListener() {
												public void onClick(DialogInterface dialog, int which) {
													Intent intent = new Intent(MainActivity.this,JoinActivity.class);
													intent.putExtra("sno", stdNo.getText().toString());
													startActivity(intent);
												}
											}).setNegativeButton("취소", new DialogInterface.OnClickListener() {
												public void onClick(DialogInterface dialog, int which) {
													dialog.cancel();
												}
											});
											AlertDialog alert = ab.create();
											alert.setTitle("학번 등록이 필요합니다.");
											alert.show();
										}else{
											dialog.dismiss();
											Intent intent = new Intent(MainActivity.this,LoginActivity.class);
											intent.putExtra("sno", stdNo.getText().toString());
											if(util.getRowString(0, "ismanager").equals("true")){
												intent.putExtra("manager", true);
											}else{
												intent.putExtra("manager", false);
											}
											startActivity(intent);
										}
									}
								}
							}
						};
						task.execute(request);
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
				}
			}
		});
	}
}
