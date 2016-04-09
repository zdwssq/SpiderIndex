package com.bestsonic.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bestsonic.domain.Keyword;

public interface KeywordMapper {

	public void insert(Keyword Keyword);

	public void insertByList(List<Keyword> Keyword);

	public void update(Keyword Keyword);

	public void updateByList(List<Keyword> Keyword);

	public void delete(Keyword Keyword);

	public void deleteById(@Param("id") int id);

	public void deleteByList(List<Keyword> Keyword);

	public void deleteByIds(List<Integer> ids);

	public Keyword select(@Param("id") int id);

	public List<Keyword> selectByIds(List<Integer> ids);

	public List<Keyword> selectAll();

	public List<Keyword> selectPage(@Param("start") int start, @Param("length") int length);

	public List<String> getKeywords(@Param("keyword") String keyword);

}
