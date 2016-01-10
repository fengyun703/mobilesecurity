package com.mobilesecurity.engine;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Xml;

public class SmsTools {

	public interface SmsBackupCallBack {
		void beforeBackup(int max);

		void onBackupProgress(int progress);
	}

	public interface SmsRecoveryCallBack {
		void beforeRecovery(int max);

		void onRecoveryProgress(int progress);

		void afterRecovery(int count);
	}

	/**
	 * 读取数据的短信，备份到xml文件中。
	 * 
	 * @param context
	 * @param fileName
	 *            xml文件名称
	 * @param callback
	 *            备份过程中进度处理
	 * @return
	 */
	public static Boolean smsBackup(Context context, String fileName,
			SmsBackupCallBack callback) {
		ContentResolver cr = context.getContentResolver();
		File file = new File(context.getFilesDir(), fileName);
		XmlSerializer serializer = Xml.newSerializer();
		int total = 0;
		int count = 0;
		try {
			serializer.setOutput(new FileOutputStream(file), "utf-8");
			Cursor cursor = cr.query(Uri.parse("content://sms/"), new String[] {
					"address", "date", "type", "body", "_id" }, null, null,
					null);
			total = cursor.getCount();
			callback.beforeBackup(total);// 设置progress最大值
			serializer.startDocument("utf-8", true);
			serializer.startTag(null, "info");
			serializer.attribute(null, "total", String.valueOf(total));

			while (cursor.moveToNext()) {
				count++;
				serializer.startTag(null, "sms");

				serializer.startTag(null, "address");
				String address = cursor.getString(0);
				serializer.text(address);
				serializer.endTag(null, "address");

				serializer.startTag(null, "date");
				String date = cursor.getString(1);
				serializer.text(date);
				serializer.endTag(null, "date");

				serializer.startTag(null, "type");
				String type = cursor.getString(2);
				serializer.text(type);
				serializer.endTag(null, "type");

				serializer.startTag(null, "body");
				String body = cursor.getString(3);
				serializer.text(body);
				serializer.endTag(null, "body");

				serializer.startTag(null, "_id");
				String _id = cursor.getString(4);
				serializer.text(_id);
				serializer.endTag(null, "_id");

				// System.out.println(address+"  "+ date+" "+type+" "+body);
				serializer.endTag(null, "sms");
				callback.onBackupProgress(count);
				Thread.sleep(500);
			}
			serializer.endTag(null, "info");
			serializer.endDocument();
			return true;

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

	}

	public static Boolean smsrecovery(Context context, String fileName,
			SmsRecoveryCallBack callback) {
		ContentResolver cr = context.getContentResolver();
		File file = new File(context.getFilesDir(), fileName);
		XmlPullParser parser = Xml.newPullParser();
		ContentValues values = null;
		int recoveryCount = 0;
		try {
			parser.setInput(new FileInputStream(file), "utf-8");
			int xmltype = parser.getEventType();
			int tatol = 0;
			int count = 0;
			String address;
			String date;
			String body;
			String type;
			String _id;
			while (xmltype != XmlPullParser.END_DOCUMENT) {
				switch (xmltype) {
				case XmlPullParser.START_TAG:
					if ("info".equals(parser.getName())) {
						tatol = Integer.parseInt(parser.getAttributeValue(null,
								"total"));
						callback.beforeRecovery(tatol);
					} else if ("sms".equals(parser.getName())) {
						values = new ContentValues();
					} else if ("address".equals(parser.getName())) {
						address = parser.nextText();
						values.put("address", address);
					} else if ("date".equals(parser.getName())) {
						date = parser.nextText();
						values.put("date", date);
					} else if ("body".equals(parser.getName())) {
						body = parser.nextText();
						values.put("body", body);
					} else if ("type".equals(parser.getName())) {
						type = parser.nextText();
						values.put("type", type);
					} else if ("_id".equals(parser.getName())) {
						_id = parser.nextText();
						values.put("_id", _id);
					}
					break;

				case XmlPullParser.END_TAG:
					if ("sms".equals(parser.getName())) {
						/*Cursor cursor = cr.query(Uri.parse("content://sms"), null, " date =?",
			                    new String[] { values.getAsString("date") }, null);
			               根据date区别短信是否存在。     
			              */
					
						Uri uri = cr
								.insert(Uri.parse("content://sms/"), values);// 若插入数据已存在，返回null;
						count++;
						/*System.out.println(values.toString() + ", count = "
								+ count);*/
						if (uri != null) {
							recoveryCount++;
							System.out.println(uri.toString());
						}

						values = null; // 每次插入后，清除values；
						callback.onRecoveryProgress(count);

						Thread.sleep(500);
					}
					break;
				default:
					break;
				}

				xmltype = parser.next();
			}

			callback.afterRecovery(recoveryCount);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
