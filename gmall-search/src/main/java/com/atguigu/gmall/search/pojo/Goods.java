package com.atguigu.gmall.search.pojo;

import com.sun.javafx.beans.IDProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author Only when I saw the eyes from mirror, I realized that's me.
 * 搜索 ElasticSearch 需要的商品实体类
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "goods", shards = 3, replicas = 2)
public class Goods {
  //搜索商品需要构建的字段 有:
  // 图片地址  标题, 副标题 >>>
  @Id
  private Long skuId;
  @Field(type = FieldType.Keyword, index = false)
  private String pic;
  @Field(type = FieldType.Text, analyzer = "ik_max_word")
  private String title;
  @Field(type = FieldType.Double)
  private Double price;
  @Field(type = FieldType.Long)
  private Long sale; //销量
  @Field(type = FieldType.Boolean)
  private Boolean store; //剩多少货
  @Field(type = FieldType.Date)
  private Date createTime; //新品排序
  @Field(type = FieldType.Long)
  private Long brandId;
  @Field(type = FieldType.Keyword)
  private String brandName;
  @Field(type = FieldType.Long)
  private Long categoryId;
  @Field(type = FieldType.Keyword)
  private String categoryName;
  @Field(type = FieldType.Nested)  //嵌套
  private List<SearchAttr> attrs;

}


