package com.inschos.message.access.http.controller.bean;

public class MsgInboxBean {
    //站内信收件箱列表
    public static class inboxListRequest extends BaseRequest {

        public long user_id;//用户id

        public int user_type;//用户类型:个人用户 3/代理人 2/企业用户 1/管理员

        public int message_status;//站内信状态:未读 0/已读 1/全部 2/删除 3 （非必传，默认为0）

        public String page_num;//分页数据

        public String last_id;//分页数据

        public String limit;//分页数据


    }

    //站内信发件箱列表
    public static class outboxListRequest extends BaseRequest {

        public long user_id;//用户id

        public int user_type;//用户类型:个人用户 3/代理人 2/企业用户 1/管理员

        public int message_status;//站内信状态:未读 0/已读 1/全部 2/删除 3 （非必传，默认为0）

        public String page_num;//分页数据

        public String last_id;//分页数据

        public String limit;//分页数据

    }

    //站内信详情
    public static class msgInfoRequest extends BaseRequest {

        public long message_id;//站内信id
    }

    //操作站内信（收件箱 读取和删除）
    public static class msgUpdateRequest extends BaseRequest {

        public long message_id;//站内信id

        public int operate_id;//操作代码:默认为1（删除/已读），2（还原/未读）

        public String operate_type;//操作类型:read 更改读取状态，del 更改删除状态
    }
}
