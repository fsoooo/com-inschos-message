package com.inschos.message.access.http.controller.bean;

import java.util.List;

public class MsgIndexBean {

    public String title;//'标题'

    public String content;//'内容'

    public String attachment;//'附件，上传附件的URL,可为空'

    public String type;//'站内信类型:系统通知、保单消息、理赔消息，其他（站内信分类，可为空）'

    public long from_id;//'发件人ID

    public int from_type;//'发件人类型，个人用户1/企业用户2/管理员等3'

    public List<MsgToBean> to_user;

    public String send_time;//'发送时间:可为空。需要延时发送的，发送时间不为空'

}
