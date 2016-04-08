package com.bestsonic.search.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.log4j.Logger;

import com.bestsonic.domain.WebPage;
import com.bestsonic.spider.Job;
import com.bestsonic.spider.utils.DBUtils;

public class PageRank implements Job {
	private final static Job job = new PageRank();
	private final static Logger LOG = Logger.getLogger(PageRank.class);
	/* 阀值 */  
    public static double MAX = 0.00000000001;  
  
    /* 阻尼系数 */  
    public static double alpha = 0.85;  
    public static double[] init;  
    
    public static double[] pr;  
	private PageRank(){}
	
	public static Job getInstance(){
		return job;
	}

	
	
    /** 
     * 计算pagerank 
     *  
     * @param init 
     * @param alpho 
     * @return 
     */  
    private static double[] doPageRank(HashMap<String,ArrayList<String>> outLinks) {  
        double[] pr = new double[init.length];  
        for (int i = 0; i < init.length; i++) {  
            double temp = 0;
            for (int j = 0; j < init.length; j++) {  
                // 计算对本页面链接相关总值  
            	ArrayList<String> ils = outLinks.get(j+"");
                if (i != j && ils.size() != 0  
                        && ils.contains(i+"")/*he0.getInLinks().contains(he.getPath())*/) {  
                    temp = temp + init[j] / ils.size();  
                }  
            }  
            //经典的pr公式  
            pr[i] = alpha + (1 - alpha) * temp;  
        }  
        return pr;  
    }  
  
    /** 
     * 判断前后两次的pr数组之间的差别是否大于我们定义的阀值 假如大于，那么返回false，继续迭代计算pr 
     *  
     * @param pr 
     * @param init 
     * @param max 
     * @return 
     */  
    private static boolean checkMax() {  
        boolean flag = true;  
        for (int i = 0; i < pr.length; i++) {  
            if (Math.abs(pr[i] - init[i]) > MAX) {  
                flag = false;  
                break;  
            }  
        }  
        return flag;  
    }  
    
	@Override
	public void run() {
		String sql = "SELECT id, inlinks, outlinks FROM webpage ORDER BY id ASC";
		List<WebPage> list = null;
		//TODO 拿到所有的outlinks
		HashMap<String,ArrayList<String>> outLinks = new HashMap<>();
		
		//2.初始化init[i] = 0.0;  
		HashSet<String> key = (HashSet<String>) outLinks.keySet();
		for(int i =0; i < key.size(); i++){
			init[i] = 0.0;
		}
		//3.计算pageRank
		pr = doPageRank(outLinks);  
        while (!(checkMax())) {  
            System.arraycopy(pr, 0, init, 0, init.length);  
            pr = doPageRank(outLinks);  
        }  
        //4.存入pageRank，下标为id，值为rank
	}
}
