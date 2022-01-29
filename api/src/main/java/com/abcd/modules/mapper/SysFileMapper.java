package com.abcd.modules.mapper;

import com.abcd.modules.sys.entity.SysFile;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * 
 * @author sunjie
 *
 */
public interface SysFileMapper extends BaseMapper<SysFile> {
	
    int deleteByPrimaryKey(String id);

    int insert(SysFile record);

    int insertSelective(SysFile record);

    SysFile selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(SysFile record);

    int updateByPrimaryKeyWithBLOBs(SysFile record);

    int updateByPrimaryKey(SysFile record);
}