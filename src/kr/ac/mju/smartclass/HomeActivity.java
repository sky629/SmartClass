package kr.ac.mju.smartclass;

import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import kr.ac.mju.adapter.ClassStatusCustomAdapter;
import kr.ac.mju.bean.TodayClassBean;
import kr.ac.mju.util.ConnectTask;
import kr.ac.mju.util.HttpRequest;
import kr.ac.mju.util.Utility;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentFilter.MalformedMimeTypeException;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.nfc.tech.NfcF;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * @author 3조
 *	홈 액티비티
 *	
 */
public class HomeActivity extends Activity {
	
	ListView todayTimetableList;
	TodayClassBean todayClassBean = new TodayClassBean(); 
	ArrayList<TodayClassBean> todayClassList = new ArrayList<TodayClassBean>();
	ClassStatusCustomAdapter customAdapter;
	ProgressDialog dialog;
	
	private String sno;
    
	ArrayList<String> aList;
	ArrayAdapter<String> adapter;
	String[] toDayDate;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = new Intent(HomeActivity.this, HomeActivity.class);
		setContentView(R.layout.activity_home);
		// 로그인 후 학번을 인텐트로 넘겨받음
		intent = getIntent();
		sno = intent.getStringExtra("sno");
		if(sno == null){
			intent = new Intent(this, MainActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
			finish();
			startActivity(intent);
		}
		TextView dateText = (TextView)findViewById(R.id.dateText);
		toDayDate = setTodayDate();
		dateText.setText(toDayDate[0]);
		getTodayClassList(toDayDate[1]);
		
		
	}
	/**
	 * 
	 * @param toDayOfWeek
	 * 오늘의 수업 리스트를 출력
	 */
	public void getTodayClassList(String toDayOfWeek){
		todayTimetableList = (ListView)findViewById(R.id.classList);
		HttpRequest request;
		// 수업정보를 가지고있는 Json URL
		String url = "http://54.201.33.197:3000/services/showclasstoday";

		try {
			request = new HttpRequest(new URL(url), HomeActivity.this);
			// 학번과 요일을 조건으로 쿼리를 날리게함
			request.addParameter("sno", sno);
			request.addParameter("dayofweek", toDayOfWeek);
			ConnectTask task = new ConnectTask(){

				@Override
				protected void onPostExecute(String result) {
					// TODO Auto-generated method stub
					todayClassList.clear();
					if(result.equals("false")){
						Toast.makeText(HomeActivity.this, "수업정보 없음", Toast.LENGTH_SHORT).show();
						dialog.dismiss();
					}else{
						Utility util = new Utility(result);
						int size = util.getRowSize();
						System.out.println(size);
						if(size==0){
							Toast.makeText(HomeActivity.this, "no data", Toast.LENGTH_SHORT).show();
						}else{
							for(int i=0; i<size;i++){
								// 강좌이름, 강좌 시간, 교수이름, 강좌번호를 저장한다.
								String className = util.getRowString(i, "cname");
								String classTime = util.getRowString(i, "ctime");
								String pName = util.getRowString(i, "pname");
								String cno = util.getRowString(i, "cno");
								todayClassList.add(new TodayClassBean(i, className, classTime, pName, cno));
							}
							customAdapter=new ClassStatusCustomAdapter(HomeActivity.this, R.layout.today_class_status_row, todayClassList);
							todayTimetableList.setAdapter(customAdapter);
							// 프로그레스 다이어로그 해제
							dialog.dismiss();
						}
					}
				}
			};
			// 프로그레스 다이어로그 실행
			dialog=ProgressDialog.show(HomeActivity.this, null, "Wait..", true);
			dialog.setCancelable(false);
			// 작업 실행
			task.execute(request);
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * 
	 * @return
	 * 오늘 날짜와 요일 계산 함수
	 */
	public String[] setTodayDate(){
		Calendar calendar;
		String[] TodayDate= new String[2];
		Date curMillis;
		int curYear, curMonth, curDay;
		String dayOfWeek="";
		
		calendar = Calendar.getInstance();
		curMillis=calendar.getTime();
		curYear=calendar.get(Calendar.YEAR);
		curMonth=calendar.get(Calendar.MONTH)+1;
		curDay=calendar.get(Calendar.DAY_OF_MONTH);
		switch (calendar.get(Calendar.DAY_OF_WEEK)) {
		
		case 1: dayOfWeek="SUN"; break;
		case 2: dayOfWeek="MON"; break;
		case 3: dayOfWeek="TUE"; break;
		case 4: dayOfWeek="WED"; break;
		case 5: dayOfWeek="THU"; break;
		case 6: dayOfWeek="FRI"; break;
		case 7: dayOfWeek="SAT"; break;
		default: break;
		}
		TodayDate[0] = curYear+"년 "+curMonth+"월 "+curDay+"일 "+dayOfWeek;
		TodayDate[1] = dayOfWeek;
		return TodayDate;
	}
	
}
