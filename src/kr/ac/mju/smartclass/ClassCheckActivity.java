package kr.ac.mju.smartclass;

/**
 * 2013-11-25   progress dialog 스피너 셀렉 후 띄우기 getCheckList 끝나고 지우기
 */

import java.net.URL;
import java.util.ArrayList;

import kr.ac.mju.adapter.ClassCheckCustomAdapter;
import kr.ac.mju.bean.ClassCheckBean;
import kr.ac.mju.util.ConnectTask;
import kr.ac.mju.util.HttpRequest;
import kr.ac.mju.util.Utility;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
/**
 * 
 * @author 3조
 *	두번째 탭의 출결현황을 보는 액티비티
 */
public class ClassCheckActivity extends Activity {
	
	ListView classCheckList; //출결현황 리스트
	ClassCheckBean classCheckBean = new ClassCheckBean(); // 출결현황 정보
	ArrayList<ClassCheckBean> checkList = new ArrayList<ClassCheckBean>(); // 출결현황의 목록을 답기위한 어레이리스트 
	ClassCheckCustomAdapter customAdapter; // 출결현황의 커스텀 어댑터
	ProgressDialog dialog; //프로그레스 다이어로그
	
	ArrayAdapter<String> CNameAdapter; 
	ArrayList<String> classList; // 스피너의 수업목록
	private String sno; // 학번
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_class_check);
		// 로그인 이후 학번을 넘겨받는다.
		Intent intent = getIntent();
		sno = intent.getStringExtra("sno");
		// 스피너 set
		setSpinner();
	}

	public void getCheckList(String selectedClass){
		classCheckList = (ListView)findViewById(R.id.checkList);
		HttpRequest request;
		String url = "http://54.201.33.197:3000/services/showstat";

		try {
			request = new HttpRequest(new URL(url), ClassCheckActivity.this);
			request.addParameter("sno", sno);
			request.addParameter("cname", selectedClass);
			ConnectTask task = new ConnectTask(){

				@Override
				protected void onPostExecute(String result) {
					// TODO Auto-generated method stub
					checkList.clear();
					if(result.equals("false")){
						dialog.dismiss();
						Toast.makeText(ClassCheckActivity.this, "출결사항 없음", Toast.LENGTH_SHORT).show();
						if(customAdapter != null){
							customAdapter.notifyDataSetChanged();
						}
					}else{
						Utility util = new Utility(result);
						int size = util.getRowSize();
						System.out.println(size);
						if(size==0){
							Toast.makeText(ClassCheckActivity.this, "no data", Toast.LENGTH_SHORT).show();
						}else{
							for(int i=0; i<size;i++){
								String classDate = util.getRowString(i, "class_date");
								String checkClass= util.getRowString(i, "check_class");
								if(checkClass.equals("0")){
									checkClass = "-";
								}else if(checkClass.equals("1")){
									checkClass = "O";
								}else if(checkClass.equals("2")){
									checkClass = "/";
								}else{
									checkClass = "X";
								}
								checkList.add(new ClassCheckBean(i, classDate, checkClass));
							}
							customAdapter=new ClassCheckCustomAdapter(ClassCheckActivity.this, R.layout.checklist_row, checkList);
							classCheckList.setAdapter(customAdapter);
							dialog.dismiss();
						}
					}
				}
			};
			task.execute(request);
		}catch(Exception e){
			
		}
	}

	public void setSpinner(){
		classList = new ArrayList<String>();
		HttpRequest request;
		// webserver 에서 뿌린 Json URL
		String url = "http://54.201.33.197:3000/services/showmyclass";

		try {
			//학번을 조건으로 수강하고있는 수강과목을 넘겨받는다.
			request = new HttpRequest(new URL(url), ClassCheckActivity.this);
			request.addParameter("sno", sno);

			ConnectTask task = new ConnectTask(){
				/**
				 * 학번을 조건으로 수강과목을 넘겨받고 classList에 담는다.
				 */
				@Override
				protected void onPostExecute(String result) {
					// TODO Auto-generated method stub
					if(result.equals("false")){
						classList.add("not Exist");
					}else{
						Utility util = new Utility(result);
						int size = util.getRowSize();
						System.out.println(size);
						if(size==0){
							Toast.makeText(ClassCheckActivity.this, "no data", Toast.LENGTH_SHORT).show();
						}else{
							for(int i=0; i<size;i++){
								String className;
								className=util.getRowString(i, "cname");
								classList.add(className);
							}
						}
					}
					// classList를 스피너에 담는다.
					Spinner spinner = (Spinner)findViewById(R.id.classSpinner);
					spinner.setPrompt("출결을 확인할 과목 선택");
					CNameAdapter = new ArrayAdapter<String>(
							ClassCheckActivity.this, 
							android.R.layout.simple_list_item_1,
							classList);
					spinner.setAdapter(CNameAdapter);

					spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

						@Override
						public void onItemSelected(AdapterView<?> parent, View view,
								int position, long id) {
							// TODO Auto-generated method stub
							String selectedClass = CNameAdapter.getItem(position);
							dialog=ProgressDialog.show(ClassCheckActivity.this, null,"Wait..", true);
							dialog.setCancelable(false);
							getCheckList(selectedClass);
						}

						@Override
						public void onNothingSelected(AdapterView<?> arg0) {
							// TODO Auto-generated method stub

						}

					});
				}
			};
			task.execute(request);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

}
