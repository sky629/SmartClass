package kr.ac.mju.smartclass;

import java.net.URL;
import java.util.ArrayList;

import kr.ac.mju.adapter.MessageCustomAdapter;
import kr.ac.mju.bean.MessageBean;
import kr.ac.mju.util.ConnectTask;
import kr.ac.mju.util.HttpRequest;
import kr.ac.mju.util.Utility;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MessageCheckActivity extends Activity {
	
	ListView messageCheckList;
	TextView notifyText;
	MessageBean messageBean = new MessageBean();
	ArrayList<MessageBean> messageList = new ArrayList<MessageBean>();
	MessageCustomAdapter customAdapter;
	ProgressDialog dialog;
	
	ArrayList<Integer> messageIdList = new ArrayList<Integer>();
	
	private String sno;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_message_check);
		// 학번을 넘겨받음
		Intent intent = getIntent();
		sno=intent.getStringExtra("sno");
		notifyText=(TextView)findViewById(R.id.notify);		
		// 메시지 목록 호출
		getMessageList();
		// 
		messageCheckList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MessageCheckActivity.this, ClassMessage.class);
				intent.putExtra("messageKey", messageIdList.get(position));
				startActivity(intent);
			}
		});
	}
	
	public void getMessageList(){
		messageCheckList = (ListView)findViewById(R.id.messageList);
		HttpRequest request;
		// 메시지 목록 서버 URL
		String url = "http://54.201.33.197:3000/services/messagelist";

		try {
			request = new HttpRequest(new URL(url), MessageCheckActivity.this);
			// 학번을 조건으로 함
			request.addParameter("sno", sno);
			ConnectTask task = new ConnectTask(){

				@Override
				protected void onPostExecute(String result) {
					// TODO Auto-generated method stub
					// 메세지 리스트 초기화 
					messageList.clear();
					if(result.equals("false")){
						dialog.dismiss();
						Toast.makeText(MessageCheckActivity.this, "메세지 없음", Toast.LENGTH_SHORT).show();
						if(customAdapter != null){
							customAdapter.notifyDataSetChanged();							
						}
					}else{
						Utility util = new Utility(result);
						int size = util.getRowSize();
						System.out.println(size);
						if(size==0){
							Toast.makeText(MessageCheckActivity.this, "no data", Toast.LENGTH_SHORT).show();
						}else{
							for(int i=0; i<size;i++){
								// 강좌이름, 메세지내용, 등록날짜, 메세지ID를 저장한다.
								String cName = util.getRowString(i, "cname");
								String message= util.getRowString(i, "message");
								if(message.length()>=12){
									message=message.substring(0,12);
								}
								String messageDate = util.getRowString(i, "send_date");
								int messageId = util.getRowInt(i, "message_id");
								messageIdList.add(messageId);
								messageList.add(new MessageBean(i, cName, message, messageDate));
							}
							// 출결 목록에 등록
							customAdapter=new MessageCustomAdapter(MessageCheckActivity.this, R.layout.messagelist_row, messageList);
							messageCheckList.setAdapter(customAdapter);
							// 프로그레스 다이어로그 해제
							dialog.dismiss();
						}
					}
				}
			};
			// 프로그레스 다이어로그 실행
			dialog=ProgressDialog.show(MessageCheckActivity.this, null, "Wait..",true);
			dialog.setCancelable(true);
			// 작업 실행
			task.execute(request);
		}catch(Exception e){
			
		}
	}
}
