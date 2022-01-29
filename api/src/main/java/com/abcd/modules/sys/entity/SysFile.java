package com.abcd.modules.sys.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import com.abcd.common.utils.fastdfs.FileUploadUtil;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@TableName("sys_file")
@ApiModel(value = "文件表")
public class SysFile {
	
	@TableId(value="id",type = IdType.AUTO)
	@ApiModelProperty(value = "商户唯一ID")
    private Long id;

    private String createBy;

	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    private Integer delFlag;

    private String updateBy;
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    private String name;

    private Long size;

    private String type;
    
    private String suffix;

    private String url;

    private String fKey;

    private String location;
    
    public SysFile() {}
    
    public SysFile(MultipartFile file,String remoteUrl,String remoteName) {
    	this.type = file.getContentType().split("/")[0];
    	if(!type.equals(FileUploadUtil.IMAGE)) {
    		this.type = FileUploadUtil.FILE;
    	}
    	this.name = file.getOriginalFilename();
    	if(file.getSize() < 0) {
    		this.size = file.getSize();
    	}else {
    		this.size = file.getSize() / 1024;
    	}
    	if(this.name.indexOf(FileUploadUtil.DIAN) > 0) {
    		this.suffix = this.name.substring(this.name.indexOf(FileUploadUtil.DIAN) + 1,this.name.length());
    	}
    	this.createTime = new Date();
    	this.url = remoteUrl;
    	this.location = remoteName;
    }

}