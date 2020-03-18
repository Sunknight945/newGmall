package com.atguigu.gmall.ums.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.atguigu.gmall.ums.entity.MemberEntity;
import com.atguigu.core.bean.PageVo;
import com.atguigu.core.bean.QueryCondition;


/**
 * 会员
 *
 * @author uiys
 * @email uiys@gmall.com
 * @date 2020-03-18 17:57:10
 */
public interface MemberService extends IService<MemberEntity> {

    PageVo queryPage(QueryCondition params);
}

