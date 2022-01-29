package com.abcd.modules.merchant.serviceimpl;

import com.abcd.modules.mapper.MerSecretKeyMapper;
import com.abcd.modules.merchant.entity.MerSecretKey;
import com.abcd.modules.merchant.service.IMerSecretKeyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 商户秘钥信息接口实现
 * @author 
 */
@Slf4j
@Service
@Transactional
public class IMerSecretKeyServiceImpl extends ServiceImpl<MerSecretKeyMapper, MerSecretKey> implements IMerSecretKeyService {

    @Autowired
    private MerSecretKeyMapper merSecretKeyMapper;
}