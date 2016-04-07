package com.bestsonic.spider.utils;

import java.io.IOException;
import java.io.Reader;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

public class DBUtils {
	
	private static SqlSessionFactory factory;
	
	static{
		String resource = "mybatis.xml";
		try {
			Reader reader = Resources.getResourceAsReader(resource);
			factory = new SqlSessionFactoryBuilder().build(reader);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("找不到配置文件!");
		}
	}
	
	public static SqlSession getSession(){
		return factory.openSession();
	}
}
