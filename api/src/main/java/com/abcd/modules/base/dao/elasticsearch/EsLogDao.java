package com.abcd.modules.base.dao.elasticsearch;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.abcd.modules.base.entity.elasticsearch.EsLog;


/**
 * @author 
 */
public interface EsLogDao extends ElasticsearchRepository<EsLog, String> {

    /**
     * 通过类型获取
     * @param type
     * @return
     */
    Page<EsLog> findByLogType(Integer type, Pageable pageable);
}
