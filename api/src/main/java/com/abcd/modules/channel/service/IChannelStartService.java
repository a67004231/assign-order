package com.abcd.modules.channel.service;

import com.abcd.common.vo.SearchVo;
import com.abcd.modules.channel.entity.ChannelInfo;
import com.abcd.modules.channel.entity.ChannelMerAuth;
import com.abcd.modules.channel.entity.ChannelStart;
import com.abcd.modules.merchant.entity.MerProductAuth;
import com.abcd.modules.order.entity.OrderInfo;
import com.abcd.modules.pay.common.Factor;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 通道启动信息接口
 * @author 
 */
public interface IChannelStartService extends IService<ChannelStart> {

    /**
     * 通道启动
     */
    public Factor ChannelStart(OrderInfo orderInfo,List<ChannelInfo> channelList);
}