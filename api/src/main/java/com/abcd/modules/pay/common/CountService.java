package com.abcd.modules.pay.common;

import java.util.List;

import com.abcd.modules.channel.entity.ChannelInfo;
import com.abcd.modules.channel.entity.ChannelMerAuth;
import com.abcd.modules.merchant.entity.MerProductAuth;
import com.abcd.modules.order.entity.OrderInfo;

/**
 * 因子智能计算通用接口
 * @author 
 */
public interface CountService {
      Factor count(List<ChannelInfo> channelInfoList, OrderInfo orderInfo);
      Factor count2(List<ChannelInfo> channelInfoList, OrderInfo orderInfo);

      boolean addFactorByApi(String channelCode, int weight);

    boolean closeFactorByApi(String channelCode);
}