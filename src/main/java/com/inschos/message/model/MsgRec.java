package com.inschos.message.model;
//消息 收件箱表
public class MsgRec {

    public Page page;

    public String body;

    public long id;

    public long msg_id;//'消息标识列'

    public long user_id;//'用户ID'

    public int user_type;//'发件人类型，个人1/企业2/代理人3/业管4

    public int sys_status;//'消息状态：默认为未读0/已读1'

    public int state;//'删除标识:默认为0，1未删除'

    public long created_at;//'创建时间，毫秒'

    public long updated_at;//'更新时间，毫秒'

    public String channel_user_name;

    public MsgSys msgSys;

}
