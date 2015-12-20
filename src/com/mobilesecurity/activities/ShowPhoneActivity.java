package com.mobilesecurity.activities;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.mobilesecurity.R;
import com.mobilesecurity.uitils.GetContactUtils;
import com.mobilesecurity.uitils.GetContactUtils.PhoneInfo;

public class ShowPhoneActivity extends Activity {
	
	private ListView lv_showphone;
	private List<PhoneInfo> phoneList;
	private LinearLayout ll_loding;
	
	private Handler handler= new Handler(){
		public void handleMessage(android.os.Message msg) {
			ll_loding.setVisibility(View.INVISIBLE);
			lv_showphone.setAdapter(new MyPhoneAdapter());
		};
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_showphone_safe3);
		lv_showphone = (ListView) findViewById(R.id.lv_showphone);
		ll_loding = (LinearLayout) findViewById(R.id.ll_loading);
		ll_loding.setVisibility(View.VISIBLE);
		getContact();
		
		
		
		
		lv_showphone.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String phone = phoneList.get(position).phome;
				Intent intent = new Intent();
				intent.putExtra("phone", phone);
				setResult(1, intent);
				finish();
			}
			
		});
	}

	private void getContact() {
		new Thread(){
			public void run() {
				phoneList = GetContactUtils.getContactInfo(ShowPhoneActivity.this);
				handler.sendEmptyMessage(0);
			};
			
		}.start();
	}
	
	class MyPhoneAdapter extends BaseAdapter{

		@Override
		public int getItemViewType(int position) {
			// TODO Auto-generated method stub
			return super.getItemViewType(position);
		}


		@Override
		public int getViewTypeCount() {
			// TODO Auto-generated method stub
			return super.getViewTypeCount();
		}


		@Override
		public int getCount() {
			return phoneList.size();
		}
		

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = View.inflate(ShowPhoneActivity.this, R.layout.item_showphone, null);
			TextView tv_name = (TextView) view.findViewById(R.id.tv_name_phone);
			TextView tv_phome = (TextView) view.findViewById(R.id.tv_num_phone);
			tv_name.setText(phoneList.get(position).name);
			tv_phome.setText(phoneList.get(position).phome);
			return view;
		}
		

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

	}
	
	
	
	
	
	
	
	

}
