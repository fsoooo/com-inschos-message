package com.inschos.message.access.http.controller.bean;

public class MsgIndexBean {
    //站内信收件箱列表
    public long from;//发件人id

    public int from_type;//发件人类型:个人用户/企业用户/管理员等

    public MsgToBean msgToBean;//收件人id

    public String title;//主题

    public String body;//内容

    public String type;//站内信类型

    public String file;// 附件 可空

    public String send_time;//发送时间 可空

}
