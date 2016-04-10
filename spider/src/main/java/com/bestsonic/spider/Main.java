package com.bestsonic.spider;

import com.bestsonic.spider.impl.Executor;
import com.bestsonic.spider.impl.Fetch;
import com.bestsonic.spider.impl.Generate;
import com.bestsonic.spider.impl.Inject;

/**
 * 爬虫入口程序，count参数控制爬取层数，默认为3层，爬取网易新闻时大概200+数据。
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
