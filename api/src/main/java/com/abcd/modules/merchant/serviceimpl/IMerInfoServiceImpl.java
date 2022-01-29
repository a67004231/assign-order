package com.abcd.modules.merchant.serviceimpl;

import com.abcd.modules.mapper.MerInfoMapper;
import com.abcd.modules.merchant.entity.MerInfo;
import com.abcd.modules.merchant.service.IMerInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 商户信息接口实现
 * @author 
 */
@Slf4j
@Service
@Transactional
public class IMerInfoServiceImpl extends ServiceImpl<MerInfoMapper, MerInfo> implements IMerInfoService {

    @Autowired
    private MerInfoMapper merInfoMapper;
}