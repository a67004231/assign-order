package com.abcd.modules.company.serviceimpl;

import com.abcd.modules.company.entity.CompanyInfo;
import com.abcd.modules.company.service.ICompanyInfoService;
import com.abcd.modules.mapper.CompanyInfoMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 商户通道路由接口实现
 * @author 
 */
@Slf4j
@Service
@Transactional
public class ICompanyInfoServiceImpl extends ServiceImpl<CompanyInfoMapper, CompanyInfo> implements ICompanyInfoService {

    @Autowired
    private CompanyInfoMapper companayInfoMapper;
}