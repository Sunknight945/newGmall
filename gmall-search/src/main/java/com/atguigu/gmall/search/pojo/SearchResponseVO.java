package com.atguigu.gmall.search.pojo;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Only when I saw the eyes from mirror, I realized that's me.
 */
@Data
public class SearchResponseVO implements Serializable {
	//品牌, 此时vo对象中的id字段保留(不用写) name 就是"品牌" value : [{id:1000, name:华为,logo:xxx},{xx}]
	private SearchResponseAttrVO brand;
	private SearchResponseAttrVO catelog; //分类
	private List<SearchResponseAttrVO> attrs = new ArrayList<>();

	private List<Goods> products = new ArrayList<>();

	private Long total; //总记录数
	private Integer pageSize; //每页显示的内容
	private Integer pageNum; //当前页面

}


