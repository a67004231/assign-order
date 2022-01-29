package com.abcd.modules.channel.serviceimpl;

import com.abcd.modules.channel.entity.ChannelMerAuth;
import com.abcd.modules.channel.service.IChannelMerAuthService;
import com.abcd.modules.mapper.ChannelMerAuthMapper;
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
public class IChannelMerAuthServiceImpl extends ServiceImpl<ChannelMerAuthMapper, ChannelMerAuth> implements IChannelMerAuthService {

    @Autowired
    private ChannelMerAuthMapper channelMerAuthMapper;

	@Override
	public List<ChannelMerAuth> selectByProduct(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return channelMerAuthMapper.selectByProduct(map);
	}
}