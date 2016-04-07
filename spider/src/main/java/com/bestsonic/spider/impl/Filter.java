package com.bestsonic.spider.impl;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.log4j.Logger;

import com.bestsonic.spider.utils.FileUtils;

public class Filter {

	private final static Logger LOG = Logger.getLogger(Filter.class);
	private static String[] regexs;
	static{
		regexs = FileUtils.getFileContent("classpath:regex-filter.txt").split("\n");
		LOG.debug("读取过滤文件：" + Arrays.toString(regexs));
	}
	public static Set<String> filter(String[] urls) {
		Set<String> result = new LinkedHashSet<String>();
		for(String url : urls){
			for(String regex : regexs){
				if(url.matches(regex)) {
					result.add(url);
					break;
				}
			}
		}
		return result;
	}
}
