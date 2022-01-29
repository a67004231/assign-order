package com.abcd.modules.pay;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.abcd.modules.log.service.OrderFlowService;
import com.abcd.modules.pay.common.IpUtil;
import com.abcd.modules.pay.common.SignUtil;
import com.abcd.modules.pay.entity.*;
import com.abcd.modules.pay.service.NoticeService;
import com.abcd.modules.pay.service.PayOrderService;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;



@Slf4j
@RestController
@Api(description = "回调接口")
@RequestMapping("/api/notice")
@Transactional
public class NoticeController {
	@Resource
    private PayOrderService payOrderService;
	@Resource
	private NoticeService noticeService;
	@Resource
	private OrderFlowService orderFlowService;
	@RequestMapping(value = "/paramNotice/{channelCode}/{channelOrderNo}")
	@ApiOperation(value = "通知")
	@ResponseBody
	public String paramNotice(HttpServletRequest request,@PathVariable("channelCode")String channelCode,@PathVariable("channelOrderNo")String channelOrderNo) {
		Map m = null;
		Map<String,String> resMap = null;
		m = request.getParameterMap();
		resMap = new HashMap<String,String>();
		for(Iterator iter = m.entrySet().iterator();iter.hasNext();){ 
	        Map.Entry element = (Map.Entry)iter.next(); 
	        Object strKey = element.getKey();
	        Object strObj = element.getValue();
	        resMap.put(strKey.toString(), ((String[])strObj)[0]);
		}
		String ipAddr = IpUtil.getIpAddr(request);
		orderFlowService.sendOrderFlow(channelOrderNo,"收到通知："+channelCode+"json: "+JSONUtil.toJsonStr(resMap));
		
		return noticeService.paramNotice(resMap, channelCode, channelOrderNo,ipAddr);
	}
	@RequestMapping(value = "/streamNotice/{channelCode}/{channelOrderNo}")
	@ApiOperation(value = "通知")
	@ResponseBody
	public String streamNotice(HttpServletRequest request,@PathVariable("channelCode")String channelCode,@PathVariable("channelOrderNo")String channelOrderNo) {
		StringBuilder sb = new StringBuilder();
    	try {
    	BufferedReader streamReader = new BufferedReader(new InputStreamReader(request.getInputStream(), "UTF-8"));
    	 
        // 写入数据到Stringbuilder
        String line = null;
        	while ((line = streamReader.readLine()) != null) {
                sb.append(line);
              }
		} catch (Exception e) {
			// TODO: handle exception
		}
    	String reqStr = sb.toString();
    	String ipAddr = IpUtil.getIpAddr(request);
		orderFlowService.sendOrderFlow(channelOrderNo,"收到通知："+channelCode+"json: "+reqStr);
		
		return noticeService.streamNotice(reqStr, channelCode, channelOrderNo,ipAddr);
	}
    public static void main(String[] args) {
    	RuiyouNoticeDto dto=new RuiyouNoticeDto();
    	String md5 = SecureUtil.md5("amt=15000&dealCode=10000&dealMsg=流程成功&msg=失败&orderNo=20210527215623677347&status=2&timestamp=1622123815062735f90e4faa5446c9fe4659ccd61c1fc");
    	System.out.println(md5);
	}
}
