package com.abcd.modules.channel.serviceimpl;

import com.abcd.modules.channel.entity.MerChannelRoute;
import com.abcd.modules.channel.service.IMerChannelRouteService;
import com.abcd.modules.mapper.MerChannelRouteMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 商户通道路由接口实现
 * @author 
 */
@Slf4j
@Service
@Transactional
public class IMerChannelRouteServiceImpl extends ServiceImpl<MerChannelRouteMapper, MerChannelRoute> implements IMerChannelRouteService {

    @Autowired
    private MerChannelRouteMapper merChannelRouteMapper;

	@Override
	public List<MerChannelRoute> getRouteList(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return merChannelRouteMapper.getRouteList(map);
	}
}