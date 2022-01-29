package com.abcd.modules.pay.entity;

import java.util.List;

import io.swagger.models.auth.In;
import lombok.Data;

@Data
public class D4NoticeDto {
	private String userSerial;
	private String timestamp;
	private String sign;
	private String version;
	private String id;
	private String outId;
	private String serveTarget;
	private String bizClassSign;
	private String skuSnapshotId;
	private String sellingPrice;
	private String state;
	private String createTime;
	private String vouchers;
}
