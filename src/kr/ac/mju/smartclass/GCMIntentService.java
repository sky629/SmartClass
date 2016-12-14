package kr.ac.mju.smartclass;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;

public class GCMIntentService extends GCMBaseIntentService {
    private static final String tag = "GCMIntentService";
    private static final String PROJECT_ID = "775333259345";
   
    public GCMIntentService(){ this(PROJECT_ID); }
   
    public GCMIntentService(String project_id) { super(project_id); }
 
    /** 푸시로 받은 메시지 */
    @Override
    protected void onMessage(Context context, Intent intent) {
//        Bundle b = intent.getExtras();
//        Iterator<String> iterator = b.keySet().iterator();
//        while(iterator.hasNext()) {
//            String key = iterator.next();
//            String value = b.get(key).toString();
//            Log.d(tag, "onMessage. "+key+" : "+value);
//        }
        
    	PendingIntent pending = PendingIntent.getActivity(GCMIntentService.this, 0, this.getPackageManager().getLaunchIntentForPackage("kr.ac.mju.smartclass"), 0);
//        Notification.Builder builder = new Notification.Builder(GCMIntentService.this)
//		.setContentText("클릭하시면 앱으로 넘어갑니다.")
//		.setContentTitle("새로운 공지가 등록 되었습니다.")
//		.setTicker("새로운 공지가 등록 되었습니다.")
//		.setSmallIcon(R.drawable.ic_launcher_mju)
//		.setContentIntent(pending)
//		.setWhen(System.currentTimeMillis())
//		.setDefaults(Notification.DEFAULT_SOUND)
//		.setVibrate(new long[]{500,1000,500})
//		.setAutoCancel(true);
        
//		Notification noti = builder.build();
//		NotificationManager nm = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
//		nm.notify(1315111, noti);
    	NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplication().getApplicationContext());
    	builder.setContentText("클릭하시면 앱으로 넘어갑니다.");
    	builder.setContentTitle("새로운 공지가 등록 되었습니다.");
    	builder.setTicker("새로운 공지가 등록 되었습니다.");
    	builder.setContentIntent(pending);
    	builder.setSmallIcon(R.drawable.ic_launcher_mju);
    	builder.setWhen(System.currentTimeMillis());
    	builder.setVibrate(new long[]{500,1000,500});
    	builder.setAutoCancel(true);
    	builder.setDefaults(Notification.DEFAULT_ALL);
    	Notification noti = builder.build();
    	NotificationManager nm = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
    	nm.notify(1315111,noti);
    }

 
    /**단말에서 GCM 서비스 등록 했을 때 등록 id를 받는다*/
    @Override
    protected void onRegistered(Context context, String regId) {
        Log.d(tag, "onRegistered. regId : "+regId);
    }

    /**단말에서 GCM 서비스 등록 해지를 하면 해지된 등록 id를 받는다*/
    @Override
    protected void onUnregistered(Context context, String regId) {
        Log.d(tag, "onUnregistered. regId : "+regId);
    }

    /**에러 발생시*/
	@Override
	protected void onError(Context arg0, String arg1) {
		System.out.println(arg1);
	}
}