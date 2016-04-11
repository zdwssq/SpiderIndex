package com.bestsonic.spider.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import com.bestsonic.mapper.WebPageMapper;
import com.bestsonic.spider.Job;
import com.bestsonic.spider.utils.DBUtils;
import com.bestsonic.spider.utils.StreamUtils;

/**
 * 从配置文件中读出爬取开始的位置
 * @author zheng
 */
public class Generate implements Job {
	private final static Logger LOG = Logger.getLogger(Generate.class);

	private final static Job job = new Generate();

	private Generate() {
	}

	public static Job getInstance() {
		return job;
	}

	@Override
	public void run() {
		SqlSession session = null;
		try {
			session = DBUtils.getSession();
			WebPageMapper mapper = session.getMapper(WebPageMapper.class);
			// 为每条记录产生一个batchId记录，为当前的时间值
			long batchId = System.currentTimeMillis();
			// 从数据库中读入需要爬取的Url记录，并判断batchId，如果存在batchId，则不爬取。

			List<Integer> ids = mapper.selectForGenerate((batchId - 1000 * 60 * 60 * 24));
			LOG.debug("Generate过程 - 查询出需要爬取的记录Id：" + ids);
			if (!ids.isEmpty()) {
				mapper.updateBatchId(batchId, ids);
				LOG.debug("Generate过程 - 增加batchId：" + batchId);
			}
			session.commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (session != null) session.rollback();
		} finally {
			StreamUtils.close(session);
		}
	}

}
