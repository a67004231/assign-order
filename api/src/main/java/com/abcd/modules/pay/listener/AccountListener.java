package com.abcd.modules.pay.listener;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

import javax.annotation.Resource;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.abcd.modules.channel.entity.ChannelAccount;
import com.abcd.modules.channel.entity.ChannelAccountLog;
import com.abcd.modules.channel.service.IChannelAccountLogService;
import com.abcd.modules.channel.service.IChannelAccountService;
import com.abcd.modules.channel.service.IChannelInfoService;
import com.abcd.modules.merchant.entity.MerAccount;
import com.abcd.modules.merchant.entity.MerAccountLog;
import com.abcd.modules.merchant.service.IMerAccountLogService;
import com.abcd.modules.merchant.service.IMerAccountService;
import com.abcd.modules.order.entity.OrderInfo;
import com.abcd.modules.order.service.IOrderInfoService;
import com.abcd.modules.pay.dto.AccountUpdateDto;
import com.abcd.modules.pay.service.PayOrderService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rabbitmq.client.Channel;

import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class AccountListener {
	@Resource
    private IMerAccountService merAccountService;
	@Resource
	private IMerAccountLogService merAccountLogService;
	@Resource
	private IOrderInfoService orderInfoService;
    @Resource
    private IChannelAccountService channelAccountService;
    @Resource
    private IChannelAccountLogService channelAccountLogService;
	@Resource
    private IChannelInfoService channelInfoService;
	@Resource
    RabbitTemplate rabbitTemplate;
	@Value("${dubbo.registry.address}")
    private  String address;
	@Value("${api.version}")
    private  String version;
	@Value("${order.account.queue}")
    private  String updateAccountQueue;
	@Autowired
	PayOrderService payOrderService;
	@RabbitListener(queues = "${order.account.queue}",concurrency = "20")
    @RabbitHandler
    @Transactional(rollbackFor=Exception.class,propagation = Propagation.NESTED)
    public void process(Channel channel, Message message) {
		try {
	        byte[] body = message.getBody();
	        String msg = new String(body);
	        AccountUpdateDto dto = JSONUtil.toBean(msg, AccountUpdateDto.class);
            // ???* ????????????????????????reqType???????????????????????????????????????????????????null
            // ??????:?????????????????? ???SettlePostSubmit
            // ??????
            // ?????????????????????
//            log.info("accountLister"+"sysOrderNo"+dto.getOrderId()+"  "+msg);
            try {
            	updateAccount(dto);
			} catch (Exception e) {
//				e.printStackTrace();
//				log.error("[{}]????????????:{}", e);
				rabbitTemplate.convertAndSend(updateAccountQueue, msg);
			}
        } catch (Exception e) {
//            log.error("[{}]????????????:{}", e);
        } finally {
            try {
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
//                log.info("time0e"+LocalDateTime.now());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
	
	@Transactional(rollbackFor=Exception.class,propagation = Propagation.NESTED)
	private void updateAccount(AccountUpdateDto dto) throws Exception {
		//????????????  ????????????????????????????????????????????????
		boolean rsc=true;
		boolean rsm=true;
		//????????????
		OrderInfo orderInfo = orderInfoService.getById(dto.getOrderId());
		if(dto.getType()==1) {
			//????????????  
			QueryWrapper<ChannelAccount> channelAccountWrapper=new QueryWrapper<ChannelAccount>();
        	channelAccountWrapper.lambda().eq(ChannelAccount::getChannelId, dto.getChannelId());
        	ChannelAccount channelAccount = channelAccountService.getOne(channelAccountWrapper);
        	String changeChannelBef=JSONUtil.toJsonStr(channelAccount);
        	channelAccount.setCreditBalanceAmt(channelAccount.getCreditBalanceAmt()-dto.getSysAmt());
        	channelAccount.setCreditFixAmt(channelAccount.getCreditFixAmt()+dto.getSysAmt());
        	String changeChannelAft=JSONUtil.toJsonStr(channelAccount);
	    	rsc = channelAccountService.updateById(channelAccount);
	    	if(!rsc) {
//	    		log.info("???????????????????????????????????????"+dto.getOrderId()+"---"+JSONUtil.toJsonStr(channelAccount));
	    		TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
	    		throw new Exception("???????????????????????????????????????"+dto.getOrderId());
	    	}
	    	
	    	QueryWrapper<MerAccount> merAccountWrapper=new QueryWrapper<MerAccount>();
        	merAccountWrapper.lambda().eq(MerAccount::getMerId, dto.getMerId());
        	MerAccount merAccount = merAccountService.getOne(merAccountWrapper);
        	String changeMerBef=JSONUtil.toJsonStr(merAccount);
			merAccount.setCreditBalanceAmt(merAccount.getCreditBalanceAmt()-dto.getMerAmt());
	    	merAccount.setCreditFixAmt(merAccount.getCreditFixAmt()+dto.getMerAmt());
	    	String changeMerAft=JSONUtil.toJsonStr(merAccount);
	    	
	    	rsm = merAccountService.updateById(merAccount);
	    	if(!rsm) {
//	    		log.info("???????????????????????????????????????"+dto.getOrderId()+"---"+JSONUtil.toJsonStr(channelAccount));
	    		TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
	    		throw new Exception("???????????????????????????????????????"+dto.getOrderId());
	    	}
	    	try {
	    		orderInfo.setChannelAmtAdd(dto.getSysAmt());
		    	orderInfo.setMerAmtAdd(dto.getMerAmt() );
		    	orderInfoService.updateMerAmountById(orderInfo);
			} catch (Exception e) {
//				log.info("?????????????????????????????????"+dto.getOrderId()+"---"+JSONUtil.toJsonStr(channelAccount));
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			}
	    	boolean insertMerAccountLog=insertMerAccountLog(merAccount, orderInfo.getSysOrderNo(), changeMerBef, changeMerAft, "??????????????????????????????"+orderInfo.getSysOrderNo(), dto.getMerAmt(),1);
	    	boolean insertChannelAccountLog=insertChannelAccountLog(channelAccount, orderInfo.getSysOrderNo(), changeChannelBef, changeChannelAft, "????????????????????????"+orderInfo.getSysOrderNo(), dto.getSysAmt(),1);
	    	if(!insertMerAccountLog ||!insertChannelAccountLog) {
	    		TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
	    		throw new Exception("?????????????????????????????????????????????"+dto.getOrderId());
	    	}
	    	
		}else if(dto.getType()==2) {
			//???????????? ?????????????????????????????????
			QueryWrapper<ChannelAccount> channelAccountWrapper=new QueryWrapper<ChannelAccount>();
        	channelAccountWrapper.lambda().eq(ChannelAccount::getChannelId, dto.getChannelId());
        	ChannelAccount channelAccount = channelAccountService.getOne(channelAccountWrapper);
        	String changeChannelBef=JSONUtil.toJsonStr(channelAccount);
        	channelAccount.setCreditFixAmt(channelAccount.getCreditFixAmt()-dto.getSysAmt());
        	channelAccount.setCreditUseAmt(channelAccount.getCreditUseAmt()+dto.getSysAmt());
        	String changeChannelAft=JSONUtil.toJsonStr(channelAccount);
        	rsc = channelAccountService.updateById(channelAccount);
        	if(!rsc) {
        		TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
	    		throw new Exception("?????????????????????????????????????????????"+dto.getOrderId());
	    	}
        	QueryWrapper<MerAccount> merAccountWrapper=new QueryWrapper<MerAccount>();
        	merAccountWrapper.lambda().eq(MerAccount::getMerId, dto.getMerId());
        	MerAccount merAccount = merAccountService.getOne(merAccountWrapper);
        	String changeMerBef=JSONUtil.toJsonStr(merAccount);
        	merAccount.setCreditUseAmt(merAccount.getCreditUseAmt()+dto.getMerAmt());
        	merAccount.setCreditFixAmt(merAccount.getCreditFixAmt()-dto.getMerAmt());
        	String changeMerAft=JSONUtil.toJsonStr(merAccount);
        	rsm = merAccountService.updateById(merAccount);
        	if(!rsm) {
        		TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
	    		throw new Exception("?????????????????????????????????????????????"+dto.getOrderId());
	    	}
        	
	    	try {
	    		orderInfo.setChannelAmtRs(dto.getSysAmt());
		    	orderInfo.setMerAmtRs(dto.getMerAmt());
		    	orderInfoService.updateMerAmountById(orderInfo);
			} catch (Exception e) {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			}
	    	boolean insertMerAccountLog = insertMerAccountLog(merAccount, orderInfo.getSysOrderNo(), changeMerBef, changeMerAft, "??????????????????????????????", dto.getMerAmt(),2);
	    	boolean insertChannelAccountLog = insertChannelAccountLog(channelAccount, orderInfo.getSysOrderNo(), changeChannelBef, changeChannelAft, "??????????????????????????????", dto.getSysAmt(),2);
	    	if(!insertMerAccountLog ||!insertChannelAccountLog) {
	    		TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
	    		throw new Exception("?????????????????????????????????????????????"+dto.getOrderId());
	    	}
		}else if(dto.getType()==3) {
			//???????????? ?????????????????????????????????
			QueryWrapper<ChannelAccount> channelAccountWrapper=new QueryWrapper<ChannelAccount>();
        	channelAccountWrapper.lambda().eq(ChannelAccount::getChannelId, dto.getChannelId());
        	ChannelAccount channelAccount = channelAccountService.getOne(channelAccountWrapper);
        	String changeChannelBef=JSONUtil.toJsonStr(channelAccount);
        	channelAccount.setCreditBalanceAmt(channelAccount.getCreditBalanceAmt()+dto.getSysAmt());
        	channelAccount.setCreditFixAmt(channelAccount.getCreditFixAmt()-dto.getSysAmt());
        	String changeChannelAft=JSONUtil.toJsonStr(channelAccount);
        	rsc = channelAccountService.updateById(channelAccount);
        	if(!rsc) {
        		TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
	    		throw new Exception("?????????????????????????????????????????????"+dto.getOrderId());
	    	}
        	QueryWrapper<MerAccount> merAccountWrapper=new QueryWrapper<MerAccount>();
        	merAccountWrapper.lambda().eq(MerAccount::getMerId, dto.getMerId());
        	MerAccount merAccount = merAccountService.getOne(merAccountWrapper);
        	String changeMerBef=JSONUtil.toJsonStr(merAccount);
        	merAccount.setCreditBalanceAmt(merAccount.getCreditBalanceAmt()+dto.getMerAmt());
        	merAccount.setCreditFixAmt(merAccount.getCreditFixAmt()-dto.getMerAmt());
        	String changeMerAft=JSONUtil.toJsonStr(merAccount);
        	rsm = merAccountService.updateById(merAccount);
        	if(!rsm) {
        		TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
	    		throw new Exception("?????????????????????????????????????????????"+dto.getOrderId());
	    	}
	    	
	    	try {
	    		orderInfo.setChannelAmtAdd(0l);
		    	orderInfo.setMerAmtAdd(0l);
		    	orderInfo.setChannelAmtRs(0l);
		    	orderInfo.setMerAmtRs(0l );
		    	orderInfoService.updateMerAmountById(orderInfo);
			} catch (Exception e) {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			}
	    	boolean insertMerAccountLog = insertMerAccountLog(merAccount, orderInfo.getSysOrderNo(), changeMerBef, changeMerAft, "??????????????????????????????", dto.getMerAmt(),3);
	    	boolean insertChannelAccountLog = insertChannelAccountLog(channelAccount, orderInfo.getSysOrderNo(), changeChannelBef, changeChannelAft, "??????????????????????????????", dto.getSysAmt(),3);
	    	if(!insertMerAccountLog ||!insertChannelAccountLog) {
	    		TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
	    		throw new Exception("?????????????????????????????????????????????"+dto.getOrderId());
	    	}
		}else if(dto.getType()==4) {
			//????????????
			if(dto.getAccountType()==1) {
				QueryWrapper<MerAccount> merAccountWrapper=new QueryWrapper<MerAccount>();
	        	merAccountWrapper.lambda().eq(MerAccount::getMerId, dto.getMerId());
	        	MerAccount merAccount = merAccountService.getOne(merAccountWrapper);
	        	String changeMerBef=JSONUtil.toJsonStr(merAccount);
	        	merAccount.setCreditBalanceAmt(merAccount.getCreditBalanceAmt()+dto.getMerAmt());
	        	merAccount.setCreditUseAmt(merAccount.getCreditUseAmt()-dto.getMerAmt());
	        	String changeMerAft=JSONUtil.toJsonStr(merAccount);
	        	rsm = merAccountService.updateById(merAccount);
	        	if(!rsm) {
	        		TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		    		throw new Exception("?????????????????????????????????????????????"+dto.getOrderId());
		    	}
	        	boolean insertMerAccountLog = insertMerAccountLog(merAccount, dto.getOrderId()+"", changeMerBef, changeMerAft, "????????????", dto.getMerAmt(),4);
		    	if(!insertMerAccountLog ) {
		    		TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		    		throw new Exception("?????????????????????????????????????????????"+dto.getOrderId());
		    	}
				
			}else if(dto.getAccountType()==2) {
				QueryWrapper<ChannelAccount> channelAccountWrapper=new QueryWrapper<ChannelAccount>();
	        	channelAccountWrapper.lambda().eq(ChannelAccount::getChannelId, dto.getChannelId());
	        	ChannelAccount channelAccount = channelAccountService.getOne(channelAccountWrapper);
	        	String changeChannelBef=JSONUtil.toJsonStr(channelAccount);
	        	channelAccount.setCreditBalanceAmt(channelAccount.getCreditBalanceAmt()+dto.getSysAmt());
	        	channelAccount.setCreditUseAmt(channelAccount.getCreditUseAmt()-dto.getSysAmt());
	        	String changeChannelAft=JSONUtil.toJsonStr(channelAccount);
	        	rsc = channelAccountService.updateById(channelAccount);
	        	if(!rsc) {
	        		TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		    		throw new Exception("?????????????????????????????????????????????"+dto.getOrderId());
		    	}
		    	boolean insertChannelAccountLog = insertChannelAccountLog(channelAccount, dto.getOrderId()+"", changeChannelBef, changeChannelAft, "????????????", dto.getSysAmt(),4);
		    	if(!insertChannelAccountLog) {
		    		TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		    		throw new Exception("?????????????????????????????????????????????"+dto.getOrderId());
		    	}
			}
        	
		}else if(dto.getType()==5) {
			//?????????????????????
			//???????????????????????????????????????
			if(dto.getAccountType()==1) {
				//????????????
				QueryWrapper<MerAccount> merAccountWrapper=new QueryWrapper<MerAccount>();
	        	merAccountWrapper.lambda().eq(MerAccount::getMerId, dto.getMerId());
	        	MerAccount merAccount = merAccountService.getOne(merAccountWrapper);
	        	String changeMerBef=JSONUtil.toJsonStr(merAccount);
	        	merAccount.setCreditBalanceAmt(merAccount.getCreditBalanceAmt()+dto.getMerAmt());
	        	merAccount.setCreditAmt(merAccount.getCreditAmt()+dto.getMerAmt());
	        	String changeMerAft=JSONUtil.toJsonStr(merAccount);
	        	rsm = merAccountService.updateById(merAccount);
	        	if(!rsm) {
	        		TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		    		throw new Exception("?????????????????????????????????????????????"+dto.getOrderId());
		    	}
	        	boolean insertMerAccountLog = insertMerAccountLog(merAccount, dto.getOrderId()+"", changeMerBef, changeMerAft, dto.getDesc(), dto.getMerAmt(),5);
	        	if(!insertMerAccountLog ) {
	        		TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		    		throw new Exception("?????????????????????????????????????????????"+dto.getOrderId());
		    	}
			}else if(dto.getAccountType()==2) {
				QueryWrapper<ChannelAccount> channelAccountWrapper=new QueryWrapper<ChannelAccount>();
	        	channelAccountWrapper.lambda().eq(ChannelAccount::getChannelId, dto.getChannelId());
	        	ChannelAccount channelAccount = channelAccountService.getOne(channelAccountWrapper);
	        	String changeChannelBef=JSONUtil.toJsonStr(channelAccount);
	        	channelAccount.setCreditBalanceAmt(channelAccount.getCreditBalanceAmt()+dto.getSysAmt());
	        	channelAccount.setCreditAmt(channelAccount.getCreditAmt()+dto.getSysAmt());
	        	String changeChannelAft=JSONUtil.toJsonStr(channelAccount);
	        	rsc = channelAccountService.updateById(channelAccount);
	        	if(!rsc) {
	        		TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		    		throw new Exception("?????????????????????????????????????????????"+dto.getOrderId());
		    	}
	        	boolean insertChannelAccountLog = insertChannelAccountLog(channelAccount, dto.getOrderId()+"", changeChannelBef, changeChannelAft, dto.getDesc(), dto.getSysAmt(),5);
	        	if(!insertChannelAccountLog) {
	        		TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		    		throw new Exception("?????????????????????????????????????????????"+dto.getOrderId());
		    	}
			}
		}
	}
	public boolean insertMerAccountLog(MerAccount merAccount,String sysOrderNo,String changeBef,String changeAft,String changeText,Long amt,int type) {
		MerAccountLog merAccountLog=new MerAccountLog();
		merAccountLog.setAccountId(merAccount.getId());
    	merAccountLog.setChangeAmt(amt);
    	merAccountLog.setChangeBef(changeBef);
    	merAccountLog.setChangeText(changeText);
    	merAccountLog.setCreateTime(new Date());
    	merAccountLog.setChangeAft(changeAft);
    	merAccountLog.setSysOrderNo(sysOrderNo);
    	merAccountLog.setMerId(merAccount.getMerId());
    	merAccountLog.setType(type);
    	return merAccountLogService.save(merAccountLog);
		
	}
	public boolean insertChannelAccountLog(ChannelAccount channelAccount,String sysOrderNo,String changeBef,String changeAft,String changeText,Long amt,int type) {
		ChannelAccountLog channelAccountLog=new ChannelAccountLog();
		channelAccountLog.setAccountId(channelAccount.getId());
		channelAccountLog.setChangeAmt(amt);
		channelAccountLog.setChangeBef(changeBef);
		channelAccountLog.setChangeText(changeText);
		channelAccountLog.setCreateTime(new Date());
		channelAccountLog.setChangeAft(changeAft);
		channelAccountLog.setSysOrderNo(sysOrderNo);
    	channelAccountLog.setChannelId(channelAccount.getChannelId());
		return channelAccountLogService.save(channelAccountLog);
	}
}
