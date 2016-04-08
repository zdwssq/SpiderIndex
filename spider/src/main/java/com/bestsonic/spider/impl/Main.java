package com.bestsonic.spider.impl;

/**
 * 爬虫入口程序
 * 
 * @author Best_
 */
public class Main {

	public static void main(String[] args) throws Exception {

		int count = 5;
		// 1. 从文件读取URL，调用Inject类，写入数据库，过滤不需要的地址。
		Executor.execute(Inject.class);
		while (count > 0) {
			Executor.execute(Generate.class);
			Executor.execute(Fetch.class);
			// 2. 从数据库中取出需要爬取的网址信息，已经爬取的不需要再次显示。

			// 3. 根据从数据库获取的需要爬取的网址信息，生成FetchJob。

			// 4. 获取线程池，执行网站的爬取和解析，并将下次需要爬取的URL写入数据库，过滤不需要的地址。

			// 5. 多次循环，直到爬取到了指定的数量。
			count--;
		}
	}

}
