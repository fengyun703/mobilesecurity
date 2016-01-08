package com.mobilesecurity.services;

import java.util.Timer;
import java.util.TimerTask;

import com.mobilesecurity.R;
import com.mobilesecurity.db.MyBlackNumberHelper;
import com.mobilesecurity.receiver.MyWidget;
import com.mobilesecurity.uitils.ProcessInfoUtils;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.IBinder;
import android.renderscript.ProgramFragmentFixedFunction.Builder.Format;
import android.text.format.Formatter;
import android.widget.RemoteViews;
import android.widget.RemoteViews.RemoteView;
import android.widget.TextView;

public class WidgetUpdateService extends Service {

	private Timer timer;
	private TimerTask task;
	private AppWidgetManager awm;
	private ComponentName cname;
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		awm = AppWidgetManager.getInstance(this);
		cname = new ComponentName(this, MyWidget.class);
		timer = new Timer();
		
		task = new TimerTask() {

			@Override
			public void run() {
				RemoteViews rv = new RemoteViews(getPackageName(), R.layout.process_widget);
				String count = "正在运行的软件:"+ProcessInfoUtils.getProcessCount(getApplicationContext());
				rv.setTextViewText(R.id.tv_process_count, count);
				String memor = "可用内存:"+Formatter.formatFileSize(getApplicationContext(), ProcessInfoUtils.getFreeMemsize(getApplicationContext()));
				rv.setTextViewText(R.id.tv_process_memory, memor);
				Intent intent = new Intent("com.mobilesecurity.killProcess");
				PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
				rv.setOnClickPendingIntent(R.id.bt_widget_clear, pendingIntent);
				awm.updateAppWidget(cname, rv);
			}
		};
		timer.schedule(task, 0, 2000);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		timer.cancel();
		task.cancel();
		timer = null;
		task = null;
	}

}
