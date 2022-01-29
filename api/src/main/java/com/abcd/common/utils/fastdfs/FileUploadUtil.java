package com.abcd.common.utils.fastdfs;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.abcd.modules.sys.entity.FileView;
import com.abcd.modules.sys.entity.SysFile;
import com.abcd.modules.sys.service.SysFileService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import cn.hutool.core.util.StrUtil;

@Component
public class FileUploadUtil {

	@Autowired
	private SysFileService sysFileService;
	@Resource
    private FastDFSClientWrapper dfsClient;
	
	public static final String DIAN = ".";
	public static final String CONTENT_DISPOSITION = "Content-disposition";
	public static final String ATTACHMENT_FILENAME = "attachment;filename=";
	public static final String UTF_8 = "UTF-8";
	public static final String IMAGE = "image";
	public static final String FILE = "file";
	
	@Value("${fdfs.web-server-url}")
	private String fdfsWebServerUrl_Temp;
	
    public static String fdfsWebServerUrl;
    
    @PostConstruct  
    public void init() {
    	fdfsWebServerUrl = fdfsWebServerUrl_Temp;  
    } 

	/**
     * 上传文件到服务器并保存到数据库
     * @param file
     * @return 返回文件对象
     * @throws IOException
     */
    public SysFile uploadFileAndSave(MultipartFile file) throws IOException {
    	String fileName = null;
    	//上传到服务器
    	fileName = dfsClient.uploadFile(file);
    	//保存到数据库
    	SysFile sysFile = new SysFile(file, fileName,getLocalFileName(fileName));
    	sysFileService.insertSelective(sysFile);
    	return sysFile;
    }
    
    
    /**
     * 上传文件到服务器并保存到数据库
     * @param file
     * @return 返回map,包含文件id和类型type
     * @throws IOException
     */
    public FileView uploadFileAndSaveToMap(MultipartFile file) throws IOException {
    	String fileName = null;
    	SysFile sysFile = null;
    	FileView fileView = new FileView();
    	//上传到服务器
    	fileName = dfsClient.uploadFile(file);
    	//保存到数据库
    	sysFile = new SysFile(file, changeUrl(fileName),getLocalFileName(fileName));
    	sysFileService.insertSelective(sysFile);
    	fileView.setId(sysFile.getId());
    	fileView.setType(sysFile.getType());
    	fileView.setUrl(sysFile.getUrl());
    	return fileView;
    }
    
    /**
     * 下载文件
     * @param fileUrl
     * @return
     */
    public byte [] downloadFile(String fileUrl) {
    	return dfsClient.downloadFile(fileUrl);
    }
    
    /**
     * 根据ID获取文件对象
     * @param id
     * @return
     */
    public SysFile getFileByUrl(String url) {
    	if(url.indexOf("//") > 0) {
    		url = changeUrl(url);
    	}
    	if(url.indexOf("#") > 0) {
    		url = url.substring(0,url.indexOf("#"));
    	}
    	QueryWrapper<SysFile> queryWrapper = new QueryWrapper<SysFile>();
    	queryWrapper.lambda().eq(SysFile::getUrl, url);
    	SysFile file = sysFileService.getOne(queryWrapper);
    	return file;
    }
    
    /**
     * 获取远端服务器的文件名
     * @param url 完成的文件访问路径
     * @return
     */
    public String getLocalFileName(String url) {
    	String name = null;
    	if(!StrUtil.isEmpty(url)) {
    		if(url.indexOf("/") > 0) {
    			name = url.substring(url.lastIndexOf("/") + 1,url.length());
    		}
    	}
    	return name;
    }
    
    /**
     * 去除上传FASTDFS后，原始上传路径中带有的IP+端口部分，只保留后面的路径
     * 如：http://10.150.6.29:9080/group1/M00/00/00/CpYGHV4YO86AL-TIAAAZNeJYve4292.png 
     * 转换后  /group1/M00/00/00/CpYGHV4YO86AL-TIAAAZNeJYve4292.png
     * @param url
     * @return
     */
    public String changeUrl(String url) {
    	String str_tmp = url.substring(url.indexOf("//") + 2);
		String str2 = str_tmp.substring(str_tmp.indexOf("/"));
		return str2;
    }
    
    /**
     * 根据数据库文件返回拼接好的原始文件名称
     * @param file
     * @return
     */
    public static String getFileByDB(SysFile file) {
    	StringBuffer buffer = new StringBuffer();
        buffer.append(file.getName()).append(FileUploadUtil.DIAN).append(file.getSuffix());
        return buffer.toString();
    }
}
