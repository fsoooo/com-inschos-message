package com.inschos.message.data.mapper;

import com.inschos.message.model.*;

public interface MsgIndexMapper {
    //发送站内信
    int addMsgSys(MsgSys msgSys);//定义返回数据类型-整形-影响数据库行数

}
