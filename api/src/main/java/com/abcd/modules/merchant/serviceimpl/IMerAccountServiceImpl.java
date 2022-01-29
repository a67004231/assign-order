package com.abcd.modules.merchant.serviceimpl;

import com.abcd.modules.mapper.MerAccountMapper;
import com.abcd.modules.merchant.entity.MerAccount;
import com.abcd.modules.merchant.service.IMerAccountService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 商户账户信息接口实现
 * @author 
 */
@Slf4j
@Service
@Transactional
public class IMerAccountServiceImpl extends ServiceImpl<MerAccountMapper, MerAccount> implements IMerAccountService {

    @Autowired
    private MerAccountMapper merAccountMapper;
}