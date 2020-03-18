package com.atguigu.gmall.pms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.atguigu.gmall.pms.entity.SpuImagesEntity;
import com.atguigu.core.bean.PageVo;
import com.atguigu.core.bean.QueryCondition;


/**
 * spu图片
 *
 * @author uiys
 * @email uiys@Gmall.com
 * @date 2020-03-18 03:23:02
 */
public interface SpuImagesService extends IService<SpuImagesEntity> {

  PageVo queryPage(QueryCondition params);
}

