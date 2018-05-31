package com.inschos.message.model;

public class MsgUpdate {

    public String manager_uuid = "-1";

    public String account_uuid = "-1";

    public long channel_id = -1;

    public long msg_id;//消息 id

    public int type = -1;

    public int operate_id;//操作代码:默认为1（删除/已读），2（还原/未读）

    public String operate_type;//操作类型:read 更改读取状态，del 更改删除状态
}
