package com.abcd.modules.pay.common;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONUtil;

import com.abcd.modules.channel.entity.ChannelInfo;
import com.abcd.modules.channel.entity.ChannelMerAuth;
import com.abcd.modules.channel.entity.MerChannelRoute;
import com.abcd.modules.channel.service.IChannelInfoService;
import com.abcd.modules.channel.service.IChannelStartService;
import com.abcd.modules.channel.service.IMerChannelRouteService;
import com.abcd.modules.factor.entity.FactorInfo;
import com.abcd.modules.factor.entity.FactorLog;
import com.abcd.modules.factor.entity.FactorType;
import com.abcd.modules.factor.service.IFactorInfoService;
import com.abcd.modules.factor.service.IFactorLogService;
import com.abcd.modules.factor.service.IFactorTypeService;
import com.abcd.modules.log.service.OrderFlowService;
import com.abcd.modules.merchant.entity.MerProductAuth;
import com.abcd.modules.order.entity.OrderInfo;
import com.abcd.modules.order.service.IOrderInfoService;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import javax.annotation.Resource;

/**
 * 通道信息接口实现
 * @author 
 */
@Slf4j
@Service
@Transactional
public class CountServiceImpl  implements CountService {
	@Autowired
    private IOrderInfoService iOrderInfoService;
    @Autowired
    private IFactorTypeService iFactorTypeService;
    @Autowired
    private IFactorInfoService iFactorInfoService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private IChannelInfoService iChannelInfoService;
    @Autowired
    private IChannelStartService iChannelStartService;
    @Autowired
    private IFactorLogService iFactorLogService;
    @Autowired
    private IMerChannelRouteService merChannelRouteService;
    @Resource
	private OrderFlowService orderFlowService;
    @Override
    public  Factor count(List<ChannelInfo> channelList, OrderInfo orderInfo){
//    	log.info("订单号："+orderInfo.getSysOrderNo()+"可用通道列表"+JSONUtil.toJsonStr(channelList));
    	orderFlowService.sendOrderFlow(orderInfo.getSysOrderNo(), "可用通道列表"+JSONUtil.toJsonStr(channelList));
    	//判断初始化通道是否存在
        Factor factorStart = iChannelStartService.ChannelStart(orderInfo,channelList);
        if(factorStart!=null){
//        	log.info("订单号："+orderInfo.getSysOrderNo()+"初始化选择通道："+factorStart.getChannelCode());
        	orderFlowService.sendOrderFlow(orderInfo.getSysOrderNo(), "初始化选择通道： 【"+factorStart.getChannelCode()+"】");
        	for (ChannelInfo channelInfo : channelList) {
				if(factorStart.getChannelId().intValue()==channelInfo.getId().intValue()) {
					return factorStart;
				}
			}
        }
        orderFlowService.sendOrderFlow(orderInfo.getSysOrderNo(), "无可用初始化通道： ");
        //若未有通道启动则正常通道路由选择
        
        long begin=System.currentTimeMillis();
        //固定因子数据
        Map<String,String> map = new HashMap<String,String>();
        map = BeanUtil.toBean(orderInfo,HashMap.class);

        //因子测试数据,组装符合的通道列表
        List<Factor> list = new ArrayList<>();
        for(ChannelInfo channel:channelList){
            Factor factor = new Factor.Builder<>().channelCode(channel.getChannelCode()).channelId(channel.getId()).total(1L).rate(channel.getRate()).build();
            list.add(factor);
        }
        //没有通道则返回null
        if(list.size()<1){
            return null;
        }
        
        QueryWrapper<FactorType> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(FactorType::getStatus,1);
        List<FactorType> factorTypeList = iFactorTypeService.list(queryWrapper);
        int step =1;
        for(FactorType factorType:factorTypeList){
            //没有通道则返回null
            if(list.size()<1){
                return null;
            }
            //根据不同的类型进入到不同的方法中
            switch(factorType.getType()){
                case FactorConstant.TYPE_1:eq(list,factorType,map,orderInfo,step);break;
                case FactorConstant.TYPE_2:add(list,factorType,map,orderInfo,step);break;
                case FactorConstant.TYPE_3:between(list,factorType,map,orderInfo,step);break;
                case FactorConstant.TYPE_4:betweenTime(list,factorType,map);break;
                case FactorConstant.TYPE_5:greater(list,factorType,map);break;
                case FactorConstant.TYPE_6:less(list,factorType,map);break;
                case FactorConstant.TYPE_7:betweenRate(list,factorType,map,orderInfo,step);break;
                case FactorConstant.TYPE_8:betweenKuCunRate(list,factorType,map,orderInfo,step);break;
                //类型错误不做任何操作进行下一步
                default:break;
            }
            step++;
        }
//        log.info("订单号："+orderInfo.getSysOrderNo()+"满足通道需求的列表为:"+ JSON.toJSONString(list));
        orderFlowService.sendOrderFlow(orderInfo.getSysOrderNo(),"满足通道需求的列表为:"+ JSON.toJSONString(list));
        //若满足条件通道为0则进行返回
        if(list.size()==0){
            return null;
        }
        //若满足条件通道为1则进行返回
        if(list.size()==1){
            if(list.get(0).getTotal()==0){
                return null;
            }
            Factor factorMax  = list.get(0);
            //写入日志
            factorLog(factorMax,orderInfo,step);
//            log.info("通道权重因子计算最终最优通道为：【"+factorMax.getChannelCode()+"】总得分为："+factorMax.getTotal());
            orderFlowService.sendOrderFlow(orderInfo.getSysOrderNo(),"通道权重因子计算最终最优通道为：【"+factorMax.getChannelCode()+"】总得分为："+factorMax.getTotal());
            return list.get(0);
        }
        //人工进行干预的通道因素加分（通道停止后可能会出现通道评分太低，进行加分后让通道正常运行）
//        for(Factor factor:list){
//            if(redisTemplate.hasKey(FactorConstant.FACTOR_CHANNEL+factor.getChannelCode())){
//                factor.setTotal(factor.getTotal()+Integer.valueOf(redisTemplate.opsForValue().get(FactorConstant.FACTOR_CHANNEL+factor.getChannelCode()).toString()));
//            }
//        }
        //找出得分最高的通道信息
        int rand = RandomUtil.randomInt(list.size());
        Factor factorMax  = list.get(rand);
        for(Factor factor:list){
            if(factor.getTotal()>factorMax.getTotal()){
                factorMax=factor;
            }
        }
        if(factorMax.getTotal()==0){
            return null;
        }
        long end=System.currentTimeMillis();
        long time = end-begin;
        //写入日志
        factorLog(factorMax,orderInfo,step);
//        log.info("通道权重因子计算最终最优通道为：【"+factorMax.getChannelCode()+"】总得分为："+factorMax.getTotal()+",耗时【"+time+"】毫秒");
        orderFlowService.sendOrderFlow(orderInfo.getSysOrderNo(),"通道权重因子计算最终最优通道为：【"+factorMax.getChannelCode()+"】总得分为："+factorMax.getTotal()+",耗时【"+time+"】毫秒");
//        log.info("订单计算最终最优通道为:【"+factorMax.getChannelCode()+"】总得分为："+factorMax.getTotal(),"factor",orderInfo.getAmt(),factorMax.getChannelCode(),factorMax.getTotal());
        return factorMax;

    }



