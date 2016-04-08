package com.bestsonic.spider.impl;

/**
 * 爬虫入口程序
 * 
 * @author Best_
 */
public class Main {

	public static void main(String[] args) throws Exception {

		int count = 3;
		Executor.execute(Inject.class);
		while (count > 0) {
			Executor.execute(Generate.class);
			Executor.execute(Fetch.class);
			count--;
		}
	}

}
