package com.mobilesecurity.test;

import com.mobilesecurity.uitils.ProcessInfoUtils;

import android.test.AndroidTestCase;

public class ProcessUtilsTest extends AndroidTestCase {

	public void testgetProcessCount(){
		ProcessInfoUtils.getProcessCount(getContext());
	}
	
	public void testgetProcessInfo(){
		ProcessInfoUtils.getProcessInfo(getContext());
	}
	
}
