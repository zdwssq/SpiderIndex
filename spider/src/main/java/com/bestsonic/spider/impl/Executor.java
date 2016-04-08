package com.bestsonic.spider.impl;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.MethodDescriptor;
import java.lang.reflect.InvocationTargetException;

import com.bestsonic.spider.Job;

public class Executor {
	
	public static <T extends Job> void execute(Class<T> clazz){
		Job job = null;
		try {
			BeanInfo info = Introspector.getBeanInfo(clazz);
			MethodDescriptor[] methods = info.getMethodDescriptors();
			for(MethodDescriptor method : methods){
				if("getInstance".equals(method.getName())){
					job = (Job) method.getMethod().invoke(null, null);
				}
			}
			job.run();
		} catch (IntrospectionException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new RuntimeException(e);
		}
		
	}
	
}
