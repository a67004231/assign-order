package com.ryhy.base.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Data;

@Data
public class OrderDataReportDto implements Serializable{
	private Long orderAmount;
	private Long orderCount;
	
	
	
}
