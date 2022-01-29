package com.abcd.modules.order.serviceimpl;

import com.abcd.modules.mapper.ChannelOrderInfoMapper;
import com.abcd.modules.order.entity.ChannelOrderInfo;
import com.abcd.modules.order.service.IChannelOrderInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 通道订单接口实现
 * @author 
 */
@Slf4j
@Service
@Transactional
public class IChannelOrderInfoServiceImpl extends ServiceImpl<ChannelOrderInfoMapper, ChannelOrderInfo> implements IChannelOrderInfoService {

    @Autowired
    private ChannelOrderInfoMapper channelOrderInfoMapper;
}