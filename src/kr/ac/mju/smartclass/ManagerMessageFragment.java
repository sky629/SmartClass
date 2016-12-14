package kr.ac.mju.smartclass;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import kr.ac.mju.util.ConnectTask;
import kr.ac.mju.util.HttpRequest;
import kr.ac.mju.util.Utility;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class ManagerMessageFragment extends Fragment{
	
	private Button sendMsgBtn;
	private ListView msgList;
	private View mFragmentView;
	private HttpRequest request;
	private String ano;
	private ArrayList<String> msgArray;
	private ArrayAdapter<String> msgAdapter;
	private ArrayList<Integer> msgNumber;
	private ArrayList<String> classList;
	private ProgressDialog dialog;
	String[] list;
	String selectedCourse;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mFragmentView = inflater.inflate(R.layout.manager_message, container, false);
		sendMsgBtn = (Button)mFragmentView.findViewById(R.id.sendMsgBtn);
		msgList = (ListView)mFragmentView.findViewById(R.id.msgList);
		
		return mFragmentView;
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		msgArray = new ArrayList<String>();
		msgNumber = new ArrayList<Integer>();
		Intent intent = this.getActivity().getIntent();
		ano = intent.getStringExtra("ano");
		
		sendMsgBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				
				classList.clear();
				String url = "http://54.201.33.197:3000/services/assistclass";
				try {
					request = new HttpRequest(new URL(url),ManagerMessageFragment.this.getActivity());
					request.addParameter("ano", ano);
					ConnectTask task = new ConnectTask(){
						@Override
						protected void onPostExecute(String result) {
							// TODO Auto-generated method stub
							Utility util = new Utility(result);
							int size = util.getRowSize();
							for(int i=0; i<size; i++){
								if(util.getRowString(i, "cno").length() == 1){
									classList.add("000"+util.getRowString(i,"cno")+" "+util.getRowString(i, "cname"));
								}else if(util.getRowString(i, "cno").length() == 2){
									classList.add("00"+util.getRowString(i,"cno")+" "+util.getRowString(i, "cname"));
								}else if(util.getRowString(i, "cno").length() == 3){
									classList.add("0"+util.getRowString(i,"cno")+" "+util.getRowString(i, "cname"));
								}else{
									classList.add(util.getRowString(i,"cno")+" "+util.getRowString(i, "cname"));
								}
							}
							list = classList.toArray(new String[classList.size()]);
							AlertDialog.Builder ab = new AlertDialog.Builder(ManagerMessageFragment.this.getActivity());
							ab.setTitle("메시지를 보낼 강좌를 선택하세요");
							ab.setSingleChoiceItems(list, 0,
									new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int whichButton) {
									// 각 리스트를 선택했을때 
									String course=classList.get(whichButton);
									Toast.makeText(ManagerMessageFragment.this.getActivity(), course, Toast.LENGTH_SHORT).show();
									selectedCourse = course;
								}
							}).setPositiveButton("선택",
									new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int whichButton) {
									
									Intent intent1 = new Intent(ManagerMessageFragment.this.getActivity(),SendMessageActivity.class);
									intent1.putExtra("course", selectedCourse);
									startActivity(intent1);
								}
							}).setNegativeButton("취소",
									new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int whichButton) {}
							});
							selectedCourse = list[0];
							ab.show();
						}
					};
					task.execute(request);
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
		
	}
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		String url = "http://54.201.33.197:3000/services/message";
		try{
			dialog = ProgressDialog.show(ManagerMessageFragment.this.getActivity(), "", "메시지를 불러오는 중...",true);
			request = new HttpRequest(new URL(url),ManagerMessageFragment.this.getActivity());
			request.addParameter("ano", ano);
			ConnectTask task = new ConnectTask(){
				@Override
				protected void onPostExecute(String result) {
					msgArray.clear();
					classList = new ArrayList<String>();
					if(result.equals("false")){
						msgArray.add("보낸 메시지가 없습니다.");
						msgAdapter = new ArrayAdapter<String>(ManagerMessageFragment.this.getActivity(), 
								android.R.layout.simple_list_item_1, 
								msgArray);
						msgList.setAdapter(msgAdapter);
					}else{
						Utility util = new Utility(result);
						int size = util.getRowSize();
						for(int i=0; i<size; i++){
							String cno;
							if(util.getRowString(i, "cno").length() == 1){
								cno = "000"+util.getRowString(i, "cno");
							}else if(util.getRowString(i, "cno").length() == 2){
								cno = "00"+util.getRowString(i, "cno");
							}else if(util.getRowString(i, "cno").length() == 3){
								cno = "0"+util.getRowString(i, "cno");
							}else{
								cno = util.getRowString(i, "cno");
							}
							
							if(util.getRowString(i,"message").length() < 18){
								String list = cno +" "+ util.getRowString(i, "cname")+"    "
										+util.getRowString(i, "message")+"\n"
										+util.getRowString(i, "send_date");
								msgArray.add(list);
								msgNumber.add(util.getRowInt(i, "message_id"));
							}else{
								String list = cno +" "+ util.getRowString(i, "cname")+"    "
										+util.getRowString(i, "message").substring(0, 18) +"...\n"
										+util.getRowString(i, "send_date");
								msgArray.add(list);
								msgNumber.add(util.getRowInt(i, "message_id"));
							}
						}
					}
					msgAdapter = new ArrayAdapter<String>(ManagerMessageFragment.this.getActivity(),
							android.R.layout.simple_list_item_1,
							msgArray);
					msgList.setAdapter(msgAdapter);
					msgList.setOnItemClickListener(new OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> parent, View view,
								int position, long arg3) {
							String msgNo = (msgNumber.get(position)).toString();
							Intent intent = new Intent(ManagerMessageFragment.this.getActivity(),MessageContent.class);
							intent.putExtra("msgNumber", msgNo);
							startActivity(intent);
						}
					});
					msgAdapter.notifyDataSetChanged();
					dialog.dismiss();
				}
			};
			task.execute(request);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
}
