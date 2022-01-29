package com.abcd.modules.base.serviceimpl;

import com.abcd.modules.base.entity.Mobile;
import com.abcd.modules.base.service.IMobileService;
import com.abcd.modules.mapper.MobileMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 手机号段信息接口实现
 * @author 
 */
@Slf4j
@Service
@Transactional
public class IMobileServiceImpl extends ServiceImpl<MobileMapper, Mobile> implements IMobileService {

    @Autowired
    private MobileMapper mobileMapper;
}