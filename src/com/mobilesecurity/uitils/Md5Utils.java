package com.mobilesecurity.uitils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Md5Utils {
	
	public static String encode(String pswd){
		try {
			MessageDigest md = MessageDigest.getInstance("md5");
			byte[] bs = md.digest(pswd.getBytes());
			StringBuilder sb = new StringBuilder();
			for(byte b :bs){
				sb.append(Integer.toHexString(b&0xff));
			}
			return sb.toString();
			
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

}
