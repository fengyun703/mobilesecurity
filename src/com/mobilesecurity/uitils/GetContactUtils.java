package com.mobilesecurity.uitils;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;


public class GetContactUtils {

	public static List<PhoneInfo> getContactInfo(Context context) {
		ContentResolver cr = context.getContentResolver();
		Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");
		Cursor cursor = cr.query(uri, new String[] { "contact_id" }, null,
				null, null);
		List<PhoneInfo> phoneList = new ArrayList<PhoneInfo>();
		Uri datauri = Uri.parse("content://com.android.contacts/data");
		
		while (cursor.moveToNext()) {
			String id = cursor.getString(0);
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//System.out.println("id+==="+id);
			if (id != null) {
				Cursor datacursor = cr.query(datauri, new String[] { "mimetype",
						"data1" }, "contact_id=?", new String[] { id }, null);
				
				PhoneInfo info = new GetContactUtils().new PhoneInfo();
				while (datacursor.moveToNext()) {
					String data = datacursor.getString(1);
					String type = datacursor.getString(0);
					if ("vnd.android.cursor.item/phone_v2".equals(type)) {
						info.phome = data;
					} else if ("vnd.android.cursor.item/name".equals(type)) {
						info.name = data;
					} else if ("vnd.android.cursor.item/email_v2".equals(type)) {
						info.email = data;
					}
				}
				datacursor.close();
				phoneList.add(info);
			}
		}
		cursor.close();
		
		
		return phoneList;

	}
	

	public class PhoneInfo{
		public String name;
		public String phome;
		public String email;
		@Override
		public String toString() {
			return "PhoneInfo [name=" + name + ", phome=" + phome + ", email="
					+ email + "]";
		}
		
	}

}
