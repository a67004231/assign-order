package com.abcd.modules.base.service.elasticsearch;

import com.abcd.common.vo.SearchVo;
import com.abcd.modules.base.entity.elasticsearch.EsLog;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author 
 */
public interface EsLogService {

    /**
     * 添加日志
     * @param esLog
     * @return
     */
    EsLog saveLog(EsLog esLog);

    /**
     * 通过id删除日志
     * @param id
     */
    void deleteLog(String id);

    /**
     * 删除全部日志
     */
    void deleteAll();

    /**
     * 分页搜索获取日志
     * @param type
     * @param key
     * @param searchVo
     * @param pageable
     * @return
     */
    Page<EsLog> findByConfition(Integer type, String key, SearchVo searchVo, Pageable pageable);
}
