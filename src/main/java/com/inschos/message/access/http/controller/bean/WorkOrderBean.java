package com.inschos.message.access.http.controller.bean;

import java.util.List;

/**
 * Created by IceAnt on 2018/5/21.
 */
public class WorkOrderBean {

    public static class WorkOrderListRequest extends BaseRequest{


    }

    public static class WorkOrderListResponse extends BaseResponse{
        public List<WorkOrderData> data;
    }

    public static class addWork extends BaseRequest{

        public String title; //工单标题

        public String content; //工单内容

        public String category_extra_name; //自定义分类名

        public int category_id; //工单分类

        public String addresseeUuid; //收件人uuid

        public String sender_uuid; //发件人 uuid

        public int type = 1; //工单类型  1 对业管的  2 业管对天眼的

        public int close_status = 1;//结单状态:1未关闭 2已关闭

        public int solve_status = 1;//处理结果:1无反馈 2未解决 3已解决

        public int handle_status = 1;//处理状态:1待处理 2处理中 3已处理

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

        public String submitTime;

        /** 1可评价 0 不可*/
        public int ratable;

        public String orderResult;

        public String orderStatus;

        public String orderStatusTxt;

    }

}
