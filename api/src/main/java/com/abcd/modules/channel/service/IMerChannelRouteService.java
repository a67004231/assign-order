package com.abcd.modules.channel.service;

import com.abcd.common.vo.SearchVo;
import com.abcd.modules.channel.entity.MerChannelRoute;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * 商户通道路由接口
 * @author 
 */
public interface IMerChannelRouteService extends IService<MerChannelRoute> {
	public List<MerChannelRoute> getRouteList(Map<String, Object>map);
}