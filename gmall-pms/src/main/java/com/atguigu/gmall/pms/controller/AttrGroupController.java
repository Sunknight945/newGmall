package com.atguigu.gmall.pms.controller;

import com.atguigu.core.bean.PageVo;
import com.atguigu.core.bean.QueryCondition;
import com.atguigu.core.bean.Resp;
import com.atguigu.gmall.pms.entity.AttrGroupEntity;
import com.atguigu.gmall.pms.service.AttrGroupService;
import com.atguigu.gmall.pms.vo.GroupVo;
import com.atguigu.gmall.pms.vo.ItemGroupVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;


/**
 * 属性分组
 *
 * @author uiys
 * @email uiys@Gmall.com
 * @date 2020-03-19 01:51:33
 */
@Api(tags = "属性分组 管理")
@RestController
@RequestMapping("pms/attrgroup")
public class AttrGroupController {
  @Autowired
  private AttrGroupService attrGroupService;

  	/**
	 *  据spuId 和 cateId 查询组及组下规格参数（带值）
	 */
	@GetMapping("itemGroupVo/{cid}/{spuId}")
  public Resp<List<ItemGroupVo>> queryItemGroupVoBySpuIdAndCateId(@PathVariable("cid")Long cid,@PathVariable("spuId")Long spuId){
	  List<ItemGroupVo> itemGroupVoList = this.attrGroupService.queryItemGroupVoBySpuIdAndCateId(cid,spuId);
	  return Resp.ok(itemGroupVoList);
  }



  /**
   * 据categoryId查 attrGroup 属性分组
   */
  @GetMapping("queryAttrGroupList/{cid}")
  public Resp<List<AttrGroupEntity>> queryAttrGroupListByCategoryId(@PathVariable("cid")Long cid){
    List<AttrGroupEntity> attrGroupEntityList =this.attrGroupService.queryAttrGroupListByCategoryId(cid);
    return Resp.ok(attrGroupEntityList);
  }


  @GetMapping("withattrs/cat/{catId}")
  public Resp<List<GroupVo>> queryAttrGroupWithAttrByCid(@PathVariable("catId") Long cid) {
    List<GroupVo> groupVoList = this.attrGroupService.queryAttrGroupWithAttrByCid(cid);
   return Resp.ok(groupVoList);
  }


  @GetMapping("withattr/{gid}")
  public Resp<GroupVo> queryGroupWithAttrsByGid(@PathVariable("gid") Long gid) {
    GroupVo groupVo = this.attrGroupService.queryGroupWithAttrsByGid(gid);
    return Resp.ok(groupVo);
  }
  //127.0.0.1:8888/pms/attrgroup/withattr/1


  @GetMapping("{catId}")
  public Resp<PageVo> queryGroupByPage(QueryCondition queryCondition, @PathVariable("catId") Long catId) {
    PageVo page = attrGroupService.queryGroupByPage(queryCondition, catId);
    return Resp.ok(page);
  }


  /**
   * 列表
   */
  @ApiOperation("分页查询(排序)")
  @GetMapping("/list")
  @PreAuthorize("hasAuthority('pms:attrgroup:list')")

  public Resp<PageVo> list(QueryCondition queryCondition) {
    PageVo page = attrGroupService.queryPage(queryCondition);

    return Resp.ok(page);
  }


  /**
   * 信息
   */
  @ApiOperation("详情查询")
  @GetMapping("/info/{attrGroupId}")
  @PreAuthorize("hasAuthority('pms:attrgroup:info')")
  public Resp<AttrGroupEntity> info(@PathVariable("attrGroupId") Long attrGroupId) {
    AttrGroupEntity attrGroup = attrGroupService.getById(attrGroupId);

    return Resp.ok(attrGroup);
  }

  /**
   * 保存
   */
  @ApiOperation("保存")
  @PostMapping("/save")
  @PreAuthorize("hasAuthority('pms:attrgroup:save')")
  public Resp<Object> save(@RequestBody AttrGroupEntity attrGroup) {
    attrGroupService.save(attrGroup);

    return Resp.ok(null);
  }

  /**
   * 修改
   */
  @ApiOperation("修改")
  @PostMapping("/update")
  @PreAuthorize("hasAuthority('pms:attrgroup:update')")
  public Resp<Object> update(@RequestBody AttrGroupEntity attrGroup) {
    attrGroupService.updateById(attrGroup);

    return Resp.ok(null);
  }

  /**
   * 删除
   */
  @ApiOperation("删除")
  @PostMapping("/delete")
  @PreAuthorize("hasAuthority('pms:attrgroup:delete')")
  public Resp<Object> delete(@RequestBody Long[] attrGroupIds) {
    attrGroupService.removeByIds(Arrays.asList(attrGroupIds));

    return Resp.ok(null);
  }

}
