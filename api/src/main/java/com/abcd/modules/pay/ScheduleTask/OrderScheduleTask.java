package com.abcd.modules.pay.ScheduleTask;

import com.abcd.common.dubbo.ChannelUtils;
import com.abcd.common.utils.moneyUtil.BigDecimalUtil;
import com.abcd.modules.channel.entity.ChannelInfo;
import com.abcd.modules.channel.entity.ChannelSettingInfo;
import com.abcd.modules.channel.entity.ChannelStock;
import com.abcd.modules.channel.service.IChannelInfoService;
import com.abcd.modules.channel.service.IChannelSettingInfoService;
import com.abcd.modules.channel.service.IChannelStockService;
import com.abcd.modules.factor.entity.FactorType;
import com.abcd.modules.factor.service.IFactorTypeService;
import com.abcd.modules.merchant.service.IMerSecretKeyService;
import com.abcd.modules.order.service.IOrderInfoService;
import com.abcd.modules.pay.service.PayOrderService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ryhy.base.dto.ChannelStockDTO;
import com.ryhy.base.dto.OrderDataReportDto;
import com.ryhy.base.vo.ResultVO;

import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Configuration      //主要用于标记配置类，兼备Component的效果。
@EnableScheduling   // 开启定时任务
@EnableAsync        // 开启多线程
@Slf4j
public class OrderScheduleTask {
	
