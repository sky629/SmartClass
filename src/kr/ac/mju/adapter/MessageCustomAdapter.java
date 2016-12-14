package kr.ac.mju.adapter;

import java.util.ArrayList;

import kr.ac.mju.bean.MessageBean;
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
 *	공지 탭의 메세지 리스트의 레이아웃을 잡는 Customadapter
 */
public class MessageCustomAdapter extends BaseAdapter {
	Context context;
	LayoutInflater Inflater;
	
	ArrayList<MessageBean> arrayList = new ArrayList<MessageBean>();
	TextView textViewClassName, textViewMessageTitle, textViewMessageDate;
	
	private int layout;
	
	public MessageCustomAdapter(Context context, int layout,
										ArrayList<MessageBean> arrayList){
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
		if(convertView==null){
			convertView = Inflater.inflate(layout, parent, false);
		}
		textViewClassName=(TextView)convertView.findViewById(R.id.className);
		textViewMessageTitle=(TextView)convertView.findViewById(R.id.messageTitle);
		textViewMessageDate=(TextView)convertView.findViewById(R.id.messageDate);
		textViewClassName.setText(arrayList.get(finalPosition).getClassName());
		textViewMessageTitle.setText(arrayList.get(finalPosition).getMessageTitle());
		textViewMessageDate.setText(arrayList.get(finalPosition).getMessageDate());
		return convertView;
	}
	
}
