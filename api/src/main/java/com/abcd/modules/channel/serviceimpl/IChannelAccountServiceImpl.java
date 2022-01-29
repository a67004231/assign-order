package com.abcd.modules.channel.serviceimpl;

import com.abcd.modules.channel.entity.ChannelAccount;
import com.abcd.modules.channel.service.IChannelAccountService;
import com.abcd.modules.mapper.ChannelAccountMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 通道账户信息接口实现
 * @author 
 */
@Slf4j
@Service
@Transactional
public class IChannelAccountServiceImpl extends ServiceImpl<ChannelAccountMapper, ChannelAccount> implements IChannelAccountService {

    @Autowired
    private ChannelAccountMapper channelAccountMapper;
}