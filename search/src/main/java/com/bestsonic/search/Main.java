package com.bestsonic.search;

import com.bestsonic.search.impl.PageRank;
import com.bestsonic.search.impl.Split;
import com.bestsonic.spider.impl.Executor;

public class Main {

	public static void main(String[] args) {
		//执行分词操作
		Executor.execute(Split.class);
		//执行PageRank计算
		Executor.execute(PageRank.class);
	}
}
