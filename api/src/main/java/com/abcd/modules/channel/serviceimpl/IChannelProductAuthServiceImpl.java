package com.abcd.modules.channel.serviceimpl;

import com.abcd.modules.channel.entity.ChannelProductAuth;
import com.abcd.modules.channel.service.IChannelProductAuthService;
import com.abcd.modules.mapper.ChannelProductAuthMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 通道产品授权信息接口实现
 * @author 
 */
@Slf4j
@Service
@Transactional
public class IChannelProductAuthServiceImpl extends ServiceImpl<ChannelProductAuthMapper, ChannelProductAuth> implements IChannelProductAuthService {

    @Autowired
    private ChannelProductAuthMapper channelProductAuthMapper;
}