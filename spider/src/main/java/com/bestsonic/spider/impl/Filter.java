package com.bestsonic.spider.impl;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.log4j.Logger;

import com.bestsonic.spider.utils.FileUtils;
/**
 * 根据过滤文件regex-filter.txt来过滤url数组，返回无序列表
 * @author Best_
 */
public class Filter {

	private final static Logger LOG = Logger.getLogger(Filter.class);
	private static String[] regexs;
	static{
		regexs = FileUtils.getFileContent("classpath:regex-filter.txt").split("\n");
		LOG.debug("读取过滤文件：" + Arrays.toString(regexs));
	}
	/**
	 * 过滤url
	 * @param urls
	 * @return
	 */
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
