package com.ryhy.provider;

import com.ryhy.base.dto.ChannelNoticeDTO;
import com.ryhy.base.dto.ChannelPayDTO;
import com.ryhy.base.dto.ChannelPayRspDTO;
import com.ryhy.base.dto.ChannelQueryDTO;
import com.ryhy.base.dto.ChannelStockDTO;
import com.ryhy.base.dto.OrderDataReportDto;
import com.ryhy.base.service.IProviderService;
import com.ryhy.base.util.BigDecimalUtil;
import com.ryhy.base.vo.ResultVO;
import com.ryhy.provider.common.Config;
import com.ryhy.provider.common.ConvertUtil;
import com.ryhy.provider.dto.ReqOrderDto;
import com.ryhy.provider.dto.RspDto;
import com.ryhy.provider.dto.RspSearchDto;
import com.ryhy.provider.service.OrderFlowService;

import cn.hutool.Hutool;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

import org.apache.dubbo.config.annotation.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.Resource;

/**
 * 生产者Dubbo接口实现
 * @Author Sans
 * @CreateTime 2019/11/6 23:01
 */
@Service(
        group = "CHANNEL_AUTO",
        version = "0.0.1"
)
@Slf4j
public class ProviderServiceImpl implements IProviderService {
	@Resource
	private Config config;
	@Resource
	private OrderFlowService orderFlowService;
    @Override
    public ResultVO channelPay(ChannelPayDTO channelPayDTO) {
    	ChannelPayRspDTO rspDto=new ChannelPayRspDTO();
    	rspDto.setSysOrderNo(channelPayDTO.getSysOrderNo());
    	if(channelPayDTO.getChannelType()==2) {
    		rspDto.setStatus(1);
    		rspDto.setChannelCode("10000");
    		rspDto.setChannelMsg("下单成功");
    	}else if(channelPayDTO.getChannelType()==3){
    		rspDto.setStatus(3);
    		rspDto.setChannelCode("90001");
    		rspDto.setChannelMsg("下单失败");
    	}
        return  new ResultVO.Builder<>().code(200).message("success").data(rspDto).build();
    }
    
    @Override
    public ResultVO channelQuery(ChannelQueryDTO channelQueryDTO) {
    	ChannelPayRspDTO rspDto=new ChannelPayRspDTO();
    	rspDto.setSysOrderNo(channelQueryDTO.getSysOrderNo());
    	log.info("处理查询任务");
    	if(channelQueryDTO.getChannelType()==2) {
    		rspDto.setStatus(2);
    		rspDto.setChannelCode("10000");
    		rspDto.setChannelMsg("下单成功");
    	}else if(channelQueryDTO.getChannelType()==3){
    		rspDto.setStatus(3);
    		rspDto.setChannelCode("90001");
    		rspDto.setChannelMsg("下单失败");
    	}
        return  new ResultVO.Builder<>().code(200).message("success").data(rspDto).build();
    }

	@Override
	public ResultVO channelStockQuery(ChannelStockDTO channelStockDTO) {
		List<OrderDataReportDto> list = new ArrayList<OrderDataReportDto>();
		OrderDataReportDto dto1=new OrderDataReportDto();
		dto1.setOrderAmount(5000l);
		dto1.setOrderCount(50l);
		OrderDataReportDto dto2=new OrderDataReportDto();
		dto2.setOrderAmount(10000l);
		dto2.setOrderCount(50l);
		OrderDataReportDto dto3=new OrderDataReportDto();
		dto3.setOrderAmount(20000l);
		dto3.setOrderCount(50l);
		list.add(dto1);
		list.add(dto2);
		list.add(dto3);
		return new ResultVO.Builder<>().code(200).message("success").data(list).build();
	}

	@Override
	public ResultVO channelNotice(ChannelNoticeDTO channelNoticeDTO) {
		// TODO Auto-generated method stub
		return null;
	}
}