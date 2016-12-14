package kr.ac.mju.adapter;

import java.util.ArrayList;

import kr.ac.mju.bean.ClassCheckBean;
import kr.ac.mju.smartclass.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
/**
 * 
 * @author 3조
 *	수업 출결 현황의 리스트의 레이아웃을 잡는 customAdapter 
 */
public class ClassCheckCustomAdapter extends BaseAdapter{
	Context context;
	LayoutInflater Inflater;
	
	ArrayList<ClassCheckBean> arrayList = new ArrayList<ClassCheckBean>();
	TextView textViewClassDateTime, textViewAttendanceCheck;
	
	private int layout;
	
	public ClassCheckCustomAdapter(Context context, int layout,
										ArrayList<ClassCheckBean> arrayList){
		this.context=context;
		Inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.layout=layout;
		this.arrayList=arrayList;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return arrayList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return arrayList.get(position).getClassDateTime();
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final int finalPosition = position;
		if(convertView==null){
			convertView = Inflater.inflate(layout, parent, false);
		}
		textViewClassDateTime=(TextView)convertView.findViewById(R.id.checklist_date);
		textViewAttendanceCheck=(TextView)convertView.findViewById(R.id.checklist_check);
		textViewClassDateTime.setText(arrayList.get(finalPosition).getClassDateTime());
		textViewAttendanceCheck.setText(arrayList.get(finalPosition).getAttendanceCheck());
		return convertView;
	}

}
