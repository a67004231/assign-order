package com.abcd.modules.channel.serviceimpl;

import com.abcd.modules.channel.entity.ChannelStock;
import com.abcd.modules.channel.service.IChannelStockService;
import com.abcd.modules.mapper.ChannelStockMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 通道库存接口实现
 * @author 
 */
@Slf4j
@Service
@Transactional
public class IChannelStockServiceImpl extends ServiceImpl<ChannelStockMapper, ChannelStock> implements IChannelStockService {

    @Autowired
    private ChannelStockMapper channelStockMapper;
}