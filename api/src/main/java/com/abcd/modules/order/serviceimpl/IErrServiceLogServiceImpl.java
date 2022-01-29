package com.abcd.modules.order.serviceimpl;

import com.abcd.modules.mapper.ErrServiceLogMapper;
import com.abcd.modules.order.entity.ErrServiceLog;
import com.abcd.modules.order.service.IErrServiceLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 异常运营商订单日志接口实现
 * @author 
 */
@Slf4j
@Service
@Transactional
public class IErrServiceLogServiceImpl extends ServiceImpl<ErrServiceLogMapper, ErrServiceLog> implements IErrServiceLogService {

    @Autowired
    private ErrServiceLogMapper errServiceLogMapper;
}