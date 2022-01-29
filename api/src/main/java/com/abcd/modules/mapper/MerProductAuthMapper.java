package com.abcd.modules.mapper;

import com.abcd.modules.merchant.entity.MerProductAuth;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;
import java.util.Map;

/**
 * 商户秘钥信息数据处理层
 * @author 
 */
public interface MerProductAuthMapper extends BaseMapper<MerProductAuth> {
	public List<MerProductAuth> selectByAccount(Map<String, Object> auth);
}