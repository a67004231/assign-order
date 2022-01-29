package com.abcd.modules.sys.service;

import org.springframework.cache.annotation.CacheConfig;

import com.abcd.modules.sys.entity.SysFile;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 商家信息接口
 * @author sunjie
 */
@CacheConfig(cacheNames = "sysFile")
public interface SysFileService extends IService<SysFile> {

	int deleteByPrimaryKey(String id);

    int insert(SysFile record);

    int insertSelective(SysFile record);

    SysFile selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(SysFile record);

    int updateByPrimaryKeyWithBLOBs(SysFile record);

    int updateByPrimaryKey(SysFile record);
}
