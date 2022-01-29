package com.abcd.modules.sys.serviceimpl;

import com.abcd.modules.mapper.SysAreaMapper;
import com.abcd.modules.sys.entity.SysArea;
import com.abcd.modules.sys.service.ISysAreaService;
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
public class ISysAreaServiceImpl extends ServiceImpl<SysAreaMapper, SysArea> implements ISysAreaService {

    @Autowired
    private SysAreaMapper sysAreaMapper;
}