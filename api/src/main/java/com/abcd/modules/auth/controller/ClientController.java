package com.abcd.modules.auth.controller;

import com.abcd.common.utils.ResultUtil;
import com.abcd.common.vo.Result;
import com.abcd.modules.auth.entity.Client;
import com.abcd.modules.auth.service.ClientService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * @author 
 */
@Slf4j
@RestController
@Api(description = "客户端管理接口")
@RequestMapping("/api/client")
@Transactional
public class ClientController  {

    @Autowired
    private ClientService clientService;

    @RequestMapping(value = "/getVersion", method = RequestMethod.GET)
    @ApiOperation(value = "获取客户端版本信息")
    public Result<String> getVersion(@RequestParam String appName){
        QueryWrapper<Client> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Client::getName,appName);
        List<Client> list = clientService.getAll();
        Client client = list.get(0);
        return new ResultUtil<String>().setData(client.getVersion());
    }
    @RequestMapping(value = "/getSecretKey", method = RequestMethod.GET)
    @ApiOperation(value = "生成随机secretKey")
    public Result<String> getSecretKey(){

        String secretKey = UUID.randomUUID().toString().replaceAll("-", "");
        return new ResultUtil<String>().setData(secretKey);
    }
    @RequestMapping(value = "/getClient", method = RequestMethod.GET)
    @ApiOperation(value = "获取客户端信息")
    public Result<Client> getClient(@RequestParam String appName){
        List<Client> list = clientService.getAll();
        Client client = list.get(0);
        return new ResultUtil<Client>().setData(client);
    }

}
