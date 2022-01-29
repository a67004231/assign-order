package com.abcd.modules.channel.serviceimpl;

import com.abcd.modules.channel.entity.ChannelSettingInfo;
import com.abcd.modules.channel.service.IChannelSettingInfoService;
import com.abcd.modules.mapper.ChannelSettingInfoMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 通道配置接口实现
 * @author 
 */
@Slf4j
@Service
@Transactional
public class IChannelSettingInfoServiceImpl extends ServiceImpl<ChannelSettingInfoMapper, ChannelSettingInfo> implements IChannelSettingInfoService {

    @Autowired
    private ChannelSettingInfoMapper channelSettingInfoMapper;
}