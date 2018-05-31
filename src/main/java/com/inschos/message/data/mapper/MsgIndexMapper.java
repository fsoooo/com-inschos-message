package com.inschos.message.data.mapper;

import com.inschos.message.model.*;

public interface MsgIndexMapper {
    //发送消息
    int addMsgSys(MsgSys msgSys);//定义返回数据类型-整形-影响数据库行数

    int addMessage(MsgSys msgSys);

    int addMessageRecord(MsgRecord msgRecord);

    int addMessageToRecord(MsgToRecord msgToRecord);
}
