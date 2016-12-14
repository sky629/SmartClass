package kr.ac.mju.smartclass;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import kr.ac.mju.util.ConnectTask;
import kr.ac.mju.util.HttpRequest;
import kr.ac.mju.util.Utility;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.Toast;

public class ManagerHomeFragment extends Fragment implements OnItemSelectedListener{
	private ArrayAdapter<String> classAdapter;
	private ArrayAdapter<String> dateAdapter;
	private View mFragmentView;
	private GridView gridView;
	private HttpRequest request;
	private ArrayList<String> classList;
	private ArrayList<String> dateList;
	private ArrayList<String> stuList;
	private Spinner classSpinner;
	private Spinner dateSpinner;
	private String ano;
	private ProgressDialog dialog;
	private String cno;
	private String class_date;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mFragmentView = inflater.inflate(R.layout.manager_home, container, false);
		gridView = (GridView)mFragmentView.findViewById(R.id.gridView);
		classSpinner = (Spinner)mFragmentView.findViewById(R.id.classspinner);
		dateSpinner = (Spinner)mFragmentView.findViewById(R.id.datespinner);
		return mFragmentView;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		classList = new ArrayList<String>();
		stuList = new ArrayList<String>();
		dateList = new ArrayList<String>();
		Intent intent = this.getActivity().getIntent();
		ano = intent.getStringExtra("ano");
		
		classSpinner.setPrompt("과목 선택");
		dateSpinner.setPrompt("날짜 선택");
		gridView.setClickable(false);
		String url = "http://54.201.33.197:3000/services/assistclass";
		try {									//해당 관리자가 맡은 과목을 불러옴
			dialog = ProgressDialog.show(ManagerHomeFragment.this.getActivity(), "", "잠시만 기다려 주세요...",true);
			request = new HttpRequest(new URL(url),this.getActivity());
			request.addParameter("ano", ano);
			ConnectTask task = new ConnectTask(){
				@Override
				protected void onPostExecute(String result) {
					// TODO Auto-generated method stub
					if(result.equals("false")){
						//관리자가 맡은 과목이 없을때
						classList.add("not Exist");
					}else{
						Utility util = new Utility(result);
						int size = util.getRowSize();
						for(int i=0;i<size;i++){
							if(util.getRowString(i, "cno").length()==1){
								String s = "000"+util.getRowString(i,"cno")+"  "+util.getRowString(i, "cname");
								classList.add(s);
							}else if(util.getRowString(i, "cno").length()==2){
								String s = "00"+util.getRowString(i,"cno")+"  "+util.getRowString(i, "cname");
								classList.add(s);
							}else if(util.getRowString(i, "cno").length()==3){
								String s = "0"+util.getRowString(i,"cno")+"  "+util.getRowString(i, "cname");
								classList.add(s);
							}else{
								String s = util.getRowString(i,"cno")+"  "+util.getRowString(i, "cname");
								classList.add(s);
							}
						}
					}
					classAdapter = new ArrayAdapter<String>(ManagerHomeFragment.this.getActivity(), 
							R.layout.spinner_item, classList);
					
					classSpinner.setAdapter(classAdapter);
					classSpinner.setOnItemSelectedListener(ManagerHomeFragment.this);
					dialog.dismiss();
				}//onPostExecute
			};
			task.execute(request);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long arg3) {				//관리자가 조교를 맡은 과목을 선택했을 때
		// TODO Auto-generated method stub
		dialog = ProgressDialog.show(ManagerHomeFragment.this.getActivity(), "", "잠시만 기다려 주세요...",true);
		cno = classList.get(position).split(" ")[0];
		String url = "http://54.201.33.197:3000/services/getdatefromclass";
										//해당 과목의 수업 날짜를 불러옴
		try {
			request = new HttpRequest(new URL(url),ManagerHomeFragment.this.getActivity());
			request.addParameter("cno", cno);
			ConnectTask task = new ConnectTask(){
				@Override
				protected void onPostExecute(String result) {
					dateList.clear();
					Utility util = new Utility(result);
					int size = util.getRowSize();
					for(int i=0; i<size; i++){
						dateList.add(util.getRowString(i, "class_date"));
					}
					dateAdapter = new ArrayAdapter<String>(ManagerHomeFragment.this.getActivity(), 
							R.layout.spinner_item, dateList);
					dateSpinner.setAdapter(dateAdapter);
					dateAdapter.notifyDataSetChanged();
					dateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
						@Override
						public void onItemSelected(AdapterView<?> parent,
								View view, int position, long arg3) {		//수업의 날짜를 선택했을 때
							dialog = ProgressDialog.show(ManagerHomeFragment.this.getActivity(), "", "학생 정보를 가져오는 중...",true);
							String url = "http://54.201.33.197:3000/services/getstufromclass";
							try {											//수업이 있는 날짜에 해당하는 학생의 출결사항을 불러옴
								request = new HttpRequest(new URL(url),ManagerHomeFragment.this.getActivity());
								request.addParameter("cno", cno);
								request.addParameter("class_date", dateList.get(position));
								ConnectTask task = new ConnectTask(){
									protected void onPostExecute(String result) {
										stuList.clear();
										stuList.add("학번");
										stuList.add("출결");
										Utility util = new Utility(result);
										int size = util.getRowSize();
										for(int i=0; i<size; i++){
											stuList.add(util.getRowString(i, "sno"));
											String checkClass = util.getRowString(i, "check_class");
											if(checkClass.equals("0")){//아직 지나지 않은 수업
												stuList.add("-");
											}else if(checkClass.equals("1")){//출석
												stuList.add("O");
											}else if(checkClass.equals("2")){//지각
												stuList.add("/");
											}else if(checkClass.equals("3")){//결석
												stuList.add("X");
											}
										}
										classAdapter = new ArrayAdapter<String>(ManagerHomeFragment.this.getActivity(),
												R.layout.gridview_item, stuList);
										gridView.setAdapter(classAdapter);
										classAdapter.notifyDataSetChanged();
										dialog.dismiss();
									}//onPostExecute
								};
								task.execute(request);
							} catch (MalformedURLException e) {
								e.printStackTrace();
							}
						}
						@Override
						public void onNothingSelected(AdapterView<?> arg0) {
							//Do Nothing
						}
					});
					dialog.dismiss();
				}//onPostExecute
			};
			task.execute(request);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		//Do Nothing
	}
}
