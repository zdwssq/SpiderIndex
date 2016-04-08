package com.bestsonic.spider.utils;

import java.io.Closeable;

public class StreamUtils {

	public static void close(Closeable close){
		if(close != null){
			try{
				close.close();
			}
			catch(Exception e){
				throw new RuntimeException("流未能正确关闭!");
			}
		}
	}
}
