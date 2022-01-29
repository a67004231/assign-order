package com.abcd.modules.pay.dto;

import lombok.Data;

@Data
public class AccountUpdateDto {
	/**账户修改资金*/
	private Long sysAmt;
	/**账户修改资金*/
	private Long merAmt;
	/**账户类型 1商户账户  2通道账户 3同时处理*/ 
	private int accountType;
	/**操作类型 1订单新增  2订单成功  3订单失败 4余额充值*/
	private int type;
	/**商户ID*/
	private Long merId;
	/**通道ID*/
	private Long channelId;
	/**订单ID*/
	private Long orderId;
	/**变更备注*/
	private String desc;
}
