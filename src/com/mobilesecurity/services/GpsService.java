package com.mobilesecurity.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.telephony.gsm.SmsManager;

public class GpsService extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public void onCreate() {
		final SharedPreferences sp = getSharedPreferences("config", MODE_PRIVATE);
		
		final LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		Criteria cr = new Criteria();
		cr.setAccuracy(Criteria.ACCURACY_FINE);
		cr.setPowerRequirement(Criteria.POWER_HIGH);
		String provider = lm.getBestProvider(cr, true);
		//System.out.println(""+provider);
		//System.out.println(lm.getAllProviders());
		lm.requestLocationUpdates(provider, 0, 0, new LocationListener() {
			
			@Override
			public void onStatusChanged(String provider, int status, Bundle extras) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProviderEnabled(String provider) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProviderDisabled(String provider) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onLocationChanged(Location location) {
				double latitude = location.getLatitude();
				double longitude = location.getLongitude();
				String locationstr= "latitude = "+latitude+"longitude = "+longitude;
				//System.out.println("发短信前"+locationstr);
				SmsManager.getDefault().sendTextMessage(sp.getString("phone", ""), null, locationstr,null,null);
				lm.removeUpdates(this);
				stopSelf();
				//System.out.println("发短信后"+locationstr);
			}
		});
	}

}
