package com.abcd.modules.merchant.serviceimpl;

import com.abcd.modules.mapper.MerAccountLogMapper;
import com.abcd.modules.merchant.entity.MerAccountLog;
import com.abcd.modules.merchant.service.IMerAccountLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 商户账户信息日志接口实现
 * @author 
 */
@Slf4j
@Service
@Transactional
public class IMerAccountLogServiceImpl extends ServiceImpl<MerAccountLogMapper, MerAccountLog> implements IMerAccountLogService {

    @Autowired
    private MerAccountLogMapper merAccountLogMapper;
}