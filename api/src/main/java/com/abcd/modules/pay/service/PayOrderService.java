package com.abcd.modules.pay.service;

import java.math.BigDecimal;
import java.util.List;

import com.abcd.modules.channel.entity.ChannelInfo;
import com.abcd.modules.channel.entity.ChannelMerAuth;
import com.abcd.modules.channel.entity.ChannelProductAuth;
import com.abcd.modules.merchant.entity.MerInfo;
import com.abcd.modules.merchant.entity.MerProductAuth;
import com.abcd.modules.order.entity.ChannelOrderInfo;
import com.abcd.modules.order.entity.OrderInfo;
import com.abcd.modules.pay.dto.OrderDoubleBookingDto;
import com.abcd.modules.pay.entity.C10NoticeDto;
import com.abcd.modules.pay.entity.C4NoticeDto;
import com.abcd.modules.pay.entity.C5NoticeDto;
import com.abcd.modules.pay.entity.C6NoticeDto;
import com.abcd.modules.pay.entity.C7NoticeDto;
import com.abcd.modules.pay.entity.C8NoticeDto;
import com.abcd.modules.pay.entity.C9NoticeDto;
import com.abcd.modules.pay.entity.D1NoticeDto;
import com.abcd.modules.pay.entity.D2NoticeDto;
import com.abcd.modules.pay.entity.PayOrderDto;
import com.abcd.modules.pay.entity.PayOrderRspDto;
import com.abcd.modules.pay.entity.RuiyouNoticeDto;
import com.abcd.modules.pay.entity.SearchMerAccountDto;
import com.abcd.modules.pay.entity.SearchOrderBy30Dto;
import com.abcd.modules.pay.entity.SearchOrderDto;
import com.abcd.modules.pay.entity.SearchOrderRspDto;
import com.abcd.modules.pay.entity.YxyNoticeDto;

public interface PayOrderService {
	/**
	 * 充值订单
	 * @param payOrderDto
	 * @param merInfo
	 * @return
	 */
	public PayOrderRspDto payOrder(PayOrderDto payOrderDto,MerInfo merInfo);
	
	/**
	 * 查询订单
	 * @param payOrderDto
	 * @param merInfo
	 * @return
	 */
	public SearchOrderRspDto searchOrder(SearchOrderDto searchOrderDto,MerInfo merInfo);
	/**
	 * 查询商户余额
	 * @param payOrderDto
	 * @param merInfo
	 * @return
	 */
	public SearchMerAccountDto searchMerAccount(SearchOrderDto searchOrderDto,MerInfo merInfo);
	/**
	 * 通知商户
	 * @param orderId
	 */
	public void noticeMer(OrderInfo orderInfo) ;
	
	/**
	 * 订单有效期失败后重新下单
	 * @param dto
	 * @return
	 */
	public PayOrderRspDto doubliBookingOrder(String sysOrderNo) ;
	/**
	 * 通道订单查询
	 * @param dto
	 * @return
	 */
	public void searchChannelOrder(ChannelOrderInfo channelOrderInfo) ;
	/**
	 * 选择通道并下单，如果下单异常则继续选择通道，直至所有通道选择完毕
	 * @param merProductAuthList
	 * @param orderInfo
	 * @param amt
	 * @return
	 */
	public void sendChannel(List<ChannelInfo> channelInfoList,OrderInfo orderInfo);
	/**
	 * 查找通道
	 * @param orderInfo
	 */
	public void findChannel(OrderInfo orderInfo) ;
}
