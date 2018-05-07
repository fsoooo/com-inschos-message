package com.inschos.message.access.http.controller.bean;

/**
 * Created by IceAnt on 2017/6/19.
 */
public class BaseRequest {
	//分页
	public String lastId = "0";
	public String pageNum = "1";
	public String pageSize = "20";

	public final static String FILEID_BUILDCODE = "buildCode";
	public final static String FILEID_PLATFORM = "platform";
	public final static String FILEID_APICODE = "apiCode";
	public final static String FILEID_ACCESS_TOKEN = "token";
	//客户端
	public final static String PLATFORM_ANDROID = "android";
	public final static String PLATFORM_IOS = "ios";
	public final static String PLATFORM_WEB = "web";
}
