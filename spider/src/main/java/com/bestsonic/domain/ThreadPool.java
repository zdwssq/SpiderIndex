package com.bestsonic.domain;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
/**
 * 线程池静态类
 * @author Best_
 */
public class ThreadPool {

	public final static ExecutorService EXECUTOR = Executors.newCachedThreadPool();
	
	private ThreadPool(){}
	
	public static Future<?> submitJob(Runnable job){
		return EXECUTOR.submit(job);
	}
	
	public static CloseableHttpClient getClient(){
		return HttpClients.createDefault();
	}
}
