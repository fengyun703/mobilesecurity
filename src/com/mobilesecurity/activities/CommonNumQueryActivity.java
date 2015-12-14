package com.mobilesecurity.activities;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.TextView;

import com.mobilesecurity.R;
import com.mobilesecurity.db.dao.CommonNumberDao;

public class CommonNumQueryActivity extends Activity {

	private ExpandableListView elv;
	private SQLiteDatabase db;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_commonnumber);
		elv = (ExpandableListView) findViewById(R.id.elv_commonnum);
		db = SQLiteDatabase.openDatabase("/data/data/com.mobilesecurity/files/commonnum.db", null, SQLiteDatabase.OPEN_READONLY);
		elv.setAdapter(new CommonNumAdapter());
		elv.setOnChildClickListener(new OnChildClickListener() {
			
			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				ViewHold hold = (ViewHold) v.getTag();
				Uri uri = Uri.parse("tel:"+hold.tv_number.getText().toString().trim());
				Intent intent = new Intent(Intent.ACTION_DIAL, uri);
				startActivity(intent);
				//System.out.println("name = "+hold.tv_name.getText()+", number"+ hold.tv_number.getText());
				return true;
			}
		});
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		db.close();
	}
	
	

	class CommonNumAdapter extends BaseExpandableListAdapter{

		@Override
		public int getGroupCount() {
			return CommonNumberDao.getGroupCount(db);
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			return CommonNumberDao.getChildCount(db, groupPosition);
		}
		
		@Override
		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
			TextView tv;
			if(convertView == null){
				tv = new TextView(CommonNumQueryActivity.this);
			}else{
				tv = (TextView) convertView;
			}
			tv.setText("       "+CommonNumberDao.getGroupNameByPosition(db, groupPosition));
			tv.setTextSize(20);
			tv.setTextColor(Color.RED);
			return tv;
		}

		@Override
		public View getChildView(int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {
			String[] childs = CommonNumberDao.getChildNameByPosition(db, groupPosition, childPosition);
			ViewHold hold;
			 if(convertView ==null){
				 hold = new ViewHold();
				 convertView = View.inflate(CommonNumQueryActivity.this, R.layout.item_commonnum_child, null);
				 hold.tv_name = (TextView) convertView.findViewById(R.id.tv_childname_commmonnum);
				 hold.tv_number = (TextView) convertView.findViewById(R.id.tv_childnum_commmonnum);
				 convertView.setTag(hold);
			 }else{
				 hold = (ViewHold) convertView.getTag();
			 }
			hold.tv_name.setText(childs[0]);
			hold.tv_number.setText(childs[1]);
			return convertView;
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			// TODO Auto-generated method stub
			return true;
		}
		

		@Override
		public Object getGroup(int groupPosition) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Object getChild(int groupPosition, int childPosition) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getGroupId(int groupPosition) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public boolean hasStableIds() {
			// TODO Auto-generated method stub
			return false;
		}

	}
	
	private class ViewHold{
		TextView tv_name;
		TextView tv_number;
	}
}