    /**
     * 静态路由因子比较型计算(dataKey静态因子与数据字段匹配，动态因子与redis键匹配)
     * @author 
     */
    private  void eq(List<Factor> list,FactorType factorType,Map<String,String> map,OrderInfo orderInfo,int step){
        for(Factor factor : list){
//            log.info("通道:【"+factor.getChannelCode()+"】计算【"+factorType.getFactorName()+"】开始");
            orderFlowService.sendOrderFlow(orderInfo.getSysOrderNo(), "通道:【"+factor.getChannelCode()+"】计算【"+factorType.getFactorName()+"】开始");
            QueryWrapper<FactorInfo> queryInfoWrapper = new QueryWrapper<>();
           if(factorType.getIsChannel()==FactorConstant.IS_CHANNEL){
                queryInfoWrapper.lambda().eq(FactorInfo::getChannelId,factor.getChannelId());
            }
            queryInfoWrapper.lambda().eq(FactorInfo::getFactorTypeId,factorType.getId());
            String value = "";
            //匹配因子数据,若为静态因子则进行匹配参数值
            if(factorType.getIsStatic()==FactorConstant.IS_STATIC){
            	value=String.valueOf(map.get(factorType.getDataKey()));
                queryInfoWrapper.lambda().eq(FactorInfo::getValue,map.get(factorType.getDataKey()));
            }
            //若为动态因子，redis主键存在则找对应通道redis值做比较，若redis为空则不进行计算分数值
            else{
                //如果不存在，则设置错误参数信息
                String redisValue = redisValue(factorType,factor,map);
                value=redisValue;
                if(redisValue.equals("error")){
                    queryInfoWrapper.lambda().eq(FactorInfo::getValue,"error");
                }else {
                    queryInfoWrapper.lambda().eq(FactorInfo::getValue,redisValue+"_"+factor.getChannelCode());
                }
            }
//                queryInfoWrapper.lambda().orderByAsc()
            FactorInfo factorInfo = iFactorInfoService.getOne(queryInfoWrapper);

            //判断该因子是否存在,则进行加分计算
            if(factorInfo!=null){
                factor.setTotal(factor.getTotal()*factorInfo.getWeight());
                factorLog(factorType,factor,factorInfo,orderInfo,value,step);
//                log.info("通道:【"+factor.getChannelCode()+"】在计算【"+factorType.getFactorName()+"="+factorInfo.getValue()+"】后得分为:"+factor.getTotal());
                orderFlowService.sendOrderFlow(orderInfo.getSysOrderNo(), "通道:【"+factor.getChannelCode()+"】在计算【"+factorType.getFactorName()+"="+factorInfo.getValue()+"】后得分为:"+factor.getTotal());
            }else {
                //判断该因子是否允许通过，不允许则进行踢出
                if(factorType.getIsPass().equals(FactorConstant.IS_NO_PASS)){
                	factor.setTotal(0l);
//                    log.info("通道:【"+factor.getChannelCode()+"】在计算【"+factorType.getFactorName()+"】中因不允许通过被排除");
                    orderFlowService.sendOrderFlow(orderInfo.getSysOrderNo(), "通道:【"+factor.getChannelCode()+"】在计算【"+factorType.getFactorName()+"】中因不允许通过被排除");
                }
            }


        }
    }
    /**
     * 路由因子加分型计算
     * @author 
     */
    private  void add(List<Factor> list,FactorType factorType,Map<String,String> map,OrderInfo orderInfo,int step){
        for(Factor factor : list){
//            log.info("通道:【"+factor.getChannelCode()+"】计算【"+factorType.getFactorName()+"】开始");
            orderFlowService.sendOrderFlow(orderInfo.getSysOrderNo(), "通道:【"+factor.getChannelCode()+"】计算【"+factorType.getFactorName()+"】开始");
            QueryWrapper<FactorInfo> queryInfoWrapper = new QueryWrapper<>();
           if(factorType.getIsChannel()==FactorConstant.IS_CHANNEL){
                queryInfoWrapper.lambda().eq(FactorInfo::getChannelId,factor.getChannelId());
            }
            queryInfoWrapper.lambda().eq(FactorInfo::getFactorTypeId,factorType.getId());
            String value = "";
            //匹配因子数据,若为静态因子则进行匹配参数值
            if(factorType.getIsStatic()==FactorConstant.IS_STATIC){
            	value=String.valueOf(map.get(factorType.getDataKey()));
                queryInfoWrapper.lambda().eq(FactorInfo::getValue,map.get(factorType.getDataKey()));
            }
            //若为动态因子，redis主键存在则找对应通道redis值做比较，若redis为空则不进行计算分数值
            else{

                //如果不存在，则设置错误参数信息
                String redisValue = redisValue(factorType,factor,map);
                value=redisValue;
                if(redisValue.equals("error")){
                    queryInfoWrapper.lambda().eq(FactorInfo::getValue,"error");
                }else {
                    queryInfoWrapper.lambda().eq(FactorInfo::getValue,redisValue+"_"+factor.getChannelCode());
                }
            }
            //不做匹配因子数据，有则加分，无则通过
            FactorInfo factorInfo = iFactorInfoService.getOne(queryInfoWrapper);
            //判断该因子是否存在,则进行加分计算
            if(factorInfo!=null){
                factor.setTotal(factor.getTotal()*factorInfo.getWeight());
                factorLog(factorType,factor,factorInfo,orderInfo,value,step);
//                log.info("通道:【"+factor.getChannelCode()+"】在计算【"+factorType.getFactorName()+"="+factorInfo.getValue()+"】后得分为:"+factor.getTotal());
                orderFlowService.sendOrderFlow(orderInfo.getSysOrderNo(), "通道:【"+factor.getChannelCode()+"】在计算【"+factorType.getFactorName()+"="+factorInfo.getValue()+"】后得分为:"+factor.getTotal());
            }else {
                //判断该因子是否允许通过，不允许则进行踢出
                if(factorType.getIsPass().equals(FactorConstant.IS_NO_PASS)){
                	factor.setTotal(0l);
//                    log.info("通道:【"+factor.getChannelCode()+"】在计算【"+factorType.getFactorName()+"】中因不允许通过被排除");
                    orderFlowService.sendOrderFlow(orderInfo.getSysOrderNo(), "通道:【"+factor.getChannelCode()+"】在计算【"+factorType.getFactorName()+"】中因不允许通过被排除");
                }
            }
        }
    }
    /**
     * 路由因子数据区间计算(dataKey静态因子与数据字段匹配，动态因子与redis键匹配)
     * @author 
     */
    private  void between(List<Factor> list,FactorType factorType,Map<String,String> map,OrderInfo orderInfo,int step){
        for(Factor factor : list){
//            log.info("通道:【"+factor.getChannelCode()+"】计算【"+factorType.getFactorName()+"】开始");
            orderFlowService.sendOrderFlow(orderInfo.getSysOrderNo(), "通道:【"+factor.getChannelCode()+"】计算【"+factorType.getFactorName()+"】开始");
            QueryWrapper<FactorInfo> queryInfoWrapper = new QueryWrapper<>();
            //是否限制于某通道
            if(factorType.getIsChannel()==FactorConstant.IS_CHANNEL){
                queryInfoWrapper.lambda().eq(FactorInfo::getChannelId,factor.getChannelId());
            }

            queryInfoWrapper.lambda().eq(FactorInfo::getFactorTypeId,factorType.getId());
            //匹配因子数据,若为静态因子则进行匹配参数值
            String value = "";
            if(factorType.getIsStatic()==FactorConstant.IS_STATIC){
            	value=String.valueOf(map.get(factorType.getDataKey()));
                //大于等于
                queryInfoWrapper.lambda().ge(FactorInfo::getMaxData,String.valueOf(map.get(factorType.getDataKey())));
                //小于
                queryInfoWrapper.lambda().lt(FactorInfo::getMinData,String.valueOf(map.get(factorType.getDataKey())));
            }
            //若为动态因子，redis主键存在则找对应通道redis值做比较，若redis为空则不进行计算分数值
            else{

                //如果不存在，则设置错误参数信息
                String redisValue = redisValue(factorType,factor,map);
                value = redisValue;
                if(redisValue.equals("error")){
                    queryInfoWrapper.lambda().eq(FactorInfo::getValue,"error");
                }else {
                    //大于等于
                    queryInfoWrapper.lambda().ge(FactorInfo::getMaxData,Double.valueOf(redisValue));
                    //小于
                    queryInfoWrapper.lambda().lt(FactorInfo::getMinData,Double.valueOf(redisValue));
                }
            }
//                queryInfoWrapper.lambda().orderByAsc()
            FactorInfo factorInfo = iFactorInfoService.getOne(queryInfoWrapper);

            //判断该因子是否存在,则进行加分计算
            if(factorInfo!=null){
                factor.setTotal(factor.getTotal()*factorInfo.getWeight());
//                factor.setTotal(factor.getTotal().multiply(BigDecimal.valueOf(factorInfo.getWeight())));
                //写入日志
                factorLog(factorType,factor,factorInfo,orderInfo,value,step);
//                log.info("通道:【"+factor.getChannelCode()+"】在计算【"+factorType.getFactorName()+"】后得分为:"+factor.getTotal());
                orderFlowService.sendOrderFlow(orderInfo.getSysOrderNo(), "通道:【"+factor.getChannelCode()+"】在计算【"+factorType.getFactorName()+"】后得分为:"+factor.getTotal());
            }else {
                //判断该因子是否允许通过，不允许则进行踢出
                if(factorType.getIsPass().equals(FactorConstant.IS_NO_PASS)){
                	factor.setTotal(0l);
//                    log.info("通道:【"+factor.getChannelCode()+"】在计算【"+factorType.getFactorName()+"】中因不允许通过被排除");
                    orderFlowService.sendOrderFlow(orderInfo.getSysOrderNo(), "通道:【"+factor.getChannelCode()+"】在计算【"+factorType.getFactorName()+"】中因不允许通过被排除");
                }
            }
        }
    }
    /**
     * 路由因子数据区间计算(dataKey静态因子与数据字段匹配，动态因子与redis键匹配)
     * @author 
     */
    private  void betweenKuCunRate(List<Factor> list,FactorType factorType,Map<String,String> map,OrderInfo orderInfo,int step){
    	for(Factor factor : list){
//    		log.info("sysOrderNo:"+orderInfo.getSysOrderNo()+"通道:【"+factor.getChannelCode()+"】计算【"+factorType.getFactorName()+"】开始");
    		orderFlowService.sendOrderFlow(orderInfo.getSysOrderNo(), "通道:【"+factor.getChannelCode()+"】计算【"+factorType.getFactorName()+"】开始");
    		QueryWrapper<FactorInfo> queryInfoWrapper = new QueryWrapper<>();
    		//是否限制于某通道
    		if(factorType.getIsChannel()==FactorConstant.IS_CHANNEL){
    			queryInfoWrapper.lambda().eq(FactorInfo::getChannelId,factor.getChannelId());
    		}
    		
    		queryInfoWrapper.lambda().eq(FactorInfo::getFactorTypeId,factorType.getId());
    		queryInfoWrapper.lambda().eq(FactorInfo::getValue,orderInfo.getAmt());
    		FactorInfo factorInfo = iFactorInfoService.getOne(queryInfoWrapper);
    		if(factorInfo==null) {
    			continue;
    		}
    		//取出通道最大订单和库存率进行比较，选出较小的值来判断是否通过
    		Integer maxData = factorInfo.getMaxData();
    		double rate = factorInfo.getRate();
    		//取出订单数
    		String value = "";
    		//判断最近时间的订单
    		String sqlTimeStr = "SELECT min(create_time) " +
					"from order_info " +
					"where create_time>=NOW()-INTERVAL 5 MINUTE " +
					"and create_time<=NOW()" +
					"and channel_code= '"+factor.getChannelCode()+"' "+
					"and amt = "+orderInfo.getAmt();
    		String time = iOrderInfoService.queryStringBySql(sqlTimeStr);
//    		log.info("sysOrderNo:"+orderInfo.getSysOrderNo()+"5分钟类最早订单时间   "+time);
    		orderFlowService.sendOrderFlow(orderInfo.getSysOrderNo(), "5分钟类最早订单时间   "+time);
    		//5分钟内没有订单直接返回
    		String kucunshu = redisValue(factorType,factor,map);
//    		log.info("KuCunsysOrderNo:"+orderInfo.getSysOrderNo()+"当前库存数量   "+kucunshu);
    		orderFlowService.sendOrderFlow(orderInfo.getSysOrderNo(), "当前库存数量   "+kucunshu);
    		if(kucunshu==null||"".equals(kucunshu)||"0".equals(kucunshu)) {
    			factor.setTotal(0l);
    			factorLog(factorType,factor,factorInfo,orderInfo,value,step);
    			continue;
    		}
    		if((time==null||"".equals(time))) {
    			factor.setTotal(factor.getTotal()*factorInfo.getWeight());
    			factorLog(factorType,factor,factorInfo,orderInfo,value,step);
    			continue;
    		}
    		DateTime dateTime = DateUtil.parse(time, "yyyy-MM-dd HH:mm:ss");
    		Long s=(System.currentTimeMillis() - dateTime.getTime()) / (1000 * 60);
//    		log.info("KuCunsysOrderNo:"+orderInfo.getSysOrderNo()+"计算订单数量分钟数   "+s);
    		orderFlowService.sendOrderFlow(orderInfo.getSysOrderNo(), "计算订单数量分钟数   "+s);
    		s=s+1;
    		String sqlStr = "SELECT count(id) " +
    				"from order_info " +
    				"where create_time>=NOW()-INTERVAL 5 MINUTE " +
    				"and create_time<=NOW()" +
    				"and channel_code= '"+factor.getChannelCode()+"' "+
    				"and amt = "+orderInfo.getAmt();
			String result = iOrderInfoService.queryStringBySql(sqlStr);
//			log.info("KuCunsysOrderNo:"+orderInfo.getSysOrderNo()+"5分钟内订单总数   "+result);	
			orderFlowService.sendOrderFlow(orderInfo.getSysOrderNo(), "5分钟内订单总数   "+result);
			//选出较小的值
			Double minData=Double.valueOf(kucunshu)*rate;
//			log.info("KuCunsysOrderNo:"+orderInfo.getSysOrderNo()+"当前库存比例   "+rate);	
			orderFlowService.sendOrderFlow(orderInfo.getSysOrderNo(), "当前库存比例   "+rate);
//			log.info("KuCunsysOrderNo:"+orderInfo.getSysOrderNo()+"当前库存数量*比例   "+minData);	
			orderFlowService.sendOrderFlow(orderInfo.getSysOrderNo(), "当前库存数量*比例   "+minData);
//			log.info("KuCunsysOrderNo:"+orderInfo.getSysOrderNo()+"固定比较订单数量   "+maxData);	
			orderFlowService.sendOrderFlow(orderInfo.getSysOrderNo(), "固定比较订单数量   "+maxData);
			//maxData= 20   minData=9.2
			if(maxData<minData) {
				minData=Double.valueOf(maxData);
			}
			
//			log.info("KuCunsysOrderNo:"+orderInfo.getSysOrderNo()+"找出最终比较值   "+minData);	
			orderFlowService.sendOrderFlow(orderInfo.getSysOrderNo(), "找出最终比较值   "+minData);
			Long rs=Long.valueOf(result)/s;
//			log.info("KuCunsysOrderNo:"+orderInfo.getSysOrderNo()+"每分钟订单数   "+rs);
			orderFlowService.sendOrderFlow(orderInfo.getSysOrderNo(), "每分钟订单数   "+rs);
    		if(rs<minData) {
//    			log.info("KuCunsysOrderNo:"+orderInfo.getSysOrderNo()+"小于当前库存   ");
//    			log.info("KuCunsysOrderNo:"+orderInfo.getSysOrderNo()+"当前:"+JSONUtil.toJsonStr(factor));
    			factor.setTotal(factor.getTotal()*factorInfo.getWeight());
//    			log.info("KuCunsysOrderNo:"+orderInfo.getSysOrderNo()+"计算库存得分后:"+JSONUtil.toJsonStr(factor));
//              factor.setTotal(factor.getTotal().multiply(BigDecimal.valueOf(factorInfo.getWeight())));
	  			//写入日志
	  			factorLog(factorType,factor,factorInfo,orderInfo,value,step);
//	  			log.info("通道:【"+factor.getChannelCode()+"】在计算【"+factorType.getFactorName()+"】后得分为:"+factor.getTotal());
    		}else {
//    			log.info("KuCunsysOrderNo:"+orderInfo.getSysOrderNo()+"大于当前库存   ");
    			factor.setTotal(0l);
//				log.info("通道:【"+factor.getChannelCode()+"】在计算【"+factorType.getFactorName()+"】中因不允许通过被排除. 库存数量："+minData+" 系统订单数量："+Double.valueOf(result));
    		}
    	}
    }
    /**
     * 路由因子数据区间计算(dataKey静态因子与数据字段匹配，动态因子与redis键匹配)
     * @author 
     */
    private  void chenggongdanshu(List<Factor> list,FactorType factorType,Map<String,String> map,OrderInfo orderInfo,int step){
    	for(Factor factor : list){
    		log.info("通道:【"+factor.getChannelCode()+"】计算【"+factorType.getFactorName()+"】开始");
    		
    		QueryWrapper<FactorInfo> queryInfoWrapper = new QueryWrapper<>();
    		//是否限制于某通道
    		if(factorType.getIsChannel()==FactorConstant.IS_CHANNEL){
    			queryInfoWrapper.lambda().eq(FactorInfo::getChannelId,factor.getChannelId());
    		}
    		
    		queryInfoWrapper.lambda().eq(FactorInfo::getFactorTypeId,factorType.getId());
    		//匹配因子数据,若为静态因子则进行匹配参数值
    		String value = "";
    		if(factorType.getIsStatic()==FactorConstant.IS_STATIC){
    			value=String.valueOf(map.get(factorType.getDataKey()));
    			//大于等于
    			queryInfoWrapper.lambda().ge(FactorInfo::getMaxData,String.valueOf(map.get(factorType.getDataKey())));
    			//小于
    			queryInfoWrapper.lambda().lt(FactorInfo::getMinData,String.valueOf(map.get(factorType.getDataKey())));
    		}
    		//若为动态因子，redis主键存在则找对应通道redis值做比较，若redis为空则不进行计算分数值
    		else{
    			
    			//如果不存在，则设置错误参数信息
    			String redisValue = redisValue(factorType,factor,map);
    			value = redisValue;
    			if(redisValue.equals("error")){
    				queryInfoWrapper.lambda().eq(FactorInfo::getValue,"error");
    			}else {
    				//大于等于
    				queryInfoWrapper.lambda().ge(FactorInfo::getMaxData,Double.valueOf(redisValue));
    				//小于
    				queryInfoWrapper.lambda().lt(FactorInfo::getMinData,Double.valueOf(redisValue));
    			}
    		}
//                queryInfoWrapper.lambda().orderByAsc()
    		FactorInfo factorInfo = iFactorInfoService.getOne(queryInfoWrapper);
    		
    		//判断该因子是否存在,则进行加分计算
    		if(factorInfo!=null){
    			factor.setTotal(factor.getTotal()*factorInfo.getWeight());
//                factor.setTotal(factor.getTotal().multiply(BigDecimal.valueOf(factorInfo.getWeight())));
    			//写入日志
    			factorLog(factorType,factor,factorInfo,orderInfo,value,step);
    			log.info("通道:【"+factor.getChannelCode()+"】在计算【"+factorType.getFactorName()+"】后得分为:"+factor.getTotal());
    		}else {
    			//判断该因子是否允许通过，不允许则进行踢出
    			if(factorType.getIsPass().equals(FactorConstant.IS_NO_PASS)){
    				factor.setTotal(0l);
    				log.info("通道:【"+factor.getChannelCode()+"】在计算【"+factorType.getFactorName()+"】中因不允许通过被排除");
    			}
    		}
    	}
    }
    /**
     * 路由因子时间数据区间计算（获取当前时间，时分转为字符串作为参数）
     * (dataKey静态因子与数据字段匹配，动态因子与redis键匹配)
     * @author 
     */
    private  void betweenTime(List<Factor> list,FactorType factorType,Map<String,String> map){
        for(Factor factor : list){
            log.info("通道:【"+factor.getChannelCode()+"】计算【"+factorType.getFactorName()+"】开始");

            QueryWrapper<FactorInfo> queryInfoWrapper = new QueryWrapper<>();
          if(factorType.getIsChannel()==FactorConstant.IS_CHANNEL){
                queryInfoWrapper.lambda().eq(FactorInfo::getChannelId,factor.getChannelId());
            }
            queryInfoWrapper.lambda().eq(FactorInfo::getFactorTypeId,factorType.getId());
            //获取当期时间字符串
            String dateTime = DateUtil.format(new Date(),"HHmmss");

            //大于等于
            queryInfoWrapper.lambda().ge(FactorInfo::getMaxData,Integer.valueOf(dateTime));
            //小于
            queryInfoWrapper.lambda().lt(FactorInfo::getMinData,Integer.valueOf(dateTime));

            FactorInfo factorInfo = iFactorInfoService.getOne(queryInfoWrapper);

            //判断该因子是否存在,则进行加分计算
            if(factorInfo!=null){
                factor.setTotal(factor.getTotal()*factorInfo.getWeight());
                log.info("通道:【"+factor.getChannelCode()+"】在计算【"+factorType.getFactorName()+"】后得分为:"+factor.getTotal());
            }else {
                //判断该因子是否允许通过，不允许则进行踢出
                if(factorType.getIsPass().equals(FactorConstant.IS_NO_PASS)){
                    factor.setTotal(0l);
                    log.info("通道:【"+factor.getChannelCode()+"】在计算【"+factorType.getFactorName()+"】中因不允许通过被排除");
                }
            }
        }
    }
    /**
     * 路由因子大于计算(dataKey静态因子与数据字段匹配，动态因子与redis键匹配)
     * @author 
     */
    private  void greater(List<Factor> list,FactorType factorType,Map<String,String> map){
        for(Factor factor : list){
            log.info("通道:【"+factor.getChannelCode()+"】计算【"+factorType.getFactorName()+"】开始");

            QueryWrapper<FactorInfo> queryInfoWrapper = new QueryWrapper<>();
           if(factorType.getIsChannel()==FactorConstant.IS_CHANNEL){
                queryInfoWrapper.lambda().eq(FactorInfo::getChannelId,factor.getChannelId());
            }
            queryInfoWrapper.lambda().eq(FactorInfo::getFactorTypeId,factorType.getId());
            //匹配因子数据,若为静态因子则进行匹配参数值
            if(factorType.getIsStatic()==FactorConstant.IS_STATIC){
                //大于
                queryInfoWrapper.lambda().lt(FactorInfo::getValue,map.get(factorType.getDataKey()));
            }
            //若为动态因子，redis主键存在则找对应通道redis值做比较，若redis为空则不进行计算分数值
            else{

                //如果不存在，则设置错误参数信息
                String redisValue = redisValue(factorType,factor,map);
                if(redisValue.equals("error")){
                    queryInfoWrapper.lambda().eq(FactorInfo::getValue,"error");
                }else {
                    //大于
                    queryInfoWrapper.lambda().lt(FactorInfo::getValue,Double.valueOf(redisValue));
                }

            }
//                queryInfoWrapper.lambda().orderByAsc()
            FactorInfo factorInfo = iFactorInfoService.getOne(queryInfoWrapper);

            //判断该因子是否存在,则进行加分计算
            if(factorInfo!=null){
                factor.setTotal(factor.getTotal()*factorInfo.getWeight());
                log.info("通道:【"+factor.getChannelCode()+"】在计算【"+factorType.getFactorName()+"】后得分为:"+factor.getTotal());
            }else {
                //判断该因子是否允许通过，不允许则进行踢出
                if(factorType.getIsPass().equals(FactorConstant.IS_NO_PASS)){
                    factor.setTotal(0l);
                    log.info("通道:【"+factor.getChannelCode()+"】在计算【"+factorType.getFactorName()+"】中因不允许通过被排除");
                }
            }
        }
    }
    /**
     * 路由因子小于计算(dataKey静态因子与数据字段匹配，动态因子与redis键匹配)
     * @author 
     */
    private  void less(List<Factor> list,FactorType factorType,Map<String,String> map){
        for(Factor factor : list){
            log.info("通道:【"+factor.getChannelCode()+"】计算【"+factorType.getFactorName()+"】开始");

            QueryWrapper<FactorInfo> queryInfoWrapper = new QueryWrapper<>();
          if(factorType.getIsChannel()==FactorConstant.IS_CHANNEL){
                queryInfoWrapper.lambda().eq(FactorInfo::getChannelId,factor.getChannelId());
            }
            queryInfoWrapper.lambda().eq(FactorInfo::getFactorTypeId,factorType.getId());
            //匹配因子数据,若为静态因子则进行匹配参数值
            if(factorType.getIsStatic()==FactorConstant.IS_STATIC){
                //小于
                queryInfoWrapper.lambda().gt(FactorInfo::getValue,map.get(factorType.getDataKey()));
            }
            //若为动态因子，redis主键存在则找对应通道redis值做比较，若redis为空则不进行计算分数值
            else{
                //如果不存在，则设置错误参数信息
                String redisValue = redisValue(factorType,factor,map);
                if(redisValue.equals("error")){
                    queryInfoWrapper.lambda().eq(FactorInfo::getValue,"error");
                }else {
                    //大于
                    queryInfoWrapper.lambda().gt(FactorInfo::getValue,Double.valueOf(redisValue));
                }
            }
//                queryInfoWrapper.lambda().orderByAsc()
            FactorInfo factorInfo = iFactorInfoService.getOne(queryInfoWrapper);

            //判断该因子是否存在,则进行加分计算
            if(factorInfo!=null){
                factor.setTotal(factor.getTotal()*factorInfo.getWeight());
                log.info("通道:【"+factor.getChannelCode()+"】在计算【"+factorType.getFactorName()+"】后得分为:"+factor.getTotal());
            }else {
                //判断该因子是否允许通过，不允许则进行踢出
                if(factorType.getIsPass().equals(FactorConstant.IS_NO_PASS)){
                    factor.setTotal(0l);
                    log.info("通道:【"+factor.getChannelCode()+"】在计算【"+factorType.getFactorName()+"】中因不允许通过被排除");
                }
            }
        }
    }
    /**
     * 路由因子商户通道费率区间计算（获取对应商户各自费率区间值进行计算）
     * (dataKey静态因子与数据字段匹配，动态因子与redis键匹配)
     * @author 
     */
    private  void betweenRate(List<Factor> list,FactorType factorType,Map<String,String> map,OrderInfo orderInfo,int step){
    	for(Factor factor : list){
    		log.info("通道:【"+factor.getChannelCode()+"】计算【"+factorType.getFactorName()+"】开始");
    		
    		QueryWrapper<FactorInfo> queryInfoWrapper = new QueryWrapper<>();
    		queryInfoWrapper.lambda().eq(FactorInfo::getFactorTypeId,factorType.getId());
    		//获取当期商户费率值
    		String rate = factor.getRate();
    		//大于等于
    		queryInfoWrapper.lambda().ge(FactorInfo::getMaxData,Double.valueOf(rate));
    		//小于
    		queryInfoWrapper.lambda().lt(FactorInfo::getMinData,Double.valueOf(rate));
    		
    		FactorInfo factorInfo = iFactorInfoService.getOne(queryInfoWrapper);
    		
    		//判断该因子是否存在,则进行加分计算
    		if(factorInfo!=null){
    			factor.setTotal(factor.getTotal()*factorInfo.getWeight());
    			//写入日志
    			factorLog(factorType,factor,factorInfo,orderInfo,rate,step);
    			log.info("通道:【"+factor.getChannelCode()+"】在计算【"+factorType.getFactorName()+"】后得分为:"+factor.getTotal());
    		}else {
    			//判断该因子是否允许通过，不允许则进行踢出
    			if(factorType.getIsPass().equals(FactorConstant.IS_NO_PASS)){
    				factor.setTotal(0l);
    				log.info("通道:【"+factor.getChannelCode()+"】在计算【"+factorType.getFactorName()+"】中因不允许通过被排除");
    			}
    		}
    	}
    }
    /**
     * 人工干预添加通道redis主键权重值
     * @author 
     */
    @Override
    public boolean addFactorByApi(String channelCode, int weight){
        QueryWrapper<ChannelInfo> queryInfoWrapper = new QueryWrapper<>();
        queryInfoWrapper.lambda().eq(ChannelInfo::getChannelCode,channelCode);
        queryInfoWrapper.lambda().eq(ChannelInfo::getStatus,1);
        ChannelInfo channelInfo = iChannelInfoService.getOne(queryInfoWrapper);
        if(channelInfo==null){
            return false;
        }
        redisTemplate.opsForValue().set(FactorConstant.FACTOR_CHANNEL+channelCode,weight);

        return redisTemplate.hasKey(FactorConstant.FACTOR_CHANNEL+channelCode);
    }
    /**
     * 取消人工干预添加通道redis主键权重值
     * @author 
     */
    @Override
    public boolean closeFactorByApi(String channelCode){
        QueryWrapper<ChannelInfo> queryInfoWrapper = new QueryWrapper<>();
        queryInfoWrapper.lambda().eq(ChannelInfo::getChannelCode,channelCode);
        queryInfoWrapper.lambda().eq(ChannelInfo::getStatus,1);
        ChannelInfo channelInfo = iChannelInfoService.getOne(queryInfoWrapper);
        if(channelInfo==null){
            return false;
        }

        return redisTemplate.delete(FactorConstant.FACTOR_CHANNEL+channelCode);
    }
    /**
     * 动态因子找redis值
     */
    private String redisValue(FactorType factorType,Factor factor,Map<String,String> map){
        String redisValue = "0";
        if(factorType.getIsAmt()==1){
            if(redisTemplate.hasKey(factorType.getDataKey()+"_"+factor.getChannelCode()+"_"+String.valueOf(map.get("amt")))){
                redisValue = redisTemplate.opsForValue().get(factorType.getDataKey()+"_"+factor.getChannelCode()+"_"+String.valueOf(map.get("amt"))).toString();
            }
        }else {
            if(redisTemplate.hasKey(factorType.getDataKey()+"_"+factor.getChannelCode())){
                 redisValue = redisTemplate.opsForValue().get(factorType.getDataKey()+"_"+factor.getChannelCode()).toString();
            }
        }
        log.info(factorType.getFactorName()+"【"+factor.getChannelCode()+"】"+"redis值："+redisValue);
        return redisValue;
    }

