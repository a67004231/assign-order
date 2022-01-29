package com.abcd.modules.order.controller;

import com.abcd.common.utils.PageUtil;
import com.abcd.common.utils.ResultUtil;
import com.abcd.common.vo.PageVo;
import com.abcd.common.vo.Result;
import com.abcd.modules.order.entity.OrderSetting;
import com.abcd.modules.order.service.IOrderSettingService;
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
@Api(description = "商户进单设置管理接口")
@RequestMapping("/api/orderSetting")
@Transactional
public class OrderSettingController {

    @Autowired
    private IOrderSettingService iOrderSettingService;

    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "通过id获取")
    public Result<OrderSetting> get(@PathVariable Long id){

        OrderSetting orderSetting = iOrderSettingService.getById(id);
        return new ResultUtil<OrderSetting>().setData(orderSetting);
    }

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    @ApiOperation(value = "获取全部数据")
    public Result<List<OrderSetting>> getAll(){

        List<OrderSetting> list = iOrderSettingService.list();
        return new ResultUtil<List<OrderSetting>>().setData(list);
    }

    @RequestMapping(value = "/getByPage", method = RequestMethod.GET)
    @ApiOperation(value = "分页获取")
    public Result<IPage<OrderSetting>> getByPage(@ModelAttribute PageVo page){

        IPage<OrderSetting> data = iOrderSettingService.page(PageUtil.initMpPage(page));
        return new ResultUtil<IPage<OrderSetting>>().setData(data);
    }

    @RequestMapping(value = "/insertOrUpdate", method = RequestMethod.POST)
    @ApiOperation(value = "编辑或更新数据")
    public Result<OrderSetting> saveOrUpdate(@ModelAttribute OrderSetting orderSetting){

        if(iOrderSettingService.saveOrUpdate(orderSetting)){
            return new ResultUtil<OrderSetting>().setData(orderSetting);
        }
        return new ResultUtil<OrderSetting>().setErrorMsg("操作失败");
    }

    @RequestMapping(value = "/delByIds/{ids}", method = RequestMethod.DELETE)
    @ApiOperation(value = "批量通过id删除")
    public Result<Object> delAllByIds(@PathVariable Long[] ids){

        for(Long id : ids){
            iOrderSettingService.removeById(id);
        }
        return ResultUtil.success("批量通过id删除数据成功");
    }
}
