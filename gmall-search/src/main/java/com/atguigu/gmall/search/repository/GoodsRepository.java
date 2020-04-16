package com.atguigu.gmall.search.repository;

import com.atguigu.gmall.search.pojo.Goods;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @author Only when I saw the eyes from mirror, I realized that's me.
 */
public interface GoodsRepository extends ElasticsearchRepository<Goods, Long> {
}
