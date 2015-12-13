package com.mobilesecurity.uitils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class StreamUtils {

	public static String getStringFromInput(InputStream in) throws IOException {

		ByteArrayOutputStream bis = new ByteArrayOutputStream();
		byte[] b = new byte[1024];
		int len = 0;
		while ((len = in.read(b)) != -1) {
			bis.write(b, 0, len);
		}
		return bis.toString();

	}
}
