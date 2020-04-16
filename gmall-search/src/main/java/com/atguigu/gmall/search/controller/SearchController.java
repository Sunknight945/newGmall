package com.atguigu.gmall.search.controller;

import com.atguigu.core.bean.Resp;
import com.atguigu.gmall.search.pojo.SearchParamVO;
import com.atguigu.gmall.search.pojo.SearchResponseVO;
import com.atguigu.gmall.search.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * @author Only when I saw the eyes from mirror, I realized that's me.
 */
@RestController
@RequestMapping("search")
public class SearchController {

	@Autowired
	private SearchService searchService;


	@GetMapping
	public Resp<SearchResponseVO> search(SearchParamVO searchParamVO) throws IOException {
	SearchResponseVO responseVO =	this.searchService.search(searchParamVO);
		return Resp.ok(responseVO);
	}


}


