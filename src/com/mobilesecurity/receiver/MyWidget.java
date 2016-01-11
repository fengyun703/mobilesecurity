package com.mobilesecurity.receiver;

import com.mobilesecurity.services.WidgetUpdateService;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;

public class MyWidget extends AppWidgetProvider {

	@Override
	public void onReceive(Context context, Intent intent) {
		super.onReceive(context, intent);
		System.out.println("Widget  onReceive");
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		super.onUpdate(context, appWidgetManager, appWidgetIds);
		//System.out.println("Widget  onUpdate");
		Intent intent = new Intent(context,WidgetUpdateService.class);
		context.startService(intent);
	}

	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {
		super.onDeleted(context, appWidgetIds);
		//System.out.println("Widget  onDeleted");
	}

	@Override
	public void onDisabled(Context context) {
		super.onDisabled(context);
		//System.out.println("Widget  onDisabled");
		Intent intent = new Intent(context,WidgetUpdateService.class);
		context.stopService(intent);
	}

	
}
