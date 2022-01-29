package com.abcd.modules.mapper;

import com.abcd.modules.channel.entity.ChannelInfo;
import com.abcd.modules.channel.entity.ChannelMerAuth;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;
import java.util.Map;

/**
 * 通道信息数据处理层
 * @author 
 */
public interface ChannelInfoMapper extends BaseMapper<ChannelInfo> {
	public List<ChannelInfo> selectByProduct(Map<String, Object> map);
}