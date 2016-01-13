package com.mobilesecurity.activities;

import android.R.string;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobilesecurity.R;
import com.mobilesecurity.uitils.Md5Utils;
import com.mobilesecurity.uitils.StartActivityUtils;
import com.mobilesecurity.uitils.ToastUtils;

public class HomeActivity extends Activity {
	private SharedPreferences sp;
	private GridView gv_home;
	private static final String[] names = { "手机防盗", "通讯卫士", "软件管理", "进程管理",
			"流量统计", "手机杀毒", "缓存清理", "高级工具", "设置中心" };
	private static int[] icons = { R.drawable.safe, R.drawable.callmsgsafe,
			R.drawable.app_selector_home, R.drawable.taskmanager,
			R.drawable.netmanager, R.drawable.trojan, R.drawable.sysoptimize,
			R.drawable.atools, R.drawable.settings };

	private AlertDialog dialog;
	private AlertDialog.Builder builder;
	private Button bt_ok;
	private Button bt_cancel;
	private EditText et_pswd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_home);
		gv_home = (GridView) findViewById(R.id.gv_home);
		gv_home.setAdapter(new MyHomeAdaptet());
		sp = getSharedPreferences("config", MODE_PRIVATE);

		gv_home.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				switch (position) {
				case 0: // 手机防盗
					if (sp.getString("password", null) == null) {
						// System.out.println(sp.getString("password", null));
						showSettingDialog();
					} else {
						// System.out.println(sp.getString("password", null));
						showEnterDialog();
					}

					break;
				case 1:
					//String s= null;
					//s.equals("fdsa");
					StartActivityUtils.startActivity(HomeActivity.this,
							BlackNumberActivity.class);
					break;
				case 2:
					StartActivityUtils.startActivity(HomeActivity.this,
							AppManagerActivity.class);
					break;
				case 3:
					StartActivityUtils.startActivity(HomeActivity.this,
							ProcessManagerActivity.class);
					break;
				case 4:
					StartActivityUtils.startActivity(HomeActivity.this,
							TrafficManagerActivity.class);
					break;
				case 5:
					StartActivityUtils.startActivity(HomeActivity.this,
							AntiVirusActivity.class);
					break;
				case 6:
					StartActivityUtils.startActivity(HomeActivity.this,
							CleanCacheActivity.class);
					break;
					
				case 7:
					StartActivityUtils.startActivity(HomeActivity.this,
							AdvancedActivity.class);
					break;
				case 8: // 设置中心
					StartActivityUtils.startActivity(HomeActivity.this,
							SettingActivity.class);
					break;
				}

			}

		});
	}

	private void showSettingDialog() {
		builder = new Builder(this);
		View view = View.inflate(this, R.layout.dialog_set_pswd_home, null);
		builder.setView(view);
		bt_ok = (Button) view.findViewById(R.id.bt_ok);
		bt_cancel = (Button) view.findViewById(R.id.bt_cancel);
		et_pswd = (EditText) view.findViewById(R.id.et_set_pswd);
		final EditText et_confirm_pswd = (EditText) view
				.findViewById(R.id.et_confirm_pswd);
		bt_ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String pswd = et_pswd.getText().toString().trim();
				String confire = et_confirm_pswd.getText().toString().trim();
				if (TextUtils.isEmpty(pswd) && TextUtils.isEmpty(confire)) {
					ToastUtils.showToast(HomeActivity.this, "密码不能为空。");
				} else if (pswd.equals(confire)) {
					String coded = Md5Utils.encode(pswd);
					Editor ed = sp.edit();
					ed.putString("password", coded);
					ed.commit();
					dialog.dismiss();
					StartActivityUtils.startActivity(HomeActivity.this,
							Safe1Activity.class);

				} else {
					ToastUtils.showToast(HomeActivity.this, "两次输入的密码不同！");
				}
			}
		});

		bt_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		dialog = builder.show();
	}

	private void showEnterDialog() {
		builder = new Builder(this);
		View view = View.inflate(this, R.layout.dialog_enter_pswd_home, null);
		builder.setView(view);
		bt_ok = (Button) view.findViewById(R.id.bt_ok);
		bt_cancel = (Button) view.findViewById(R.id.bt_cancel);
		et_pswd = (EditText) view.findViewById(R.id.et_set_pswd);

		bt_ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String pswd = et_pswd.getText().toString().trim();
				if (TextUtils.isEmpty(pswd)) {
					ToastUtils.showToast(HomeActivity.this, "密码不能为空。");
				} else {
					String coded = Md5Utils.encode(pswd);
					String passwrod = sp.getString("password", null);
					if (coded.equals(passwrod)) {
						dialog.dismiss();
						Boolean finish = sp.getBoolean("finishsetup", false);
						if (finish) {
							StartActivityUtils.startActivity(HomeActivity.this,
									SafeMainActivity.class);
						} else {
							StartActivityUtils.startActivity(HomeActivity.this,
									Safe1Activity.class);
						}
					} else {
						ToastUtils.showToast(HomeActivity.this, "密码不对！");
					}
				}
			}
		});

		bt_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		dialog = builder.show();

	}

	class MyHomeAdaptet extends BaseAdapter {

		@Override
		public int getCount() {
			return names.length;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder hold;
			if (convertView == null) {
				convertView = View.inflate(HomeActivity.this,
						R.layout.item_home, null);
				hold = new ViewHolder();
				hold.iv_home = (ImageView) convertView
						.findViewById(R.id.iv_home);
				hold.tv_home = (TextView) convertView
						.findViewById(R.id.tv_home);
				convertView.setTag(hold);

			} else {
				hold = (ViewHolder) convertView.getTag();
			}
			// System.out.println(hold.iv_home +"++++"+hold.tv_home);
			hold.iv_home.setImageResource(icons[position]);
			hold.tv_home.setText(names[position]);
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

		class ViewHolder {
			TextView tv_home;
			ImageView iv_home;
		}

	}

}
