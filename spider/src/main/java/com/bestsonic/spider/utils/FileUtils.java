package com.bestsonic.spider.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class FileUtils {

	public static String getFileContent(String location) {
		StringBuilder sb = new StringBuilder();
		if(location.startsWith("classpath:")){
			location = location.substring(location.indexOf(':') + 1);
		}
		try {
			InputStream in = FileUtils.class.getClassLoader().getResourceAsStream(location);
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			String str = null;
			while((str = reader.readLine()) != null){
				sb.append(str + "\n");
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return sb.toString();
	}
}
