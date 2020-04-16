package com.atguigu.gmall.wms.service;

import com.atguigu.core.bean.PageVo;
import com.atguigu.core.bean.QueryCondition;
import com.atguigu.gmall.wms.entity.WareInfoEntity;
import com.baomidou.mybatisplus.extension.service.IService;


/**
 * 仓库信息
 *
 * @author uiys
 * @email uiys@Gmall.com
 * @date 2020-03-18 18:05:22
 */
public interface WareInfoService extends IService<WareInfoEntity> {

    PageVo queryPage(QueryCondition params);
}

