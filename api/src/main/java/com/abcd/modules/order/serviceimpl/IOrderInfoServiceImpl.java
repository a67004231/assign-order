package com.abcd.modules.order.serviceimpl;

import com.abcd.modules.mapper.OrderInfoMapper;
import com.abcd.modules.order.entity.OrderInfo;
import com.abcd.modules.order.service.IOrderInfoService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 订单信息接口实现
 * @author 
 */
@Slf4j
@Service
@Transactional
public class IOrderInfoServiceImpl extends ServiceImpl<OrderInfoMapper, OrderInfo> implements IOrderInfoService {

    @Autowired
    private OrderInfoMapper orderInfoMapper;

    @Override
    public String queryStringBySql(String sqlStr) {
        return orderInfoMapper.queryStringBySql(sqlStr);
    }

	@Override
	public Map<String, Object> saveOrder(Map<String, Object> map) {
		return orderInfoMapper.saveOrder(map);
		
	}

	@Override
	public int updateMerAmountById(OrderInfo orderInfo) {
		// TODO Auto-generated method stub
		return orderInfoMapper.updateMerAmountById(orderInfo);
	}

	@Override
	public int updateNoticeById(OrderInfo orderInfo) {
		// TODO Auto-generated method stub
		return orderInfoMapper.updateNoticeById(orderInfo);
	}


	@Override
	public int updateSearchCountById(OrderInfo orderInfo) {
		// TODO Auto-generated method stub
		return orderInfoMapper.updateSearchCountById(orderInfo);
	}
}