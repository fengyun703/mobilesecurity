package com.mobilesecurity.test;

import android.net.TrafficStats;
import android.test.AndroidTestCase;
import android.text.format.Formatter;

public class TrafficTest extends AndroidTestCase {

	public void test(){
		long  l1 = TrafficStats.getTotalRxBytes()+TrafficStats.getTotalTxBytes();
		long  l2 = TrafficStats.getMobileRxBytes()+TrafficStats.getMobileTxBytes();
		System.out.println(Formatter.formatFileSize(getContext(), l1));
		System.out.println(Formatter.formatFileSize(getContext(), l2));
	}
}
