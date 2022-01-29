package com.abcd.modules.channel.serviceimpl;

import com.abcd.modules.channel.entity.ChannelInfo;
import com.abcd.modules.channel.entity.ChannelMerAuth;
import com.abcd.modules.channel.service.IChannelInfoService;
import com.abcd.modules.mapper.ChannelInfoMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 通道信息接口实现
 * @author 
 */
@Slf4j
@Service
@Transactional
public class IChannelInfoServiceImpl extends ServiceImpl<ChannelInfoMapper, ChannelInfo> implements IChannelInfoService {

    @Autowired
    private ChannelInfoMapper channelInfoMapper;

	@Override
	public List<ChannelInfo> selectByProduct(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return channelInfoMapper.selectByProduct(map);
	}
}