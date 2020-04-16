package com.atguigu.gmall.search.service;

import com.atguigu.gmall.search.pojo.SearchParamVO;
import com.atguigu.gmall.search.pojo.SearchResponseVO;

import java.io.IOException;

/**
 * @author Only when I saw the eyes from mirror, I realized that's me.
 */
public interface SearchService {

  /**
   * 前端页面获取后端搜索结果
   * @param searchParamVO
   * @return
   */
  SearchResponseVO search(SearchParamVO searchParamVO) throws IOException;
}


