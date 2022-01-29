package com.abcd.modules.channel.controller;

import com.abcd.common.utils.PageUtil;
import com.abcd.common.utils.ResultUtil;
import com.abcd.common.vo.PageVo;
import com.abcd.common.vo.Result;
import com.abcd.modules.channel.entity.MerChannelRoute;
import com.abcd.modules.channel.service.IMerChannelRouteService;
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
@Api(description = "商户通道路由管理接口")
@RequestMapping("/api/merChannelRoute")
@Transactional
public class MerChannelRouteController {

    @Autowired
    private IMerChannelRouteService iMerChannelRouteService;

    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "通过id获取")
    public Result<MerChannelRoute> get(@PathVariable Long id){

        MerChannelRoute merChannelRoute = iMerChannelRouteService.getById(id);
        return new ResultUtil<MerChannelRoute>().setData(merChannelRoute);
    }

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    @ApiOperation(value = "获取全部数据")
    public Result<List<MerChannelRoute>> getAll(){

        List<MerChannelRoute> list = iMerChannelRouteService.list();
        return new ResultUtil<List<MerChannelRoute>>().setData(list);
    }

    @RequestMapping(value = "/getByPage", method = RequestMethod.GET)
    @ApiOperation(value = "分页获取")
    public Result<IPage<MerChannelRoute>> getByPage(@ModelAttribute PageVo page){

        IPage<MerChannelRoute> data = iMerChannelRouteService.page(PageUtil.initMpPage(page));
        return new ResultUtil<IPage<MerChannelRoute>>().setData(data);
    }

    @RequestMapping(value = "/insertOrUpdate", method = RequestMethod.POST)
    @ApiOperation(value = "编辑或更新数据")
    public Result<MerChannelRoute> saveOrUpdate(@ModelAttribute MerChannelRoute merChannelRoute){

        if(iMerChannelRouteService.saveOrUpdate(merChannelRoute)){
            return new ResultUtil<MerChannelRoute>().setData(merChannelRoute);
        }
        return new ResultUtil<MerChannelRoute>().setErrorMsg("操作失败");
    }

    @RequestMapping(value = "/delByIds/{ids}", method = RequestMethod.DELETE)
    @ApiOperation(value = "批量通过id删除")
    public Result<Object> delAllByIds(@PathVariable Long[] ids){

        for(Long id : ids){
            iMerChannelRouteService.removeById(id);
        }
        return ResultUtil.success("批量通过id删除数据成功");
    }
}
