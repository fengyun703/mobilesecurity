package com.mobilesecurity.activities;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.mobilesecurity.R;
import com.mobilesecurity.db.bean.MyProcessInfo;
import com.mobilesecurity.uitils.ProcessInfoUtils;
import com.mobilesecurity.uitils.StartActivityUtils;
import com.mobilesecurity.uitils.ToastUtils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ProcessManagerActivity extends Activity {
	private LinearLayout ll_loding;
	private TextView tv_status;
	private TextView tv_processcount;
	private TextView tv_meminfo;
	private ListView lv_process;
	private List<MyProcessInfo> infos;
	private List<MyProcessInfo> useInfos;
	private List<MyProcessInfo> sysInfos;
	private MyAdapter mAdapter;

	private long freesize;
	private long totalsize;
	private int process_count;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			ll_loding.setVisibility(View.INVISIBLE);
			mAdapter = new MyAdapter();
			lv_process.setAdapter(mAdapter);

		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_processmanager);
		ll_loding = (LinearLayout) findViewById(R.id.ll_loading);
		tv_status = (TextView) findViewById(R.id.tv_status_process);
		lv_process = (ListView) findViewById(R.id.lv_content_process);
		tv_processcount = (TextView) findViewById(R.id.tv_count_process);
		tv_meminfo = (TextView) findViewById(R.id.tv_meminfo_process);
		process_count = ProcessInfoUtils.getProcessCount(this);
		freesize = ProcessInfoUtils.getFreeMemsize(this);
		totalsize = ProcessInfoUtils.getTotalMemsize(this);

		tv_processcount.setText("运行进程：" + process_count + "个");
		tv_meminfo.setText("剩余/总内存:"
				+ Formatter.formatFileSize(ProcessManagerActivity.this,
						freesize)
				+ "/"
				+ Formatter.formatFileSize(ProcessManagerActivity.this,
						totalsize));

		initData();

		lv_process.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				MyProcessInfo info = null;
				if (position == 0) {

					return;
				} else if (position == useInfos.size() + 1) {

					return;
				} else if (position <= useInfos.size()) {
					// 用户进程
					info = useInfos.get(position - 1);
				} else {
					// 系统进程
					info = sysInfos.get(position - 2 - useInfos.size());
				}

				if (info.getPackageName().equals(getPackageName())) {
					return;
				}

				if (info.isChecked()) {
					info.setChecked(false);
				} else {
					info.setChecked(true);
				}
				mAdapter.notifyDataSetChanged();
			}

		});

		lv_process.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				if (useInfos != null && sysInfos != null) {
					if (firstVisibleItem <= useInfos.size()) {
						tv_status.setText("用户进程：" + useInfos.size() + "个");
					} else {
						tv_status.setText("系统进程：" + sysInfos.size() + "个");
					}
				}
			}
		});
	}

	private void initData() {
		ll_loding.setVisibility(View.VISIBLE);
		new Thread() {
			public void run() {
				infos = ProcessInfoUtils
						.getProcessInfo(ProcessManagerActivity.this);
				useInfos = new ArrayList<MyProcessInfo>();
				sysInfos = new ArrayList<MyProcessInfo>();
				for (MyProcessInfo info : infos) {
					if (info.isUserProcess()) {
						useInfos.add(info);
					} else {
						sysInfos.add(info);
					}
				}

				handler.sendEmptyMessage(0);

			};
		}.start();
	}

	@Override
	protected void onStart() {
		super.onStart();
		if(mAdapter!=null){
			mAdapter.notifyDataSetChanged();
		}
	}
	private class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			if (getSharedPreferences("config", Context.MODE_PRIVATE)
					.getBoolean("showsys", true)) {
				return useInfos.size() + sysInfos.size() + 2;
			} else {
				return useInfos.size() + 1;
			}
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			MyProcessInfo info = null;
			if (position == 0) {
				TextView tv = new TextView(ProcessManagerActivity.this);
				tv.setText("用户进程：" + useInfos.size() + "个");
				tv.setTextSize(16);
				tv.setBackgroundColor(Color.GRAY);
				return tv;
			} else if (position == useInfos.size() + 1) {
				TextView tv = new TextView(ProcessManagerActivity.this);
				tv.setText("系统进程：" + sysInfos.size() + "个");
				tv.setTextSize(16);
				tv.setBackgroundColor(Color.GRAY);
				return tv;
			} else if (position <= useInfos.size()) {
				// 用户进程
				info = useInfos.get(position - 1);
			} else {
				// 系统进程
				info = sysInfos.get(position - 2 - useInfos.size());
			}
			ViewHold hold = null;
			if (convertView instanceof RelativeLayout) {
				hold = (ViewHold) convertView.getTag();
			} else {
				convertView = View.inflate(ProcessManagerActivity.this,
						R.layout.item_processinfo, null);
				hold = new ViewHold();
				hold.img = (ImageView) convertView
						.findViewById(R.id.iv_processicon);
				hold.tv_appname = (TextView) convertView
						.findViewById(R.id.tv_appname);
				hold.tv_memsize = (TextView) convertView
						.findViewById(R.id.tv_memsize);
				hold.cb_process = (CheckBox) convertView
						.findViewById(R.id.cb_process);
				convertView.setTag(hold);
			}

			hold.img.setImageDrawable(info.getIcon());
			hold.tv_appname.setText(info.getAppName());
			hold.tv_memsize.setText("内存占用："
					+ Formatter.formatFileSize(ProcessManagerActivity.this,
							info.getMemSize()));

			if (info.isChecked()) {
				hold.cb_process.setChecked(true);
			} else {
				hold.cb_process.setChecked(false);
			}

			if (info.getPackageName().equals(getPackageName())) {
				hold.cb_process.setVisibility(View.INVISIBLE);
			} else {
				hold.cb_process.setVisibility(View.VISIBLE);
			}

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

	private class ViewHold {
		ImageView img;
		TextView tv_appname;
		TextView tv_memsize;
		CheckBox cb_process;
	}

	public void selectAll(View view) {

		for (MyProcessInfo info : useInfos) {

			if (info.getPackageName().equals(getPackageName())) {
				continue;
			}
			info.setChecked(true);
		}

		for (MyProcessInfo info : sysInfos) {
			info.setChecked(true);
		}
		mAdapter.notifyDataSetChanged();
	}

	public void reverse(View view) {
		for (MyProcessInfo info : useInfos) {
			if (info.getPackageName().equals(getPackageName())) {
				continue;
			}

			if (info.isChecked()) {
				info.setChecked(false);
			} else {
				info.setChecked(true);
			}

		}

		for (MyProcessInfo info : sysInfos) {
			if (info.isChecked()) {
				info.setChecked(false);
			} else {
				info.setChecked(true);
			}
		}
		mAdapter.notifyDataSetChanged();
	}

	public void clear(View view) {
		ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		Iterator<MyProcessInfo> iter = useInfos.iterator();
		int killcount = 0;
		int killmem = 0;
		while (iter.hasNext()) {
			MyProcessInfo info = iter.next();
			if (info.isChecked()) {
				am.killBackgroundProcesses(info.getPackageName());
				killcount++;
				killmem += info.getMemSize();
				iter.remove();
			}
		}

		iter = sysInfos.iterator();
		while (iter.hasNext()) {
			MyProcessInfo info = iter.next();
			if (info.isChecked()) {
				am.killBackgroundProcesses(info.getPackageName());
				killcount++;
				killmem += info.getMemSize();
				iter.remove();

			}
		}
		freesize += killmem;
		process_count -= killcount;
		ToastUtils.showToast(
				this,
				"关闭"
						+ killcount
						+ "个进程，释放"
						+ Formatter.formatFileSize(ProcessManagerActivity.this,
								killmem) + "内存!");

		tv_processcount.setText("运行进程：" + process_count + "个");
		tv_meminfo.setText("剩余/总内存:"
				+ Formatter.formatFileSize(ProcessManagerActivity.this,
						freesize)
				+ "/"
				+ Formatter.formatFileSize(ProcessManagerActivity.this,
						totalsize));
		mAdapter.notifyDataSetChanged();

	}

	public void setup(View view) {
		StartActivityUtils.startActivity(this, ProcessSettingActivity.class);
	}

}
