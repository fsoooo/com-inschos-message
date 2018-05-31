package com.inschos.message.model;

public class MsgRecord {

    public long msg_id;

    public long rec_id;//'收件人ID'

    public int type;//'收件人类型'

    public int status;//'消息状态：默认为未读1/已读2'

    public int state;

    public long created_at;//'创建时间，毫秒'

    public long updated_at;//'更新时间，毫秒'

}
