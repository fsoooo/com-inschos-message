package com.inschos.message.access.http.controller.bean;

import com.inschos.message.annotation.CheckParams;

import java.util.List;

/**
 * Created by IceAnt on 2018/5/21.
 */
public class WorkOrderBean {


    public static class WorkOrderReplyRequest extends BaseRequest{

        @CheckParams(stringType = CheckParams.StringType.NUMBER,maxLen = 20)
        public String woId;
        @CheckParams(stringType = CheckParams.StringType.STRING,maxLen = 500)
        public String content;
    }

    public static class WorkOrderCommentRequest extends BaseRequest{

        @CheckParams(stringType = CheckParams.StringType.NUMBER,maxLen = 20)
        public String woId;
        @CheckParams(stringType = CheckParams.StringType.NUMBER,maxLen = 11)
        public String solveStatus ;
    }

    public static class WorkOrderGetRequest extends BaseRequest{

        @CheckParams(stringType = CheckParams.StringType.NUMBER,maxLen = 20)
        public String woId;

    }

    public static class WorkOrderGetResponse extends BaseResponse{
        public WorkOrderData data;
    }


    public static class WorkOrderListRequest extends BaseRequest{


    }

    public static class WorkOrderListResponse extends BaseResponse{
        public List<WorkOrderData> data;
    }

    public static class WOCategoryListResponse extends BaseResponse{
        public List<WOCategoryData> data;
    }

    public static class addWork extends BaseRequest{

        @CheckParams(stringType = CheckParams.StringType.STRING,maxLen =100 )
        public String title; //工单标题
        @CheckParams(stringType = CheckParams.StringType.STRING,maxLen =2000 )
        public String content; //工单内容
        @CheckParams(stringType = CheckParams.StringType.NUMBER,maxLen =20)
        public long categoryId; //工单分类

        public String categoryExtraName; //自定义分类名
        
        public int state = 1;//删除标识 0删除 1未删除

    }



    public static class WorkOrderData{
        public long id;

        public String woNum;

        public String title;

        public String content;

        public String channelName;

        public String senderName;

        public String categoryName;

        public String submitTimeTxt;

        /** 1可评价 0 不可*/
        public int ratable;

        public String orderResult;

        public int orderStatus;

        public String orderStatusTxt;

        public List<WOReplyData> replyList;

    }

    public static class WOCategoryData {
        public long id;

        public String name;

        public int writable;
    }

    public static class WOReplyData{

        public String replyTimeTxt;

        public String content;

        public String replierName;

    }
}
