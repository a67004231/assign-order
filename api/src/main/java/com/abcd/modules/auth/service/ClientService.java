package com.abcd.modules.auth.service;

import com.abcd.base.BaseService;
import com.abcd.common.vo.SearchVo;
import com.abcd.modules.auth.entity.Client;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 客户端接口
 * @author 
 */
public interface ClientService extends BaseService<Client,String> {

    /**
    * 多条件分页获取
    * @param client
    * @param searchVo
    * @param pageable
    * @return
    */
    Page<Client> findByCondition(Client client, SearchVo searchVo, Pageable pageable);

}