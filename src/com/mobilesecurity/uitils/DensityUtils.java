package com.mobilesecurity.uitils;

import android.content.Context;

public class DensityUtils {
	/**
	 * �����ֻ��ķֱ��ʴ�dip ת��Ϊ px
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		//System.out.println(scale);
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * �����ֻ��ķֱ��ʴ� px(����) �ĵ�λ ת��Ϊ dp
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		//System.out.println(scale);
		return (int) (pxValue / scale + 0.5f);
	}
}
