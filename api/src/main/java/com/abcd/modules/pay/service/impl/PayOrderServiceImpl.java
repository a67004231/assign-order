package com.abcd.modules.pay.service.impl;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URLDecoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.abcd.common.dubbo.ChannelUtils;
import com.abcd.common.enums.OederRspEnum;
import com.abcd.common.utils.ResultUtil;
import com.abcd.common.utils.moneyUtil.BigDecimalUtil;
import com.abcd.modules.base.entity.Mobile;
import com.abcd.modules.base.service.IMobileService;
import com.abcd.modules.channel.entity.ChannelAccount;
import com.abcd.modules.channel.entity.ChannelInfo;
import com.abcd.modules.channel.entity.ChannelMerAuth;
import com.abcd.modules.channel.entity.ChannelProductAuth;
import com.abcd.modules.channel.entity.ChannelSettingInfo;
import com.abcd.modules.channel.service.IChannelAccountService;
import com.abcd.modules.channel.service.IChannelInfoService;
import com.abcd.modules.channel.service.IChannelMerAuthService;
import com.abcd.modules.channel.service.IChannelProductAuthService;
import com.abcd.modules.channel.service.IChannelSettingInfoService;
import com.abcd.modules.company.entity.CompanyInfo;
import com.abcd.modules.company.service.ICompanyInfoService;
import com.abcd.modules.log.service.OrderFlowService;
import com.abcd.modules.merchant.entity.MerAccount;
import com.abcd.modules.merchant.entity.MerInfo;
import com.abcd.modules.merchant.entity.MerProductAuth;
import com.abcd.modules.merchant.entity.MerSecretKey;
import com.abcd.modules.merchant.service.IMerAccountLogService;
import com.abcd.modules.merchant.service.IMerAccountService;
import com.abcd.modules.merchant.service.IMerInfoService;
import com.abcd.modules.merchant.service.IMerProductAuthService;
import com.abcd.modules.merchant.service.IMerSecretKeyService;
import com.abcd.modules.order.entity.ChannelOrderInfo;
import com.abcd.modules.order.entity.ErrServiceLog;
import com.abcd.modules.order.entity.OrderInfo;
import com.abcd.modules.order.service.IChannelOrderInfoService;
import com.abcd.modules.order.service.IErrServiceLogService;
import com.abcd.modules.order.service.IOrderInfoService;
import com.abcd.modules.pay.common.CountService;
import com.abcd.modules.pay.common.Factor;
import com.abcd.modules.pay.common.QueryPhoneDto;
import com.abcd.modules.pay.common.QueryPhoneUtil;
import com.abcd.modules.pay.common.SignUtil;
import com.abcd.modules.pay.dto.AccountUpdateDto;
import com.abcd.modules.pay.dto.OrderDoubleBookingDto;
import com.abcd.modules.pay.dto.OrderSendChannelDto;
import com.abcd.modules.pay.entity.C10NoticeDto;
import com.abcd.modules.pay.entity.C4NoticeBody;
import com.abcd.modules.pay.entity.C4NoticeDto;
import com.abcd.modules.pay.entity.C5NoticeDto;
import com.abcd.modules.pay.entity.C6NoticeDto;
import com.abcd.modules.pay.entity.C7NoticeDto;
import com.abcd.modules.pay.entity.C8NoticeDto;
import com.abcd.modules.pay.entity.C9NoticeDto;
import com.abcd.modules.pay.entity.D2NoticeDto;
import com.abcd.modules.pay.entity.PayOrderDto;
import com.abcd.modules.pay.entity.PayOrderRspDto;
import com.abcd.modules.pay.entity.RuiyouNoticeDto;
import com.abcd.modules.pay.entity.SearchMerAccountDto;
import com.abcd.modules.pay.entity.SearchOrderBy30Dto;
import com.abcd.modules.pay.entity.SearchOrderDto;
import com.abcd.modules.pay.entity.SearchOrderRspDto;
import com.abcd.modules.pay.entity.YxyNoticeDto;
import com.abcd.modules.pay.service.PayOrderService;
import com.abcd.modules.product.entity.ProductInfo;
import com.abcd.modules.product.service.IProductInfoService;
import com.abcd.modules.sys.entity.SysArea;
import com.abcd.modules.sys.service.ISysAreaService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ryhy.base.dto.ChannelPayDTO;
import com.ryhy.base.dto.ChannelPayRspDTO;
import com.ryhy.base.dto.ChannelQueryDTO;
import com.ryhy.base.vo.ResultVO;

