package kr.ac.mju.smartclass;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;

public class ManagerTabActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		ActionBar abar = getActionBar();
		abar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		Tab tab01 = abar.newTab();
//		tab01.setText("홈");
		tab01.setTabListener(new ProductTabListener(this,ManagerHomeFragment.class.getName()));
		tab01.setIcon(R.drawable.ic_tab_home);
		abar.addTab(tab01);
		
		Tab tab02 = abar.newTab();
//		tab02.setText("메세지 관리");
		tab02.setTabListener(new ProductTabListener(this,ManagerMessageFragment.class.getName()));
		tab02.setIcon(R.drawable.ic_tab_mail);
		abar.addTab(tab02);
		Intent intent = getIntent();
		abar.setTitle("관리자");
		abar.setSubtitle("관리자 번호 : "+intent.getStringExtra("ano"));
	}
	class ProductTabListener implements ActionBar.TabListener{

		Fragment mFragment;
		Activity mActivity;
		String mFragName;
		public ProductTabListener(Activity activity, String fragName) {
			// TODO Auto-generated constructor stub
			mActivity = activity;
			mFragName = fragName;
		}
		@Override
		public void onTabReselected(Tab tab, FragmentTransaction ft) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onTabSelected(Tab tab, FragmentTransaction ft) {
			// TODO Auto-generated method stub
			mFragment = Fragment.instantiate(mActivity, mFragName);
			ft.add(android.R.id.content, mFragment);
		}

		@Override
		public void onTabUnselected(Tab tab, FragmentTransaction ft) {
			// TODO Auto-generated method stub
			ft.remove(mFragment);
			mFragment = null;
		}

	}

}
