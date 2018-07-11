package com.inschos.message.access.rpc.bean;

import com.inschos.message.access.http.controller.bean.AddMsgToBean;

import java.util.List;

public class MessageBean {
    public static class Request {
        public String title;//'标题'
        public String content;//'内容'
        public String attachment;//'附件，上传附件的URL,可为空'
        public int type;//'消息 类型:系统通知1/保单助手2/理赔进度3/最新任务4/客户消息5/活动消息6/顾问消息7/'
        public long fromId;//'发件人ID
        public int fromType;//'发件人类型:个人1/企业2/代理人4/业管3/渠道5/系统6
        public List<AddMsgToBean> toUser;//发送对象
        public String sendTime;//'发送时间:可为空。需要延时发送的，发送时间不为空'
        public String businessId = "-1";//业务id
        //当系统需要发送消息,managerUuid、accountUuid、都等于sysId.
        public String managerUuid;//业管id
        public String accountUuid;//账号id
        public int sysId;//系统id
    }

    public  static class AddMsgToBean {
        public long toId;//'收件人id'
        public int toType;//'收件人类型:个人1/企业2/代理人4/业管3/渠道5
    }

    public static class Response {
        public long code;//200:发送成功,500:发送失败
        public String message;//备注信息
    }
}
