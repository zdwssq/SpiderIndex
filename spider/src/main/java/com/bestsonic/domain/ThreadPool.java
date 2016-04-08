package com.bestsonic.domain;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class ThreadPool {

	public final static ExecutorService EXECUTOR = Executors.newFixedThreadPool(10);
	
	//private final static CloseableHttpClient client = //HttpClients.custom().setConnectionManager(new PoolingHttpClientConnectionManager()).build();
	
	private ThreadPool(){}
	
	public static Future<?> submitJob(Runnable job){
		return EXECUTOR.submit(job);
	}
	
	public static CloseableHttpClient getClient(){
		return HttpClients.createDefault();
	}
	
	public static void joinPool(){
		int i = 0;
		while(true){
			try {
				Thread.sleep(1000);
				System.out.println(++i + "dddddd");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if(EXECUTOR.isTerminated()){
				break;
			}
		}
	}
}
