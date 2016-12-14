package kr.ac.mju.util;

import org.json.JSONArray;
import org.json.JSONException;

public class Utility {
	private JSONArray jarray;
	
	public Utility(String json) {
		try {
			jarray = new JSONArray(json);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void setJSON(String json){
		try {
			jarray = new JSONArray(json);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public int getRowInt(int row,String name){
		int result;
		try {
			result = jarray.getJSONObject(row).getInt(name);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result = -1;
		}
		return result;
	}
	
	public String getRowString(int row,String name){
		String result;
		try {
			result = jarray.getJSONObject(row).getString(name);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result = null;
		}
		return result;
	}
	
	public int getRowSize(){
		return jarray.length();
	}
}
