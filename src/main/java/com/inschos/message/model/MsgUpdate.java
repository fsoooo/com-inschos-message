package com.inschos.message.model;

public class MsgUpdate {

    public long msg_id;//消息 id

    public int operate_id;//操作代码:默认为1（删除/已读），2（还原/未读）

    public String operate_type;//操作类型:read 更改读取状态，del 更改删除状态
}
