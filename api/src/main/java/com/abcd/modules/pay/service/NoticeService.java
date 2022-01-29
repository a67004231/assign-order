package com.abcd.modules.pay.service;

import java.util.Map;

import com.abcd.modules.pay.entity.D1NoticeDto;
import com.abcd.modules.pay.entity.D3NoticeDto;
import com.abcd.modules.pay.entity.D4NoticeDto;
import com.abcd.modules.pay.entity.D6NoticeDto;
import com.abcd.modules.pay.entity.SearchOrderRspDto;

public interface NoticeService {

	public String paramNotice(Map<String,String> resMap, String channelCode,String channelOrderNo,String ipAddr);
	public String streamNotice(String resStr, String channelCode,String channelOrderNo,String ipAddr);
	
}
