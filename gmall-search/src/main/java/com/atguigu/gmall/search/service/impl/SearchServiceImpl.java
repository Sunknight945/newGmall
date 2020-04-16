package com.atguigu.gmall.search.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.atguigu.gmall.search.pojo.Goods;
import com.atguigu.gmall.search.pojo.SearchParamVO;
import com.atguigu.gmall.search.pojo.SearchResponseAttrVO;
import com.atguigu.gmall.search.pojo.SearchResponseVO;
import com.atguigu.gmall.search.service.SearchService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.nested.ParsedNested;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedLongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Only when I saw the eyes from mirror, I realized that's me.
 */
@Service
public class SearchServiceImpl implements SearchService {

	@Autowired
	private RestHighLevelClient restHighLevelClient;

	public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

	/**
	 * 前端页面获取后端搜索结果
	 *
	 * @param searchParamVO
	 * @return
	 */
	@Override
	public SearchResponseVO search(SearchParamVO searchParamVO) throws IOException {
		//构建查询条件
		SearchRequest searchRequest = this.buildQueryDsl(searchParamVO);
		System.out.println(searchRequest);
		SearchResponse response = this.restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
		SearchResponseVO searchResponseVO = this.parseHandleResponse(response);
		searchResponseVO.setPageSize(searchParamVO.getPageSize());
		searchResponseVO.setPageNum(searchParamVO.getPageNum());
		System.out.println(searchParamVO);
		return searchResponseVO;
	}

