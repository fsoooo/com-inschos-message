package com.inschos.message.access.http.controller.bean;

public class MsgSendResBean {

    public long toId;//'收件人id'

    public int toType;//'收件人类型，，个人1/企业2/代理人3/业管4

    public String toName = "收件人姓名";//'收件人类型，，个人1/企业2/代理人3/业管4

    public long channelId;//渠道

    public String channelName = "渠道姓名";//渠道

    public String sendRes;//发送结果
}
