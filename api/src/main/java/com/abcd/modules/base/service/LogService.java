package com.abcd.modules.base.service;


import com.abcd.base.BaseService;
import com.abcd.common.vo.SearchVo;
import com.abcd.modules.base.entity.Log;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 日志接口
 * @author 
 */
public interface LogService extends BaseService<Log,String> {

    /**
     * 分页搜索获取日志
     * @param type
     * @param key
     * @param searchVo
     * @param pageable
     * @return
     */
    Page<Log> findByConfition(Integer type, String key, SearchVo searchVo, Pageable pageable);
    /**
     * 删除所有
     */
    void deleteAll();
}
