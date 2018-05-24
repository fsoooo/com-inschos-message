package com.inschos.message.access.http.controller.bean;

public class MsgInboxBean {
    //消息 收件箱列表
    public static class inboxListRequest extends BaseRequest {

        public long userId;//用户id

        public int userType;//用户类型:个人用户 1/企业用户 2/代理人 3/业管用户 4

        public int messageStatus;//消息 状态:未读 1/已读 2/全部 3/删除 4 （非必传，默认为1）

        public int messageType;//消息 类型:系统通知1/保单助手2/理赔进度3/最新任务4/客户消息5/活动消息6/顾问消息7/'

        public String pageNum;//分页数据

        public String lastId;//分页数据

        public String limit;//分页数据


    }

    //消息 发件箱列表
    public static class outboxListRequest extends BaseRequest {

        public long userId;//用户id

        public int userType;//用户类型:个人用户 1/企业用户 2/代理人 3/业管用户 4

        public int messageStatus;//消息 状态:未读 1/已读 2/全部 3/删除 4 （非必传，默认为1）

        public int messageType;//消息 类型:系统通知1/保单助手2/理赔进度3/最新任务4/客户消息5/活动消息6/顾问消息7/'

        public String pageNum;//分页数据

        public String lastId;//分页数据

        public String limit;//分页数据

    }

    //消息 详情
    public static class msgInfoRequest extends BaseRequest {

        public long messageId;//消息 id
    }

    //操作消息 （收件箱 读取和删除）
    public static class msgUpdateRequest extends BaseRequest {

        public long messageId;//消息 id

        public int operateId;//操作代码:默认为1（删除/已读），2（还原/未读）

        public String operateType;//操作类型:read 更改读取状态，del 更改删除状态
    }
}
