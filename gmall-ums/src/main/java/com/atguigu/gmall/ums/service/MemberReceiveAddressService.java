package com.atguigu.gmall.ums.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.atguigu.gmall.ums.entity.MemberReceiveAddressEntity;
import com.atguigu.core.bean.PageVo;
import com.atguigu.core.bean.QueryCondition;

import java.util.List;


/**
 * 会员收货地址
 *
 * @author uiys
 * @email uiys@gmall.com
 * @date 2020-03-18 17:57:09
 */
public interface MemberReceiveAddressService extends IService<MemberReceiveAddressEntity> {

    PageVo queryPage(QueryCondition params);

	/**
	 * 根据用户id 查询他的收货地址
	 * @param memberId
	 * @return
	 */
	List<MemberReceiveAddressEntity> queryAddressesByUserId(Long memberId);
}

