package com.abcd.modules.factor.serviceimpl;

import com.abcd.modules.factor.entity.FactorLog;
import com.abcd.modules.factor.service.IFactorLogService;
import com.abcd.modules.mapper.FactorLogMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 因子计算日志接口实现
 * @author 
 */
@Slf4j
@Service
@Transactional
public class IFactorLogServiceImpl extends ServiceImpl<FactorLogMapper, FactorLog> implements IFactorLogService {

    @Autowired
    private FactorLogMapper factorLogMapper;
}