package com.abcd.modules.merchant.serviceimpl;

import com.abcd.modules.mapper.MerProductAuthMapper;
import com.abcd.modules.merchant.entity.MerProductAuth;
import com.abcd.modules.merchant.service.IMerProductAuthService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 商户秘钥信息接口实现
 * @author 
 */
@Slf4j
@Service
@Transactional
public class IMerProductAuthServiceImpl extends ServiceImpl<MerProductAuthMapper, MerProductAuth> implements IMerProductAuthService {

    @Autowired
    private MerProductAuthMapper merProductAuthMapper;

	@Override
	public List<MerProductAuth> selectByAccount(Map<String, Object> auth) {
		// TODO Auto-generated method stub
		return merProductAuthMapper.selectByAccount(auth);
	}
}