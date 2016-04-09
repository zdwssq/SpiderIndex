package com.bestsonic.spider;

import com.bestsonic.spider.impl.Executor;
import com.bestsonic.spider.impl.Fetch;
import com.bestsonic.spider.impl.Generate;
import com.bestsonic.spider.impl.Inject;

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
