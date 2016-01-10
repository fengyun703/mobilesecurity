package com.mobilesecurity.fragment;

import java.util.List;

import com.mobilesecurity.R;
import com.mobilesecurity.db.bean.LockInfo;
import com.mobilesecurity.db.bean.LockViewHold;
import com.mobilesecurity.myinterface.ApplockInerface;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.animation.Animation.AnimationListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class ShowNoLockFragment extends Fragment {

	private List<LockInfo> nolockInfos;
	private ApplockInerface applockipm;
	private ListView lv_nolock;
	private MyAdpater adpater;
	private TextView tv; 
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//System.out.println("NoLockFragment++++ onCreate");
	}
	
	public void setnoInfos(List<LockInfo> list){
		nolockInfos = list;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		//System.out.println("NoLockFragment++++ onCreateView");
		View view  = inflater.inflate(R.layout.fragment_appnolock, container, false) ;
		tv = (TextView) view.findViewById(R.id.tv_appnolock);
		lv_nolock = (ListView) view.findViewById(R.id.lv_appnolock);
		if(adpater ==null){
			adpater = new MyAdpater();
			
		}
		
		lv_nolock.setAdapter(adpater);
		return view;
	}
	
	private class MyAdpater  extends BaseAdapter{

		@Override
		public int getCount() {
			tv.setText("未加锁应用:"+nolockInfos.size()+"个");
			return nolockInfos.size();
		}

		@Override
		public View getView(int position,  View convertView, ViewGroup parent) {
			LockViewHold hold ;
			
			if(convertView ==null){
				convertView = View.inflate(getActivity(), R.layout.item_appnolock, null);
				hold = new LockViewHold();
				hold.icon = (ImageView) convertView.findViewById(R.id.iv_appicon);
				hold.tv_appname = (TextView) convertView.findViewById(R.id.tv_appname);
				hold.actionicon = (ImageView) convertView.findViewById(R.id.iv_lockicon);
				convertView.setTag(hold);
			}else{
				hold = (LockViewHold) convertView.getTag();
			}
			final LockInfo info = nolockInfos.get(position);
			final View view = convertView;
			hold.icon.setImageDrawable(info.getIcon());
			hold.tv_appname.setText(info.getAppName());
			hold.actionicon.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					nolockInfos.remove(info);
					applockipm.addLock(info);
					Animation a = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 1f
							, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0);
					a.setDuration(500);
					a.setAnimationListener(new AnimationListener() {
						
						@Override
						public void onAnimationStart(Animation animation) {
						}
						
						@Override
						public void onAnimationRepeat(Animation animation) {
						}
						
						@Override
						public void onAnimationEnd(Animation animation) {
							adpater.notifyDataSetChanged();
						}
					});
					view.startAnimation(a);
					
				}
			});
			return convertView;
		}
		
		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}
	}

	@Override
	public void onAttach(Activity activity) {
		//System.out.println("NoLockFragment++++ onAttach");
		try{  
			applockipm =(ApplockInerface)activity;  
	      }catch(ClassCastException e){  
	          throw new ClassCastException(activity.toString()+"must implement ApplockInerface");  
	      }  
		super.onAttach(activity);
	}

	
	
	/*@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		System.out.println("NoLockFragment++++ onActivityCreated");
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onStart() {
		System.out.println("NoLockFragment++++ onStart");
		super.onStart();
	}

	@Override
	public void onResume() {
		System.out.println("NoLockFragment++++ onResume");
		super.onResume();
	}

	@Override
	public void onPause() {
		System.out.println("NoLockFragment++++ onPause");
		super.onPause();
	}

	@Override
	public void onStop() {
		System.out.println("NoLockFragment++++ onStop");
		super.onStop();
	}

	@Override
	public void onDestroyView() {
		System.out.println("NoLockFragment++++ onDestroyView");
		super.onDestroyView();
	}

	@Override
	public void onDestroy() {
		System.out.println("NoLockFragment++++ onDestroy");
		super.onDestroy();
	}

	@Override
	public void onDetach() {
		System.out.println("NoLockFragment++++ onDetach");
		super.onDetach();
	}*/
	
	
}