	private SearchResponseVO parseHandleResponse(SearchResponse response) throws JsonProcessingException {
		SearchResponseVO searchResponseVO = new SearchResponseVO();
		SearchHits hits = response.getHits();
		searchResponseVO.setTotal(hits.totalHits);
		SearchHit[] realHits = hits.getHits();
		List<Goods> goodsList = new ArrayList<>();
		for (SearchHit realHit : realHits) {
			Goods goods = OBJECT_MAPPER.readValue(realHit.getSourceAsString().toString(), new TypeReference<Goods>() {
			});
			goods.setTitle(realHit.getHighlightFields().get("title").getFragments()[0].toString());
			goodsList.add(goods);
		}
		searchResponseVO.setProducts(goodsList);
		//总聚合
		Map<String, Aggregation> totalAggregation = response.getAggregations().asMap();
		//从总聚合获取 品牌聚合
		ParsedLongTerms brandIdAgg = (ParsedLongTerms) totalAggregation.get("brandIdAgg");
		List<String> brandValues = brandIdAgg.getBuckets().stream().map(bucket -> {
			Map<String, String> map = new HashMap<>();
			map.put("id", ((Terms.Bucket) bucket).getKeyAsString());
			ParsedStringTerms brandNameAgg = ((Terms.Bucket) bucket).getAggregations().get("brandNameAgg");
			map.put("name", brandNameAgg.getBuckets().get(0).getKeyAsString());
			try {
				return OBJECT_MAPPER.writeValueAsString(map);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
			return null;
		}).collect(Collectors.toList());
		SearchResponseAttrVO brand = new SearchResponseAttrVO();
		brand.setName("品牌");
		brand.setValue(brandValues);
		searchResponseVO.setBrand(brand);

		//从总聚合获取 分类聚合
		ParsedLongTerms categoryIdAgg = (ParsedLongTerms) totalAggregation.get("categoryIdAgg");
		List<String> categoryValues = categoryIdAgg.getBuckets().stream().map(bucket -> {
			Map<String, String> map = new HashMap<>();
			map.put("id", ((Terms.Bucket) bucket).getKeyAsString());
			ParsedStringTerms categoryNameAgg = ((Terms.Bucket) bucket).getAggregations().get("categoryNameAgg");
			map.put("name", categoryNameAgg.getBuckets().get(0).getKeyAsString());
			try {
				return OBJECT_MAPPER.writeValueAsString(map);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
			return null;
		}).collect(Collectors.toList());
		SearchResponseAttrVO category = new SearchResponseAttrVO();
		category.setName("分类");
		category.setValue(categoryValues);
		searchResponseVO.setCatelog(category);

		//从总聚合获取 属性聚合
		ParsedNested attrAgg = (ParsedNested) totalAggregation.get("attrAgg");
		ParsedLongTerms attrIdAgg = attrAgg.getAggregations().get("attrIdAgg");
		List<? extends Terms.Bucket> attrIdAggBuckets = attrIdAgg.getBuckets();
		if (CollectionUtil.isNotEmpty(attrIdAggBuckets)) {
			List<SearchResponseAttrVO> searchResponseAttrVOList = attrIdAggBuckets.stream().map(attrIdAggBucket -> {
				SearchResponseAttrVO attrVO = new SearchResponseAttrVO();
				attrVO.setProductAttributeId(((Terms.Bucket) attrIdAggBucket).getKeyAsNumber().longValue());
				ParsedStringTerms attrNameAgg = ((Terms.Bucket) attrIdAggBucket).getAggregations().get("attrNameAgg");
				attrVO.setName(attrNameAgg.getBuckets().get(0).getKeyAsString());
				ParsedStringTerms attrValueAgg = ((Terms.Bucket) attrIdAggBucket).getAggregations().get("attrValueAgg");
				List<? extends Terms.Bucket> attrValueAggBuckets = attrValueAgg.getBuckets();
				if (CollectionUtil.isNotEmpty(attrValueAggBuckets)) {
					List<String> attrValues = attrValueAggBuckets.stream().map(Terms.Bucket::getKeyAsString).collect(Collectors.toList());
					attrVO.setValue(attrValues);
				}
				return attrVO;
			}).collect(Collectors.toList());
			searchResponseVO.setAttrs(searchResponseAttrVOList);
		}
		return searchResponseVO;
	}

	/**
	 * 构建查询条件
	 *
	 * @param searchParamVO
	 * @return
	 */
	private SearchRequest buildQueryDsl(SearchParamVO searchParamVO) {
		SearchRequest searchRequest = new SearchRequest();
		SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		String keyword = searchParamVO.getKeyword();
		if (StrUtil.isEmpty(keyword)) {
			return null;
		}
		boolQueryBuilder.must(QueryBuilders.matchQuery("title", keyword).operator(Operator.AND));
		if (searchParamVO.getBrand() != null) {
			boolQueryBuilder.filter(QueryBuilders.termsQuery("brandId", searchParamVO.getBrand()));
		}
		if (searchParamVO.getCatelog3() != null) {
			boolQueryBuilder.filter(QueryBuilders.termsQuery("categoryId", searchParamVO.getCatelog3()));
		}
		//2:win10-android-
		//3:4g
		//4:5.5
		String[] props = searchParamVO.getProps();
		if (props != null && props.length > 0) {
			for (String prop : props) {
				String[] split = StrUtil.split(prop, ":");
				if (split == null || split.length != 2) {
					continue;
				}
				String[] attrValue = StrUtil.split(split[1], "-");
				BoolQueryBuilder queryBuild = QueryBuilders.boolQuery();
				queryBuild.must(QueryBuilders.termQuery("attrs.attrId", split[0]));
				queryBuild.must(QueryBuilders.termsQuery("attrs.attrValue", attrValue));
				boolQueryBuilder.filter(QueryBuilders.nestedQuery("attrs", queryBuild, ScoreMode.None));
			}
		}
		// if (props != null && props.length != 0) {
		// 	for (String prop : props) {
		// 		String[] split = StrUtil.split(prop, ":");
		// 		String id = split[0];
		// 		String[] attrValues = StrUtil.split(split[1], "-");
		// 		BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery().must(QueryBuilders.termQuery("attrs.attrId", id)).must(QueryBuilders.termsQuery("attrs.attrValue", attrValues));
		// 		boolQueryBuilder.filter(QueryBuilders.boolQuery().must(QueryBuilders.nestedQuery("attrs", queryBuilder, ScoreMode.None)));
		// 	}
		// }

		Integer pageNum = searchParamVO.getPageNum();
		Integer pageSize = searchParamVO.getPageSize();
		if (pageNum != null && pageSize != null) {
			sourceBuilder.from((pageNum - 1) * pageSize);
			sourceBuilder.size(pageSize);
		}

		// order=1:asc  排序规则   0:asc
		// private String order;// 0：综合排序  1：销量  2：价格
		String order = searchParamVO.getOrder();
		if (order != null) {
			String[] orderA = StrUtil.split(order, ":");
			String orderField = "sale";
			switch (orderA[0]) {
				case "1":
					orderField = "sale";
					break;
				case "2":
					orderField = "price";
					break;
			}
			sourceBuilder.sort(orderField, StrUtil.equals("asc", orderA[1]) ? SortOrder.ASC : SortOrder.DESC);
		}
		//高亮
		sourceBuilder.highlighter(new HighlightBuilder().field("title").preTags("<em>").postTags("</em>"));

		//三个聚合  品牌  分类  属性
		sourceBuilder.aggregation(AggregationBuilders.terms("brandIdAgg").field("brandId")
			                          .subAggregation(AggregationBuilders.terms("brandNameAgg").field("brandName")));
		sourceBuilder.aggregation(AggregationBuilders.terms("categoryIdAgg").field("categoryId")
			                          .subAggregation(AggregationBuilders.terms("categoryNameAgg").field("categoryName")));

		sourceBuilder.aggregation(AggregationBuilders.nested("attrAgg", "attrs")
			                          .subAggregation(AggregationBuilders.terms("attrIdAgg").field("attrs.attrId")
				                                          .subAggregation(AggregationBuilders.terms("attrNameAgg").field("attrs.attrName"))
				                                          .subAggregation(AggregationBuilders.terms("attrValueAgg").field("attrs.attrValue"))));
		sourceBuilder.fetchSource(new String[]{"skuId", "pic", "title", "price"}, null);
		sourceBuilder.query(boolQueryBuilder);
		System.out.println(sourceBuilder.toString());
		searchRequest.source(sourceBuilder);
		return searchRequest;
	}
}


