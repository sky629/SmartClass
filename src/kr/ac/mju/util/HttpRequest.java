package kr.ac.mju.util;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import android.content.Context;
import android.util.Log;

public class HttpRequest {
	public static final String CRLF = "\r\n";
    
    /**
     * 연결할 URL
     */
    private URL targetURL;
    
    /**
     * 파라미터 목록을 저장하고 있다.
     * 파라미터 이름과 값이 차례대로 저장된다.
     */
    private ArrayList<Object> list;
//    private Context mContext;

	public HttpRequest(URL target,Context ctx) {
        this(target, 20, ctx);
    }
    
    /**
     * HttpRequest를 생성한다.
     * 
     * @param target HTTP 메시지를 전송할 대상 URL
     */
    public HttpRequest(URL target, int initialCapicity, Context ctx) {
        this.targetURL = target;
        this.list = new ArrayList<Object>(initialCapicity);
//        this.mContext = ctx;
    }
    
    /**
     * 파라미터를 추가한다.
     * @param parameterName 파라미터 이름
     * @param parameterValue 파라미터 값
     * @exception IllegalArgumentException parameterValue가 null일 경우
     */
    public void addParameter(String parameterName, String parameterValue) {
        if (parameterValue == null) 
        throw new IllegalArgumentException("parameterValue can't be null!");
        
        list.add(parameterName);
        list.add(parameterValue);
    }
    
    /**
     * 파일 파라미터를 추가한다.
     * 만약 parameterValue가 null이면(즉, 전송할 파일을 지정하지 않는다면
     * 서버에 전송되는 filename 은 "" 이 된다.
     * 
     * @param parameterName 파라미터 이름
     * @param parameterValue 전송할 파일
     * @exception IllegalArgumentException parameterValue가 null일 경우
     */
    public void addFile(String parameterName, File parameterValue) {
        // paramterValue가 null일 경우 NullFile을 삽입한다.
        if (parameterValue == null) {
            list.add(parameterName);
            list.add(new NullFile());
        } else {
            list.add(parameterName);
            list.add(parameterValue);
        }
    }
    
    private class NullFile {
        NullFile() {
        }
        public String toString() {
            return "";
        }
    }
    
    private static String encodeString(ArrayList<Object> parameters) {
        StringBuffer sb = new StringBuffer(256);
        
        Object[] obj = new Object[parameters.size()];
        parameters.toArray(obj);
        
        for (int i = 0 ; i < obj.length ; i += 2) {
            if ( obj[i+1] instanceof File || obj[i+1] instanceof NullFile ) continue;
            try{
	            sb.append(URLEncoder.encode((String)obj[i],"UTF-8") );
	            sb.append('=');
	            sb.append(URLEncoder.encode((String)obj[i+1],"UTF-8") );
            }catch(Exception e){
            	e.printStackTrace();
            }
            if (i + 2 < obj.length) sb.append('&');
        }
        return sb.toString();
    }
    
    /**
     * GET 방식으로 대상 URL에 파라미터를 전송한 후
     * 응답을 InputStream으로 리턴한다.
     * @return InputStream
     * @throws URISyntaxException 
     */
    public InputStream sendGet() throws IOException, URISyntaxException {
        String paramString = null;
        if (list.size() > 0)
            paramString = "?" + encodeString(list);
        else
            paramString = "";
        
        URL url = new URL(targetURL.toExternalForm() + paramString);
        
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
 
        conn.setAllowUserInteraction(false); //no상관
        conn.setConnectTimeout(10000); //no상관
        conn.setReadTimeout(10000); //no상관
        conn.setDoInput(true);
        conn.setUseCaches(false);
        
        conn.connect();
        
        int status = conn.getResponseCode();
        
        Log.d("HTTP",""+status);
        
        if(status == 200 || status == 201){
        	return conn.getInputStream();
        }else{
        	return null;
        }
    }
    
    /**
     * POST 방식으로 대상 URL에 파라미터를 전송한 후
     * 응답을 InputStream으로 리턴한다.
     * @return InputStream
     * @throws URISyntaxException 
     */
    public InputStream sendPost() throws IOException, URISyntaxException {
        String paramString = null;
        if (list.size() > 0)
            paramString = encodeString(list);
        else
            paramString = "";
        
        HttpURLConnection conn = (HttpURLConnection)targetURL.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type",
                                "application/x-www-form-urlencoded");
        

        conn.setAllowUserInteraction(false); //no상관
        conn.setConnectTimeout(10000); //no상관
        conn.setReadTimeout(10000); //no상관
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setUseCaches(false);
        
        DataOutputStream out = null;
        try {
            out = new DataOutputStream(conn.getOutputStream());
            out.writeBytes(paramString);
            out.flush();
        } finally {
            if (out != null) out.close();
        }
        
        conn.connect();
        
        int status = conn.getResponseCode();
        
        if(status == 200 || status == 201){
        	return conn.getInputStream();
        }else{
        	return null;
        }
    }
}