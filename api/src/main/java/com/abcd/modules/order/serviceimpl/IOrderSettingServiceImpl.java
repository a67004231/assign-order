package com.abcd.modules.order.serviceimpl;

import com.abcd.modules.mapper.OrderSettingMapper;
import com.abcd.modules.order.entity.OrderSetting;
import com.abcd.modules.order.service.IOrderSettingService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 商户进单设置接口实现
 * @author 
 */
@Slf4j
@Service
@Transactional
public class IOrderSettingServiceImpl extends ServiceImpl<OrderSettingMapper, OrderSetting> implements IOrderSettingService {

    @Autowired
    private OrderSettingMapper orderSettingMapper;
}