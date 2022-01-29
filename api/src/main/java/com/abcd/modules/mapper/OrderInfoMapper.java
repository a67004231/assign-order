package com.abcd.modules.mapper;

import com.abcd.modules.order.entity.OrderInfo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.mapping.StatementType;

/**
 * 订单信息数据处理层
 * @author 
 */
public interface OrderInfoMapper extends BaseMapper<OrderInfo> {
    /**
     * 自定义查询sql
     * @author 
     * return Strig
     */
    public String queryStringBySql(String sqlStr);
    
    @Select("{CALL save_order(#{in_phone,mode=IN,jdbcType=INTEGER},"
    		+ "#{in_mer_id,mode=IN,jdbcType=INTEGER},#{in_type,mode=IN,jdbcType=INTEGER},"
    		+ "#{in_max_time,mode=IN,jdbcType=INTEGER},#{in_order_no,mode=IN,jdbcType=VARCHAR},"
    		+ "#{in_mer_no,mode=IN,jdbcType=VARCHAR},#{in_sys_order_no,mode=IN,jdbcType=VARCHAR},"
    		+ "#{in_notice_url,mode=IN,jdbcType=VARCHAR},#{in_amt,mode=IN,jdbcType=INTEGER},"
    		+ "#{code,mode=OUT,jdbcType=INTEGER},#{message,mode=OUT,jdbcType=VARCHAR})}")
    @Options(statementType = StatementType.CALLABLE)
    public Map<String, Object> saveOrder(Map<String, Object> map);
    public int updateMerAmountById(OrderInfo orderInfo);
    public int updateNoticeById(OrderInfo orderInfo);
    public int updateSearchCountById(OrderInfo orderInfo);
}