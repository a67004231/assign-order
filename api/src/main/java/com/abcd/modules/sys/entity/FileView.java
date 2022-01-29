package com.abcd.modules.sys.entity;

import com.abcd.common.utils.fastdfs.FileUploadUtil;

import lombok.Data;

@Data
public class FileView {
	
    private Long id;

    private String type;
    
    private String url;
    
    public void setCompleteUrl() {
    	this.url = FileUploadUtil.fdfsWebServerUrl + this.url;
    }

}