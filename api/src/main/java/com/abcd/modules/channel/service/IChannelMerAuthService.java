package com.abcd.modules.channel.service;

import com.abcd.common.vo.SearchVo;
import com.abcd.modules.channel.entity.ChannelMerAuth;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * 通道信息接口
 * @author 
 */
public interface IChannelMerAuthService extends IService<ChannelMerAuth> {
	public List<ChannelMerAuth> selectByProduct(Map<String, Object> map);
}