    /**
     * 因子过程日志
     */
    @Async
    public void factorLog(FactorType factorType,Factor factor,FactorInfo factorInfo,OrderInfo orderInfo,String redisValue,int step){
        //组装数据
        FactorLog factorLog =new FactorLog();
        factorLog.setMerOrderNo(orderInfo.getMerOrderNo());
        factorLog.setSysOrderNo(orderInfo.getSysOrderNo());
        factorLog.setProductId(orderInfo.getProductId());
        factorLog.setTel(orderInfo.getTel());
        factorLog.setAmt(orderInfo.getAmt());
        factorLog.setChannelCode(factor.getChannelCode());
        factorLog.setChannelId(factor.getChannelId());
        factorLog.setCreateTime(new Date());
        factorLog.setFactorName(factorType.getFactorName());
        factorLog.setFactorId(factorType.getId());
        factorLog.setMaxData(factorInfo.getMaxData());
        factorLog.setMinData(factorInfo.getMinData());
        factorLog.setMerId(orderInfo.getMerId());
        factorLog.setMerNo(orderInfo.getMerNo());
        factorLog.setRate(factor.getRate());
        factorLog.setTotal(factor.getTotal());
        factorLog.setValue(redisValue);
        factorLog.setWeight(factorInfo.getWeight());
        factorLog.setStep(step);
        iFactorLogService.save(factorLog);
    }
    /**
     * 因子结果日志
     */
    @Async
    public void factorLog(Factor factorMax, OrderInfo orderInfo, int step) {
        //组装数据
        FactorLog factorLog = new FactorLog();
        factorLog.setMerOrderNo(orderInfo.getMerOrderNo());
        factorLog.setSysOrderNo(orderInfo.getSysOrderNo());
        factorLog.setProductId(orderInfo.getProductId());
        factorLog.setTel(orderInfo.getTel());
        factorLog.setAmt(orderInfo.getAmt());
        factorLog.setChannelCode(factorMax.getChannelCode());
        factorLog.setChannelId(factorMax.getChannelId());
        factorLog.setCreateTime(new Date());
        factorLog.setMerId(orderInfo.getMerId());
        factorLog.setMerNo(orderInfo.getMerNo());
        factorLog.setRate(factorMax.getRate());
        factorLog.setTotal(factorMax.getTotal());
        factorLog.setLastChannel(factorMax.getChannelCode());
        factorLog.setStep(step);
        iFactorLogService.save(factorLog);
    }
    public static void main(String[] args) {
    	Long total=5l;
    	Long a=null;
    	Long b =total*a;
    	System.out.println(b);
    	
	}



