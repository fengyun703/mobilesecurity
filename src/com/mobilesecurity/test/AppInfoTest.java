package com.mobilesecurity.test;

import java.util.ArrayList;
import java.util.List;

import com.mobilesecurity.db.bean.MyPackageInfo;
import com.mobilesecurity.uitils.AppInfoUtils;

import android.test.AndroidTestCase;

public class AppInfoTest extends AndroidTestCase {

	public void testAppInfoTest(){
		List<MyPackageInfo> listUser = new ArrayList<MyPackageInfo>();
		List<MyPackageInfo> listSys = new ArrayList<MyPackageInfo>();
		AppInfoUtils.getPackageInfo(getContext(), listUser, listSys);
	}
}
