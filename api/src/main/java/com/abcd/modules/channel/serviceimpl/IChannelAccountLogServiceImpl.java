package com.abcd.modules.channel.serviceimpl;

import com.abcd.modules.channel.entity.ChannelAccountLog;
import com.abcd.modules.channel.service.IChannelAccountLogService;
import com.abcd.modules.mapper.ChannelAccountLogMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 通道账户日志信息接口实现
 * @author 
 */
@Slf4j
@Service
@Transactional
public class IChannelAccountLogServiceImpl extends ServiceImpl<ChannelAccountLogMapper, ChannelAccountLog> implements IChannelAccountLogService {

    @Autowired
    private ChannelAccountLogMapper channelAccountLogMapper;
}