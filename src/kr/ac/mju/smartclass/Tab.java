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
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.TabActivity;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentFilter.MalformedMimeTypeException;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.nfc.tech.NfcF;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.TabHost;
import android.widget.Toast;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
public class Tab extends TabActivity {
	TabHost tabHost;
	private String sno;

	private static final String TAG = "SmartClass";

	private NfcAdapter mAdapter;
	private PendingIntent mPendingIntent;
	private IntentFilter[] mFilters;
	private String[][] mTechLists;

	ArrayList<String> aList;
	ArrayAdapter<String> adapter;
	private String all_cno = "";
	private String[] cno_array;
	private String[] Cno;
	private String CnoTemp = "";
	private String c;
	private String[] cTime;
	private String cTemp = "";
	private String[] cTemp2;
	private String cTemp3 = "";

	NdefMessage[] message;

	TodayClassBean todayClassBean = new TodayClassBean(); 
	ArrayList<TodayClassBean> todayClassList = new ArrayList<TodayClassBean>();
	ClassStatusCustomAdapter customAdapter;
	ProgressDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mAdapter = NfcAdapter.getDefaultAdapter(this);
		setContentView(R.layout.tab);
		this.setTitle("홈");
		Intent intent = getIntent();
		sno = intent.getStringExtra("sno"); 
		setTab();

		//포어그라운드디스패치를 위한 인텐트 설정
		mPendingIntent = PendingIntent.getActivity(this, 0,
				new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
		//싱글탑 플래그 설정
		// Setup an intent filter for all MIME based dispatches
		IntentFilter ndef = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
		try {
			ndef.addDataType("text/plain");
		} catch (MalformedMimeTypeException e) {
			throw new RuntimeException("fail", e);
		}
		mFilters = new IntentFilter[] {
				ndef
		};

		// Setup a tech list for all NfcF tags
		mTechLists = new String[][] { new String[] { NfcF.class.getName() } };


	}

	@Override
	public void onResume() {
		super.onResume();

		//오늘 날짜를 받아와 디비에서 정보 받아오기
		String today[] = setTodayDate();
		getTodayInfo(today[1]);

		//포어그라운드디스패치 설정
		if (mAdapter != null) mAdapter.enableForegroundDispatch(this, mPendingIntent, mFilters,
				mTechLists);
	}

