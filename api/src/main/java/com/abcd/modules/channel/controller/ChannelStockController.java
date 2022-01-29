package com.abcd.modules.channel.controller;

import com.abcd.common.utils.PageUtil;
import com.abcd.common.utils.ResultUtil;
import com.abcd.common.vo.PageVo;
import com.abcd.common.vo.Result;
import com.abcd.modules.channel.entity.ChannelStock;
import com.abcd.modules.channel.service.IChannelStockService;
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
@Api(description = "通道库存管理接口")
@RequestMapping("/api/channelStock")
@Transactional
public class ChannelStockController {

    @Autowired
    private IChannelStockService iChannelStockService;

    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "通过id获取")
    public Result<ChannelStock> get(@PathVariable Long id){

        ChannelStock channelStock = iChannelStockService.getById(id);
        return new ResultUtil<ChannelStock>().setData(channelStock);
    }

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    @ApiOperation(value = "获取全部数据")
    public Result<List<ChannelStock>> getAll(){

        List<ChannelStock> list = iChannelStockService.list();
        return new ResultUtil<List<ChannelStock>>().setData(list);
    }

    @RequestMapping(value = "/getByPage", method = RequestMethod.GET)
    @ApiOperation(value = "分页获取")
    public Result<IPage<ChannelStock>> getByPage(@ModelAttribute PageVo page){

        IPage<ChannelStock> data = iChannelStockService.page(PageUtil.initMpPage(page));
        return new ResultUtil<IPage<ChannelStock>>().setData(data);
    }

    @RequestMapping(value = "/insertOrUpdate", method = RequestMethod.POST)
    @ApiOperation(value = "编辑或更新数据")
    public Result<ChannelStock> saveOrUpdate(@ModelAttribute ChannelStock channelStock){

        if(iChannelStockService.saveOrUpdate(channelStock)){
            return new ResultUtil<ChannelStock>().setData(channelStock);
        }
        return new ResultUtil<ChannelStock>().setErrorMsg("操作失败");
    }

    @RequestMapping(value = "/delByIds/{ids}", method = RequestMethod.DELETE)
    @ApiOperation(value = "批量通过id删除")
    public Result<Object> delAllByIds(@PathVariable Long[] ids){

        for(Long id : ids){
            iChannelStockService.removeById(id);
        }
        return ResultUtil.success("批量通过id删除数据成功");
    }
}