	@Autowired
	private PayOrderService payOrderInfoService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private IOrderInfoService iOrderInfoService;
    @Autowired
    private IChannelInfoService iChannelInfoService;
    @Resource
    private IChannelSettingInfoService channelSettingInfoService;
    @Resource
    private IMerSecretKeyService merSecretKeyService;
    @Resource
    private IOrderInfoService orderInfoSerivce;
	@Autowired
	private IFactorTypeService factorTypeService;
	@Autowired
	private IChannelStockService channelStockService;
    @Value("${dubbo.registry.address}")
    private  String address;
	@Value("${api.version}")
    private  String version;
	/**
	 * 判断因子类型是否有效
	 * @author 
	 */
	private boolean isFactorStatus(String dataKey){
		QueryWrapper<FactorType> queryWrapper = new QueryWrapper<>();
		queryWrapper.lambda().eq(FactorType::getDataKey,dataKey);
		queryWrapper.lambda().eq(FactorType::getStatus,1);
		List<FactorType> list = factorTypeService.list(queryWrapper);
		if(list.size()<1){
			return false;
		}else {
			return true;
		}
	}
	/**
	 * 多线程定时任务，每分钟获取过去2分钟内库存除以订单数，推送数据到redis
	 * @author 
	 */
//	@Async
//	@Scheduled(fixedRate=60000)
	public void KucunlvTask() {
		//判断是否有效,无效则不执行
		if(!isFactorStatus("kucunlv")){
			return;
		}
		log.info("执行每分钟获取过去2分钟库存率时任务时间: " + LocalDateTime.now());
		//redis动态数据
		QueryWrapper<ChannelInfo> queryWrapper = new QueryWrapper<>();
		queryWrapper.lambda().eq(ChannelInfo::getStatus,1);
		List<ChannelInfo> channelInfoList = iChannelInfoService.list(queryWrapper);
		//分别查询各个通道服务的数据
		for(ChannelInfo channelInfo:channelInfoList){
			String sqlStr = "SELECT count(id) " +
					"from order_info " +
					"where create_time>=NOW()-INTERVAL 10 MINUTE " +
					"and channel_code= '"+channelInfo.getChannelCode()+"'";
			String result = iOrderInfoService.queryStringBySql(sqlStr);
			//redis主键与factor_type配置主键一致，主键后加下划线和通道编码
			redisTemplate.opsForValue().set("kucunlv_"+channelInfo.getChannelCode(),result,2, TimeUnit.MINUTES);
		}
	}
    /**
     * 多线程定时任务，每分钟获取过去2分钟订单成功率，推送数据到redis
     * @author 
     */
    @Async
    @Scheduled(fixedRate=60000)
    public void OrderScheduleTask() {
    	//判断是否有效,无效则不执行
		if(!isFactorStatus("jingchenggonglv")){
			return;
		}
        log.info("执行每分钟获取过去2分钟订单成功率(排除25秒内失败订单)定时任务时间: " + LocalDateTime.now());
        //redis动态数据
        QueryWrapper<ChannelInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ChannelInfo::getStatus,1);
        List<ChannelInfo> channelInfoList = iChannelInfoService.list(queryWrapper);
        //分别查询各个通道服务的数据
        for(ChannelInfo channelInfo:channelInfoList){
        	
            String sqlStr = "SELECT ROUND(IFNULL(sum(case when status=2 then 1 else 0 end),0)/IFNULL(((case when count(id)=0 then 1 else count(id) end)-sum(case when status=3 and result_time-create_time<25 then 1 else 0 end)),1),4) " +
					"from order_info " +
					" where create_time>=NOW()-INTERVAL 4 MINUTE " +
					"and create_time<=NOW()-INTERVAL 2 MINUTE "+
                    "and channel_code= '"+channelInfo.getChannelCode()+"'";
            String result = iOrderInfoService.queryStringBySql(sqlStr);
            result = BigDecimalUtil.mul(result,"100");
            //redis主键与factor_type配置主键一致，主键后加下划线和通道编码
            redisTemplate.opsForValue().set("jingchenggonglv_"+channelInfo.getChannelCode(),result,2, TimeUnit.MINUTES);

        }
    }
    /**
     * 多线程定时任务，每分钟获取过去2分钟成功订单耗时，推送数据到redis
     * @author 
     */
    @Async
    @Scheduled(fixedRate=60000)
    public void TimeScheduleTask() {
		//判断是否有效,无效则不执行
		if(!isFactorStatus("xiangyingshijian")){
			return;
		}
        log.info("执行每分钟获取过去2分钟成功订单耗时定时任务时间: " + LocalDateTime.now());
        //redis动态数据
        QueryWrapper<ChannelInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ChannelInfo::getStatus,1);
        List<ChannelInfo> channelInfoList = iChannelInfoService.list(queryWrapper);
        //分别查询各个通道服务的数据
        for(ChannelInfo channelInfo:channelInfoList){
            String sqlStr = "SELECT IFNULL(sum(result_time-create_time)/count(id),10000) " +
					"from order_info  " +
					"where create_time>=NOW()-INTERVAL 4 MINUTE " +
					"and create_time<=NOW()-INTERVAL 2 MINUTE " +
					"and status= 2 " +
                    "and channel_code= '"+channelInfo.getChannelCode()+"'";
            String result = iOrderInfoService.queryStringBySql(sqlStr);
            //redis主键与factor_type配置主键一致，主键后加下划线和通道编码
            redisTemplate.opsForValue().set("xiangyingshijian_"+channelInfo.getChannelCode(),result,2, TimeUnit.MINUTES);
        }
    }
	/**
	 * 多线程定时任务，每分钟获取过去十分钟内匹配中订单数，推送数据到redis
	 * @author 
	 */
	@Async
	@Scheduled(fixedRate=60000)
	public void OrderNumPipeiTask() {//判断是否有效,无效则不执行
		if(!isFactorStatus("pipeidanshu")){
			return;
		}

		log.info("执行每分钟获取过去十分钟匹配中订单数量时任务时间: " + LocalDateTime.now());
		//redis动态数据
		QueryWrapper<ChannelInfo> queryWrapper = new QueryWrapper<>();
		queryWrapper.lambda().eq(ChannelInfo::getStatus,1);
		List<ChannelInfo> channelInfoList = iChannelInfoService.list(queryWrapper);
		//分别查询各个通道服务的数据
		for(ChannelInfo channelInfo:channelInfoList){
			String sqlStr = "SELECT count(id) " +
					"from order_info " +
					"where create_time>=NOW()-INTERVAL 10 MINUTE " +
					"and status=1 " +
					"and channel_code= '"+channelInfo.getChannelCode()+"'";
			String result = iOrderInfoService.queryStringBySql(sqlStr);
			//redis主键与factor_type配置主键一致，主键后加下划线和通道编码
			redisTemplate.opsForValue().set("pipeidanshu_"+channelInfo.getChannelCode(),result,2, TimeUnit.MINUTES);
		}
	}
	/**
	 * 多线程定时任务，每分钟获取过去十分钟内成功订单数，推送数据到redis
	 * @author 
	 */
	@Async
	@Scheduled(fixedRate=60000)
	public void OrderSuccessNumTask() {
		if(!isFactorStatus("chenggongdanshu")){
			return;
		}
		log.info("执行每分钟获取过去十分钟成功订单数量时任务时间: " + LocalDateTime.now());
		//redis动态数据
		QueryWrapper<ChannelInfo> queryWrapper = new QueryWrapper<>();
		queryWrapper.lambda().eq(ChannelInfo::getStatus,1);
		List<ChannelInfo> channelInfoList = iChannelInfoService.list(queryWrapper);
		//分别查询各个通道服务的数据
		for(ChannelInfo channelInfo:channelInfoList){
			String sqlStr = "SELECT count(id) " +
					"from order_info " +
					"where create_time>=NOW()-INTERVAL 10 MINUTE " +
					"and status=2 " +
					"and channel_code= '"+channelInfo.getChannelCode()+"'";
			String result = iOrderInfoService.queryStringBySql(sqlStr);
			//redis主键与factor_type配置主键一致，主键后加下划线和通道编码
			redisTemplate.opsForValue().set("chenggongdanshu_"+channelInfo.getChannelCode(),result,2, TimeUnit.MINUTES);
		}
	}
	/**
	 * 多线程定时任务，每分钟获取过去十分钟内失败订单数，推送数据到redis
	 * @author 
	 */
	@Async
	@Scheduled(fixedRate=60000)
	public void OrderFailNumTask() {
		if(!isFactorStatus("shibaidanshu")){
			return;
		}
		log.info("执行每分钟获取十分钟内失败订单数时任务时间: " + LocalDateTime.now());
		//redis动态数据
		QueryWrapper<ChannelInfo> queryWrapper = new QueryWrapper<>();
		queryWrapper.lambda().eq(ChannelInfo::getStatus,1);
		List<ChannelInfo> channelInfoList = iChannelInfoService.list(queryWrapper);
		//分别查询各个通道服务的数据
		for(ChannelInfo channelInfo:channelInfoList){
			String sqlStr = "SELECT count(id) " +
					"from order_info " +
					"where create_time>=NOW()-INTERVAL 10 MINUTE " +
					"and status=3 " +
					"and channel_code= '"+channelInfo.getChannelCode()+"'";
			String result = iOrderInfoService.queryStringBySql(sqlStr);
			//redis主键与factor_type配置主键一致，主键后加下划线和通道编码
			redisTemplate.opsForValue().set("shibaidanshu_"+channelInfo.getChannelCode(),result,2, TimeUnit.MINUTES);
		}
	}
	/**
	 * 多线程定时任务，每分钟获取通道库存率，推送数据到redis
	 * @author 
	 */
	@Async
	@Scheduled(fixedRate=60000)
	public void OrderKucunTask() {
		if(!isFactorStatus("kucunlv")){
			return;
		}
		log.info("执行每分钟获取通道库存率定时任务时间: " + LocalDateTime.now());
		//测试代码库存数
		//redis动态数据
		QueryWrapper<ChannelInfo> queryWrapper = new QueryWrapper<>();
		queryWrapper.lambda().eq(ChannelInfo::getStatus,1);
		List<ChannelInfo> channelInfoList = iChannelInfoService.list(queryWrapper);
		log.info(JSONUtil.toJsonStr(channelInfoList));
		//分别查询各个通道服务的数据
		for(ChannelInfo channelInfo:channelInfoList){
			log.info("channelCode:"+channelInfo.getChannelCode());
			//查询通道库存率数据
			//组装远程调用数据dto
			QueryWrapper<ChannelSettingInfo> channelSettingWrapper=new QueryWrapper<ChannelSettingInfo>();
			channelSettingWrapper.lambda().eq(ChannelSettingInfo::getChannelId, channelInfo.getId());
			List<ChannelSettingInfo> ChannelSettingInfoList = channelSettingInfoService.list(channelSettingWrapper);
			if(ChannelSettingInfoList==null|| ChannelSettingInfoList.size()==0) {
				continue;
			}
			ChannelSettingInfo channelSettingInfo = ChannelSettingInfoList.get(0);
			ChannelStockDTO dto = new ChannelStockDTO();
			dto.setChannelCode(channelInfo.getChannelCode());
			dto.setAddress(address);
			dto.setVersion(version);
            dto.setChannelCode(channelInfo.getGroupCode());
            dto.setAppid(channelSettingInfo.getAppid());
            dto.setChannelKey(channelSettingInfo.getMd5Key());
			ResultVO resultVO = ChannelUtils.invokeStockQuery(dto);
			Double stock = Double.valueOf(0);
			if(resultVO.getCode()==200) {

				//处理数据
				List<OrderDataReportDto> list = (List<OrderDataReportDto>) resultVO.getData();
				//若库存数据为
				if(list.isEmpty()){
					continue;
				}
				for(OrderDataReportDto dataReportDto:list){
					String sqlStr = "SELECT count(id) " +
							"from order_info " +
							"where create_time>=NOW()-INTERVAL 4 MINUTE " +
							"and create_time<=NOW()-INTERVAL 2 MINUTE " +
							"and channel_code= '"+channelInfo.getChannelCode()+"' "+
							"and amt = "+dataReportDto.getOrderAmount();
					String result = iOrderInfoService.queryStringBySql(sqlStr);
					if(result.equals("0")){
						result="1";
					}
					//保存库存
					ChannelStock channelStock = new ChannelStock();
					channelStock.setAmt(dataReportDto.getOrderAmount());
					channelStock.setChannelCode(channelInfo.getChannelCode());
					channelStock.setChannelName(channelInfo.getChannelName());
					channelStock.setCreateTime(new Date());
					channelStock.setStockNum(dataReportDto.getOrderCount());
					channelStockService.save(channelStock);
					String kucunlv = BigDecimalUtil.div(dataReportDto.getOrderCount()*100,result);
					//redis主键与factor_type配置主键一致，主键后加下划线和通道编码
					redisTemplate.opsForValue().set("kucunshu_"+channelInfo.getChannelCode()+"_"+String.valueOf(dataReportDto.getOrderAmount()),String.valueOf(dataReportDto.getOrderCount()),2, TimeUnit.MINUTES);
					redisTemplate.opsForValue().set("kucunlv_"+channelInfo.getChannelCode()+"_"+String.valueOf(dataReportDto.getOrderAmount()),kucunlv,2, TimeUnit.MINUTES);
				}
			}

		}
	}
    /**
     * 每分钟同步一次支付中订单状态
     * @author 
     */
//    /**
//     * 每10秒通知一次商户
//     * @author 
//     */
//    @Async
//    @Scheduled(fixedRate=10000)
//    public void orderNoticeTask() {
//    	QueryWrapper<OrderInfo>orderWrapper=new QueryWrapper<OrderInfo>();
//    	orderWrapper.lambda().eq(OrderInfo::getNoticeStatus, 0).lt(OrderInfo::getNoticeCount, 5).and(wrapper ->wrapper.eq(OrderInfo::getStatus, 2).or().eq(OrderInfo::getStatus, 3));
//
//    	List<OrderInfo> orderInfoList = orderInfoSerivce.list(orderWrapper);
//    	if(orderInfoList!=null && !orderInfoList.isEmpty()){
//    		for (OrderInfo orderInfo : orderInfoList) {
//    			payOrderInfoService.noticeMer(orderInfo.getId());
//    		}
//    	}
//    }

}
