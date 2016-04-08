package com.bestsonic.domain;

import java.io.IOException;
import java.io.InputStream;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.bestsonic.mapper.WebPageMapper;
import com.bestsonic.spider.impl.Fetch;
import com.bestsonic.spider.impl.Filter;
import com.bestsonic.spider.impl.Parse;
import com.bestsonic.spider.utils.DBUtils;
import com.bestsonic.spider.utils.StreamUtils;

public class FetchTask implements Runnable {
	private final static Logger LOG = Logger.getLogger(Fetch.class);
	private WebPage webpage;
	private CountDownLatch latch;

	public FetchTask(WebPage webpage, CountDownLatch latch) {
		this.webpage = webpage;
		this.latch = latch;
	}

	@Override
	public void run() {
		SqlSession session = null;
		try {
			session = DBUtils.getSession();
			WebPageMapper mapper = session.getMapper(WebPageMapper.class);
			LOG.debug(Thread.currentThread() + "Fetch过程 - 线程池执行爬取方法!");
			// 获取爬取时间
			long fetchTime = System.currentTimeMillis();

			// 从webpage对象中获取url
			String url = webpage.getBaseUrl();
			// 利用HttpClient或URL进行爬取
			HttpGet get = new HttpGet(url);
			get.addHeader("User-Agent",
					"Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.80 Safari/537.36");
			get.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
			get.addHeader("Referer", "http://news.163.com");
			get.addHeader("Upgrade-Insecure-Requests", "1");
			HttpResponse response = ThreadPool.getClient().execute(get);
			// 状态码
			webpage.setStatus(Integer.valueOf(response.getStatusLine().getStatusCode()));

			// 响应头
			StringBuilder sb = new StringBuilder();
			for (Header header : response.getAllHeaders()) {
				sb.append(header.getName() + ":" + header.getValue() + "\n");
			}
			webpage.setHeaders(sb.toString());

			InputStream in = response.getEntity().getContent();

			// Html文本
			String content = Parse.getContent(in, "GBK");
			webpage.setContent(content);
			// 正文文本
			Document root = Jsoup.parse(content);
			String text = Parse.getText(root);
			webpage.setText(text);
			// Html标题
			String title = Parse.getTitle(root);
			webpage.setTitle(title);

			// 获取outlinks
			String[] ourls = Parse.getOutLinks(root);
			Set<String> urls = Filter.filter(ourls);
			/*urls.removeAll(Fetch.allUrls);
			Fetch.allUrls.addAll(urls);*/
			// urls.remove(webpage.getBaseUrl());
			// 为每个outlinks(已存在则增加inlinks)创建webpage对象(baseUrl和parentUrl,
			// inlinks)，并存入数据库，获得id，如果outlinks在数据库中已存在，则更新inlinks。
			StringBuilder outlinks = new StringBuilder();
			for (String baseUrl : urls) {
				String webpage_url = mapper.selectUrl(baseUrl);
				if(webpage_url != null)
					continue;
				
				WebPage page = new WebPage();
				page.setBaseUrl(baseUrl);
				page.setParentUrl(webpage.getBaseUrl());
				LOG.debug("Fetch阶段 - 插入新增页面!");
				LOG.debug(page.getBaseUrl());
				
				mapper.insert(page);
				outlinks.append(page.getId() + ",");
			}
			// 主键返回outlinks
			webpage.setOutlinks(outlinks.toString().substring(0, outlinks.length() - 1));
			// 设置爬取时间
			webpage.setFetchTime(fetchTime);

			// 将webpage结果存入数据库
			mapper.update(webpage);
			System.out.println("before:" + latch.getCount());
			session.commit();
//			latch.countDown();
			System.out.println("after:" + latch.getCount());
		} catch (UnsupportedOperationException | IOException e) {
			e.printStackTrace();
			if (session != null)
				session.rollback();
		} finally {
			latch.countDown();
			StreamUtils.close(session);
		}
	}

}
