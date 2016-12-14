package kr.ac.mju.smartclass;

import java.net.URL;

import kr.ac.mju.util.ConnectTask;
import kr.ac.mju.util.HttpRequest;
import kr.ac.mju.util.Utility;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class ClassMessage extends Activity {
	TextView className;
	TextView message;
	TextView messageDate;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_class_message);
		this.setTitle("공지");
		Intent intent = getIntent();
		// 공지 메세지의 Id를 넘겨 받는다.
		int messageId = intent.getIntExtra("messageKey", 0);
		getMessage(messageId);
	}
	
	public void getMessage(int messageId){
		HttpRequest request;
		// 공지 메세지를 담고있는 URL 
		String url = "http://54.201.33.197:3000/services/messagecontent";
		
		className=(TextView)findViewById(R.id.messageClassName);
		messageDate=(TextView)findViewById(R.id.messageDate);
		message=(TextView)findViewById(R.id.message);
		try {
			request = new HttpRequest(new URL(url), ClassMessage.this);
			// 공지 메세지 ID를 조건으로 데이터를 받아옴
			request.addParameter("message_id", Integer.toString(messageId));
			ConnectTask task = new ConnectTask(){
				/**
				 * 공지 메세지출력을 위해 강좌이름, 공지 메세지 내용, 등록날짜를 저장한다.
				 */
				@Override
				protected void onPostExecute(String result) {
					// TODO Auto-generated method stub
					if(result.equals("false")){
						Toast.makeText(ClassMessage.this, "메시지 없음", Toast.LENGTH_SHORT).show();
					}else{
						Utility util = new Utility(result);
						int size = util.getRowSize();
						System.out.println(size);
						if(size==0){
							Toast.makeText(ClassMessage.this, "no data", Toast.LENGTH_SHORT).show();
						}else{
							for(int i=0; i<size;i++){
								String cName = util.getRowString(i, "cname");
								String message= util.getRowString(i, "message");
								String messageDate = util.getRowString(i, "send_date");
								// 화면에 출력
								className.setText(cName);
								ClassMessage.this.messageDate.setText(messageDate);
								ClassMessage.this.message.setText(message);
							}
						}
					}
				}
			};
			task.execute(request);
		}catch(Exception e){
			
		}
	}
}
