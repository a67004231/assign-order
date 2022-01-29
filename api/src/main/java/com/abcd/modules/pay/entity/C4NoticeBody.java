package com.abcd.modules.pay.entity;

import lombok.Data;

@Data
public class C4NoticeBody {
	private String product_id;
	private String trade_amount;
	private String order_id;
	private String order_time;
	private String remark;
	private String platform_order_id;
	private String completed_time;
	private String order_status;
	private String settle_amount;
	private String failure_code;
	private String voucher_ids;
	private String notify_id;
	private String notify_time;
}
