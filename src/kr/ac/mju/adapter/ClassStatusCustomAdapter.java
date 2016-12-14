package kr.ac.mju.adapter;

import java.util.ArrayList;

import kr.ac.mju.bean.TodayClassBean;
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
 *	홈화면의 오늘 들어야할 수업 리스트의 레이아웃을잡는 customadapter
 */
public class ClassStatusCustomAdapter extends BaseAdapter{
	Context context;
	LayoutInflater Inflater;
	
	ArrayList<TodayClassBean> arrayList = new ArrayList<TodayClassBean>();
	TextView textViewClassName, textViewClassDuration, textViewPName;
	
	private int layout;
	
	public ClassStatusCustomAdapter(Context context, int layout,
										ArrayList<TodayClassBean> arrayList){
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
		return arrayList.get(position).getClassName();
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
		if(convertView == null){
			convertView = Inflater.inflate(layout, parent, false);
		}
		
		textViewClassName=(TextView)convertView.findViewById(R.id.classListClassName);
		textViewClassDuration=(TextView)convertView.findViewById(R.id.classListClassDuration);
		textViewPName=(TextView)convertView.findViewById(R.id.classListPName);
		textViewClassName.setText(arrayList.get(finalPosition).getClassName());
		textViewClassDuration.setText(arrayList.get(finalPosition).getClassDuration());
		textViewPName.setText(arrayList.get(finalPosition).getpName());
		
		return convertView;
	}

}
