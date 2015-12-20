package com.mobilesecurity.test;

import com.mobilesecurity.uitils.DensityUtils;

import android.test.AndroidTestCase;

public class DensityUtilsTest extends AndroidTestCase{
	
	public void dp2pxTest(){
		DensityUtils.px2dip(getContext(), 12);
	}

}
