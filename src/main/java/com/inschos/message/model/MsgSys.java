package com.inschos.message.model;

//站内信系统表
public class MsgSys {

    public Page page;

    public long id;

    public String title;//'标题'

    public String content;//'内容'

    public String attachment;//'附件，上传附件的URL,可为空'

    public String type;//'站内信类型:系统通知、保单消息、理赔消息，其他（站内信分类，可为空）'

    public long from_id;//'发件人ID

    public int from_type;//'发件人类型，个人用户1/企业用户2/管理员等3'

    public long to_id;//'收件人id'

    public int to_type;//'收件人类型，，个人用户1/企业用户2/管理员等3'

    public int status;//'读取状态:标识站内信是否已被读取,未读0/已读1.避免重复向收件箱表插入数据,默认为0'

    public String send_time;//'发送时间:可为空。需要延时发送的，发送时间不为空'

    public int state;//'删除标识:默认为0，1未删除'

    public long created_at;//'创建时间，毫秒'

    public long updated_at;//'更新时间，毫秒'

}