import cn.hutool.core.codec.Base64Decoder;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PayOrderServiceImpl implements PayOrderService{
	@Value("${order.account.queue}")
    private  String updateAccountQueue;
	@Value("${order.notify.delay}")
	private  String notifyMerDelayQueue;
	@Value("${order.notify.queue}")
	private  String notifyMerQueue;
	@Value("${order.search.queue.post}")
	private  String searchOrderQueue;
	@Value("${order.send.queue}")
	private  String sendChannelQueue;
	@Value("${dubbo.registry.address}")
	private  String address;
	@Value("${api.version}")
    private  String version;
	@Value("${api.getServiceNameUrl}")
	private  String getServiceNameUrl;
	@Value("${api.maxtime}")
	private Long maxTime;
	@Autowired
    private RedisTemplate redisTemplate;
	@Resource
    RabbitTemplate rabbitTemplate;
	@Resource
    private ISysAreaService areaInfoService;
    @Resource
    private IProductInfoService productInfoService;
    @Resource
    private IOrderInfoService orderInfoService;
    @Resource
    private IChannelOrderInfoService channelOrderInfoService;
    @Resource
    private IMerProductAuthService merProductAuthService;
    @Resource
    private IErrServiceLogService errServiceLogService;;
    @Resource
	private CountService countService;
    @Resource
    private IChannelInfoService channelInfoService;
    @Resource
    private IChannelMerAuthService channelMerAuthService;
    @Resource
    private IMerAccountService merAccountService;
    @Resource
    private IChannelAccountService channelAccountService;
    @Resource
    private IChannelSettingInfoService channelSettingInfoService;
    @Resource
    private IChannelProductAuthService channelProductAuthService;
    @Resource
    private IMerAccountLogService merAccountLogService;
    @Resource
    private IMerSecretKeyService merSecretKeyService;
    @Resource
    private IMerInfoService merInfoService;
    @Resource
    private ICompanyInfoService companyInfoService;
    @Resource
	private OrderFlowService orderFlowService;
    @Resource
    private IMobileService mobileService;
    private String REGEX_ZIPCODE = "^(010|02\\d|0[3-9]\\d{2})\\d{6,8}$";
    private Pattern PATTERN_ZIPCODE;
	@Override
	public PayOrderRspDto payOrder(PayOrderDto payOrderDto,MerInfo merInfo) {
		PayOrderRspDto rspDto=new PayOrderRspDto();
		String phone = payOrderDto.getPhone();
		String orderNo = payOrderDto.getOrderNo();
    	String productCode=null;
    	String province=null;
    	String city=null;
    	String orderAmount = payOrderDto.getOrderAmount();
    	BigDecimal amt = new BigDecimal(BigDecimalUtil.mul(orderAmount, 100));
    	//????????????????????????????????????
    	QueryWrapper<MerAccount> merAccountWrapper=new QueryWrapper<MerAccount>();
    	merAccountWrapper.lambda().eq(MerAccount::getMerId, merInfo.getId());
//    	MerAccount merAccount = merAccountService.getById(1l);
    	MerAccount merAccount = merAccountService.getOne(merAccountWrapper);
    	if(merAccount.getCreditBalanceAmt()<amt.longValue()) {
    		rspDto.setCode(OederRspEnum.CODE_40002.code);
			rspDto.setMessage(OederRspEnum.CODE_40002.desc);
			orderFlowService.sendOrderFlow(orderNo, "??????????????????.  ???????????????"+amt.toString()+"  ???????????????"+merAccount.getCreditBalanceAmt().toString());
			return rspDto;
    	}
    	PATTERN_ZIPCODE=Pattern.compile(REGEX_ZIPCODE);
    	
    	//????????????????????????
    	QueryWrapper<Mobile> mobileWra=new QueryWrapper<Mobile>();
    	String yunyingName="";
    	if(payOrderDto.getType()==1) {
	    	if(phone !=null) {
	    		String telMob=phone.substring(0, 7);
	    		mobileWra.lambda().eq(Mobile::getMobile, telMob);
	    		List<Mobile> list = mobileService.list(mobileWra);
	    		
	    		if(list==null||list.size()==0) {
	    			rspDto.setCode(OederRspEnum.CODE_20001.code);
	    			rspDto.setMessage(OederRspEnum.CODE_20001.desc);
	    			orderFlowService.sendOrderFlow(orderNo, "?????????????????????");
	    			return rspDto;
	    		}
	    		Mobile mobile = list.get(0);
	//    		QueryPhoneDto queryPhoneDto = QueryPhoneUtil.queryPhone(phone);
	    		province=mobile.getProvince();
	    		city=mobile.getCity();
	    		if(merInfo.getServiceType()==0) {
	    			yunyingName=mobile.getIsp();
		    		if(yunyingName.equals("????????????")) {
		    			productCode="RG_FAST_CM";
		    		}else if(yunyingName.equals("????????????")) {
		    			productCode="RG_FAST_CU";
		    		}else if(yunyingName.equals("????????????")) {
		    			productCode="RG_FAST_CT";
		    		}else {
		    			rspDto.setCode(OederRspEnum.CODE_20001.code);
		    			rspDto.setMessage(OederRspEnum.CODE_20001.desc);
		    			return rspDto;
		    		}
	    		}else if(merInfo.getServiceType()==1) {
	    			yunyingName="????????????";
	    			productCode="RG_FAST_CM";
	    		}else if(merInfo.getServiceType()==2) {
	    			yunyingName="????????????";
	    			productCode="RG_FAST_CU";
	    		}else if(merInfo.getServiceType()==3) {
	    			yunyingName="????????????";
	    			productCode="RG_FAST_CT";
	    		}
	    		
	    	}else {
	    		rspDto.setCode(OederRspEnum.CODE_20005.code);
		    	rspDto.setMessage(OederRspEnum.CODE_20005.desc);
		    	orderFlowService.sendOrderFlow(orderNo, "???????????????");
		    	return rspDto;
	    	}
    	}else if(payOrderDto.getType()==5) {
	    	if(phone !=null) {
	    		String telMob=phone.substring(0, 7);
	    		mobileWra.lambda().eq(Mobile::getMobile, telMob);
	    		List<Mobile> list = mobileService.list(mobileWra);
	    		
	    		if(list==null||list.size()==0) {
	    			rspDto.setCode(OederRspEnum.CODE_20001.code);
	    			rspDto.setMessage(OederRspEnum.CODE_20001.desc);
	    			orderFlowService.sendOrderFlow(orderNo, "?????????????????????");
	    			return rspDto;
	    		}
	    		Mobile mobile = list.get(0);
	    		city=mobile.getCity();
	    		province=mobile.getProvince();
	//    		QueryPhoneDto queryPhoneDto = QueryPhoneUtil.queryPhone(phone);
//	    		diqu=mobile.getProvince();
	    		if(merInfo.getServiceType()==0) {
		    		yunyingName=mobile.getIsp()+"(???)";
		    		
		    		if(yunyingName.equals("????????????(???)")) {
		    			productCode="RG_SLOW_CM";
		    		}else if(yunyingName.equals("????????????(???)")) {
		    			productCode="RG_SLOW_CU";
		    		}else if(yunyingName.equals("????????????(???)")) {
		    			productCode="RG_SLOW_CT";
		    		}else {
		    			rspDto.setCode(OederRspEnum.CODE_20001.code);
		    			rspDto.setMessage(OederRspEnum.CODE_20001.desc);
		    			return rspDto;
		    		}
	    		}else if(merInfo.getServiceType()==1) {
	    			yunyingName="????????????(???)";
	    			productCode="RG_SLOW_CM";
	    		}else if(merInfo.getServiceType()==2) {
	    			yunyingName="????????????(???)";
	    			productCode="RG_SLOW_CU";
	    		}else if(merInfo.getServiceType()==3) {
	    			yunyingName="????????????(???)";
	    			productCode="RG_SLOW_CT";
	    		}
	    	}else {
	    		orderFlowService.sendOrderFlow(orderNo, "???????????????");
	    		rspDto.setCode(OederRspEnum.CODE_20005.code);
		    	rspDto.setMessage(OederRspEnum.CODE_20005.desc);
		    	return rspDto;
	    	}
    	}else if(payOrderDto.getType()==2) {
    		Matcher matcher = PATTERN_ZIPCODE.matcher(phone);
    		if (matcher.find()) {
    			String group = matcher.group(1);
    			mobileWra.lambda().eq(Mobile::getAreaCode, group);
    			List<Mobile> mobileList = mobileService.list(mobileWra);
    			if(mobileList==null||mobileList.isEmpty()) {
    				rspDto.setCode(OederRspEnum.CODE_20001.code);
	    			rspDto.setMessage(OederRspEnum.CODE_20001.desc);
	    			return rspDto;
    			}
    			Mobile mobile=mobileList.get(0);
    			province=mobile.getProvince();
	    		yunyingName="????????????";
	    		productCode="RG_FIXED_CT";
    		}else {
    			orderFlowService.sendOrderFlow(orderNo, "?????????????????????");
    			rspDto.setCode(OederRspEnum.CODE_20005.code);
		    	rspDto.setMessage(OederRspEnum.CODE_20005.desc);
    			return rspDto;
    		}
    	}else if(payOrderDto.getType()==6) {
    		try {
				city=URLDecoder.decode(payOrderDto.getCity(),"utf-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		yunyingName="??????";
    		productCode="RG_FAST_ELECT";
    	}
    	ProductInfo productInfo =null;
    	QueryWrapper<ProductInfo> productWarpper=new QueryWrapper<ProductInfo>();
    	//????????????
    	OrderInfo orderInfo=new OrderInfo();
    	if(payOrderDto.getType()==1||payOrderDto.getType()==2||payOrderDto.getType()==5) {
	    	productCode=productCode+"_"+payOrderDto.getOrderAmount().toString();
	    	//??????????????????
	    	QueryWrapper<SysArea> areaWrapper=new QueryWrapper<SysArea>();
	    	areaWrapper.lambda().like(SysArea::getName, province);
	    	areaWrapper.lambda().eq(SysArea::getLevel, 1);
	    	SysArea sysArea = areaInfoService.getOne(areaWrapper);
	    	SysArea cityArea =null;
	    	if(sysArea!=null) {
	    		QueryWrapper<SysArea> cityWrapper=new QueryWrapper<SysArea>();
	    		cityWrapper.lambda().like(SysArea::getName, city);
	    		cityWrapper.lambda().eq(SysArea::getLevel, 2);
	    		cityArea = areaInfoService.getOne(cityWrapper);
	    		if(cityArea==null) {
	    			cityWrapper=new QueryWrapper<SysArea>();
	    			cityWrapper.lambda().like(SysArea::getName, city);
	    			cityWrapper.lambda().eq(SysArea::getLevel, 3);
	    			cityArea = areaInfoService.getOne(areaWrapper);
	    		}
	    		//??????????????????
		    	productWarpper.lambda().eq(ProductInfo::getAreaId, sysArea.getId());
		    	productWarpper.lambda().eq(ProductInfo::getParentCode, productCode);
	        	orderInfo.setProvince(sysArea.getName());
	        	orderInfo.setAreaId(sysArea.getId());
	        	orderInfo.setCity(city);
	        	orderInfo.setCityId(cityArea.getId());
	        	productInfo = productInfoService.getOne(productWarpper);
	    	}
	    	
    	}else if(payOrderDto.getType()==3||payOrderDto.getType()==4) {
    		if(payOrderDto.getType()==3) {
    			productCode="RG_QQ_"+payOrderDto.getOrderAmount().toString();
    			yunyingName="Q???";
    		}else {
    			productCode="RG_DY_"+payOrderDto.getOrderAmount().toString();
    			yunyingName="?????????";
    		}
    		productWarpper.lambda().eq(ProductInfo::getCode, productCode);
    		productInfo = productInfoService.getOne(productWarpper);
    	}else if(payOrderDto.getType()==6) {
    		
    		productCode=productCode+"_"+payOrderDto.getOrderAmount().toString();
	    	//??????????????????
	    	QueryWrapper<SysArea> areaWrapper=new QueryWrapper<SysArea>();
	    	areaWrapper.lambda().like(SysArea::getName, province);
	    	areaWrapper.lambda().eq(SysArea::getLevel, 1);
	    	SysArea sysArea = areaInfoService.getOne(areaWrapper);
	    	SysArea cityArea =null;
	    	if(sysArea!=null) {
	    		QueryWrapper<SysArea> cityWrapper=new QueryWrapper<SysArea>();
	    		cityWrapper.lambda().like(SysArea::getName, city);
	    		cityWrapper.lambda().eq(SysArea::getLevel, 2);
	    		cityArea = areaInfoService.getOne(cityWrapper);
	    		if(cityArea==null) {
	    			cityWrapper=new QueryWrapper<SysArea>();
	    			cityWrapper.lambda().like(SysArea::getName, city);
	    			cityWrapper.lambda().eq(SysArea::getLevel, 3);
	    			cityArea = areaInfoService.getOne(areaWrapper);
	    		}
	    		//??????????????????
		    	productWarpper.lambda().eq(ProductInfo::getAreaId, sysArea.getId());
		    	productWarpper.lambda().eq(ProductInfo::getParentCode, productCode);
	        	orderInfo.setProvince(sysArea.getName());
	        	orderInfo.setAreaId(sysArea.getId());
	        	orderInfo.setCity(city);
	        	orderInfo.setCityId(cityArea.getId());
	        	productInfo = productInfoService.getOne(productWarpper);
	    	}
    	}
    	
    	
    	if(productInfo==null) {
			rspDto.setCode(OederRspEnum.CODE_20003.code);
			rspDto.setMessage(OederRspEnum.CODE_20003.desc);
			orderFlowService.sendOrderFlow(orderNo, "??????????????????");
			return rspDto;
    	}
    	
    	
    	int randNum=(int)((Math.random()*9+1)*10000000);
    	
    	String sysOrderNo = DateUtil.format(new Date(), "yyyyMMddHHmmss")+merInfo.getId()+randNum;
    	orderFlowService.sendOrderFlow(orderNo, "????????????????????????"+sysOrderNo);
//    	String sysOrderNo=UUID.randomUUID().toString().replaceAll("-", "");
    	//??????????????????
    	QueryWrapper<MerProductAuth> merProductAuthWrapper=new QueryWrapper<MerProductAuth>();
    	merProductAuthWrapper.lambda().eq(MerProductAuth::getMerId, merInfo.getId());
    	merProductAuthWrapper.lambda().eq(MerProductAuth::getProductId, productInfo.getId());
    	
    	MerProductAuth merProductAuth = merProductAuthService.getOne(merProductAuthWrapper);
    	if(merProductAuth==null) {
    		orderFlowService.sendOrderFlow(sysOrderNo, "?????????????????????");
    		rspDto.setCode(OederRspEnum.CODE_20008.code);
			rspDto.setMessage(OederRspEnum.CODE_20008.desc);
			return rspDto;
    	}
    	//??????????????????
    	String merAmt = BigDecimalUtil.mulbyFormat(BigDecimalUtil.div(amt, 10000), merProductAuth.getRate());
    	try {
    		orderInfo.setAmt(amt);
    		orderInfo.setMerActAmt(Long.valueOf(merAmt));
    		orderInfo.setMerRate(merProductAuth.getRate());
        	orderInfo.setSysOrderNo(sysOrderNo);
        	orderInfo.setMerNo(merInfo.getMerNo());
//        	orderInfo.setMerOrderNo(UUID.randomUUID().toString().replaceAll("-", ""));
        	orderInfo.setMerOrderNo(payOrderDto.getOrderNo());
        	orderInfo.setTel(payOrderDto.getPhone());
        	orderInfo.setProductId(productInfo.getId());
        	orderInfo.setServiceName(yunyingName);
        	orderInfo.setCreateTime(new Date());
        	orderInfo.setUpdateTime(new Date());
        	orderInfo.setMerId(merInfo.getId());
        	orderInfo.setStatus(-1);
        	orderInfo.setNoticeUrl(payOrderDto.getNotifyUrl());
        	orderInfo.setIp(payOrderDto.getIp());
        	orderInfo.setProductType(payOrderDto.getType());
        	orderInfo.setCompanyId(merInfo.getCompanyId());
        	orderInfo.setExt1(payOrderDto.getExt1());
        	if(orderInfo.getProductType()==5) {
        		orderInfo.setMaxTime(86400l);
        	}else {
        		orderInfo.setMaxTime(merInfo.getMaxTime());
        	}
        	orderInfoService.save(orderInfo);
        	orderFlowService.sendOrderFlow(orderNo, "??????????????????");
		} catch (Exception e) {
			e.printStackTrace();
			rspDto.setCode(OederRspEnum.CODE_90002.code);
			rspDto.setMessage(OederRspEnum.CODE_90002.desc);
			orderFlowService.sendOrderFlow(orderNo, "??????????????????"+e.getMessage());
			return rspDto;
		}
    	//????????????????????????????????????
    	QueryWrapper<OrderInfo>orderWrapper=new QueryWrapper<OrderInfo>();
    	orderWrapper.lambda().eq(OrderInfo::getSysOrderNo, orderInfo.getSysOrderNo());
    	orderInfo=orderInfoService.getOne(orderWrapper);
    	rspDto.setCode(OederRspEnum.CODE_10000.code);
    	rspDto.setMessage(OederRspEnum.CODE_10000.desc);
    	rspDto.setOrderNo(orderInfo.getMerOrderNo());
    	rspDto.setSysOrderNo(orderInfo.getSysOrderNo());
    	OrderSendChannelDto sendDto=new OrderSendChannelDto();
    	sendDto.setMerProductAuth(null);
    	sendDto.setMerProductAuthList(null);
    	sendDto.setOrderInfo(orderInfo);
    	sendDto.setType(1);
    	rabbitTemplate.convertAndSend(sendChannelQueue, JSONUtil.toJsonStr(sendDto));
    	//????????????????????????mq
		return rspDto;
	}
	@Override
	public SearchOrderRspDto searchOrder(SearchOrderDto dto, MerInfo merInfo) {
		SearchOrderRspDto rspDto=new SearchOrderRspDto();
		String orderNo = dto.getOrderNo();
		if(orderNo==null) {
			orderFlowService.sendOrderFlow(orderNo, "?????????????????????");
	    	return rspDto;
		}
		QueryWrapper<OrderInfo> orderWrapper=new QueryWrapper<OrderInfo>();
		orderWrapper.lambda().eq(OrderInfo::getMerOrderNo, orderNo);
		orderWrapper.lambda().eq(OrderInfo::getMerId, merInfo.getId());
		OrderInfo orderInfo = orderInfoService.getOne(orderWrapper);
		if(orderInfo==null) {
			rspDto.setStatus(3);
			rspDto.setCode(OederRspEnum.CODE_20006.code);
	    	rspDto.setMessage(OederRspEnum.CODE_20006.desc);
	    	return rspDto;
		}
		if(orderInfo.getStatus()==1) {
			rspDto.setCode(OederRspEnum.CODE_10002.code);
	    	rspDto.setMessage(OederRspEnum.CODE_10002.desc);
		}else if(orderInfo.getStatus()==2) {
			rspDto.setCode(OederRspEnum.CODE_10000.code);
	    	rspDto.setMessage(OederRspEnum.CODE_10000.desc);
		}else {
			rspDto.setCode(OederRspEnum.CODE_10001.code);
	    	rspDto.setMessage(OederRspEnum.CODE_10001.desc);
		}
    	rspDto.setMerNo(orderInfo.getMerNo());
    	rspDto.setOrderAmount(BigDecimalUtil.divd(orderInfo.getAmt(), 100).longValue());
    	rspDto.setOrderNo(orderNo);
    	rspDto.setOrderTime(DateUtil.format(orderInfo.getCreateTime(), "yyyyMMddHHmmss"));
    	rspDto.setPhone(orderInfo.getTel());
    	rspDto.setResultTime(DateUtil.format(orderInfo.getResultTime(), "yyyyMMddHHmmss"));
    	rspDto.setStatus(orderInfo.getStatus());
    	rspDto.setSysOrderNo(orderInfo.getSysOrderNo());
    	rspDto.setSerialNumber(orderInfo.getSerialNumber());
		return rspDto;
	}
	
	/**
	 * ????????????????????????????????????????????????????????????????????????????????????????????????
	 * @param merProductAuthList
	 * @param orderInfo
	 * @param amt
	 * @return
	 */
	public void findChannel(OrderInfo orderInfo) {
		ProductInfo productInfo =null;
		productInfo = productInfoService.getById(orderInfo.getProductId());
		MerInfo merInfo = merInfoService.getById(orderInfo.getMerId());
		
//		if(orderInfo.getProductType()==1 && merInfo.getServiceType()==0) {
//
//			String orderNo = "Q_" + System.currentTimeMillis();
//			String phone =orderInfo.getTel();
//			Map<String,String> paramMap = new HashMap<String, String>();
//			paramMap.put("orderNo", orderNo);
//			paramMap.put("phoneNo", phone);
//			
//			HashMap<String, String> headers = new HashMap<String, String>();
//			headers.put("Content-Type", "application/json");
//			
//			String param = JSONUtil.toJsonStr(paramMap);
//			String productCode="";
//			Long amt=orderInfo.getAmt().longValue()/100;
//			String yunyingName="";
//			try {
//				String str = HttpRequest.post(getServiceNameUrl).timeout(10000).addHeaders(headers).body(param).execute().body();
//				log.info("??????????????????"+orderInfo.getSysOrderNo()+"  rsp"+str);
//				JSON json = JSONUtil.parse(str);
//				
//				yunyingName=(String) json.getByPath("str");
//				String[] split = yunyingName.split(" ");
//				
//				yunyingName=split[split.length-1];
//				if(orderInfo.getProductType()==1) {
//					if(yunyingName.equals("????????????")) {
//						productCode="RG_FAST_CM";
//					}else if(yunyingName.equals("????????????")) {
//						productCode="RG_FAST_CU";
//					}else if(yunyingName.equals("????????????")) {
//						productCode="RG_FAST_CT";
//					}
//				}else {
//					if(yunyingName.equals("????????????")) {
//						productCode="RG_SLOW_CM";
//					}else if(yunyingName.equals("????????????")) {
//						productCode="RG_SLOW_CU";
//					}else if(yunyingName.equals("????????????")) {
//						productCode="RG_SLOW_CT";
//					}
//				}
//				productCode=productCode+"_"+amt.toString();
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//			
//			
//			if(!productCode.equals("")) {
//				try {
//					String diqu="";
//					QueryWrapper<Mobile> mobileWra=new QueryWrapper<Mobile>();
//			    	if(productInfo.getType()==1||productInfo.getType()==5) {
//			    		String telMob=phone.substring(0, 7);
//			    		mobileWra.lambda().eq(Mobile::getMobile, telMob);
//			    		List<Mobile> list = mobileService.list(mobileWra);
//			    		
//			    		Mobile mobile = list.get(0);
//			//    		QueryPhoneDto queryPhoneDto = QueryPhoneUtil.queryPhone(phone);
//			    		diqu=mobile.getProvince();
//			    	}else if(productInfo.getType()==2) {
//			    		Matcher matcher = PATTERN_ZIPCODE.matcher(phone);
//			    		if (matcher.find()) {
//			    			String group = matcher.group(1);
//			    			mobileWra.lambda().eq(Mobile::getAreaCode, group);
//			    			List<Mobile> mobileList = mobileService.list(mobileWra);
//			    			Mobile mobile=mobileList.get(0);
//			    			diqu=mobile.getProvince();
//				    		yunyingName="????????????";
//			    		}
//			    	}
//			    	QueryWrapper<SysArea> areaWrapper=new QueryWrapper<SysArea>();
//			    	areaWrapper.lambda().like(SysArea::getName, diqu);
//			    	areaWrapper.lambda().eq(SysArea::getLevel, 1);
//			    	SysArea sysArea = areaInfoService.getOne(areaWrapper);
//			    	
//					QueryWrapper<ProductInfo> productWarpper=new QueryWrapper<ProductInfo>();
//			    	productWarpper.lambda().eq(ProductInfo::getAreaId, sysArea.getId());
//			    	productWarpper.lambda().eq(ProductInfo::getParentCode, productCode);
//			    	productInfo = productInfoService.getOne(productWarpper);
//			    	if(productInfo==null) {
//			    		orderInfo.setStatus(3);
//			    		log.info("??????????????????: "+orderInfo.getSysOrderNo()+" productCode"+productCode);
//			    		orderInfo.setResultTime(new Date());
//			    		orderInfo.setUpdateTime(new Date());
//			    		orderInfoService.updateById(orderInfo);
//			    		rabbitTemplate.convertAndSend(notifyMerQueue, JSONUtil.toJsonStr(orderInfo));
//			    		return;
//			    	}
//			    	//????????????????????????
//			    	if(productInfo.getId().intValue()!=orderInfo.getProductId().intValue()) {
//			    		ErrServiceLog errServiceLog=new ErrServiceLog();
//			    		errServiceLog.setCreateTime(new Date());
//			    		errServiceLog.setOldServiceName(orderInfo.getServiceName());
//			    		errServiceLog.setServiceName(yunyingName);
//			    		errServiceLog.setSysOrderNo(orderInfo.getSysOrderNo());
//			    		errServiceLog.setTel(orderInfo.getTel());
//			    		errServiceLogService.save(errServiceLog);
//			    		
//			    		QueryWrapper<MerProductAuth> merProductAuthWrapper=new QueryWrapper<MerProductAuth>();
//			        	merProductAuthWrapper.lambda().eq(MerProductAuth::getMerId, merInfo.getId());
//			        	merProductAuthWrapper.lambda().eq(MerProductAuth::getProductId, productInfo.getId());
//			        	MerProductAuth merProductAuth = merProductAuthService.getOne(merProductAuthWrapper);
//			        	//??????????????????
//			        	String merAmt = BigDecimalUtil.mulbyFormat(BigDecimalUtil.div(amt, 10000), merProductAuth.getRate());
//			    		orderInfo.setProductId(productInfo.getId());
//			        	orderInfo.setServiceName(yunyingName);
//			        	orderInfo.setMerRate(merProductAuth.getRate());
//			        	orderInfo.setMerActAmt(Long.valueOf(merAmt));
//			        	orderInfoService.updateById(orderInfo);
//			    	}
//				} catch (Exception e) {
//					e.printStackTrace();
//					orderInfo.setStatus(3);
//		    		log.info("??????????????????: "+orderInfo.getSysOrderNo());
//		    		orderInfo.setResultTime(new Date());
//		    		orderInfo.setUpdateTime(new Date());
//		    		orderInfoService.updateById(orderInfo);
//		    		rabbitTemplate.convertAndSend(notifyMerQueue, JSONUtil.toJsonStr(orderInfo));
//		    		return;
//				}
//			}
//		}
		//??????????????????
    	
    	
    	Map<String, Object> authMap=new HashMap<String, Object>();
    	authMap.put("productId", productInfo.getId());
    	authMap.put("companyId", orderInfo.getCompanyId());
    	authMap.put("amt", orderInfo.getAmt());
    	authMap.put("time", orderInfo.getMaxTime()-orderInfo.getUseTime());
    	List<ChannelInfo> channelInfoList = channelInfoService.selectByProduct(authMap);
    	if(channelInfoList==null||channelInfoList.isEmpty()) {
//    		log.info("???????????????????????????"+orderInfo.getSysOrderNo()+"   "+JSONUtil.toJsonStr(channelInfoList));
    		orderFlowService.sendOrderFlow(orderInfo.getSysOrderNo(), "??????????????????????????? ?????????"+JSONUtil.toJsonStr(authMap)+"???");
    	}
//    	List<ChannelMerAuth> channelMerAuthList = channelMerAuthService.selectByProduct(authMap);
    	//????????????????????????
    	
		sendChannel(channelInfoList, orderInfo);	
	}
	@Override
	public void sendChannel(List<ChannelInfo> channelInfoList,OrderInfo orderInfo) {
		BigDecimal amt = orderInfo.getAmt();
		PayOrderRspDto rspDto=new PayOrderRspDto();
		if(channelInfoList==null||channelInfoList.isEmpty()) {
    		orderInfo.setStatus(3);
    		orderInfo.setResultTime(new Date());
    		orderInfo.setUpdateTime(new Date());
    		orderInfoService.updateById(orderInfo);
    		rabbitTemplate.convertAndSend(notifyMerQueue, JSONUtil.toJsonStr(orderInfo));
    		return ;
    	}
		//??????????????????
//		MerProductAuth merProductAuthTmp = merProductAuthList.get(0);
		CompanyInfo companyInfo = companyInfoService.getById(orderInfo.getCompanyId());
		Factor factor=null;
		try {
			if(companyInfo.getRouteType()==1) {
				factor=countService.count(channelInfoList,orderInfo);
			}else {
				factor=countService.count2(channelInfoList,orderInfo);
			}
		} catch (Exception e) {
			log.info("?????????????????????"+orderInfo.getSysOrderNo());
			e.printStackTrace();
			orderFlowService.sendOrderFlow(orderInfo.getSysOrderNo(), "?????????????????????"+e.getMessage());
		}
		Map<String, Object>map=new HashMap<String, Object>();
    	if(factor==null) {
    		orderInfo.setStatus(3);
    		orderInfo.setResultTime(new Date());
    		orderInfo.setUpdateTime(new Date());
    		orderInfoService.updateById(orderInfo);
    		rspDto.setCode(OederRspEnum.CODE_30004.code);
    		rspDto.setMessage(OederRspEnum.CODE_30004.desc);
    		orderFlowService.sendOrderFlow(orderInfo.getSysOrderNo(), "?????????????????? ");
    		rabbitTemplate.convertAndSend(notifyMerQueue, JSONUtil.toJsonStr(orderInfo));
    		return;
    	}
//		log.info(orderInfo.getSysOrderNo()+"???????????????"+factor.getChannelCode());
		orderFlowService.sendOrderFlow(orderInfo.getSysOrderNo(), "???????????????"+factor.getChannelCode());
    	//??????????????????
		QueryWrapper<ChannelSettingInfo>channelSettingWrapper=new QueryWrapper<ChannelSettingInfo>();
    	channelSettingWrapper.lambda().eq(ChannelSettingInfo::getChannelId, factor.getChannelId());
    	channelSettingWrapper.lambda().eq(ChannelSettingInfo::getCompanyId, orderInfo.getCompanyId());
    	ChannelSettingInfo channelSetting=channelSettingInfoService.getOne(channelSettingWrapper);
    	//??????????????????
    	ChannelInfo channelInfo = channelInfoService.getById(factor.getChannelId());
    	//??????????????????
    	QueryWrapper<ChannelProductAuth>channelProductAuthWrapper=new QueryWrapper<ChannelProductAuth>();
    	channelProductAuthWrapper.lambda().eq(ChannelProductAuth::getChannelId, channelInfo.getId());
    	channelProductAuthWrapper.lambda().eq(ChannelProductAuth::getProductId, orderInfo.getProductId());
    	ChannelProductAuth channelProductAuth = channelProductAuthService.getOne(channelProductAuthWrapper);
    	//??????????????????
    	String fee = BigDecimalUtil.mulbyFormat(BigDecimalUtil.div(amt, 10000), channelProductAuth.getRate());
    	int randNum=(int)((Math.random()*9+1)*10000000);	
    	String channelOrderNo = DateUtil.format(new Date(), "yyyyMMddHHmmss")+channelInfo.getId()+randNum;
    	ChannelOrderInfo channelOrderInfo=new ChannelOrderInfo();
    	channelOrderInfo.setChannelCode(channelInfo.getChannelCode());
    	channelOrderInfo.setChannelId(channelInfo.getId());
    	channelOrderInfo.setCreateTime(new Date());
    	channelOrderInfo.setOrderId(orderInfo.getId());
    	channelOrderInfo.setStatus(0);
    	channelOrderInfo.setSysOrderNo(orderInfo.getSysOrderNo());
    	channelOrderInfo.setChannelOrderNo(channelOrderNo);
    	channelOrderInfo.setTel(orderInfo.getTel());
    	channelOrderInfo.setAmt(amt.longValue());
    	channelOrderInfo.setSysActAmt(Long.valueOf(fee));
    	
    	boolean saveFlag = channelOrderInfoService.save(channelOrderInfo);
    	orderFlowService.sendOrderFlow(orderInfo.getSysOrderNo(), "????????????????????????  ??????????????????"+channelOrderNo);
    	if(!saveFlag) {
    		orderInfo.setStatus(3);
    		orderInfo.setUpdateTime(new Date());
    		orderInfo.setResultTime(new Date());
    		orderInfoService.updateById(orderInfo);
    		rabbitTemplate.convertAndSend(notifyMerQueue, JSONUtil.toJsonStr(orderInfo));
    		return;
    	}
    	//?????????????????????????????????????????????????????????  ????????????????????????
    	
    	//?????????????????????
    	ChannelPayDTO channelPayDTO = new ChannelPayDTO();
        channelPayDTO.setAddress(address);
        channelPayDTO.setVersion(version);
        channelPayDTO.setChannelCode(channelInfo.getGroupCode());
        channelPayDTO.setAmount(orderInfo.getAmt().toString());
        channelPayDTO.setMobile(orderInfo.getTel());
        channelPayDTO.setAppid(channelSetting.getAppid());
        channelPayDTO.setChannelKey(channelSetting.getMd5Key());
        channelPayDTO.setSysOrderNo(channelOrderNo);
        channelPayDTO.setExt1(orderInfo.getExt1());
        if(orderInfo.getProductType()==6) {
        	channelPayDTO.setPrivinceName(orderInfo.getCity());
        }else {
            channelPayDTO.setPrivinceName(orderInfo.getProvince());
        }
        channelPayDTO.setProductName(orderInfo.getServiceName());
        channelPayDTO.setChannelType(channelInfo.getType());
        channelPayDTO.setNotifyUrl(channelInfo.getNotifyUrl());
        channelPayDTO.setChannelName(channelSetting.getTradePwd());
        orderInfo.setChannelSettingId(channelSetting.getId());
    	orderInfo.setChannelId(channelInfo.getId());
    	orderInfo.setChannelCode(channelInfo.getChannelCode());
    	orderInfo.setRate(channelProductAuth.getRate());
    	orderInfo.setSysActAmt(new BigDecimal(fee));
    	orderInfo.setFee(new BigDecimal(orderInfo.getMerActAmt()).subtract(orderInfo.getSysActAmt()));
		orderInfo.setStatus(1);
    	orderInfo.setUpdateTime(new Date());
    	updateAccount(orderInfo, 1, 3);
    	channelOrderInfo.setStatus(1);
		channelOrderInfoService.updateById(channelOrderInfo);
		boolean flag = orderInfoService.updateById(orderInfo);
		if(!flag) {
			return;
		}
        try {
        	ResultVO resultVO = ChannelUtils.invokePay(channelPayDTO);
//            log.info("???????????????  "+channelInfo.getChannelCode()+" ???????????????"+JSONUtil.toJsonStr(resultVO));
            orderFlowService.sendOrderFlow(orderInfo.getSysOrderNo(), "???????????????  "+channelInfo.getChannelCode()+" ???????????????"+JSONUtil.toJsonStr(resultVO));
            //??????????????????
            //????????????????????????
            if(resultVO.getCode()==200) {
            	ChannelPayRspDTO channelPayRspDTO = (ChannelPayRspDTO) resultVO.getData();
            	if(channelPayRspDTO.getStatus()==1) {
//            		String thirdOrderNo = channelPayRspDTO.getThirdOrderNo();
//            		orderInfo.setThirdOrderNo(thirdOrderNo);
//            		orderInfoService.updateById(orderInfo);
                	rabbitTemplate.convertAndSend(channelInfo.getSearchMq(), JSONUtil.toJsonStr(channelOrderInfo));
                	return;
            	}else if(channelPayRspDTO.getStatus()==3){
            		//????????????  ????????????????????????
            		channelOrderInfo.setStatus(3);
            		channelOrderInfo.setResultDate(new Date());
            		channelOrderInfoService.updateById(channelOrderInfo);
            		updateAccount(orderInfo, 3, 3);
            		//?????????????????????????????????????????????
            		for (int i = 0; i < channelInfoList.size(); i++) {
            			ChannelInfo tmp = channelInfoList.get(i);
            			if(tmp.getId().compareTo(channelInfo.getId())==0) {
            				channelInfoList.remove(i);
            				Date createTime = orderInfo.getCreateTime();
            				long useTime=(new Date().getTime()-createTime.getTime())/1000;
            				if(redisTemplate.hasKey(orderInfo.getSysOrderNo())) {
                				orderInfo.setUseTime(useTime);
                				orderInfoService.updateById(orderInfo);
                				OrderDoubleBookingDto orderDto = (OrderDoubleBookingDto) redisTemplate.opsForValue().get(orderInfo.getSysOrderNo());
                				if(!orderDto.getChannelIdList().contains(orderInfo.getChannelId())) {
                					orderDto.getChannelIdList().add(orderInfo.getChannelId());
                				}
                				orderDto.setOrderInfo(orderInfo);
                				redisTemplate.opsForValue().set(orderInfo.getSysOrderNo(), orderDto,3, TimeUnit.MINUTES);
                			}else {
                				OrderDoubleBookingDto orderDto=new OrderDoubleBookingDto();
                				orderInfo.setUseTime(useTime);
                				orderInfoService.updateById(orderInfo);
                				List<Long> channelIdList =new ArrayList<Long>();
                				channelIdList.add(orderInfo.getChannelId());
                				orderDto.setChannelIdList(channelIdList);
                				orderDto.setOrderInfo(orderInfo);
                				redisTemplate.opsForValue().set(orderInfo.getSysOrderNo(), orderDto,3, TimeUnit.MINUTES);
                			}
            				
            				break;
            			}
        			}
            		sendChannel(channelInfoList,orderInfo);
            	}else {
//            		//???????????????????????????????????????????????????????????????????????????????????????
//            		//????????????  ??????????????????
//                	updateAccount(orderInfo, 1, 3);
                	//??????????????????
                	rabbitTemplate.convertAndSend(channelInfo.getSearchMq(), JSONUtil.toJsonStr(channelOrderInfo));
                	return;
            	}
            }else {
            	orderFlowService.sendOrderFlow(orderInfo.getSysOrderNo(), "?????????????????????  "+channelInfo.getChannelCode()+" ???????????????"+JSONUtil.toJsonStr(resultVO));
//            	//????????????
//            	//??????????????????????????????????????????????????????????????????????????????????????????
//            	//????????????  ??????????????????
//            	updateAccount(orderInfo, 1, 3);
            	rabbitTemplate.convertAndSend(channelInfo.getSearchMq(), JSONUtil.toJsonStr(channelOrderInfo));
        		return;
            }
		} catch (Exception e) {
			orderFlowService.sendOrderFlow(orderInfo.getSysOrderNo(), "?????????????????????  "+channelInfo.getChannelCode());
			e.printStackTrace();
			rabbitTemplate.convertAndSend(channelInfo.getSearchMq(), JSONUtil.toJsonStr(channelOrderInfo));
		}
        
	}
	@Override
	public void noticeMer(OrderInfo orderInfo) {
//		OrderInfo orderInfo = orderInfoService.getById(orderId);
		if(orderInfo==null) {
			return;
		}
		//?????????????????? ?????????????????????????????????
		if(orderInfo.getNoticeUrl()==null) {
			orderInfo.setNoticeStatus(2);
			orderInfoService.updateNoticeById(orderInfo);
			return ;
		}
		SearchOrderRspDto rspDto=new SearchOrderRspDto();
		if(orderInfo.getStatus()==2) {
			rspDto.setCode(OederRspEnum.CODE_10000.code);
	    	rspDto.setMessage(OederRspEnum.CODE_10000.desc);
		}else if(orderInfo.getStatus()==3) {
			rspDto.setCode(OederRspEnum.CODE_10001.code);
	    	rspDto.setMessage(OederRspEnum.CODE_10001.desc);
		}
    	rspDto.setMerNo(orderInfo.getMerNo());
    	rspDto.setOrderAmount(BigDecimalUtil.divd(orderInfo.getAmt(), 100).longValue());
    	rspDto.setOrderNo(orderInfo.getMerOrderNo());
    	rspDto.setOrderTime(DateUtil.format(orderInfo.getCreateTime(), "yyyyMMddHHmmss"));
    	rspDto.setPhone(orderInfo.getTel());
    	rspDto.setResultTime(DateUtil.format(orderInfo.getResultTime(), "yyyyMMddHHmmss"));
    	rspDto.setStatus(orderInfo.getStatus());
    	rspDto.setSysOrderNo(orderInfo.getSysOrderNo());
    	rspDto.setSerialNumber(orderInfo.getSerialNumber());
    	QueryWrapper<MerSecretKey> merSecretKeyWrapper=new QueryWrapper<MerSecretKey>();
    	merSecretKeyWrapper.lambda().eq(MerSecretKey::getMerId, orderInfo.getMerId());
    	MerSecretKey merSecretKey = merSecretKeyService.getOne(merSecretKeyWrapper);
    	String sign = SignUtil.sign(rspDto, merSecretKey.getMd5Key());
    	rspDto.setSign(sign);
    	try {
//    		log.info("notifyReq:  "+rspDto.getSysOrderNo()+"  "+orderInfo.getNoticeUrl()+"     "+JSONUtil.toJsonStr(rspDto));
    		orderFlowService.sendOrderFlow(orderInfo.getSysOrderNo(),"notifyReq:  "+rspDto.getSysOrderNo()+"  "+orderInfo.getNoticeUrl()+"     "+JSONUtil.toJsonStr(rspDto));
    		String rspBody = "";
    		if(orderInfo.getNoticeUrl().contains("127.0.0.1")) {
    			rspBody = HttpRequest.post(orderInfo.getNoticeUrl()).header("Content-Type","application/json").timeout(10000).body(JSONUtil.toJsonStr(rspDto)).execute().body();
    		}else {
    			Proxy proxy=new Proxy(Proxy.Type.HTTP,new InetSocketAddress("156.240.109.127", 60086));
    			rspBody = HttpRequest.post(orderInfo.getNoticeUrl()).setProxy(proxy).header("Content-Type","application/json").timeout(10000).body(JSONUtil.toJsonStr(rspDto)).execute().body();	
    		}
        	
//        	log.info("notifyRsp:  "+rspDto.getSysOrderNo()+" "+rspBody);
        	orderFlowService.sendOrderFlow(orderInfo.getSysOrderNo(),"notifyRsp:  "+rspDto.getSysOrderNo()+" "+rspBody);
        	if("success".equalsIgnoreCase(rspBody)) {
        		orderInfo.setNoticeStatus(1);
        		orderInfoService.updateNoticeById(orderInfo);
        	}else {
        		if(orderInfo.getNoticeCount()>=4) {
        			orderInfo.setNoticeStatus(2);
        		}else {
        			orderInfo.setNoticeCount(orderInfo.getNoticeCount()+1);
        			rabbitTemplate.convertAndSend(notifyMerDelayQueue, JSONUtil.toJsonStr(orderInfo));
        		}
        		orderInfoService.updateNoticeById(orderInfo);
        	}
		} catch (Exception e) {
			e.printStackTrace();
//			log.info("????????????????????? "+rspDto.getSysOrderNo()+" "+e.getMessage());
			orderFlowService.sendOrderFlow(orderInfo.getSysOrderNo(),"????????????????????? "+rspDto.getSysOrderNo()+" "+e.getMessage());
			if(orderInfo.getNoticeCount()>=4) {
    			orderInfo.setNoticeStatus(2);
    		}else {
    			orderInfo.setNoticeCount(orderInfo.getNoticeCount()+1);
    			rabbitTemplate.convertAndSend(notifyMerDelayQueue, JSONUtil.toJsonStr(orderInfo));
    		}
			orderInfoService.updateNoticeById(orderInfo);
		}
	}
	@Override
	public PayOrderRspDto doubliBookingOrder(String sysOrderNo) {
		OrderDoubleBookingDto orderDto = (OrderDoubleBookingDto) redisTemplate.opsForValue().get(sysOrderNo);
		OrderInfo orderInfo = orderDto.getOrderInfo();
		List<Long> channelIdList = orderDto.getChannelIdList();
		//????????????????????????
//    	QueryWrapper<MerProductAuth> merProductAuthWapper=new QueryWrapper<MerProductAuth>();
//    	merProductAuthWapper.lambda().eq(MerProductAuth::getProductId, orderInfo.getProductId());
//    	merProductAuthWapper.lambda().eq(MerProductAuth::getMerId, orderInfo.getMerId());
//    	for (Long channelId : channelIdList) {
//    		merProductAuthWapper.lambda().ne(MerProductAuth::getChannelId, channelId);
//		}
    	
    	//????????????????????????
    	Map<String, Object> authMap=new HashMap<String, Object>();
    	authMap.put("productId", orderInfo.getProductId());
    	authMap.put("companyId", orderInfo.getCompanyId());
    	authMap.put("amt", orderInfo.getAmt());
    	authMap.put("time", orderInfo.getMaxTime()-orderInfo.getUseTime());
    	//????????????????????????
    	List<ChannelInfo> channelInfoList = channelInfoService.selectByProduct(authMap);
    	
    	List<ChannelInfo> newChannelInfoList=new ArrayList<ChannelInfo>();
    	for (ChannelInfo channelInfo : channelInfoList) {
    		boolean flag=true;
    		for (Long id : channelIdList) {
    			ChannelInfo channel = channelInfoService.getById(channelInfo.getId());
    			//????????????????????????????????????????????????
    			if(channel.getIsRepeat()==1) {
    				flag=true;
    				break;
    			}
    			if(channel.getId().intValue()==id.intValue()) {
    				flag=false;
    				break;
    			}
			}
    		if(flag) {
    			newChannelInfoList.add(channelInfo);
    		}
		}
    	sendChannel(newChannelInfoList,orderInfo);
		return null;
	}
	
	@Override
	public void searchChannelOrder(ChannelOrderInfo channelOrderInfo) {
		String sysOrderNo = channelOrderInfo.getSysOrderNo();
		String channelCode = channelOrderInfo.getChannelCode();
		
		channelOrderInfo = channelOrderInfoService.getById(channelOrderInfo.getId());
		if(channelOrderInfo==null||channelOrderInfo.getStatus()!=1) {
			return;
		}
		QueryWrapper<OrderInfo>orderInfoWrapper=new QueryWrapper<OrderInfo>();
		orderInfoWrapper.lambda().eq(OrderInfo::getId, channelOrderInfo.getOrderId());
		orderInfoWrapper.lambda().eq(OrderInfo::getChannelId, channelOrderInfo.getChannelId());
		orderInfoWrapper.lambda().eq(OrderInfo::getStatus, 1);
		OrderInfo orderInfo=orderInfoService.getOne(orderInfoWrapper);
		if(orderInfo==null) {
			return;
		}
		
		QueryWrapper<ChannelSettingInfo>channelSettingWrapper=new QueryWrapper<ChannelSettingInfo>();
    	channelSettingWrapper.lambda().eq(ChannelSettingInfo::getChannelId, channelOrderInfo.getChannelId());
    	channelSettingWrapper.lambda().eq(ChannelSettingInfo::getCompanyId, orderInfo.getCompanyId());
    	ChannelSettingInfo channelSetting=channelSettingInfoService.getOne(channelSettingWrapper);
    	ChannelInfo channelInfo = channelInfoService.getById(channelOrderInfo.getChannelId());
		ChannelQueryDTO dto=new ChannelQueryDTO();
		dto.setAddress(address);
		dto.setVersion(version);
		dto.setChannelCode(channelInfo.getGroupCode());
		dto.setAppid(channelSetting.getAppid());
		dto.setSysOrderNo(channelOrderInfo.getChannelOrderNo());
		dto.setChannelType(channelInfo.getType());
		dto.setChannelKey(channelSetting.getMd5Key());
		dto.setThirdOrderNo(orderInfo.getThirdOrderNo());
		try {
			ResultVO resultVO = ChannelUtils.invokeQuery(dto);
	        if(resultVO.getCode()==200) {
	        	//???????????? ?????????????????????????????????????????????????????????????????????????????????????????????
	    		Long maxTime = orderInfo.getMaxTime();
	    		Date createTime = orderInfo.getCreateTime();
	    		long useTime=(new Date().getTime()-createTime.getTime())/1000;
	    		
	        	//?????????????????? ?????????????????????????????????  ???????????????
	        	ChannelPayRspDTO channelPayRspDTO = (ChannelPayRspDTO) resultVO.getData();
	        	channelOrderInfo.setSerialNumber(channelPayRspDTO.getSerialNumber());
	    		orderInfo.setSerialNumber(channelPayRspDTO.getSerialNumber());        	
	        	if(channelPayRspDTO.getStatus()==2) {
	        		//???????????? ????????????????????????
	        		channelOrderInfo.setStatus(2);
	    			channelOrderInfo.setResultDate(new Date());
	    			boolean saveChannelOrderRs = channelOrderInfoService.updateById(channelOrderInfo);
	    			//??????????????????  ????????????????????????
	    			if(!saveChannelOrderRs) {
//	    				log.info("???????????????,?????????????????????"+searchOrderQueue+".delay");
	    				orderFlowService.sendOrderFlow(orderInfo.getSysOrderNo(),"??????????????????????????????????????????????????????"+searchOrderQueue+".delay");
	            		orderInfo.setSearchCount(orderInfo.getSearchCount()+1);
	            		orderInfoService.updateSearchCountById(orderInfo);
	            		rabbitTemplate.convertAndSend(searchOrderQueue+".delay", JSONUtil.toJsonStr(channelOrderInfo));
	            		return;
	    			}
		        	orderInfo.setStatus(channelPayRspDTO.getStatus());
		        	orderInfo.setUpdateTime(new Date());
		        	orderInfo.setResultTime(new Date());
	//	        	orderInfo.setSerialNumber(channelPayRspDTO.getSerialNumber());
		        	boolean saveOrderRs = orderInfoService.updateById(orderInfo);
		        	if(!saveOrderRs) {
//	    				log.info("???????????????,?????????????????????"+searchOrderQueue+".delay");
	    				orderFlowService.sendOrderFlow(orderInfo.getSysOrderNo(),"??????????????????????????????????????????????????????"+searchOrderQueue+".delay");
	            		orderInfo.setSearchCount(orderInfo.getSearchCount()+1);
	            		orderInfoService.updateSearchCountById(orderInfo);
	            		rabbitTemplate.convertAndSend(searchOrderQueue+".delay", JSONUtil.toJsonStr(channelOrderInfo));
	            		return;
	    			}
		        	//????????????
		        	//????????????  ??????????????????
		        	updateAccount(orderInfo, 2, 3);
		        	//??????????????????
		        	String jsonStr = JSONUtil.toJsonStr(orderInfo);
		        	rabbitTemplate.convertAndSend(notifyMerQueue, jsonStr);
	        	}else if (channelPayRspDTO.getStatus()==3){
	        		channelOrderInfo.setStatus(3);
	    			channelOrderInfo.setResultDate(new Date());
	    			boolean saveChannelOrderRs = channelOrderInfoService.updateById(channelOrderInfo);
	    			//??????????????????  ????????????????????????
	    			if(!saveChannelOrderRs) {
//	    				log.info("???????????????,?????????????????????"+searchOrderQueue+".delay");
	    				orderFlowService.sendOrderFlow(orderInfo.getSysOrderNo(),"??????????????????????????????????????????????????????"+searchOrderQueue+".delay");
	            		orderInfo.setSearchCount(orderInfo.getSearchCount()+1);
	            		orderInfoService.updateSearchCountById(orderInfo);
	            		rabbitTemplate.convertAndSend(searchOrderQueue+".delay", JSONUtil.toJsonStr(channelOrderInfo));
	            		return;
	    			}
	        		//????????????  ??????????????????
		        	updateAccount(orderInfo, 3, 3);
	        		//???????????????????????????????????????
	        		if(maxTime-useTime>0) {
	        			if(redisTemplate.hasKey(orderInfo.getSysOrderNo())) {
	        				orderInfo.setUseTime(useTime);
	        				orderInfoService.updateById(orderInfo);
	        				OrderDoubleBookingDto orderDto = (OrderDoubleBookingDto) redisTemplate.opsForValue().get(orderInfo.getSysOrderNo());
	        				if(!orderDto.getChannelIdList().contains(orderInfo.getChannelId())) {
	        					orderDto.getChannelIdList().add(orderInfo.getChannelId());
	        				}
	        				orderDto.setOrderInfo(orderInfo);
	        				redisTemplate.opsForValue().set(orderInfo.getSysOrderNo(), orderDto,3, TimeUnit.MINUTES);
	        				doubliBookingOrder(orderInfo.getSysOrderNo());
	        			}else {
	        				OrderDoubleBookingDto orderDto=new OrderDoubleBookingDto();
	        				orderInfo.setUseTime(useTime);
	        				orderInfoService.updateById(orderInfo);
	        				List<Long> channelIdList =new ArrayList<Long>();
	        				channelIdList.add(orderInfo.getChannelId());
	        				orderDto.setChannelIdList(channelIdList);
	        				orderDto.setOrderInfo(orderInfo);
	        				redisTemplate.opsForValue().set(orderInfo.getSysOrderNo(), orderDto,3, TimeUnit.MINUTES);
	        				doubliBookingOrder(orderInfo.getSysOrderNo());
	        			}
	        		}else {
	        			//??????????????????????????????
	        			orderInfo.setStatus(channelPayRspDTO.getStatus());
	    	        	orderInfo.setUpdateTime(new Date());
	    	        	orderInfo.setResultTime(new Date());
	    	        	boolean saveOrderRs = orderInfoService.updateById(orderInfo);
	    	        	if(!saveOrderRs) {
//	        				log.info("???????????????,?????????????????????"+searchOrderQueue+".delay");
	    	        		orderFlowService.sendOrderFlow(orderInfo.getSysOrderNo(),"??????????????????????????????????????????????????????"+searchOrderQueue+".delay");
	                		orderInfo.setSearchCount(orderInfo.getSearchCount()+1);
	                		orderInfoService.updateSearchCountById(orderInfo);
	                		rabbitTemplate.convertAndSend(searchOrderQueue+".delay", JSONUtil.toJsonStr(channelOrderInfo));
	                		return;
	        			}
	    	        	//??????????????????
	    	        	rabbitTemplate.convertAndSend(notifyMerQueue, JSONUtil.toJsonStr(orderInfo));
	        		}
	        	}else if(channelPayRspDTO.getStatus()==4){
	        		//????????????
	        		//?????????????????????
	            	ChannelPayDTO channelPayDTO = new ChannelPayDTO();
	                channelPayDTO.setAddress(address);
	                channelPayDTO.setVersion(version);
	                channelPayDTO.setChannelCode(channelInfo.getGroupCode());
	                channelPayDTO.setAmount(orderInfo.getAmt().toString());
	                channelPayDTO.setMobile(orderInfo.getTel());
	                channelPayDTO.setAppid(channelSetting.getAppid());
	                channelPayDTO.setChannelKey(channelSetting.getMd5Key());
	                channelPayDTO.setSysOrderNo(channelOrderInfo.getChannelOrderNo());
	                if(orderInfo.getProductType()==6) {
	                	channelPayDTO.setPrivinceName(orderInfo.getCity());
	                }else {
	                    channelPayDTO.setPrivinceName(orderInfo.getProvince());
	                }
	                channelPayDTO.setProductName(orderInfo.getServiceName());
	                channelPayDTO.setChannelType(channelInfo.getType());
	                channelPayDTO.setNotifyUrl(channelInfo.getNotifyUrl());
	                channelPayDTO.setChannelName(channelSetting.getTradePwd());
	                resultVO = ChannelUtils.invokePay(channelPayDTO);
	        	}else {
//	        		log.info("???????????????,?????????????????????"+searchOrderQueue+".delay");
	        		orderFlowService.sendOrderFlow(orderInfo.getSysOrderNo(),"??????????????????????????????????????????????????????"+searchOrderQueue+".delay");
	        		orderInfo.setSearchCount(orderInfo.getSearchCount()+1);
	        		orderInfoService.updateSearchCountById(orderInfo);
	        		rabbitTemplate.convertAndSend(searchOrderQueue+".delay", JSONUtil.toJsonStr(channelOrderInfo));
	        	}
	        }else {
//	        	log.info("???????????????,?????????????????????"+searchOrderQueue+".delay");
	        	orderFlowService.sendOrderFlow(orderInfo.getSysOrderNo(),"???????????????,?????????????????????"+searchOrderQueue+".delay");
	    		orderInfo.setSearchCount(orderInfo.getSearchCount()+1);
	    		orderInfoService.updateSearchCountById(orderInfo);
	    		rabbitTemplate.convertAndSend(searchOrderQueue+".delay", JSONUtil.toJsonStr(channelOrderInfo));
	        }
		} catch (Exception e) {
			e.printStackTrace();
//			log.info("???????????????,?????????????????????"+searchOrderQueue+".delay");
			orderFlowService.sendOrderFlow(orderInfo.getSysOrderNo(),"????????????,?????????????????????"+searchOrderQueue+".delay");
    		orderInfo.setSearchCount(orderInfo.getSearchCount()+1);
    		orderInfoService.updateSearchCountById(orderInfo);
    		rabbitTemplate.convertAndSend(searchOrderQueue+".delay", JSONUtil.toJsonStr(channelOrderInfo));
		}
	}
	@Override
	public SearchMerAccountDto searchMerAccount(SearchOrderDto searchOrderDto, MerInfo merInfo) {
		QueryWrapper<MerAccount> merAccountWrapper=new QueryWrapper<MerAccount>();
    	merAccountWrapper.lambda().eq(MerAccount::getMerId, merInfo.getId());
    	MerAccount merAccount = merAccountService.getOne(merAccountWrapper);
    	SearchMerAccountDto dto=new SearchMerAccountDto(); 
    	dto.setCreditAmt(merAccount.getCreditAmt()/100);
    	dto.setCreditBalanceAmt(merAccount.getCreditBalanceAmt()/100);
    	dto.setCreditFixAmt(merAccount.getCreditFixAmt()/100);
    	dto.setCreditUseAmt(merAccount.getCreditUseAmt()/100);
    	dto.setMerNo(merInfo.getMerNo());
		return dto;
	}
	/**
	 * 
	 * @param orderInfo
	 * @param type ???????????? 1????????????  2???????????? 3????????????
	 * @param accountType 1?????????????????? 2??????????????????  3????????????
	 * @return
	 */
	private boolean updateAccount(OrderInfo orderInfo,int type,int accountType) {
		boolean rs=true;
		//????????????
		try {
			AccountUpdateDto accountDto=new AccountUpdateDto();
			accountDto.setType(type);
	    	accountDto.setAccountType(accountType);
	    	accountDto.setMerId(orderInfo.getMerId());
	    	accountDto.setSysAmt(orderInfo.getSysActAmt().longValue());
	    	accountDto.setMerAmt(orderInfo.getMerActAmt().longValue());
	    	accountDto.setChannelId(orderInfo.getChannelId());
	    	accountDto.setOrderId(orderInfo.getId());
	    	rabbitTemplate.convertAndSend(updateAccountQueue, JSONUtil.toJsonStr(accountDto));
		} catch (Exception e) {
			e.printStackTrace();
			rs=false;
		}
    	return rs;
	}
	
}
