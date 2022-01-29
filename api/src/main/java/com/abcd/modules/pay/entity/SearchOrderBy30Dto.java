package com.abcd.modules.pay.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SearchOrderBy30Dto {
	@ApiModelProperty(value="时间戳")
	private String timestamp;
	@ApiModelProperty(value="交易签名")
	private String sign;
	@ApiModelProperty(value="时间范围")
	private int time;
	@ApiModelProperty(value="状态")
	private String status;
	@ApiModelProperty(value="商户ID")
	private Long merId;
}
