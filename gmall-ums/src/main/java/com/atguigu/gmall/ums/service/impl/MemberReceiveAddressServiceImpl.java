package com.atguigu.gmall.ums.service.impl;

import com.atguigu.core.bean.PageVo;
import com.atguigu.core.bean.Query;
import com.atguigu.core.bean.QueryCondition;
import com.atguigu.gmall.ums.dao.MemberReceiveAddressDao;
import com.atguigu.gmall.ums.entity.MemberReceiveAddressEntity;
import com.atguigu.gmall.ums.service.MemberReceiveAddressService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * @author ovo
 */
@Service("memberReceiveAddressService")
public class MemberReceiveAddressServiceImpl extends ServiceImpl<MemberReceiveAddressDao, MemberReceiveAddressEntity> implements MemberReceiveAddressService {

    @Override
    public PageVo queryPage(QueryCondition params) {
        IPage<MemberReceiveAddressEntity> page = this.page(
                new Query<MemberReceiveAddressEntity>().getPage(params),
          new QueryWrapper<>()
        );

        return new PageVo(page);
    }

    /**
     * 根据用户id 查询他的收货地址
     *
     */
    @Override
    public List<MemberReceiveAddressEntity> queryAddressesByUserId(Long memberId) {
        QueryWrapper<MemberReceiveAddressEntity> queryWrapper = new QueryWrapper<>();
        return this.list(queryWrapper.eq("member_id", memberId));
    }
}
