package com.abcd.modules.product.serviceimpl;

import com.abcd.modules.mapper.ProductInfoMapper;
import com.abcd.modules.product.entity.ProductInfo;
import com.abcd.modules.product.service.IProductInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 地区信息接口实现
 * @author 
 */
@Slf4j
@Service
@Transactional
public class IProductInfoServiceImpl extends ServiceImpl<ProductInfoMapper, ProductInfo> implements IProductInfoService {

    @Autowired
    private ProductInfoMapper productInfoMapper;
}