package com.atguigu.gmall.pms.service;

import com.atguigu.gmall.pms.entity.SpuInfoDescEntity;
import com.atguigu.gmall.pms.vo.SpuInfoVo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.atguigu.core.bean.PageVo;
import com.atguigu.core.bean.QueryCondition;


/**
 * spu信息介绍
 *
 * @author uiys
 * @email uiys@Gmall.com
 * @date 2020-03-19 01:51:33
 */
public interface SpuInfoDescService extends IService<SpuInfoDescEntity> {

    PageVo queryPage(QueryCondition params);

    /**
     * 1.2.保存spm_spu_info_desc 描述信息
     *
     * @param spuInfoVo
     * @param spuId
     */
    void saveSpuInfoDesc(SpuInfoVo spuInfoVo, Long spuId);
}

