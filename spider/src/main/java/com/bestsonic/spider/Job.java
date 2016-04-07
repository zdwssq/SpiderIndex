package com.bestsonic.spider;

/**
 * 任务接口
 * @author Best_
 */
public interface Job {

	void run();
	
	default void run(String[] urls){
		
	};
}
