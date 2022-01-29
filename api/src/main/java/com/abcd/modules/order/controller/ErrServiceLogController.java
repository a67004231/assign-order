package com.abcd.modules.order.controller;

import com.abcd.common.utils.PageUtil;
import com.abcd.common.utils.ResultUtil;
import com.abcd.common.vo.PageVo;
import com.abcd.common.vo.Result;
import com.abcd.modules.order.entity.ErrServiceLog;
import com.abcd.modules.order.service.IErrServiceLogService;
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
@Api(description = "异常运营商订单日志管理接口")
@RequestMapping("/api/errServiceLog")
@Transactional
public class ErrServiceLogController {

    @Autowired
    private IErrServiceLogService iErrServiceLogService;

    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "通过id获取")
    public Result<ErrServiceLog> get(@PathVariable Long id){

        ErrServiceLog errServiceLog = iErrServiceLogService.getById(id);
        return new ResultUtil<ErrServiceLog>().setData(errServiceLog);
    }

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    @ApiOperation(value = "获取全部数据")
    public Result<List<ErrServiceLog>> getAll(){

        List<ErrServiceLog> list = iErrServiceLogService.list();
        return new ResultUtil<List<ErrServiceLog>>().setData(list);
    }

    @RequestMapping(value = "/getByPage", method = RequestMethod.GET)
    @ApiOperation(value = "分页获取")
    public Result<IPage<ErrServiceLog>> getByPage(@ModelAttribute PageVo page){

        IPage<ErrServiceLog> data = iErrServiceLogService.page(PageUtil.initMpPage(page));
        return new ResultUtil<IPage<ErrServiceLog>>().setData(data);
    }

    @RequestMapping(value = "/insertOrUpdate", method = RequestMethod.POST)
    @ApiOperation(value = "编辑或更新数据")
    public Result<ErrServiceLog> saveOrUpdate(@ModelAttribute ErrServiceLog errServiceLog){

        if(iErrServiceLogService.saveOrUpdate(errServiceLog)){
            return new ResultUtil<ErrServiceLog>().setData(errServiceLog);
        }
        return new ResultUtil<ErrServiceLog>().setErrorMsg("操作失败");
    }

    @RequestMapping(value = "/delByIds/{ids}", method = RequestMethod.DELETE)
    @ApiOperation(value = "批量通过id删除")
    public Result<Object> delAllByIds(@PathVariable Long[] ids){

        for(Long id : ids){
            iErrServiceLogService.removeById(id);
        }
        return ResultUtil.success("批量通过id删除数据成功");
    }
}
