package com.mobilesecurity.uitils;

import android.content.Context;

public class DensityUtils {
	/**
	 * 根据手机的分辨率从dip 转成为 px
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		//System.out.println(scale);
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		//System.out.println(scale);
		return (int) (pxValue / scale + 0.5f);
	}
}
