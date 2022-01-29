package com.abcd.modules.merchant.service;

import com.abcd.common.vo.SearchVo;
import com.abcd.modules.merchant.entity.MerProductAuth;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * 商户秘钥信息接口
 * @author 
 */
public interface IMerProductAuthService extends IService<MerProductAuth> {
	public List<MerProductAuth> selectByAccount(Map<String, Object> auth);
}