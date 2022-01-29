package com.abcd.modules.mapper;

import com.abcd.modules.channel.entity.ChannelMerAuth;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;
import java.util.Map;

/**
 * 通道信息数据处理层
 * @author 
 */
public interface ChannelMerAuthMapper extends BaseMapper<ChannelMerAuth> {
	public List<ChannelMerAuth> selectByProduct(Map<String, Object> map);
}