	@Override
	public Factor count2(List<ChannelInfo> channelInfoList, OrderInfo orderInfo) {
		List<Long> channelIdList=new ArrayList<Long>();
		for (ChannelInfo channelInfo : channelInfoList) {
        	channelIdList.add(channelInfo.getId());
		}
		Factor factor;
		String format = DateUtil.format(new Date(), "HHmm");
		QueryWrapper<MerChannelRoute> merChannelRouteWrapper=new QueryWrapper<MerChannelRoute>();
		merChannelRouteWrapper.lambda().like(MerChannelRoute::getAreaIds, ","+orderInfo.getAreaId()+",");
		merChannelRouteWrapper.lambda().like(MerChannelRoute::getHasAmout, ","+orderInfo.getAmt().intValue()/100+",");
		merChannelRouteWrapper.lambda().like(MerChannelRoute::getOperators, ","+orderInfo.getServiceName()+",");
		merChannelRouteWrapper.lambda().eq(MerChannelRoute::getMerId, orderInfo.getMerId());
		merChannelRouteWrapper.lambda().ge(MerChannelRoute::getEndTime, format);
		merChannelRouteWrapper.lambda().le(MerChannelRoute::getStartTime, format);
		merChannelRouteWrapper.lambda().in(MerChannelRoute::getChannelId,channelIdList);
		merChannelRouteWrapper.lambda().orderByAsc(MerChannelRoute::getSort);
		
		List<MerChannelRoute> list = merChannelRouteService.list(merChannelRouteWrapper);
		if(list==null||list.size()==0) {
			orderFlowService.sendOrderFlow(orderInfo.getSysOrderNo(),"获取路由失败：参数【"+JSONUtil.toJsonStr(merChannelRouteWrapper.lambda().getParamNameValuePairs()));
			factor=null;
		}else {
			MerChannelRoute merChannelRoute = list.get(0);
			factor = new Factor.Builder<>().channelCode(merChannelRoute.getChannelCode()).channelId(merChannelRoute.getChannelId()).total(1L).rate("").build();
		}
		
		return factor;
	}
}