package com.abcd.modules.factor.serviceimpl;

import com.abcd.modules.factor.entity.FactorType;
import com.abcd.modules.factor.service.IFactorTypeService;
import com.abcd.modules.mapper.FactorTypeMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 路由因子类型接口实现
 * @author 
 */
@Slf4j
@Service
@Transactional
public class IFactorTypeServiceImpl extends ServiceImpl<FactorTypeMapper, FactorType> implements IFactorTypeService {

    @Autowired
    private FactorTypeMapper factorTypeMapper;
}