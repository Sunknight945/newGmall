package com.atguigu.gmall.pms.vo;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.atguigu.gmall.pms.entity.ProductAttrValueEntity;
import lombok.Data;

import java.util.List;

/**
 * @author Only when I saw the eyes from mirror, I realized that's me.
 */
@Data
public class BaseAttrVo extends ProductAttrValueEntity {
  // private List<String> valueSelected;

  //重写他的set方法
  public void setValueSelected(List<String> valueSelectedList) {
    if (CollectionUtil.isNotEmpty(valueSelectedList)) {
      this.setAttrValue(StrUtil.join(",", valueSelectedList));
    }
  }

}


