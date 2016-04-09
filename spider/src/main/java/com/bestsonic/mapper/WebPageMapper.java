package com.bestsonic.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bestsonic.domain.WebPage;

public interface WebPageMapper {

	public void insert(WebPage webpage);

	public void insertByList(List<WebPage> webpage);

	public void update(WebPage webpage);

	public void updateBatchId(@Param("batchId") long batchId, @Param("list") List<Integer> ids);

	public void updateByList(List<WebPage> webpage);

	public void delete(WebPage webpage);

	public void deleteById(@Param("id") int id);

	public void deleteByList(List<WebPage> webpage);

	public void deleteByIds(List<Integer> ids);

	public WebPage select(@Param("id") int id);

	public List<WebPage> selectByIds(List<Integer> ids);

	public List<WebPage> selectAll();

	public List<String> selectAllUrls();

	public List<WebPage> selectPage(@Param("start") int start, @Param("length") int length);

	public List<WebPage> selectForFetch();

	public List<Integer> selectForGenerate(@Param("time") long batchId);

	public List<String> selectExistUrls(List<String> urls);

	public List<String> selectUrl(@Param("url") String url);

	public List<WebPage> selectAllNotNull();

	public List<WebPage> selectLinks();

	public Integer selectId(@Param("start") int i);
}
