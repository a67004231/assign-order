package com.abcd.modules.order.controller;

import com.abcd.common.utils.PageUtil;
import com.abcd.common.utils.ResultUtil;
import com.abcd.common.vo.PageVo;
import com.abcd.common.vo.Result;
import com.abcd.modules.order.entity.ChannelOrderInfo;
import com.abcd.modules.order.service.IChannelOrderInfoService;
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
@Api(description = "通道订单管理接口")
@RequestMapping("/api/channelOrderInfo")
@Transactional
public class ChannelOrderInfoController {

    @Autowired
    private IChannelOrderInfoService iChannelOrderInfoService;

    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "通过id获取")
    public Result<ChannelOrderInfo> get(@PathVariable Long id){

        ChannelOrderInfo channelOrderInfo = iChannelOrderInfoService.getById(id);
        return new ResultUtil<ChannelOrderInfo>().setData(channelOrderInfo);
    }

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    @ApiOperation(value = "获取全部数据")
    public Result<List<ChannelOrderInfo>> getAll(){

        List<ChannelOrderInfo> list = iChannelOrderInfoService.list();
        return new ResultUtil<List<ChannelOrderInfo>>().setData(list);
    }

    @RequestMapping(value = "/getByPage", method = RequestMethod.GET)
    @ApiOperation(value = "分页获取")
    public Result<IPage<ChannelOrderInfo>> getByPage(@ModelAttribute PageVo page){

        IPage<ChannelOrderInfo> data = iChannelOrderInfoService.page(PageUtil.initMpPage(page));
        return new ResultUtil<IPage<ChannelOrderInfo>>().setData(data);
    }

    @RequestMapping(value = "/insertOrUpdate", method = RequestMethod.POST)
    @ApiOperation(value = "编辑或更新数据")
    public Result<ChannelOrderInfo> saveOrUpdate(@ModelAttribute ChannelOrderInfo channelOrderInfo){

        if(iChannelOrderInfoService.saveOrUpdate(channelOrderInfo)){
            return new ResultUtil<ChannelOrderInfo>().setData(channelOrderInfo);
        }
        return new ResultUtil<ChannelOrderInfo>().setErrorMsg("操作失败");
    }

    @RequestMapping(value = "/delByIds/{ids}", method = RequestMethod.DELETE)
    @ApiOperation(value = "批量通过id删除")
    public Result<Object> delAllByIds(@PathVariable Long[] ids){

        for(Long id : ids){
            iChannelOrderInfoService.removeById(id);
        }
        return ResultUtil.success("批量通过id删除数据成功");
    }
}
