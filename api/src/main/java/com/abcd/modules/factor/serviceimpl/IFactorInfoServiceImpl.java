package com.abcd.modules.factor.serviceimpl;

import com.abcd.modules.factor.entity.FactorInfo;
import com.abcd.modules.factor.service.IFactorInfoService;
import com.abcd.modules.mapper.FactorInfoMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 路由因子基础配置信息接口实现
 * @author 
 */
@Slf4j
@Service
@Transactional
public class IFactorInfoServiceImpl extends ServiceImpl<FactorInfoMapper, FactorInfo> implements IFactorInfoService {

    @Autowired
    private FactorInfoMapper factorInfoMapper;
}