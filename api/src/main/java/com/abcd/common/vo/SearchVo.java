package com.abcd.common.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author 
 */
@Data
public class SearchVo implements Serializable {

    private String startDate;

    private String endDate;
}
