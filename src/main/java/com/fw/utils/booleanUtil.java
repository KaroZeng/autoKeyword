package com.fw.utils;

public class booleanUtil {

	public static boolean returnBoolean(String data) {
		if (data.equals("0")) {
			return false;
		}
		else if (data.equals("true")) {
			return true;
		} 
		else if (data.equals("false")) {
			return false;
		}
		else
		{
			return true;
		}
	}
}
