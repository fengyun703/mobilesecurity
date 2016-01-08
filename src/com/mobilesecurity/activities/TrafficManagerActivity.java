package com.mobilesecurity.activities;

import java.util.List;

import com.mobilesecurity.R;
import com.mobilesecurity.db.bean.MyTrafficInfo;
import com.mobilesecurity.uitils.ToastUtils;
import com.mobilesecurity.uitils.TrafficUtils;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.net.TrafficStats;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class TrafficManagerActivity extends Activity {
	
	private TextView tv_wify;
	private TextView tv_mobile;
	private LinearLayout ll_loading;
	private ListView lv;
	private List<MyTrafficInfo> list;

	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			ll_loading.setVisibility(View.INVISIBLE);
			
			if(list.size()>0){
				lv.setAdapter(new MyAdapter());
			}else{
				ToastUtils.showToast(TrafficManagerActivity.this, "没有应用程序消耗流量！");
			}
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_trafficmanager);
		tv_mobile = (TextView) findViewById(R.id.tv_moblietotal);
		tv_wify = (TextView) findViewById(R.id.tv_wifytotal);
		ll_loading = (LinearLayout) findViewById(R.id.ll_loading);
		lv = (ListView) findViewById(R.id.lv_traffic);
		
		long mobileRx =  TrafficStats.getMobileRxBytes();
		long mobileTx =  TrafficStats.getMobileTxBytes();
		long mobleTotal = mobileRx+mobileTx;
		String mobile = "本次开机以来2G/3G/4G总用流量："+Formatter.formatFileSize(TrafficManagerActivity.this, mobleTotal);
		
		long totalRx =  TrafficStats.getTotalRxBytes();
		long totalTx =  TrafficStats.getTotalTxBytes();
		long totaltal = totalRx+totalTx;
		long wifyTotal = totaltal - mobleTotal;
		String wify ="本次开机以来Wify总用流量:" +Formatter.formatFileSize(TrafficManagerActivity.this, wifyTotal);	
		
		tv_mobile.setText(mobile);
		tv_wify.setText(wify);
		//System.out.println(wify+"\n"+mobile+"\n"+totaltal);
		getAppTraffic();
		
	}
	private void getAppTraffic() {
		ll_loading.setVisibility(View.VISIBLE);
		new Thread(){
			public void run() {
				list = TrafficUtils.getTafficInfo(TrafficManagerActivity.this);
				handler.sendEmptyMessage(0);
			};
		}.start();
		
	}
	
	private class MyAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHold hold = null;
			if(convertView != null){
				hold = (ViewHold) convertView.getTag();
			}else{
				convertView = View.inflate(TrafficManagerActivity.this, R.layout.item_traffic, null);
				hold = new ViewHold();
				hold.icon = (ImageView) convertView.findViewById(R.id.iv_icon_traffic);
				hold.tv_rx = (TextView) convertView.findViewById(R.id.tv_rx_traffic);
				hold.tv_appName = (TextView) convertView.findViewById(R.id.tv_appname_traffic);
				hold.tv_tx = (TextView) convertView.findViewById(R.id.tv_tx_traffic);
				hold.tv_total= (TextView) convertView.findViewById(R.id.tv_total_traffic);
				convertView.setTag(hold);
			}
			MyTrafficInfo info = list.get(position);
			hold.icon.setImageDrawable(info.getIcon());
			hold.tv_rx.setText( Formatter.formatFileSize(TrafficManagerActivity.this, info.getRx()));
			hold.tv_tx.setText( Formatter.formatFileSize(TrafficManagerActivity.this, info.getTx()));
			hold.tv_total.setText( Formatter.formatFileSize(TrafficManagerActivity.this, info.getTotal()));
			hold.tv_appName.setText(info.getAppName());
			return convertView;
		}
		
	}
	
	private class ViewHold{
		private ImageView icon;
		private TextView tv_appName;
		private TextView tv_rx;
		private TextView tv_tx;
		private TextView tv_total;
		
	}

}
