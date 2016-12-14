package kr.ac.mju.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URISyntaxException;

import android.os.AsyncTask;

public class ConnectTask extends AsyncTask<HttpRequest,String,String>{

	@Override
	protected String doInBackground(HttpRequest... params) {
		// TODO Auto-generated method stub
		try {
            BufferedReader br = new BufferedReader(new InputStreamReader(params[0].sendGet()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            br.close();
            return sb.toString();
            //onPostExecute(String result)로 result전달
	    } catch (MalformedURLException ex) {
	        ex.printStackTrace();
	    } catch (IOException ex) {
	        ex.printStackTrace();
	    } catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    return null;
	}
}
