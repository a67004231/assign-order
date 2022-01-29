package com.abcd.modules.base.controller;

import com.abcd.common.utils.PageUtil;
import com.abcd.common.utils.ResultUtil;
import com.abcd.common.vo.PageVo;
import com.abcd.common.vo.Result;
import com.abcd.modules.base.entity.Mobile;
import com.abcd.modules.base.service.IMobileService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author 
 */
@Slf4j
@RestController
@Api(description = "手机号段信息管理接口")
@RequestMapping("/api/mobile")
@Transactional
public class MobileController {

    @Autowired
    private IMobileService iMobileService;

    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "通过id获取")
    public Result<Mobile> get(@PathVariable Long id){

        Mobile mobile = iMobileService.getById(id);
        return new ResultUtil<Mobile>().setData(mobile);
    }

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    @ApiOperation(value = "获取全部数据")
    public Result<List<Mobile>> getAll(){

        List<Mobile> list = iMobileService.list();
        return new ResultUtil<List<Mobile>>().setData(list);
    }

    @RequestMapping(value = "/getByPage", method = RequestMethod.GET)
    @ApiOperation(value = "分页获取")
    public Result<IPage<Mobile>> getByPage(@ModelAttribute PageVo page){

        IPage<Mobile> data = iMobileService.page(PageUtil.initMpPage(page));
        return new ResultUtil<IPage<Mobile>>().setData(data);
    }

    @RequestMapping(value = "/insertOrUpdate", method = RequestMethod.POST)
    @ApiOperation(value = "编辑或更新数据")
    public Result<Mobile> saveOrUpdate(@ModelAttribute Mobile mobile){

        if(iMobileService.saveOrUpdate(mobile)){
            return new ResultUtil<Mobile>().setData(mobile);
        }
        return new ResultUtil<Mobile>().setErrorMsg("操作失败");
    }

    @RequestMapping(value = "/delByIds/{ids}", method = RequestMethod.DELETE)
    @ApiOperation(value = "批量通过id删除")
    public Result<Object> delAllByIds(@PathVariable Long[] ids){

        for(Long id : ids){
            iMobileService.removeById(id);
        }
        return ResultUtil.success("批量通过id删除数据成功");
    }
}
