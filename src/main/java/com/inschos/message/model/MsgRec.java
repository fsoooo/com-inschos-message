package com.inschos.message.model;
//消息 收件箱表
public class MsgRec {

    public Page page;

    public MsgStatus msgStatus;

    public long id;

    public long manager_uuid = -1;

    public long account_uuid = -1;

    public long channel_id = -1;

    public long parent_id = -1;

    public long msg_id;//'消息标识列'

    public long user_id;//'用户ID'

    public int user_type;//'发件人类型，个人1/企业2/代理人3/业管4

    public int sys_status;//'消息状态：默认为未读0/已读1'

    public int type;//'消息 类型:系统通知1/保单助手2/理赔进度3/最新任务4/客户消息5/活动消息6/顾问消息7/'

    public int state;//'删除标识:默认为0，1未删除'

    public long created_at;//'创建时间，毫秒'

    public long updated_at;//'更新时间，毫秒'

    public String channel_user_name;

    public MsgSys msgSys;

}
