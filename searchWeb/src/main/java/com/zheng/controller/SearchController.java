package com.zheng.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bestsonic.domain.Page;
import com.bestsonic.domain.WebPage;
import com.bestsonic.search.utils.RankedList;

import net.sf.json.JSONObject;

/**
 * <p>
 * Description:
 * </p>
 * 
 * @author zheng
 */
@Controller
public class SearchController {

	@RequestMapping("searchRes")
	public @ResponseBody JSONObject search(@RequestParam("keywords") String keywords,
			@RequestParam("currentNum") int currentNum, @RequestParam("pageSize") int pageSize, HttpServletRequest request) {
		String[] keys = keywords.split(" ");
		//pages对象不会为空
		Page<WebPage> pages = RankedList.rankedList(keys, currentNum, pageSize);
		pages.setPageNo(currentNum);
		pages.setKeywords(keywords);
		return JSONObject.fromObject(pages);
	}

	@RequestMapping("searchView")
	public String mainSearch(HttpServletRequest request) {
		return "forward:list.html?" + request.getQueryString();
	}
}
