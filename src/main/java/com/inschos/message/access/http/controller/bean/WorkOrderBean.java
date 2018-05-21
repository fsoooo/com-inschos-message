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
