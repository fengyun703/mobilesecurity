package com.mobilesecurity.activities;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.mobilesecurity.R;
import com.mobilesecurity.db.bean.BlackNumberInfo;
import com.mobilesecurity.db.dao.MyBlackNumberDao;
import com.mobilesecurity.uitils.ToastUtils;

public class BlackNumberActivity extends Activity {
	private MyBlackNumberDao dao;

	private ListView lv_blacknumber;
	private LinearLayout ll_loading;
	private List<BlackNumberInfo> list;
	private MyAdapter myAdapter;

	private int startid = 0;
	private int maxCount = 10;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			ll_loading.setVisibility(View.INVISIBLE);
			if (myAdapter == null) {
				myAdapter = new MyAdapter();
				lv_blacknumber.setAdapter(myAdapter);
			} else {
				myAdapter.notifyDataSetChanged();
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_blacknumber);
		lv_blacknumber = (ListView) findViewById(R.id.lv_blacknumber);
		dao = new MyBlackNumberDao(this);
		ll_loading = (LinearLayout) findViewById(R.id.ll_loading);
		
		lv_blacknumber.setOnScrollListener(new AbsListView.OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				switch (scrollState) {
				case SCROLL_STATE_IDLE:
					if (lv_blacknumber.getLastVisiblePosition() == (list.size() - 1)) {
						if (startid >= dao.getCount()) {
							ToastUtils.showToast(BlackNumberActivity.this, "�Ѿ���ĩβ��");
							return;
						}
						startid = list.size();
						//System.out.println("startid = " + list.size());
						ll_loading.setVisibility(View.VISIBLE);
						getBlackNumberList();
					}
					break;

				default:
					break;
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub

			}
		});

		ll_loading.setVisibility(View.VISIBLE);
		getBlackNumberList();
		/*
		 * for(BlackNumberInfo data:list){
		 * System.out.println(data.getPhone()+"   "+data.getType()); }
		 */

	}

	private void getBlackNumberList() {
		new Thread() {
			public void run() {
				if (list == null) {
					list = dao.queryPart(startid, maxCount);
				} else {
					list.addAll(dao.queryPart(startid, maxCount));
				}
				
				handler.sendEmptyMessage(0);
			};
		}.start();
	}

	public void add(View view) {
		AlertDialog.Builder builder = new Builder(this);
		final AlertDialog dialog = builder.create();
		View addView = View
				.inflate(this, R.layout.dialog_add_blacknumber, null);
		final EditText et_phone = (EditText) addView
				.findViewById(R.id.et_phone_blackdialog);
		final RadioGroup rg_type = (RadioGroup) addView
				.findViewById(R.id.rg_type_blackdialog);
		Button bt_ok = (Button) addView.findViewById(R.id.bt_ok);
		Button bt_cancel = (Button) addView.findViewById(R.id.bt_cancel);
		bt_ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String phone = et_phone.getText().toString().trim();
				if (TextUtils.isEmpty(phone)) {
					ToastUtils.showToast(BlackNumberActivity.this, "�绰���벻��Ϊ��");
					return;
				}
				String type = "3";
				switch (rg_type.getCheckedRadioButtonId()) {
				case R.id.rb_tyep_tel:
					type = "1";
					break;
				case R.id.rb_tyep_sms:
					type = "2";
					break;
				case R.id.rb_tyep_all:
					type = "3";
					break;
				}
				if (dao.add(phone, type)) {
					ToastUtils.showToast(BlackNumberActivity.this, "��Ӻ������ɹ���");
					BlackNumberInfo data = new BlackNumberInfo();
					data.setPhone(phone);
					data.setType(type);
					list.add(0, data);
					myAdapter.notifyDataSetChanged();
				} else {
					ToastUtils.showToast(BlackNumberActivity.this, "��Ӻ�����ʧ�ܣ�");
				}

				dialog.dismiss();
			}
		});

		bt_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		dialog.setView(addView, 0, 0, 0, 0);
		dialog.show();
	}

	class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			ViewHold hold;
			if (convertView == null) {
				hold = new ViewHold();
				convertView = View.inflate(BlackNumberActivity.this,
						R.layout.item_blacknumber, null);
				hold.tv_phone = (TextView) convertView
						.findViewById(R.id.tv_phone_blackNumber);
				hold.tv_type = (TextView) convertView
						.findViewById(R.id.tv_type_blackNumber);
				convertView.setTag(hold);
				hold.iv_delete = (ImageView) convertView
						.findViewById(R.id.iv_delete_blackNumber);

				/*
				 * hold.iv_delete.setOnClickListener(new OnClickListener() {
				 * ����ѵ���¼�������
				 * ��ÿ��positionС�����ڶ���ֻ��һ����Ļ��ʾ��item�ĸ����������30��������ɾ����30��������ɾ������ǰ�����е�һ��
				 * �� ˵����������position�ǰ󶨺��ˡ�
				 * 
				 * @Override public void onClick(View v) { if
				 * (dao.remove(list.get(position).getPhone())) {
				 * System.out.println
				 * ("ɾ���ĵ绰�� "+list.get(position).getPhone()+",   position = "
				 * +position); ToastUtils.showToast(BlackNumberActivity.this,
				 * "ɾ���ɹ�"); list.remove(position);
				 * myAdapter.notifyDataSetChanged(); } else {
				 * ToastUtils.showToast(BlackNumberActivity.this, "ɾ��ʧ��"); } }
				 * });
				 */

			} else {
				hold = (ViewHold) convertView.getTag();
			}

			hold.tv_phone.setText(list.get(position).getPhone());
			String type = "";
			String mode = list.get(position).getType();
			if ("1".equals(mode)) {
				type = "�绰����";
			} else if ("2".equals(mode)) {
				type = "��������";
			} else if ("3".equals(mode)) {
				type = "ȫ������";
			}

			/*
			 * if("1"== mode){ type = "�绰����"; System.out.println("type �绰����  " +
			 * mode); }else if("2"== mode ){ type = "��������";
			 * System.out.println("type ��������   " + mode); }else if("3" ==mode){
			 * type = "ȫ������"; System.out.println("type ȫ������    " + mode); }
			 */
			hold.tv_type.setText(type);
			hold.iv_delete.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					AlertDialog.Builder builder = new Builder(
							BlackNumberActivity.this);
					builder.setTitle("ɾ��������");
					builder.setMessage("�Ƿ�ȷ��ɾ����");
					builder.setPositiveButton("ȷ��",
							new AlertDialog.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									if (dao.remove(list.get(position)
											.getPhone())) {
										ToastUtils.showToast(
												BlackNumberActivity.this,
												"ɾ���ɹ�");
										// System.out.println("ɾ���ĵ绰�� "+list.get(position).getPhone()+",   position = "+position);
										list.remove(position);
										myAdapter.notifyDataSetChanged();
									} else {
										ToastUtils.showToast(
												BlackNumberActivity.this,
												"ɾ��ʧ��");
									}
								}
							});

					builder.setNegativeButton("ȡ��", null);
					builder.show();

				}
			});
			return convertView;
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

	class ViewHold {
		TextView tv_phone;
		TextView tv_type;
		ImageView iv_delete;
	}

}
