package com.abcd.common.dubbo;

import com.ryhy.base.dto.ChannelNoticeDTO;
import com.ryhy.base.dto.ChannelPayDTO;
import com.ryhy.base.dto.ChannelQueryDTO;
import com.ryhy.base.dto.ChannelStockDTO;
import com.ryhy.base.service.IProviderService;
import com.ryhy.base.vo.ResultVO;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.springframework.beans.factory.annotation.Value;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ChannelUtils {

    private static ApplicationConfig application = new ApplicationConfig();

    private static Map<String, RegistryConfig> registryConfigCache = new ConcurrentHashMap<>();

    private static Map<String, ReferenceConfig<IProviderService>> referenceCache = new ConcurrentHashMap<>();

    static {
        application.setName("channel");
    }

    private static RegistryConfig getRegistryConfig(String address,String group, String version) {
        String key = address + "-" + group + "-" + version;
        RegistryConfig registryConfig = registryConfigCache.get(key);
        if (null == registryConfig) {
            registryConfig = new RegistryConfig();
            registryConfig.setAddress(address);
            registryConfigCache.put(key, registryConfig);
        }
        return registryConfig;
    }

    /**
     * 获取服务的代理对象
     *
     */
    private static ReferenceConfig<IProviderService> getReferenceConfig( String group,String address,
                                                                     String version) {
        String referenceKey = group;
        ReferenceConfig<IProviderService> referenceConfig = referenceCache.get(referenceKey);
        if (null == referenceConfig) {
            referenceConfig = new ReferenceConfig<>();
            referenceConfig.setApplication(application);
            referenceConfig.setRegistry(getRegistryConfig(address, group, version));
            referenceConfig.setInterface(IProviderService.class);
            referenceConfig.setVersion(version);
            referenceConfig.setGroup(group);
            referenceCache.put(referenceKey, referenceConfig);
        }
        return referenceConfig;
    }



    /**
     * 调用远程服务-支付接口
     *
     */
    public static ResultVO invokePay(ChannelPayDTO channelPayDTO) {
        String group= channelPayDTO.getChannelCode();
        String address = channelPayDTO.getAddress();
        String version = channelPayDTO.getVersion();
        try {
        	ReferenceConfig<IProviderService> reference = getReferenceConfig(group, address, version);
            if (null != reference) {
                IProviderService providerService = reference.get();
                if (null != providerService) {
                    return providerService.channelPay(channelPayDTO);
                }
            }
		} catch (Exception e) {
			
		}
        return null;
    }
    /**
     * 调用远程服务-查询接口
     *
     */
    public static ResultVO invokeQuery(ChannelQueryDTO channelQueryDTO) {
        String group= channelQueryDTO.getChannelCode();
        String address = channelQueryDTO.getAddress();
        String version = channelQueryDTO.getVersion();
        ReferenceConfig<IProviderService> reference = getReferenceConfig(group, address, version);
        if (null != reference) {
            IProviderService providerService = reference.get();
            if (null != providerService) {
                return providerService.channelQuery(channelQueryDTO);
            }
        }
        return null;
    }
    /**
     * 调用远程服务-存款查询接口
     *
     */
    public static ResultVO invokeStockQuery(ChannelStockDTO channelStockDTO) {
        String group= channelStockDTO.getChannelCode();
        String address = channelStockDTO.getAddress();
        String version = channelStockDTO.getVersion();
        ReferenceConfig<IProviderService> reference = getReferenceConfig(group, address, version);
        if (null != reference) {
            IProviderService providerService = reference.get();
            if (null != providerService) {
                return providerService.channelStockQuery(channelStockDTO);
            }
        }
        return null;
    }
    /**
     * 调用远程服务-通知接口
     *
     */
    public static ResultVO invokeNotice(ChannelNoticeDTO noticeDto) {
    	String group= noticeDto.getChannelCode();
    	String address = noticeDto.getAddress();
    	String version = noticeDto.getVersion();
    	ReferenceConfig<IProviderService> reference = getReferenceConfig(group, address, version);
    	if (null != reference) {
    		IProviderService providerService = reference.get();
    		if (null != providerService) {
    			return providerService.channelNotice(noticeDto);
    		}
    	}
    	return null;
    }
}
