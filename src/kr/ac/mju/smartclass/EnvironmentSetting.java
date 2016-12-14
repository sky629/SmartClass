package kr.ac.mju.smartclass;

import kr.ac.mju.util.Constants;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class EnvironmentSetting extends Activity {

	CheckBox autoLogin;
	CheckBox vibrateSetting;
	String sno;
	
	SQLiteDatabase mDb;
	Cursor c;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_environment_setting);
		this.setTitle("환경 설정");
		Intent intent = getIntent();
		sno = intent.getStringExtra("sno");
		autoLogin = (CheckBox)findViewById(R.id.autoLogin);	//자동 로그인을 위한 CheckBox
		vibrateSetting = (CheckBox)findViewById(R.id.vibrateSet);
		
		mDb = openOrCreateDatabase("smartclass.db", MODE_PRIVATE, null);
		try{
			mDb.execSQL("drop table member;");
			autoLogin.setChecked(true);
			mDb.execSQL("create table member(a integer);");
			mDb.execSQL("insert into member values("+sno+");");
			System.out.println("check : table exist");
		}catch(Exception e){
			autoLogin.setChecked(false);
			System.out.println("check : table not exist");
		}
		autoLogin.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked){		//체크하면 테이블을 만들고 학번 입력
					mDb.execSQL("create table member(a integer);");
					mDb.execSQL("insert into member values("+sno+");");
					System.out.println("create table");
				}else if(!isChecked){//체크 해제 하면 테이블 삭제
					mDb.execSQL("drop table member;");
					System.out.println("drop table");
				}
			}
		});
		
		vibrateSetting.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked){
					Constants.vibratable = false;
				}else if(!isChecked){
					Constants.vibratable = true;
				}
			}
		});
		
	}
}
