package com.mobilesecurity.test;

import com.mobilesecurity.uitils.AppInfoUtils;

import android.test.AndroidTestCase;

public class AppInfoTest extends AndroidTestCase {

	public void testAppInfoTest(){
		AppInfoUtils.getPackageInfoAll(getContext());
	}
}
