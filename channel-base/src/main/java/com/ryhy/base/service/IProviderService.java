package com.ryhy.base.service;

import com.ryhy.base.dto.ChannelNoticeDTO;
import com.ryhy.base.dto.ChannelPayDTO;
import com.ryhy.base.dto.ChannelQueryDTO;
import com.ryhy.base.dto.ChannelStockDTO;
import com.ryhy.base.vo.ResultVO;

import java.util.List;

/**
 * RPC接口
 * @Author Sans
 * @CreateTime 2019/11/6 23:03
 */
public interface IProviderService {
    //渠道支付下单接口
    ResultVO channelPay(ChannelPayDTO channelPayDTO);

    //渠道查询接口
    ResultVO channelQuery(ChannelQueryDTO channelQueryDTO);

    //渠道库存查询接口
    ResultVO channelStockQuery(ChannelStockDTO channelStockDTO);
    //渠道库存查询接口
    ResultVO channelNotice(ChannelNoticeDTO channelNoticeDTO);
}