	@Override
	public void onNewIntent(Intent intent) {
		//	    	super.onNewIntent(intent);

		Log.i("Foreground dispatch", "Discovered tag with intent: " + intent);

		String action = intent.getAction();
		if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)){

			message = getNdefMessages(intent);
			byte[] payload = message[0].getRecords()[0].getPayload();
			all_cno = new String(payload);		
			cno_array = all_cno.split(",");		//NFC 태그에서 받아온 강좌번호를 각각 저장

			Calendar cal = Calendar.getInstance();
			String hour = String.valueOf(cal.get(Calendar.HOUR_OF_DAY));

			//오늘 내 수업과 NFC 태그에서 받아온 수업중 시간에 맞는 수업 선택후 출석체크
			for(int i=0; i<Cno.length; i++)
				for(int j=0; j<cno_array.length; j++)
					if(Cno[i].equals(cno_array[j]))
						if(cTime[i].equals(hour)){
							c = Cno[i];
							attend();
							break;
						}

			// 변수 초기화
			cTemp = "";
			cTemp2 = null;
			cTemp3 = "";
			cTime = null;
			c = "";

			all_cno = "";
			cno_array = null;
			Cno = null;
			CnoTemp = "";



		}

	}

	@Override
	public void onPause() {
		super.onPause();
		if (mAdapter != null) mAdapter.disableForegroundDispatch(this);
	}

	NdefMessage[] getNdefMessages(Intent intent) {
		// Parse the intent
		NdefMessage[] msgs = null;
		String action = intent.getAction(); 
		if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action))	
		{
			Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);

			if (rawMsgs != null) {

				msgs = new NdefMessage[rawMsgs.length];

				for (int i = 0; i < rawMsgs.length; i++)
				{
					msgs[i] = (NdefMessage) rawMsgs[i];

				}
			}

		}
		return msgs;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		getMenuInflater().inflate(R.menu.action_bar, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch(item.getItemId()){
		case R.id.mjuLink:
			Intent intent = new Intent(Tab.this, MJUWebView.class);
			startActivity(intent);
			return true;
		case R.id.environmentSettings:
			/**
			 * =============================환경설정 부분==============================
			 */
			intent = new Intent(Tab.this, EnvironmentSetting.class);
			intent.putExtra("sno", sno);
			startActivity(intent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	// 텝 설정
	public void setTab(){
		tabHost = getTabHost();
		TabSpec spec =null;
		Intent intent =null;

		// 탭 홈
		spec = tabHost.newTabSpec("Home");
		intent = new Intent(this, HomeActivity.class);
		intent.putExtra("sno", sno);
		Drawable icon = getResources().getDrawable(R.drawable.home);
		spec.setContent(intent);
		spec.setIndicator("", icon);
		tabHost.addTab(spec);

		// 탭 출결현황
		spec = tabHost.newTabSpec("ClassCheck");
		intent = new Intent(this, ClassCheckActivity.class);
		intent.putExtra("sno", sno);
		icon = getResources().getDrawable(R.drawable.ic_action_accept);
		spec.setContent(intent);
		spec.setIndicator("", icon);
		tabHost.addTab(spec);

		// 탭 공지
		spec = tabHost.newTabSpec("message");
		intent = new Intent(this, MessageCheckActivity.class);
		intent.putExtra("sno", sno);
		icon = getResources().getDrawable(R.drawable.ic_action_chat);
		spec.setContent(intent);
		spec.setIndicator("", icon);
		tabHost.addTab(spec);

		// 탭클릭시 액션바 이름 변경
		tabHost.setOnTabChangedListener(new OnTabChangeListener() {
			@Override
			public void onTabChanged(String tabId) {
				// TODO Auto-generated method stub
				if(tabId.equals("Home")){
					getWindow().setTitle("홈");
				}else if(tabId.equals("ClassCheck")){
					getWindow().setTitle("출결 현황");
				}else if(tabId.equals("message")){
					getWindow().setTitle("공지");
				}
			}
		});
	}	

	//학번을 가지고 오늘 나의 강의정보를 받아온다
	public void getTodayInfo(String toDayOfWeek){
		HttpRequest request;
		String url = "http://54.201.33.197:3000/services/showclasstoday";

		try {
			request = new HttpRequest(new URL(url), Tab.this);
			request.addParameter("sno", sno);
			request.addParameter("dayofweek", toDayOfWeek);
			ConnectTask task = new ConnectTask(){

				@Override
				protected void onPostExecute(String result) {
					// TODO Auto-generated method stub
					todayClassList.clear();
					if(result.equals("false")){
						Toast.makeText(Tab.this, "수업정보 없음", Toast.LENGTH_SHORT).show();
					}else{
						Utility util = new Utility(result);
						int size = util.getRowSize();
						System.out.println(size);
						if(size==0){
							Toast.makeText(Tab.this, "no data", Toast.LENGTH_SHORT).show();
						}else{
							for(int i=0; i<size;i++){
								cTemp += util.getRowString(i, "ctime") + "/";
								CnoTemp += util.getRowString(i, "cno") +  "/";
							}
							Cno = CnoTemp.split("/");

							cTemp2 = cTemp.split("/");
							for(int i=0; i<cTemp2.length; i++){
								cTemp3 += cTemp2[i].substring(0,2)+ " ";
							}
							cTime = cTemp3.split(" ");
						}
					}

				}
			};
			task.execute(request);

		}catch(Exception e){
			e.printStackTrace();
		}
	}
	// 춣석 체크
	public void attend(){
		String url = "http://54.201.33.197:3000/services/atten";
		HttpRequest request;

		Calendar cal = Calendar.getInstance();


		String dateToString;
		dateToString = String.format("%04d-%02d-%02d", cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH));

		try{     
			//시간에 따라 출석, 지각, 결석 결정
			request = new HttpRequest(new URL(url),Tab.this);
			if(cal.get(Calendar.MINUTE) <= 5 && cal.get(Calendar.MINUTE) >= 0){
				request.addParameter("check_class", "1");
				toast("출석");
			}
			else if(cal.get(Calendar.MINUTE) <= 30 && cal.get(Calendar.MINUTE) >= 6){
				request.addParameter("check_class", "2");
				toast("지각");
			}
			else if(cal.get(Calendar.MINUTE) <= 59 && cal.get(Calendar.MINUTE) >= 31){
				request.addParameter("check_class", "3");
				toast("결석");
			}

			request.addParameter("class_date", dateToString);
			request.addParameter("sno", sno); //여기서 아이디를 Parameter로 넘김.
			request.addParameter("cno", c);
			ConnectTask task = new ConnectTask(){};
			task.execute(request);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	// 토스트
	private void toast(String text) {
		Log.i(TAG, "toast");
		Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
	}


	// 오늘 날짜 정보 가져오기
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
